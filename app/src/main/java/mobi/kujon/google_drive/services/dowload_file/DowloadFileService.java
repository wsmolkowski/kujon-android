package mobi.kujon.google_drive.services.dowload_file;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import mobi.kujon.google_drive.utils.TempFileCreator;
import rx.Subscription;

/**
 *
 */

public class DowloadFileService extends Service implements FileStreamUpdateMVP.CancelView {
    public static final String FILE_TO_DOWLOAD_ID = "file_to_dowload_id";
    public static final String FILE_TO_DOWLOAD_NAME = "file_to_dowload_name";


    private Subscription subscription;


    public static void startService(Context context, String fileId, String fileName) {
        Intent intent = new Intent(context, DowloadFileService.class);
        intent.putExtra(FILE_TO_DOWLOAD_ID, fileId);
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
    FileDownloadApi fileDownloadApi;
    @Inject
    SchedulersHolder schedulersHolder;

    @Inject
    TempFileCreator tempFileCreator;

    @Inject
    FileStreamUpdateMVP.Model model;


    @Override
    public void onCreate() {
        super.onCreate();
        Injector<DowloadFileService> injector = ((KujonApplication) this.getApplication()).getInjectorProvider()
                .provideDowloadFileService();
        injector.inject(this);

    }


    private String fileId;
    private String fileName;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() == null && intent.getStringExtra(FILE_TO_DOWLOAD_ID) == null) {
            stopSelf();
        }
        this.fileId = intent.getStringExtra(FILE_TO_DOWLOAD_ID);
        this.fileName = intent.getStringExtra(FILE_TO_DOWLOAD_NAME);

        createCall();
        return START_REDELIVER_INTENT;
    }


    private void createCall() {
        cancelPresenter.subscribeToStream(this);
        subscription = fileDownloadApi.downloadFile(fileId)
                .subscribeOn(schedulersHolder.subscribe())
                .map(it -> tempFileCreator.writeToDowload(it, fileName, percent -> {
                    model.updateStream(new FileUpdateDto(fileName, percent));
                }))
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    model.updateStream(new FileUpdateDto(fileName, 100, true));
                    this.stopSelf();
                }, error -> {
                    model.updateStream(new FileUpdateDto(fileName, 100, true, getString(R.string.download_failed)));
                    this.stopSelf();
                });
    }

    @Override
    public void onCancel(String fileName) {
        if (fileName.equals(this.fileName)) {
            if (subscription != null) {
                subscription.unsubscribe();
            }
            this.stopSelf();
        }
    }
}
