package mobi.kujon.google_drive.services.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.gson.Gson;

import javax.inject.Inject;

import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadMVP;
import mobi.kujon.google_drive.mvp.upload_file.UploadFileMVP;
import mobi.kujon.google_drive.utils.SchedulersHolder;


public class DowloadUploadFileServices extends Service implements UploadFileMVP.View, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String FILE_UPLOAD_DTO = "fileUploadDto";
    public static final String DRIVE_ID_KEY = "driveIdKey";

    public static void startService(Context context,String fileUploadDto,DriveId driveId){
        Intent intent = new Intent(context, DowloadUploadFileServices.class);
        intent.putExtra(FILE_UPLOAD_DTO,fileUploadDto);
        intent.putExtra(DRIVE_ID_KEY,driveId);
        context.startService(intent);
    }
    private FileUploadDto fileUploadDto;

    private DriveId driveId;
    private GoogleApiClient apiClient;


    @Inject
    GoogleDriveDowloadMVP.Model googleDowloadModel;

    @Inject
    SchedulersHolder schedulersHolder;

    @Inject
    UploadFileMVP.Presenter presenter;

    @Inject
    Gson gson;

    public DowloadUploadFileServices() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Injector<DowloadUploadFileServices> injector = ((KujonApplication) this.getApplication()).getInjectorProvider().provideInjectorForService();
        injector.inject(this);
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() == null && intent.getStringExtra(FILE_UPLOAD_DTO) == null) {
            stopSelf();
        }
        fileUploadDto = gson.fromJson(intent.getStringExtra(FILE_UPLOAD_DTO), FileUploadDto.class);
        driveId = intent.getParcelableExtra(DRIVE_ID_KEY);
        if(apiClient.isConnected()){
            handleApiCalls();
        }else {
            apiClient.connect();
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        handleApiCalls();
    }

    private void handleApiCalls() {
        googleDowloadModel.setGoogleClient(apiClient);
        googleDowloadModel.dowloadFile(driveId)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    presenter.uploadFile(it, fileUploadDto);
                }, error -> {
                    //TODO restart Service with same parameters
                });
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
        this.stopSelf();
    }

    @Override
    public void handleException(Throwable throwable) {
//TODO restart Service with same parameters
    }
}
