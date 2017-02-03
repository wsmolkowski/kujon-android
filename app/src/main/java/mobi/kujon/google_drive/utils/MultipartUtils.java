package mobi.kujon.google_drive.utils;


import android.support.annotation.NonNull;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Collection;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.request.ProgressRequestBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartUtils {

    @NonNull
    public RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    public RequestBody createPartFromCollection(Collection collection) {
        if (collection == null) {
            return RequestBody.create(MultipartBody.FORM, "[]");
        } else {
            String joined;
            joined = StringUtils.join(collection, ",");
            return RequestBody.create(MultipartBody.FORM, joined);
        }
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, String filePath) {
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(okhttp3.MultipartBody.FORM, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, DataForFileUpload upload, ProgressRequestBody.UploadCallbacks callbacks) {
        RequestBody requestFile = new ProgressRequestBody(upload, callbacks);
        return MultipartBody.Part.createFormData(partName, upload.getTitle(), requestFile);
    }
}
