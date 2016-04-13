package mobi.kujon.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;

public class UsoswebLoginActivity extends BaseActivity {

    public static final String USOS_URL = "USOS_URL";

    @Bind(R.id.webView) WebView webView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usos_login);
        ButterKnife.bind(this);

        String usosUrl = getIntent().getStringExtra(USOS_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

        });
        webView.loadUrl("https://usosapps.demo.usos.edu.pl/apps/authorize?oauth_token=aHn6w2zaJY8RwERdVCP9&interactivity=minimal");
    }
}
