package mobi.kujon.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import bolts.Continuation;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.LoginActivity;
import mobi.kujon.google_drive.network.KujonException;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Response;

public class ErrorHandlerUtil {

    private static final String TAG = "ErrorHandlerUtil";


    private static Date lastToast = new Date(0);

    private static Map<String, String> errorTranslations = new HashMap<String, String>() {{
        put("only-if-cached", "Tryb offline. Nie wszystkie dane są dostępne");
        put("Bad Gateway", "Wystąpił błąd techniczny. Pracujemy nad rozwiązaniem. Spróbuj za chwilę");
        put("Canceled", "");
    }};

    public static void translateAndToastErrorMessage(String message) {
        String finalMessage = "Error: " + message;
        for (String key : errorTranslations.keySet()) {
            if (!TextUtils.isEmpty(message) && message.contains(key)) {
                finalMessage = errorTranslations.get(key);
            }
        }
        if (checkErrorFrequency() && finalMessage != null && finalMessage.length() > 0) {
            Toast.makeText(KujonApplication.getApplication(), finalMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean checkErrorFrequency() {
        Date now = new Date();
        boolean result = now.getTime() - lastToast.getTime() > 3000;
        lastToast = now;
        return result;
    }

    public static <T> void handleResponseError(Response<KujonResponse<T>> response, String message) {
        translateAndToastErrorMessage(message);
    }

    public static void handleError(Throwable throwable) {
        handleError(throwable, true);
    }

    public static void handleError(Throwable throwable, boolean showToast) {
        throwable.printStackTrace();
        if (showToast) translateAndToastErrorMessage(throwable.getMessage());
    }

    public static <T> boolean handleResponse(Response<KujonResponse<T>> response) {
        return handleResponse(response, false);
    }

    public static void handleKujonError(KujonException e){
        Integer code  = e.getCode();
        if (code != null) {
            if (code == 504) {
                showErrorInRed(e.getMessage());
            } else if (code == 401) {
                Intent intent = new Intent(KujonApplication.getApplication(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                KujonApplication.getApplication().startActivity(intent);
            }
        } else {
            Toast.makeText(KujonApplication.getApplication(), R.string.server_communication_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static <T> boolean handleResponse(Response<KujonResponse<T>> response, boolean acceptNull) {
        if (!response.isSuccessful()) {
            handleResponseError(response, "Network error " + response.message());
            return false;
        }

        if (response.body() == null) {
            handleResponseError(response, "Network error");
            return false;
        }

        Integer code = response.body().code;
        if (code != null) {
            if (code == 504) {
                showErrorInRed(response.body().message);
                return false;
            } else if (code == 401) {
                Intent intent = new Intent(KujonApplication.getApplication(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                KujonApplication.getApplication().startActivity(intent);
            }
        } else {
            Toast.makeText(KujonApplication.getApplication(), R.string.server_communication_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!response.body().isSuccessful()) {
            handleResponseError(response, response.body().message);
            return false;
        }

        if (!acceptNull && response.body().data == null) {
            handleResponseError(response, "Brak danych");
            return false;
        }

        return true;
    }

    public static void showErrorInRed(String message) {
        if (checkErrorFrequency()) {
            LayoutInflater inflater = (LayoutInflater) KujonApplication.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.red_toast, null);

            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(message);

            Toast toast = new Toast(KujonApplication.getApplication());
            toast.setGravity(Gravity.CENTER | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    public static final Continuation<Object, Object> ERROR_HANDLER = task1 -> {
        Exception error = task1.getError();
        if (error != null) {
            System.err.println(error);
            error.printStackTrace();
        }
        return null;
    };

}
