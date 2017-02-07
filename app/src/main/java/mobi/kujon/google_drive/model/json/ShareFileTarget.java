package mobi.kujon.google_drive.model.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;

public class ShareFileTarget {

    public ShareFileTarget() {}

    public ShareFileTarget(FileShareDto shareDto) {
        this.fileId = shareDto.getFileId();
        this.shareWithTargetType = shareDto.getShareType();
        List<String> studentIds = shareDto.getStudentsListToShare();
        this.shareWithTarget = new ArrayList<>(studentIds.size());
        for(String studentId : studentIds) {
            shareWithTarget.add(studentId);
        }
    }

    @SerializedName("file_id") @Expose
    public String fileId;

    @SerializedName("file_shared_with") @Expose
    @ShareFileTargetType
    public String shareWithTargetType;

    @SerializedName("file_shared_with_ids") @Expose
    public List<String> shareWithTarget;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShareFileTarget target = (ShareFileTarget) o;

        if (fileId != null ? !fileId.equals(target.fileId) : target.fileId != null) return false;
        if (shareWithTargetType != null ? !shareWithTargetType.equals(target.shareWithTargetType) : target.shareWithTargetType != null)
            return false;
        return shareWithTarget != null ? shareWithTarget.equals(target.shareWithTarget) : target.shareWithTarget == null;

    }

    @Override
    public int hashCode() {
        int result = fileId != null ? fileId.hashCode() : 0;
        result = 31 * result + (shareWithTargetType != null ? shareWithTargetType.hashCode() : 0);
        result = 31 * result + (shareWithTarget != null ? shareWithTarget.hashCode() : 0);
        return result;
    }
}
