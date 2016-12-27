package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.ProgrammeSingle;
import mobi.kujon.network.json.Programme_;

public class ProgrammeDetailsActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.programme_full_name) TextView programmeFullName;
    @Bind(R.id.programme_id) TextView programmeId;
    @Bind(R.id.programme_duration) TextView programmeDuration;
    @Bind(R.id.programme_mode) TextView programmeMode;
    @Bind(R.id.programme_name) TextView programmeName;
    @Bind(R.id.programme_level) TextView programmeLevel;
    @Bind(R.id.programme_ects_sum) TextView ectsSum;
    @Bind(R.id.ects_sum_label) TextView ectsSumLabel;


    public static final String LEVEL_OF_STUDIES = "LEVEL_OF_STUDIES";
    public static final String DURATION = "DURATION";
    public static final String ID = "ID";
    public static final String MODE = "MODE";
    public static final String NAME = "NAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String ECTS_SUM = "ECTS_SUM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme_details);
        ButterKnife.bind(this);
        KujonApplication.getComponent().inject(this);
        toolbarTitle.setText(R.string.programme);
        setViews();
    }

    private void setViews() {
        Bundle bundle = getIntent().getExtras();
        programmeFullName.setText(bundle.getString(FULL_NAME));
        programmeLevel.setText(bundle.getString(LEVEL_OF_STUDIES));
        programmeId.setText(bundle.getString(ID));
        programmeDuration.setText(bundle.getString(DURATION));
        programmeMode.setText(bundle.getString(MODE));
        ectsSum.setText(bundle.getString(ECTS_SUM));
        programmeName.setText(bundle.getString(NAME));
    }

    public static void showProgrammeDetails(Activity activity, Programme_ programme, String name) {
        Intent intent = new Intent(activity, ProgrammeDetailsActivity.class);
        intent.putExtra(LEVEL_OF_STUDIES, programme.levelOfStudies);
        intent.putExtra(DURATION, programme.duration);
        intent.putExtra(ID, programme.id);
        intent.putExtra(MODE, programme.modeOfStudies);
        intent.putExtra(NAME, programme.description);
        intent.putExtra(FULL_NAME, name);
        intent.putExtra(ECTS_SUM, programme.ectsUsedSum);
        activity.startActivity(intent);
    }

    public static void showProgrammeDetails(Activity activity, mobi.kujon.network.json.gen.Programme programme, String name) {
        Intent intent = new Intent(activity, ProgrammeDetailsActivity.class);
        intent.putExtra(LEVEL_OF_STUDIES, programme.level_of_studies);
        intent.putExtra(DURATION, programme.duration);
        intent.putExtra(ID, programme.id);
        intent.putExtra(MODE, programme.mode_of_studies);
        intent.putExtra(NAME, programme.name);
        intent.putExtra(FULL_NAME, name);
        activity.startActivity(intent);
    }

    public static void showProgrammeDetails(Activity activity, ProgrammeSingle programme, String name) {
        Intent intent = new Intent(activity, ProgrammeDetailsActivity.class);
        intent.putExtra(LEVEL_OF_STUDIES, programme.levelOfStudies);
        intent.putExtra(DURATION, programme.duration);
        intent.putExtra(ID, programme.programmeId);
        intent.putExtra(MODE, programme.modeOfStudies);
        intent.putExtra(NAME, programme.name);
        intent.putExtra(FULL_NAME, name);
        activity.startActivity(intent);
    }
}
