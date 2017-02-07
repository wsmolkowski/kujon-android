package mobi.kujon.google_drive.services.add_to_google_drive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.drive.DriveId;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Arrays;

import javax.inject.Inject;

import bolts.Task;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.upload_to_drive.UploadToDrive;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.Subscription;

import static mobi.kujon.google_drive.services.upload.DowloadUploadFileServices.DRIVE_ID_KEY;

/**
 *
 */

public class AddToGoogleDriveService extends Service implements UploadToDrive.View, FileStreamUpdateMVP.CancelView {
    public static final String FILE_TO_DOWLOAD_ID = "file_to_dowload_id";
    public static final String DRIVE_FOLDER_ID = "drive_folder_id";
    public static final String FILE_TO_DOWLOAD_NAME="name_of_file";
    public static final String FILE_TO_DOWLOAD_MIME_TYPE="mimetype_of_file";


    private GoogleAccountCredential mCredential;
    private static final String[] SCOPES = {DriveScopes.DRIVE_METADATA};
    private DriveId driveId;
    private FileUploadInfoDto fileUploadInfoDto;
    private Subscription subscription;
    private Drive mService;


    public static void startService(Context context, FileUploadInfoDto file, DriveId driveId) {
        Intent intent = new Intent(context, AddToGoogleDriveService.class);
        intent.putExtra(FILE_TO_DOWLOAD_ID, file.getId());
        intent.putExtra(FILE_TO_DOWLOAD_NAME, file.getName());
        intent.putExtra(FILE_TO_DOWLOAD_MIME_TYPE, file.getContentType());
        intent.putExtra(DRIVE_FOLDER_ID, driveId);
        context.startService(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Inject
    FileStreamUpdateMVP.CancelPresenter cancelPresenter;
    @Inject
    UploadToDrive.Presenter presenter;

    @Inject
    SchedulersHolder schedulersHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        Injector<AddToGoogleDriveService> injector = ((KujonApplication) this.getApplication()).getInjectorProvider().provideAddToGooogleInjector();
        injector.inject(this);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setSelectedAccountName(retrieveEmail())
                .setBackOff(new ExponentialBackOff());

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
        if (intent != null && intent.getExtras() == null && intent.getStringExtra(DRIVE_ID_KEY) == null) {
            stopSelf();
        }

        fileUploadInfoDto = new FileUploadInfoDto(intent.getStringExtra(FILE_TO_DOWLOAD_MIME_TYPE),intent.getStringExtra(FILE_TO_DOWLOAD_NAME),intent.getStringExtra(FILE_TO_DOWLOAD_ID));
        driveId = intent.getParcelableExtra(DRIVE_FOLDER_ID);


        createCall();


        return START_REDELIVER_INTENT;
    }
    private void createDriveService() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();
    }
    private void createCall() {
        createDriveService();
        cancelPresenter.subscribeToStream(this);
        presenter.setDrive(mService);
        subscription = Observable.just(driveId)
                .subscribeOn(schedulersHolder.subscribe())
                .map(DriveId::getResourceId)
                .observeOn(schedulersHolder.observ())
                .subscribe(it->{
                    presenter.uploadToDrive(fileUploadInfoDto,it);
                },error->{
                    printErrorAndStop(error);
                });
    }

    private void printErrorAndStop(Throwable error) {
        error.getCause().printStackTrace();
        this.stopSelf();
    }

    @Override
    public void fileUploaded() {
        this.stopSelf();

    }

    @Override
    public void authenticate(UserRecoverableAuthIOException  e) {
        startActivity(e.getIntent());
        this.stopSelf();
    }

    @Override
    public void handleException(Throwable throwable) {
        printErrorAndStop(throwable);
    }


    @Override
    public void onCancel(String fileName) {
        if(fileName.equals(fileUploadInfoDto.getName())){
            presenter.clearSubscriptions();
            if(subscription !=null){
                subscription.unsubscribe();
            }
            this.stopSelf();
        }
    }
}
