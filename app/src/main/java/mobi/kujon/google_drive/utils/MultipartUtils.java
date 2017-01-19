package mobi.kujon.google_drive.utils;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartUtils {

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull RequestBody createPartFromList(List<Object> sharedIds) {
        String joined = org.apache.commons.lang.StringUtils.join(sharedIds, ",");
        joined = "[" + joined + "]";
        return RequestBody.create(MultipartBody.FORM, joined);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String filePath) {
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(okhttp3.MultipartBody.FORM, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
