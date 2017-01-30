package mobi.kujon.google_drive.model.dto.file_share;

import java.util.List;

public class FileShareDtoBuilder {
    private String fileId;
    private String shareType;
    private List<String> studentsListToShare;

    public FileShareDtoBuilder setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public FileShareDtoBuilder setShareType(String shareType) {
        this.shareType = shareType;
        return this;
    }

    public FileShareDtoBuilder setStudentsListToShare(List<String> studentsListToShare) {
        this.studentsListToShare = studentsListToShare;
        return this;
    }

    public FileShareDto createFileShareDto() {
        return new FileShareDto(fileId, shareType, studentsListToShare);
    }
}