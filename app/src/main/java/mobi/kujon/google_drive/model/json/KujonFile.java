package mobi.kujon.google_drive.model.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;

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

    @SerializedName("file_content_type") @Expose
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
    public Date createdTime;

    @SerializedName("file_size") @Expose
    public String fileSize;

    @SerializedName("file_shared_by_me") @Expose
    public boolean myFile;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KujonFile)) return false;

        KujonFile kujonFile = (KujonFile) o;

        if (myFile != kujonFile.myFile) return false;
        if (fileName != null ? !fileName.equals(kujonFile.fileName) : kujonFile.fileName != null)
            return false;
        if (courseId != null ? !courseId.equals(kujonFile.courseId) : kujonFile.courseId != null)
            return false;
        if (termId != null ? !termId.equals(kujonFile.termId) : kujonFile.termId != null)
            return false;
        if (shareType != null ? !shareType.equals(kujonFile.shareType) : kujonFile.shareType != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(fileSharedWith, kujonFile.fileSharedWith)) return false;
        if (contentType != null ? !contentType.equals(kujonFile.contentType) : kujonFile.contentType != null)
            return false;
        if (firstName != null ? !firstName.equals(kujonFile.firstName) : kujonFile.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(kujonFile.lastName) : kujonFile.lastName != null)
            return false;
        if (usosUserId != null ? !usosUserId.equals(kujonFile.usosUserId) : kujonFile.usosUserId != null)
            return false;
        if (fileId != null ? !fileId.equals(kujonFile.fileId) : kujonFile.fileId != null)
            return false;
        if (createdTime != null ? !createdTime.equals(kujonFile.createdTime) : kujonFile.createdTime != null)
            return false;
        return fileSize != null ? fileSize.equals(kujonFile.fileSize) : kujonFile.fileSize == null;

    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        result = 31 * result + (termId != null ? termId.hashCode() : 0);
        result = 31 * result + (shareType != null ? shareType.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(fileSharedWith);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (usosUserId != null ? usosUserId.hashCode() : 0);
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (myFile ? 1 : 0);
        return result;
    }
}
