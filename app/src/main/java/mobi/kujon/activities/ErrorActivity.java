package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import mobi.kujon.R;

public class ErrorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        getSupportActionBar().setTitle("Błąd systemu USOS");
    }

    public static void open(Activity from) {
        from.startActivity(new Intent(from, ErrorActivity.class));
    }
}
