package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.fragments.ThesesFragment;
import mobi.kujon.network.json.gen.Thesis;

public class ThesesActivity extends BaseActivity {
    public static final String THESE_KEY = "THESE_KEY";
    public static void openWithThese(Activity activity, Thesis thesis){
        Intent intent  = new Intent(activity,ThesesActivity.class);
        Gson gson = new Gson();
        String  s = gson.toJson(thesis);
        intent.putExtra(THESE_KEY,s);
        activity.startActivity(intent);
    }
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theses);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.placeholder, new ThesesFragment()).commit();
    }


    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
}
