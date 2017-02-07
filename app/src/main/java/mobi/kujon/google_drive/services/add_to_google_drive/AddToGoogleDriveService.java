package mobi.kujon.google_drive.services.add_to_google_drive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.drive.DriveId;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;

import java.util.Arrays;

import bolts.Task;
import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;

import static mobi.kujon.google_drive.services.upload.DowloadUploadFileServices.DRIVE_ID_KEY;
import static mobi.kujon.google_drive.services.upload.DowloadUploadFileServices.FILE_UPLOAD_DTO;

/**
 *
 */

public class AddToGoogleDriveService extends Service {
    public static final String FILE_TO_DOWLOAD_ID = "file_to_dowload_id";
    public static final String DRIVE_FOLDER_ID = "drive_folder_id";
    public static final String FILE_TO_DOWLOAD_NAME="name_of_file";
    public static final String FILE_TO_DOWLOAD_MIME_TYPE="mimetype_of_file";


    private GoogleAccountCredential mCredential;
    private static final String[] SCOPES = {DriveScopes.DRIVE_METADATA};
    private String fileId;
    private Parcelable driveId;
    private FileUploadInfoDto fileUploadInfoDto;


    public static void startService(Context context, FileUploadInfoDto file, DriveId driveId) {
        Intent intent = new Intent(context, DowloadUploadFileServices.class);
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



    @Override
    public void onCreate() {
        super.onCreate();
//        Injector<DowloadUploadFileServices> injector = ((KujonApplication) this.getApplication()).getInjectorProvider().provideInjectorForService();
//        injector.inject(this);

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
        if (intent != null && intent.getExtras() == null && intent.getStringExtra(FILE_UPLOAD_DTO) == null) {
            stopSelf();
        }
        fileId = intent.getStringExtra(FILE_TO_DOWLOAD_ID);
        fileUploadInfoDto = new FileUploadInfoDto(intent.getStringExtra(FILE_TO_DOWLOAD_MIME_TYPE),intent.getStringExtra(FILE_TO_DOWLOAD_NAME),intent.getStringExtra(FILE_TO_DOWLOAD_ID));
        driveId = intent.getParcelableExtra(DRIVE_ID_KEY);

        return START_REDELIVER_INTENT;
    }
}
