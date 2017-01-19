package mobi.kujon.google_drive.utils;


import android.support.annotation.NonNull;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;

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
        String joined = StringUtils.join(collection, ",");
        joined = "[" + joined + "]";
        return RequestBody.create(MultipartBody.FORM, joined);
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, String filePath) {
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(okhttp3.MultipartBody.FORM, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
