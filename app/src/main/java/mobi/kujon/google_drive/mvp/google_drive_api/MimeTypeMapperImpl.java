package mobi.kujon.google_drive.mvp.google_drive_api;


public class MimeTypeMapperImpl implements MimeTypeMapper {

    public static final String DOC = "application/vnd.google-apps.document";
    public static final String IMG = "application/vnd.google-apps.drawing";
    public static final String PPT = "application/vnd.google-apps.presentation";
    public static final String XLS = "application/vnd.google-apps.spreadsheet";

    @Override
    public String convertMimeType(String mimeType) {
        switch (mimeType) {
            case DOC:
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case IMG:
                return "image/jpeg";
            case PPT:
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case XLS:
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }

    @Override
    public String getExtension(String mimeType) {
        switch (mimeType) {
            case DOC:
                return ".docx";
            case IMG:
                return ".jpeg";
            case PPT:
                return ".pptx";
            case XLS:
                return ".xlsx";
            default:
                return "";
        }
    }
}
