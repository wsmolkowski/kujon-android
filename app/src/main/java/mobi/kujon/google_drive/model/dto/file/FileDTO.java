package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.DrawableRes;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.model.ShareFileTargetType;

/**
 *
 */

public abstract class FileDTO {

    private @ShareFileTargetType String shareType;
    private int numberOfShares;
    private String fileName;
    private String fileSize;
    private String userName;
    private String fileId;



    public FileDTO(KujonFile kujonFile) {
        this.fileId = kujonFile.fileId;
        this.shareType = kujonFile.shareType;
        this.numberOfShares = kujonFile.fileSharedWith.length;
        this.fileName = kujonFile.fileName;
        this.fileSize = kujonFile.fileSize;
        this.userName = new StringBuilder(kujonFile.fileName).append(" ").append(kujonFile.lastName).toString();
    }


    public String getShareType() {
        return shareType;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileId() {
        return fileId;
    }

    public abstract @DrawableRes int getImageIcon();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDTO)) return false;

        FileDTO fileDTO = (FileDTO) o;

        if (numberOfShares != fileDTO.numberOfShares) return false;
        if (shareType != null ? !shareType.equals(fileDTO.shareType) : fileDTO.shareType != null)
            return false;
        if (fileName != null ? !fileName.equals(fileDTO.fileName) : fileDTO.fileName != null)
            return false;
        if (fileSize != null ? !fileSize.equals(fileDTO.fileSize) : fileDTO.fileSize != null)
            return false;
        if (userName != null ? !userName.equals(fileDTO.userName) : fileDTO.userName != null)
            return false;
        return fileId != null ? fileId.equals(fileDTO.fileId) : fileDTO.fileId == null;

    }

    @Override
    public int hashCode() {
        int result = shareType != null ? shareType.hashCode() : 0;
        result = 31 * result + numberOfShares;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        return result;
    }
}
