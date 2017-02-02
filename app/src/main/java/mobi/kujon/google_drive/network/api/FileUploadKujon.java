package mobi.kujon.google_drive.network.api;


import java.util.List;

import mobi.kujon.google_drive.model.UploadedFile;
import mobi.kujon.network.json.KujonResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface FileUploadKujon {
    @Multipart
    @POST("filesupload")
    Observable<KujonResponse<List<UploadedFile>>> uploadFile(
            @Part("course_id") RequestBody courseId,
            @Part("term_id") RequestBody termId,
            @Part("file_shared_with") RequestBody shareWith,
            @Part("file_shared_with_ids") RequestBody sharedWithList,
            @Part MultipartBody.Part files);
}
