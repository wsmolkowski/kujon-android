package mobi.kujon.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import mobi.kujon.R;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected SharedPreferences kujonPreferences;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kujonPreferences = getSharedPreferences("kujon_preferences", MODE_PRIVATE);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
