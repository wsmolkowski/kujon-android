package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.network.json.Usos;

public class CongratulationsActivity extends BaseActivity {

    @Bind(R.id.congratulations) TextView congratulations;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.login_title);

        String usosPojo = getIntent().getStringExtra(UsoswebLoginActivity.USOS_POJO);
        Usos usos = gson.fromJson(usosPojo, Usos.class);

        congratulations.setText(getString(R.string.congratulations, usos.name));
    }

    @OnClick(R.id.next)
    public void next() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
