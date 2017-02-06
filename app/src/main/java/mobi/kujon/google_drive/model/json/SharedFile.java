package mobi.kujon.google_drive.model.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SharedFile {

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("file_shared_with") @Expose
    public List<String> fileSharedWith;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SharedFile that = (SharedFile) o;

        if (fileId != null ? !fileId.equals(that.fileId) : that.fileId != null) return false;
        return fileSharedWith != null ? fileSharedWith.equals(that.fileSharedWith) : that.fileSharedWith == null;

    }

    @Override
    public int hashCode() {
        int result = fileId != null ? fileId.hashCode() : 0;
        result = 31 * result + (fileSharedWith != null ? fileSharedWith.hashCode() : 0);
        return result;
    }
}
