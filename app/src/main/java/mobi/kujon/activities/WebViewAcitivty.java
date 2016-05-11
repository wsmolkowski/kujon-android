package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;

public class WebViewAcitivty extends BaseActivity {

    public static final String URL = "URL";
    @Bind(R.id.webView) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        getSupportActionBar().setTitle(R.string.regulations_activity_title);

        String url = getIntent().getStringExtra(URL);
        webView.loadUrl(url);
    }

    public static void showUrl(Activity activity, String url) {
        Intent intent = new Intent(activity, WebViewAcitivty.class);
        intent.putExtra(URL, url);
        activity.startActivity(intent);
    }
}
