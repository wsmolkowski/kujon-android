package mobi.kujon.google_drive.model.dto.file_upload_info;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class FileUploadInfoDto {

    private String contentType,name,id;


    public FileUploadInfoDto(FileDTO fileDTO) {
        this.contentType = fileDTO.getMimeType();
        this.name = fileDTO.getFileName();
        this.id = fileDTO.getFileId();
    }

    public FileUploadInfoDto(String contentType, String name, String id) {
        this.contentType = contentType;
        this.name = name;
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof FileUploadInfoDto)) return false;

        FileUploadInfoDto that = (FileUploadInfoDto) o;

        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
