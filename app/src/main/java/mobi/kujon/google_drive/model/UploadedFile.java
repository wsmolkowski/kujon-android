package mobi.kujon.google_drive.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadedFile {

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("file_name") @Expose
    public String fileName;

    @SerializedName("shared_user_usos_ids") @Expose
    public List<String> sharedWith;
}