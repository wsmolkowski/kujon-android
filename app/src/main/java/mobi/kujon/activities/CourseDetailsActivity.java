package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.fragments.TermsFragment;
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

    public static final String COURSE_ID = "COURSE_ID";
    public static final String TERM_ID = "TERM_ID";

    @Bind(R.id.course_fac) TextView courseFac;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.course_name) TextView courseName;
    @Bind(R.id.bibliography) TextView bibliography;
    @Bind(R.id.assessment_criteria) TextView assessmentCriteria;
    @Bind(R.id.course_term_name) TextView courseTermName;
    @Bind(R.id.course_lecturers) LinearLayout courseLecturers;
    @Bind(R.id.course_coordinators) LinearLayout courseCoordinators;
    @Bind(R.id.course_class_type) TextView courseClassType;
    @Bind(R.id.course_students) LinearLayout courseStudents;
    @Bind(R.id.scrollView) ScrollView scrollView;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.lang) TextView langView;
    @Bind(R.id.is_conducted) TextView isConductedView;
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Bind(R.id.lang_label) TextView langLabel;
    @Bind(R.id.is_conducted_label) TextView isConductedLabel;
    @Bind(R.id.description_label) TextView descriptionLabel;
    @Bind(R.id.bibliography_label) TextView bibliographyLabel;
    @Bind(R.id.assessment_criteria_label) TextView assessmentCriteriaLabel;
    @Bind(R.id.course_term_name_label) TextView courseTermNameLabel;
    @Bind(R.id.course_class_type_label) TextView courseClassTypeLabel;
    @Bind(R.id.course_lecturers_label) TextView courseLecturersLabel;
    @Bind(R.id.course_coordinators_label) TextView courseCoordinatorsLabel;
    @Bind(R.id.course_students_label) TextView courseStudentsLabel;

    private LayoutInflater layoutInflater;
    private CourseDetails courseDetails;

    private String courseId;
    private String termId;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
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

    private void loadData(boolean refresh) {
        Call<KujonResponse<CourseDetails>> call = getCourseCall(refresh);

        if (refresh) {
            swipeContainer.setRefreshing(true);
        } else {
            showProgress(true);
        }

        call.enqueue(new Callback<KujonResponse<CourseDetails>>() {
            @Override public void onResponse(Call<KujonResponse<CourseDetails>> call, Response<KujonResponse<CourseDetails>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    courseDetails = response.body().data;

                    courseFac.setText(courseDetails.facId.name);

                    setText(description, descriptionLabel, Html.fromHtml(CourseDetailsActivity.this.courseDetails.description.replace("\n", "<br>")));
                    courseName.setText(String.format("%s (%s)", courseDetails.name, courseDetails.courseId));
                    setText(langView, langLabel, isEmpty(courseDetails.langId) ? "Brak" : courseDetails.langId);
                    setText(isConductedView, isConductedLabel, courseDetails.isCurrentlyConducted);
                    setText(bibliography, bibliographyLabel, courseDetails.bibliography.replace("\n", "\n\n"));
                    setText(assessmentCriteria, assessmentCriteriaLabel, courseDetails.assessmentCriteria.replace("\n", "\n\n"));
                    assessmentCriteria.setText(courseDetails.assessmentCriteria.replace("\n", "\n\n"));
                    if (courseDetails.term != null && courseDetails.term.size() > 0 && courseDetails.term.get(0).name.length() > 0) {
                        courseTermName.setText(courseDetails.term.get(0).name);
                    } else {
                        courseTermName.setVisibility(View.GONE);
                        courseTermNameLabel.setVisibility(View.GONE);
                    }

                    setText(courseClassType, courseClassTypeLabel, $.join($.collect(courseDetails.groups, it -> it.classType + ", numer grupy: " + it.groupNumber), "\n"));

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

            @Override public void onFailure(Call<KujonResponse<CourseDetails>> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void setText(TextView textView, TextView label, Spanned text) {
        textView.setText(text);
        if (text.toString().length() == 0) {
            textView.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        }
    }

    private void setText(TextView textView, TextView label, String text) {
        textView.setText(text);
        if (text.length() == 0) {
            textView.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        }
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
            holder.name.setText(term.name);
            holder.termId.setText(term.termId);
            holder.active.setText(term.active ? "TAK" : "NIE");
            holder.startDate.setText(term.startDate);
            holder.endDate.setText(term.endDate);
            holder.finishDate.setText(term.finishDate);

            dlgAlert.setView(termView);
            dlgAlert.setTitle(term.name);
            dlgAlert.setCancelable(false);
            dlgAlert.setNegativeButton("OK", (dialog, which) -> {
                dialog.dismiss();
            });
            dlgAlert.create().show();
        }
    }
}

