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




}
