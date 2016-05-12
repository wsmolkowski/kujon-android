package mobi.kujon.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Response;

public class ErrorHandlerUtil {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandlerUtil.class);

    private static Map<String, String> errorTranslations = new HashMap<String, String>() {{
        put("only-if-cached", "Tryb offline. Nie wszystkie dane są dostępne");
        put("Bad Gateway", "Wystąpił błąd techniczny. Pracujemy nad rozwiązaniem. Spróbuj za chwilę");
    }};

    public static void translateAndToastErrorMessage(String message) {
        String finalMessage = "Error: " + message;
        for (String key : errorTranslations.keySet()) {
            if (!TextUtils.isEmpty(message) && message.contains(key)) {
                finalMessage = errorTranslations.get(key);
            }
        }
        log.error(finalMessage);
        Toast.makeText(KujonApplication.getApplication(), finalMessage, Toast.LENGTH_SHORT).show();
        Crashlytics.logException(new Exception(finalMessage));
    }

    public static void handleError(Throwable throwable) {
        log.error(throwable.getMessage());
        translateAndToastErrorMessage(throwable.getMessage());
        Crashlytics.logException(throwable);
    }

    public static <T> boolean handleResponse(Response<KujonResponse<T>> response) {
        if (!response.isSuccessful()) {
            log.error(response.raw().toString());
            translateAndToastErrorMessage("Network error " + response.message());
            return false;
        }

        if (response.body() == null) {
            log.error(response.raw().toString());
            translateAndToastErrorMessage("Network error");
            return false;
        }

        if (!response.body().isSuccessful()) {
            log.error(response.raw().toString());
            translateAndToastErrorMessage(response.body().message);
            return false;
        }

        if (response.body().data == null) {
            log.error(response.raw().toString());
            translateAndToastErrorMessage("Brak danych");
            return false;
        }

        return true;
    }
}
