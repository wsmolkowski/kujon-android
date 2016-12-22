package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

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

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new ThesesFragment()).commit();
    }



}
