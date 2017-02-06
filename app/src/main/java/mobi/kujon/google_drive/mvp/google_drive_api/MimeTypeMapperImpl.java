package mobi.kujon.google_drive.mvp.google_drive_api;


public class MimeTypeMapperImpl implements MimeTypeMapper {
    @Override
    public String convertMimeType(String mimeType) {
        switch (mimeType) {
            case "application/vnd.google-apps.document":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "application/vnd.google-apps.drawing":
                return "image/jpeg";
            case "application/vnd.google-apps.presentation":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "application/vnd.google-apps.spreadsheet":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return mimeType;
        }
    }
}
