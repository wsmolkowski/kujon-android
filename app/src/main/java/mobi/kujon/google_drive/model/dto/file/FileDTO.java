package mobi.kujon.google_drive.model.dto.file;

import mobi.kujon.google_drive.model.ShareFileTargetType;

/**
 *
 */

public class FileDTO {

    private @ShareFileTargetType String shareType;
    private int numberOfShares;
    private String fileName;
    private String fileSize;
    private int fileType;

}
