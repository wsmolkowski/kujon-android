package mobi.kujon.google_drive.services;

import android.app.Service;
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
import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadMVP;
import mobi.kujon.google_drive.utils.SchedulersHolder;


public class DowloadUploadFileServices extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String FILE_UPLOAD_DTO = "fileUploadDto";
    public static final String DRIVE_ID_KEY = "driveIdKey";

    private FileUpdateDto fileUpdateDto;

    private DriveId driveId;
    private GoogleApiClient apiClient;


    @Inject
    GoogleDriveDowloadMVP.Model googleDowloadModel;

    @Inject
    SchedulersHolder schedulersHolder;

    @Inject
    Gson gson;

    public DowloadUploadFileServices() {
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ((KujonApplication) this.getApplication()).getInjectorProvider();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() == null) {
            stopSelf();
        }
        fileUpdateDto = gson.fromJson(intent.getStringExtra(FILE_UPLOAD_DTO), FileUpdateDto.class);
        driveId = intent.getParcelableExtra(DRIVE_ID_KEY);
        apiClient.connect();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        googleDowloadModel.setGoogleClient(apiClient);
        googleDowloadModel.dowloadFile(driveId)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    //TODO create presenter which will update this byte[] woth fileUpdateDto
                }, error -> {

                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopSelf();
    }
}
