package mobi.kujon.google_drive.model.dto.file_upload;

import java.util.Arrays;

import okhttp3.MediaType;

/**
 *
 */

public class DataForFileUpload {
    private byte[] bytes;
    private MediaType mediaType;

    public DataForFileUpload(byte[] bytes, String mediaType) {
        this.bytes = bytes;
        this.mediaType = MediaType.parse(mediaType);
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
