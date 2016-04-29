package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;

public class CongratulationsActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.next)
    public void next() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
