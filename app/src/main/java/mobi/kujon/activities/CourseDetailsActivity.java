package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.underscore.$;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.fragments.TermsFragment;
import mobi.kujon.google_drive.ui.activities.files.FilesActivity;
import mobi.kujon.network.json.Coordinator;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.network.json.Term2;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static mobi.kujon.utils.CommonUtils.showList;

public class CourseDetailsActivity extends BaseActivity {

    public static void showCourseDetails(Fragment fragment, String courseId, String termId, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), CourseDetailsActivity.class);
        intent.putExtra(COURSE_ID, courseId);
        intent.putExtra(TERM_ID, termId);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showCourseDetails(Activity activity, String courseId, String termId) {
        Intent intent = new Intent(activity, CourseDetailsActivity.class);
        intent.putExtra(COURSE_ID, courseId);
        intent.putExtra(TERM_ID, termId);
        activity.startActivity(intent);
    }

    public static void showCourseDetails(Activity activity, String courseId) {
        Intent intent = new Intent(activity, CourseDetailsActivity.class);
        intent.putExtra(COURSE_ID, courseId);
        activity.startActivity(intent);
    }

    public static final String COURSE_ID = "COURSE_ID";
    public static final String TERM_ID = "TERM_ID";
    public static final int REQUEST_CODE_OPEN_FILE_ACTIVITY = 2647;

    @BindView(R.id.course_fac)
    TextView courseFac;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.course_name)
    TextView courseName;
    @BindView(R.id.bibliography)
    TextView bibliography;
    @BindView(R.id.assessment_criteria)
    TextView assessmentCriteria;
    @BindView(R.id.course_term_name)
    TextView courseTermName;
    @BindView(R.id.course_lecturers)
    LinearLayout courseLecturers;
    @BindView(R.id.course_coordinators)
    LinearLayout courseCoordinators;
    @BindView(R.id.course_class_type)
    TextView courseClassType;
    @BindView(R.id.course_students)
    LinearLayout courseStudents;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.before_layout)
    View beforeLayout;

    @BindView(R.id.description_label)
    TextView descriptionLabel;
    @BindView(R.id.bibliography_label)
    TextView bibliographyLabel;
    @BindView(R.id.assessment_criteria_label)
    TextView assessmentCriteriaLabel;
    @BindView(R.id.course_term_name_label)
    TextView courseTermNameLabel;
    @BindView(R.id.course_class_type_label)
    TextView courseClassTypeLabel;
    @BindView(R.id.course_lecturers_label)
    TextView courseLecturersLabel;
    @BindView(R.id.course_coordinators_label)
    TextView courseCoordinatorsLabel;
    @BindView(R.id.course_students_label)
    TextView courseStudentsLabel;
    @BindView(R.id.course_additional_info)
    TextView courseAdditionalInfo;
    @BindView(R.id.course_files)
    View sharedFiles;
    @BindView(R.id.files_count)
    TextView fileCount;
    private LayoutInflater layoutInflater;
    private CourseDetails courseDetails;

    private String courseId;
    private String termId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        ButterKnife.bind(this);
        layoutInflater = getLayoutInflater();
        toolbarTitle.setText(R.string.course_title);

        courseId = getIntent().getStringExtra(COURSE_ID);
        termId = getIntent().getStringExtra(TERM_ID);

        swipeContainer.setOnRefreshListener(() -> loadData(true));
        showProgress(true);
        handler.post(() -> loadData(false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_FILE_ACTIVITY && resultCode == RESULT_OK) {
            if (termId != null) {
                utils.invalidateEntry("courseseditions/" + courseId + "/" + termId);
            } else {
                utils.invalidateEntry("courses/" + courseId);
            }
            this.setResult(RESULT_OK);
            loadData(false);
        }
    }

    private void loadData(boolean refresh) {
        cancelLastCallIfExist();
        Call<KujonResponse<CourseDetails>> call = getCourseCall(refresh);

        if (refresh) {
            swipeContainer.setRefreshing(true);
        } else {
            showProgress(true);
        }

        call.enqueue(new Callback<KujonResponse<CourseDetails>>() {
            @Override
            public void onResponse(Call<KujonResponse<CourseDetails>> call, Response<KujonResponse<CourseDetails>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    courseDetails = response.body().data;
                    sharedFiles.setOnClickListener(v -> FilesActivity.openActivity(CourseDetailsActivity.this, courseId, termId, courseDetails.name, REQUEST_CODE_OPEN_FILE_ACTIVITY));
                    courseFac.setText(courseDetails.facId.name);
                    fileCount.setText(String.valueOf(courseDetails.fileCount));
                    courseName.setText(courseDetails.name);
                    courseAdditionalInfo.setText(String.format("id: %s, %s: %s, %s: %s", courseDetails.courseId, getString(R.string.language),
                            isEmpty(courseDetails.langId) ? getString(R.string.missing) : courseDetails.langId, getString(R.string.is_currently_conducted),
                            courseDetails.isCurrentlyConducted));


                    handle4Layouts();


                    if (courseDetails.term != null && courseDetails.term.size() > 0 && courseDetails.term.get(0).name.length() > 0) {
                        courseTermName.setText(courseDetails.term.get(0).name);
                    } else {
                        courseTermName.setVisibility(View.GONE);
                        courseTermNameLabel.setVisibility(View.GONE);
                    }


                    List<String> lecturers = $.collect(courseDetails.lecturers, it -> it.lastName + " " + it.firstName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseLecturers, lecturers, position -> {
                        Lecturer lecturer = courseDetails.lecturers.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, lecturer.userId);
                    });

                    courseLecturers.setVisibility(lecturers.size() > 0 ? View.VISIBLE : View.GONE);
                    courseLecturersLabel.setVisibility(lecturers.size() > 0 ? View.VISIBLE : View.GONE);

                    List<String> coordinators = $.collect(courseDetails.coordinators, it -> it.lastName + " " + it.firstName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseCoordinators, coordinators, position -> {
                        Coordinator coordinator = courseDetails.coordinators.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, coordinator.userId);
                    });

                    courseCoordinators.setVisibility(coordinators.size() > 0 ? View.VISIBLE : View.GONE);
                    courseCoordinatorsLabel.setVisibility(coordinators.size() > 0 ? View.VISIBLE : View.GONE);

                    List<String> participants = $.collect(courseDetails.participants, participant -> participant.lastName + " " + participant.firstName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseStudents, participants,
                            position -> StudentDetailsActivity.showStudentDetails(CourseDetailsActivity.this, courseDetails.participants.get(position).userId));

                    courseStudents.setVisibility(participants.size() > 0 ? View.VISIBLE : View.GONE);
                    courseStudentsLabel.setVisibility(participants.size() > 0 ? View.VISIBLE : View.GONE);

                    handler.post(() -> scrollView.scrollTo(0, 0));

                }
                handler.postDelayed(() -> {
                    swipeContainer.setRefreshing(false);
                    showProgress(false);
                }, 200);
            }

            @Override
            public void onFailure(Call<KujonResponse<CourseDetails>> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
        backendCall  = call;
    }

    private void handle4Layouts() {
        if (setText(bibliography, bibliographyLabel, Html.fromHtml(courseDetails.bibliography.replace("\n", "<br/><br/>"))) &
                setText(assessmentCriteria, assessmentCriteriaLabel, Html.fromHtml(courseDetails.assessmentCriteria.replace("\n", "<br/><br/>"))) &
                setText(description, descriptionLabel, Html.fromHtml(CourseDetailsActivity.this.courseDetails.description.replace("\n", "<br>"))) &
                setText(courseClassType, courseClassTypeLabel, $.join($.collect(courseDetails.groups, it -> it.classType + ", " + getString(R.string.group_number) + ": " + it.groupNumber), "\n"))) {
            beforeLayout.setVisibility(View.GONE);
        }
    }

    private boolean setText(TextView textView, TextView label, Spanned text) {
        textView.setText(text);
        if (text.toString().length() == 0) {
            textView.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private boolean setText(TextView textView, TextView label, String text) {
        textView.setText(text);
        if (text.length() == 0) {
            textView.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private Call<KujonResponse<CourseDetails>> getCourseCall(boolean refresh) {
        if (termId != null) {
            if (refresh) utils.invalidateEntry("courseseditions/" + courseId + "/" + termId);
            return refresh ? kujonBackendApi.courseDetailsRefresh(courseId, termId) : kujonBackendApi.courseDetails(courseId, termId);
        } else {
            if (refresh) utils.invalidateEntry("courses/" + courseId);
            return refresh ? kujonBackendApi.courseDetailsRefresh(courseId) : kujonBackendApi.courseDetails(courseId);
        }
    }


    @OnClick(R.id.course_fac)
    public void navigate() {
        if (courseDetails != null && courseDetails.facId != null) {
            FacultyDetailsActivity.showFacultyDetails(this, courseDetails.facId.facId);
        }
    }

    @OnClick(R.id.course_term_name)
    public void termDesc() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        View termView = getLayoutInflater().inflate(R.layout.row_terms, null);
        TermsFragment.ViewHolder holder = new TermsFragment.ViewHolder(termView);

        if (courseDetails.term != null && courseDetails.term.size() > 0) {
            Term2 term = courseDetails.term.get(0);
            holder.termName.setText(term.name);
            holder.termId.setText(term.termId);
            holder.section.setText(term.active ? getString(R.string.active) : getString(R.string.inactive));
            holder.startDate.setText(term.startDate);
            holder.endDate.setText(term.endDate);
            holder.finishDate.setText(term.finishDate);

            dlgAlert.setView(termView);
            dlgAlert.setCancelable(false);
            dlgAlert.setNegativeButton(R.string.ok, (dialog, which) -> {
                dialog.dismiss();
            });
            dlgAlert.create().show();
        }
    }
}

