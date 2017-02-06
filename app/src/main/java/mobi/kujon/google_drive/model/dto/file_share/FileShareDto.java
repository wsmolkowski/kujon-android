package mobi.kujon.google_drive.model.dto.file_share;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.dto.StudentShareDto;

/**
 *
 */

public class FileShareDto {
    private String fileId;
    private @ShareFileTargetType String shareType;
    private List<String> studentsListToShare;

    public FileShareDto(String fileId, String shareType, List<String> studentsListToShare) {
        this.fileId = fileId;
        this.shareType = shareType;
        this.studentsListToShare = studentsListToShare;
    }

    public FileShareDto(String fileId, List<StudentShareDto> studentShareDtos) {
        this.fileId = fileId;
        this.shareType = ShareFileTargetType.NONE;
        this.studentsListToShare = new ArrayList<>();
        for (StudentShareDto dto : studentShareDtos) {
            if (dto.isChosen()) {
                studentsListToShare.add(dto.getStudentId());
            }
        }
        if (studentsListToShare.size() > 0) {
            shareType = ShareFileTargetType.LIST;
        }
        if (studentsListToShare.size() == studentShareDtos.size()) {
            shareType = ShareFileTargetType.ALL;
        }
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public @ShareFileTargetType String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public List<String> getStudentsListToShare() {
        return studentsListToShare;
    }

    public void setStudentsListToShare(List<String> studentsListToShare) {
        this.studentsListToShare = studentsListToShare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileShareDto)) return false;

        FileShareDto that = (FileShareDto) o;

        if (fileId != null ? !fileId.equals(that.fileId) : that.fileId != null) return false;
        if (shareType != null ? !shareType.equals(that.shareType) : that.shareType != null)
            return false;
        return studentsListToShare != null ? studentsListToShare.equals(that.studentsListToShare) : that.studentsListToShare == null;

    }

    @Override
    public int hashCode() {
        int result = fileId != null ? fileId.hashCode() : 0;
        result = 31 * result + (shareType != null ? shareType.hashCode() : 0);
        result = 31 * result + (studentsListToShare != null ? studentsListToShare.hashCode() : 0);
        return result;
    }
}
