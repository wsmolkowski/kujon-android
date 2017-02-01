package mobi.kujon.google_drive.model.dto.file_upload;

import com.google.android.gms.drive.DriveResource;

import java.util.Arrays;

import okhttp3.MediaType;

/**
 *
 */

public class DataForFileUpload {
    private byte[] bytes;
    private MediaType mediaType;
    private String title;

    public DataForFileUpload(byte[] bytes, String mediaType,String title) {
        this.bytes = bytes;
        this.mediaType = MediaType.parse(mediaType);
        this.title = title;
    }

    public DataForFileUpload(byte[] bytes,DriveResource.MetadataResult metadataResult) {
        this(bytes,metadataResult.getMetadata().getMimeType(),metadataResult.getMetadata().getTitle());
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataForFileUpload)) return false;

        DataForFileUpload that = (DataForFileUpload) o;

        if (!Arrays.equals(bytes, that.bytes)) return false;
        return mediaType != null ? mediaType.equals(that.mediaType) : that.mediaType == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(bytes);
        result = 31 * result + (mediaType != null ? mediaType.hashCode() : 0);
        return result;
    }
}
