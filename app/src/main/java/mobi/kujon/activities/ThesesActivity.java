package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import mobi.kujon.fragments.ThesesFragment;

public class ThesesActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Prace dyplomowe");
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new ThesesFragment()).commit();
    }
}
