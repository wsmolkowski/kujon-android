package mobi.kujon.google_drive;


import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.model.ShareFileTarget;
import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.SharedFile;
import mobi.kujon.google_drive.model.UploadedFile;
import mobi.kujon.network.json.KujonResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

public interface KujonFilesharingApi {

    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getAllFiles();

    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getFiles(@Query("course_id") String courseId, @Query("term_id") String termId);

    @POST("filesshare")
    Observable<KujonResponse<SharedFile>> shareFile(@Body ShareFileTarget shareFileTarget);

    @Streaming
    @GET("files/{fileId}")
    Observable<KujonResponse<ResponseBody>> downloadFile(@Path("fileId") String fileId);

    @DELETE("files/{fileId}")
    Observable<KujonResponse<String>> deleteFile(@Path("fileId") String fileId);

    @Multipart
    @POST("filesupload")
    Observable<KujonResponse<UploadedFile>> uploadFile(@Part("coures_id") String courseId,
                                                       @Part("term_id") String termId,
                                                       @Part("file_share_with") @ShareFileTargetType.ShareFileTargetTypes String shareFileWith,
                                                       @Part("share_list") List<Integer> shareTargets,
                                                       @Part("files") RequestBody file);


}
