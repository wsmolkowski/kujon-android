package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.ProgrammeSingle;
import mobi.kujon.network.json.Programme_;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammeDetailsActivity extends BaseActivity {

    public static final String PRGRAMME_ID_KEY = "PRGRAMME_ID_KEY";


    @Inject
    KujonBackendApi kujonBackendApi;

    @Inject
    KujonUtils kujonUtils;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.programme_full_name)
    TextView programmeFullName;
    @Bind(R.id.programme_id)
    TextView programmeIdTextView;
    @Bind(R.id.programme_duration)
    TextView programmeDuration;
    @Bind(R.id.programme_mode)
    TextView programmeMode;
    @Bind(R.id.programme_name)
    TextView programmeName;
    @Bind(R.id.programme_level)
    TextView programmeLevel;
    @Bind(R.id.programme_ects_sum)
    TextView ectsSum;
    @Bind(R.id.ects_sum_label)
    TextView ectsSumLabel;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    public static final String LEVEL_OF_STUDIES = "LEVEL_OF_STUDIES";
    public static final String DURATION = "DURATION";
    public static final String ID = "ID";
    public static final String MODE = "MODE";
    public static final String NAME = "NAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String ECTS_SUM = "ECTS_SUM";
    private Call<KujonResponse<List<ProgrammeSingle>>> programmesCall;
    private String programmeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
        setContentView(R.layout.activity_programme_details);
        ButterKnife.bind(this);
        KujonApplication.getComponent().inject(this);
        toolbarTitle.setText(R.string.programme);
        if (getIntent().getStringExtra(PRGRAMME_ID_KEY) != null) {
            handleOnlyIdComming(false);
            setUpRefreshLayout();
        } else {
            refreshLayout.setEnabled(false);
            setViews();
        }
    }

    private void setUpRefreshLayout() {
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            kujonUtils.invalidateEntry("programmes/" + programmeId);
            handleOnlyIdComming(true);
        });
    }

    private void handleOnlyIdComming(boolean b) {
        programmeId = getIntent().getStringExtra(PRGRAMME_ID_KEY);
        programmesCall = kujonBackendApi.programmes(programmeId, String.valueOf(b));
        doTheCall(programmesCall);
    }

    private void doTheCall(Call<KujonResponse<List<ProgrammeSingle>>> call) {
        call.enqueue(new Callback<KujonResponse<List<ProgrammeSingle>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<ProgrammeSingle>>> call, Response<KujonResponse<List<ProgrammeSingle>>> response) {
                ProgrammeDetailsActivity.this.showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    processResponse(response);
                }
            }
            @Override
            public void onFailure(Call<KujonResponse<List<ProgrammeSingle>>> call, Throwable t) {
                ProgrammeDetailsActivity.this.showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void processResponse(Response<KujonResponse<List<ProgrammeSingle>>> response) {
        ProgrammeSingle programme = response.body().data.get(0);
        fillUpDataWithProgramme(programme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (programmesCall != null && programmesCall.isExecuted()) {
            programmesCall.cancel();
        }
    }

    private void fillUpDataWithProgramme(ProgrammeSingle programme) {
        programmeFullName.setText(programme.getFullName());
        programmeLevel.setText(programme.levelOfStudies);
        programmeIdTextView.setText(programme.programmeId);
        programmeDuration.setText(programme.duration);
        programmeMode.setText(programme.modeOfStudies);
        programmeName.setText(programme.name);
    }

    private void setViews() {
        Bundle bundle = getIntent().getExtras();
        programmeFullName.setText(bundle.getString(FULL_NAME));
        programmeLevel.setText(bundle.getString(LEVEL_OF_STUDIES));
        programmeIdTextView.setText(bundle.getString(ID));
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

    public static void showProgrammeDetailsWithLoad(Activity activity, String programmeId) {
        Intent intent = new Intent(activity, ProgrammeDetailsActivity.class);
        intent.putExtra(PRGRAMME_ID_KEY, programmeId);
        activity.startActivity(intent);
    }

    private void setRefreshing(boolean refreshing) {
        this.refreshLayout.setRefreshing(refreshing);
    }
}
