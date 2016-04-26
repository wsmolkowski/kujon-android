package mobi.kujon.utils;

import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.HashMap;
import java.util.Map;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Response;

public class ErrorHandlerUtil {

    private static Map<String, String> errorTranslations = new HashMap<String, String>() {{
        put("only-if-cached", "Tryb offline. Nie wszystkie dane są dostępne");
    }};

    public static void handleError(String message) {
        String finalMessage = "Error: " + message;
        for (String key : errorTranslations.keySet()) {
            if (message.contains(key)) {
                finalMessage = errorTranslations.get(key);
            }
        }
        Toast.makeText(KujonApplication.getApplication(), finalMessage, Toast.LENGTH_LONG).show();
    }

    public static void handleError(Throwable throwable) {
        Toast.makeText(KujonApplication.getApplication(), "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
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
