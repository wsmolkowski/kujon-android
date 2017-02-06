package mobi.kujon.google_drive.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharedFile {

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("file_shared_with") @Expose
    public List<String> fileSharedWith;

}
