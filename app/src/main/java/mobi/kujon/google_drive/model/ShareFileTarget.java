package mobi.kujon.google_drive.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareFileTarget {

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("share_with") @Expose
    public String shareWithTarget;
}
