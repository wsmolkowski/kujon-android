package mobi.kujon.google_drive;


import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.model.ShareFileTarget;
import mobi.kujon.google_drive.model.SharedFile;
import mobi.kujon.google_drive.model.UploadedFile;
import mobi.kujon.network.json.KujonResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

public interface KujonFilesharingApi {

    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getAllFiles();

    @GET("files/{termId}/{courseId}")
    Observable<KujonResponse<List<KujonFile>>> getFiles(@Path("term_id") String termId, @Path("course_id") String courseId);

    @Streaming
    @GET("files/{fileId}")
    Observable<KujonResponse<ResponseBody>> downloadFile(@Path("fileId") String fileId);

    @DELETE("files/{fileId}")
    Observable<KujonResponse<String>> deleteFile(@Path("fileId") String fileId);

    @Multipart
    @POST("filesupload")
    Observable<KujonResponse<UploadedFile>> uploadFile(@Part("coures_id") String courseId,
                                                       @Part("term_id") String termId,
                                                       @Part("shared_user_usos_ids") List<String> sharedUserIds,
                                                       @Part MultipartBody.Part file);

    @POST("filesshare")
    Observable<KujonResponse<SharedFile>> shareFile(@Body ShareFileTarget shareFileTarget);

}
