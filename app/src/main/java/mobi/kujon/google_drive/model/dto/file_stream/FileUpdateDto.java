package mobi.kujon.google_drive.model.dto.file_stream;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class FileUpdateDto {

    private  FileDTO fileDto;
    private boolean doNotUpdate;
    private boolean error;
    private String fileName;
    private int progress;
    private String id;
    private String errorReason;

    private @FileStreamType int streamType;
    private boolean ended;

    public FileUpdateDto(String fileName, int progress) {
        this.fileName = fileName;
        this.progress = progress;
    }

    public FileUpdateDto(String fileName, int progress, boolean ended) {
        this(fileName, progress);
        this.ended = ended;
        this.doNotUpdate = false;
    }

    public FileUpdateDto(String fileName, int progress, boolean ended, boolean doNotUpdate) {
        this(fileName, progress);
        this.ended = ended;
        this.doNotUpdate = doNotUpdate;
    }

    public FileUpdateDto(String title, int i, boolean ended, String b1) {
        this(title, i, ended, false);
        this.error = true;
        errorReason = b1;
    }

    public FileUpdateDto(String fileName, String id, int streamType, boolean ended, FileDTO fileDTO) {
        this.fileName = fileName;
        this.id = id;
        this.streamType = streamType;
        this.ended = ended;
        this.fileDto = fileDTO;
    }


    public FileDTO getFileDto() {
        return fileDto;
    }

    public int getStreamType() {
        return streamType;
    }

    public boolean isError() {
        return error;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public boolean isDoNotUpdate() {
        return doNotUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileUpdateDto)) return false;

        FileUpdateDto that = (FileUpdateDto) o;

        if (progress != that.progress) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null)
            return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + progress;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
