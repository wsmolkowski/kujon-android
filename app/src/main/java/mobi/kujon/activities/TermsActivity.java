package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import mobi.kujon.R;
import mobi.kujon.fragments.TermsFragment;

public class TermsActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.study_cycles);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new TermsFragment()).commit();
    }
}
