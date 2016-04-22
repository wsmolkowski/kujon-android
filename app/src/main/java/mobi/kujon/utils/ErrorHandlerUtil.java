package mobi.kujon.utils;

import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Response;

public class ErrorHandlerUtil {
    public static void handleError(String message) {
        Toast.makeText(KujonApplication.getApplication(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    public static void handleError(Throwable throwable) {
        Toast.makeText(KujonApplication.getApplication(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Crashlytics.logException(throwable);
    }

    public static <T> boolean handleResponse(Response<KujonResponse<T>> response) {
        if (!response.isSuccessful()) {
            handleError("Network error " + response.message());
            return false;
        }

        if (response.body() == null) {
            handleError("Network error");
            return false;
        }

        if (!response.body().isSuccessful()) {
            handleError(response.body().message);
            return false;
        }

        if (response.body().data == null) {
            handleError("Brak danych");
            return false;
        }

        return true;
    }
}
