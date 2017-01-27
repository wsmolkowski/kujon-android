package mobi.kujon.google_drive.model.dto.file_stream;

/**
 *
 */

public class FileUpdateDto {

    private String fileName;
    private int progress;
    private String id;


    public FileUpdateDto(String fileName, int progress, String id) {
        this.fileName = fileName;
        this.progress = progress;
        this.id = id;
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
