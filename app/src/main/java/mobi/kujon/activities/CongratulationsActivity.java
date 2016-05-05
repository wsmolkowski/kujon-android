package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;

public class CongratulationsActivity extends BaseActivity {

    @Bind(R.id.congratulations) TextView congratulations;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        ButterKnife.bind(this);

        String usosName = getIntent().getStringExtra(UsoswebLoginActivity.USOS_NAME);

        congratulations.setText(getString(R.string.congratulations, usosName));
    }

    @OnClick(R.id.next)
    public void next() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
