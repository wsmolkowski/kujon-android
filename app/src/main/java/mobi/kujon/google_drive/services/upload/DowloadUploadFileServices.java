package mobi.kujon.google_drive.services.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;

import java.util.Arrays;

import javax.inject.Inject;

import bolts.Task;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDowloadProvider;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadMVP;
import mobi.kujon.google_drive.mvp.google_drive_api.MimeTypeMapper;
import mobi.kujon.google_drive.mvp.google_drive_api.MimeTypeMapperImpl;
import mobi.kujon.google_drive.mvp.upload_file.UploadFileMVP;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.Subscription;


public class DowloadUploadFileServices extends Service implements UploadFileMVP.View,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleDriveDowloadMVP.GoogleClientProvider, FileStreamUpdateMVP.CancelView {

    public static final String FILE_UPLOAD_DTO = "fileUploadDto";
    public static final String DRIVE_ID_KEY = "driveIdKey";
    private String mimeType;
    private String title;
    private Subscription subscription;
    private MimeTypeMapper mimeTypeMapper;

    public static void startService(Context context, String fileUploadDto, DriveId driveId) {
        Intent intent = new Intent(context, DowloadUploadFileServices.class);
        intent.putExtra(FILE_UPLOAD_DTO, fileUploadDto);
        intent.putExtra(DRIVE_ID_KEY, driveId);
        context.startService(intent);
    }

    private FileUploadDto fileUploadDto;

    private DriveId driveId;
    private GoogleApiClient apiClient;


    @Inject
    GoogleDowloadProvider googleDowloadProvider;

    @Inject
    SchedulersHolder schedulersHolder;

    @Inject
    UploadFileMVP.Presenter presenter;

    @Inject
    FileStreamUpdateMVP.CancelPresenter cancelPresenter;

    @Inject
    Gson gson;

    public DowloadUploadFileServices() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final String[] SCOPES = {DriveScopes.DRIVE_METADATA_READONLY};
    GoogleAccountCredential mCredential;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector<DowloadUploadFileServices> injector = ((KujonApplication) this.getApplication()).getInjectorProvider().provideInjectorForService();
        injector.inject(this);
        mimeTypeMapper = new MimeTypeMapperImpl();
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setSelectedAccountName(retrieveEmail())
                .setBackOff(new ExponentialBackOff());
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private String retrieveEmail() {
        Task<GoogleSignInResult> loginStatus = KujonApplication.getApplication().getLoginStatus();
        if (loginStatus.isCompleted() && loginStatus.getResult() != null && loginStatus.getResult().getSignInAccount() != null) {
            GoogleSignInAccount account = loginStatus.getResult().getSignInAccount();
            return account.getEmail();
        } else {
            return null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() == null && intent.getStringExtra(FILE_UPLOAD_DTO) == null) {
            stopSelf();
        }
        fileUploadDto = gson.fromJson(intent.getStringExtra(FILE_UPLOAD_DTO), FileUploadDto.class);
        driveId = intent.getParcelableExtra(DRIVE_ID_KEY);
        if (apiClient.isConnected()) {
            handleApiCalls();
        } else {
            apiClient.connect();
        }
        cancelPresenter.subscribeToStream(this);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        handleApiCalls();
    }

    private com.google.api.services.drive.Drive mService = null;

    private void handleApiCalls() {
        createDriveService();
        subscription = Observable.just(driveId.asDriveFile())
                .map(driveFile -> {
                    DriveResource.MetadataResult mdRslt = driveFile.getMetadata(apiClient).await();
                    mimeType = mdRslt.getMetadata().getMimeType();
                    title = mdRslt.getMetadata().getTitle();
                    GoogleDriveDowloadMVP.Model model = googleDowloadProvider.getModel(mimeType);
                    model.setGoogleClient(this);
                    return model;
                }).flatMap(model -> model.dowloadFile(driveId, mimeType, title))
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    presenter.uploadFile(it, fileUploadDto);

                }, error -> {
                    if (error.getCause() instanceof UserRecoverableAuthIOException) {
                        startActivity(((UserRecoverableAuthIOException) error).getIntent());
                    } else {
                        this.stopSelf();
                        error.printStackTrace();
                    }
                });
    }

    private void createDriveService() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopSelf();
    }

    @Override
    public void onFileUploaded() {
        Log.d("DOWLOAD", "COMPLETE");
        this.stopSelf();
    }

    @Override
    public void handleException(Throwable throwable) {
        throwable.printStackTrace();
        this.stopSelf();
//TODO restart Service with same parameters
    }

    @Override
    public GoogleApiClient getGoogleClient() {
        return apiClient;
    }

    @Override
    public com.google.api.services.drive.Drive getGoogleDrive() {
        return mService;
    }

    @Override
    public void onCancel(String fileName) {
        if (fileName.equals(title) || fileName.equals(fileName(title, mimeType))) {
            this.presenter.clearSubscriptions();
            if (subscription != null)
                this.subscription.unsubscribe();
            this.stopSelf();
        }
    }

    private String fileName(String mimeType, String title) {

        return title + mimeTypeMapper.getExtension(mimeType);
    }
}
