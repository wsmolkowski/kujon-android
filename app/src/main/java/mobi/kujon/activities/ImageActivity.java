package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;

import static android.text.TextUtils.isEmpty;

public class ImageActivity extends BaseActivity {

    public static final String URI = "URI";
    public static final String TITLE = "TITLE";
    @Bind(R.id.image) ImageView image;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        String uri =  getIntent().getStringExtra(URI);
        String title =  getIntent().getStringExtra(TITLE);
        getSupportActionBar().setTitle(title);

        picasso.load(uri).fit().centerInside().into(image);
    }

    public static void show(Activity activity, String path, String title) {
        if (!isEmpty(path)) {
            Intent intent = new Intent(activity, ImageActivity.class);
            intent.putExtra(URI, path);
            intent.putExtra(TITLE, title);
            activity.startActivity(intent);
        } else {
            Toast.makeText(KujonApplication.getApplication(), R.string.no_photo, Toast.LENGTH_SHORT).show();
        }
    }
}
