package mobi.kujon.google_drive.model.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShareFileTarget {

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("share_with") @Expose
    @ShareFileTargetType
    public String shareWithTargetType;

    @SerializedName("list_share") @Expose
    public List<Integer> shareWithTarget;

}
