package mobi.kujon.google_drive.network;

/**
 *
 */
public class KujonException extends Throwable {
    private String message;
    public KujonException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
