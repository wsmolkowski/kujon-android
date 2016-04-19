package mobi.kujon.network.json;

public class KujonResponse<T> {
    public String status;
    public T data;

    public boolean isSuccessful() {
        return "success".equals(status);
    }
}
