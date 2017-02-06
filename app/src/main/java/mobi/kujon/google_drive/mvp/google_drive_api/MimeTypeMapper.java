package mobi.kujon.google_drive.mvp.google_drive_api;

/**
 *
 */

public interface MimeTypeMapper {
    String convertMimeType(String mimeType);
    String getExtension(String mimeType);
}
