package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;

public class TextViewActivity extends BaseActivity {

    public static final String TEXT = "TEXT";
    public static final String TITLE = "TITLE";
    @Bind(R.id.text) TextView text;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        ButterKnife.bind(this);

        String stringExtra = getIntent().getStringExtra(TEXT);
        String title = getIntent().getStringExtra(TITLE);
        text.setText(stringExtra);

        getSupportActionBar().setTitle(title);
    }

    public static void showText(Activity activity, String title, String text){
        Intent intent = new Intent(activity, TextViewActivity.class);
        intent.putExtra(TEXT, text);
        intent.putExtra(TITLE, title);
        activity.startActivity(intent);
    }
}
