package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.ProgrammeSingle;
import mobi.kujon.network.json.StudentProgramme;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mobi.kujon.R.string.programme;

public class ProgrammeDetailsActivity extends BaseActivity {

    public static final String PRGRAMME_ID_KEY = "PRGRAMME_ID_KEY";
    public static final String STATUS = "STATUS";
    public static final String IMAGERES = "IMAGERES";


    @Inject
    KujonBackendApi kujonBackendApi;

    @Inject
    KujonUtils kujonUtils;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.programme_full_name)
    TextView programmeFullName;
    @BindView(R.id.programme_id)
    TextView programmeIdTextView;
    @BindView(R.id.programme_duration)
    TextView programmeDuration;
    @BindView(R.id.programme_mode)
    TextView programmeMode;
    @BindView(R.id.programme_name)
    TextView programmeName;
    @BindView(R.id.programme_level)
    TextView programmeLevel;
    @BindView(R.id.programme_ects_sum)
    TextView ectsSum;
    @BindView(R.id.ects_sum_label)
    TextView ectsSumLabel;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.status_text)
    TextView statusTextView;

    public static final String LEVEL_OF_STUDIES = "LEVEL_OF_STUDIES";
    public static final String DURATION = "DURATION";
    public static final String ID = "ID";
    public static final String MODE = "MODE";
    public static final String NAME = "NAME";
    public static final String FULL_NAME = "FULL_NAME";
    public static final String ECTS_SUM = "ECTS_SUM";
    private String programmeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
        setContentView(R.layout.activity_programme_details);
        ButterKnife.bind(this);
        KujonApplication.getComponent().inject(this);
        toolbarTitle.setText(programme);
        if (getIntent().getStringExtra(PRGRAMME_ID_KEY) != null) {
            ectsSum.setVisibility(View.GONE);
            ectsSumLabel.setVisibility(View.GONE);
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
        Call<KujonResponse<List<ProgrammeSingle>>> programmesCall = kujonBackendApi.programmes(programmeId, String.valueOf(b));
        cancelLastCallIfExist();
        doTheCall(programmesCall);
        backendCall = programmesCall;
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


    private void fillUpDataWithProgramme(ProgrammeSingle programme) {
        programmeFullName.setText(programme.getFullName());
        programmeLevel.setText(programme.levelOfStudies);
        programmeIdTextView.setText(programme.programmeId);
        programmeDuration.setText(programme.duration);
        programmeMode.setText(programme.modeOfStudies);
        programmeName.setText(programme.name);
        fillUpStatus(getIntent().getExtras());

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
        fillUpStatus(bundle);

    }

    private void fillUpStatus(Bundle bundle) {
        if (bundle.getInt(STATUS) != 0) {
            statusTextView.setText(bundle.getInt(STATUS));
            statusTextView.setCompoundDrawablesWithIntrinsicBounds(bundle.getInt(IMAGERES), 0, 0, 0);
        } else {
            statusTextView.setVisibility(View.GONE);
        }
    }

    public static void showProgrammeDetails(Activity activity, StudentProgramme studentProgramme, Programme programmeFull, String name) {
        Programme programme = programmeFull;
        Intent intent = new Intent(activity, ProgrammeDetailsActivity.class);
        intent.putExtra(LEVEL_OF_STUDIES, programme.levelOfStudies);
        intent.putExtra(DURATION, programme.duration);
        intent.putExtra(ID, programme.id);
        intent.putExtra(MODE, programme.modeOfStudies);
        intent.putExtra(NAME, programme.description);
        intent.putExtra(FULL_NAME, name);
        intent.putExtra(ECTS_SUM, programme.ectsUsedSum);
        try {
            intent.putExtra(STATUS, studentProgramme.getGraduateText());
            intent.putExtra(IMAGERES, studentProgramme.getImage());
        }catch (NullPointerException npe){

        }
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

    public static void showProgrammeDetailsWithLoad(Activity activity, StudentProgramme studentProgramme) {
        Intent intent = new Intent(activity, ProgrammeDetailsActivity.class);
        intent.putExtra(PRGRAMME_ID_KEY, studentProgramme.programme.id);
        try {
            intent.putExtra(STATUS, studentProgramme.getGraduateText());
            intent.putExtra(IMAGERES, studentProgramme.getImage());
        }catch (NullPointerException npe){
            intent.putExtra(STATUS, 0);
            intent.putExtra(IMAGERES, 0);
        }
        activity.startActivity(intent);
    }

    private void setRefreshing(boolean refreshing) {
        this.refreshLayout.setRefreshing(refreshing);
    }
}
