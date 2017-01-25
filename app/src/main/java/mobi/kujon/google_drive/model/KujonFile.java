package mobi.kujon.google_drive.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KujonFile {

    @SerializedName("file_name") @Expose
    public String fileName;

    @SerializedName("course_id") @Expose
    public String courseId;

    @SerializedName("term_id") @Expose
    public String termId;

    @SerializedName("file_shared_with") @Expose
    public @ShareFileTargetType String shareType;

    @SerializedName("file_shared_with_ids") @Expose
    public String[] fileSharedWith = {};

    @SerializedName("content_type") @Expose
    public String contentType;

    @SerializedName("first_name") @Expose
    public String firstName;

    @SerializedName("last_name") @Expose
    public String lastName;

    @SerializedName("usos_user_id") @Expose
    public String usosUserId;

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("created_time") @Expose
    public String createdTime;

    @SerializedName("file_size") @Expose
    public String fileSize;

    @SerializedName("my_file") @Expose
    public boolean myFile;
}
