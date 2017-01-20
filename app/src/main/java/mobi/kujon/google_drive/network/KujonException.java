package mobi.kujon.google_drive.network;

/**
 *
 */
public class KujonException extends Throwable {
    private String message;
    private Integer code;
    public KujonException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
