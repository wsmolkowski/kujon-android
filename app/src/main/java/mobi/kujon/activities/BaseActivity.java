package mobi.kujon.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    public static final String LOGGED = "logged";
    protected SharedPreferences kujonPreferences;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kujonPreferences = getSharedPreferences("kujon_preferences", MODE_PRIVATE);
        handleLoginStatus(kujonPreferences.getBoolean(LOGGED, false));
    }

    protected void handleLoginStatus(boolean logged) {
        if (!logged) {
            Log.w(TAG, "Not logged in. Finish activity");
            finish();
        }
    }
}
