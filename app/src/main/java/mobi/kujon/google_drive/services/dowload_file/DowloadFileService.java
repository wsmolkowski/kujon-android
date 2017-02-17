package mobi.kujon.google_drive.services.dowload_file;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import mobi.kujon.KujonApplication;
import mobi.kujon.NetModule;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.request.ProgressRequestBody;
import mobi.kujon.google_drive.model.request.ProgressResponseBody;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.api.FileDownloadKujon;
import mobi.kujon.google_drive.network.facade.FileDownloadApiFacade;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import mobi.kujon.google_drive.utils.TempFileCreator;
import mobi.kujon.network.ApiProvider;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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
        intent.putExtra(FILE_TO_DOWLOAD_NAME, fileName);
        context.startService(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Inject
    FileStreamUpdateMVP.CancelPresenter cancelPresenter;

    private FileDownloadApi fileDownloadApi;
    @Inject
    SchedulersHolder schedulersHolder;

    @Inject
    TempFileCreator tempFileCreator;

    @Inject
    FileStreamUpdateMVP.Model model;

    @Inject
    NetModule.AuthenticationInterceptor authenticationInterceptor;


    @Inject
    ApiProvider apiProvider;
    @Override
    public void onCreate() {
        super.onCreate();
        Injector<DowloadFileService> injector = ((KujonApplication) this.getApplication()).getInjectorProvider()
                .provideDowloadFileService();
        injector.inject(this);
        createDowloadApiWithProgress();
    }

    private void createDowloadApiWithProgress() {
        ProgressRequestBody.UploadCallbacks listener = percentage ->
                model.updateStream(new FileUpdateDto(fileName,(int)Math.round(0.75*percentage)));
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(authenticationInterceptor)
                .addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), listener))
                            .build();
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiProvider.getApiURL())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(apiProvider.getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        fileDownloadApi = new FileDownloadApiFacade(retrofit.create(FileDownloadKujon.class));
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
        model.updateStream(new FileUpdateDto(fileName,0));
        subscription = fileDownloadApi.downloadFile(fileId)
                .subscribeOn(schedulersHolder.subscribe())
                .map(it -> tempFileCreator.writeToDowload(it, fileName, percent ->
                        model.updateStream(new FileUpdateDto(fileName, (int)Math.round(0.25*percent)))))
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    model.updateStream(new FileUpdateDto(fileName, 100, true,true));
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
