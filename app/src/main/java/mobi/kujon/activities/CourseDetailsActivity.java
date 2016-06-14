package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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
    @Bind(R.id.course_id) TextView courseIdTextView;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private LayoutInflater layoutInflater;
    private CourseDetails courseDetails;

    private String courseId;
    private String termId;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        ButterKnife.bind(this);
        layoutInflater = getLayoutInflater();
        getSupportActionBar().setTitle(R.string.course_title);

        courseId = getIntent().getStringExtra(COURSE_ID);
        termId = getIntent().getStringExtra(TERM_ID);

        swipeContainer.setOnRefreshListener(() -> loadData(true));
        handler.post(() -> loadData(false));
    }

    private void loadData(boolean refresh) {
        if (refresh) {
            utils.invalidateEntry("courseseditions/" + courseId + "/" + termId);
        }

        swipeContainer.setRefreshing(true);
        Call<KujonResponse<CourseDetails>> call = refresh ? kujonBackendApi.courseDetailsRefresh(courseId, termId) : kujonBackendApi.courseDetails(courseId, termId);
        call.enqueue(new Callback<KujonResponse<CourseDetails>>() {
            @Override public void onResponse(Call<KujonResponse<CourseDetails>> call, Response<KujonResponse<CourseDetails>> response) {
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    courseDetails = response.body().data;

                    courseFac.setText(courseDetails.facId.name);
                    description.setText(Html.fromHtml(courseDetails.description.replace("\n", "<br>")));
                    courseName.setText(courseDetails.name);
                    courseIdTextView.setText(String.format("Id: %s, język: %s, prowadzony: %s", courseDetails.courseId, courseDetails.langId, courseDetails.isCurrentlyConducted));
                    bibliography.setText(courseDetails.bibliography.replace("\n", "\n\n"));
                    assessmentCriteria.setText(courseDetails.assessmentCriteria.replace("\n", "\n\n"));
                    if (courseDetails.term != null) {
                        courseTermName.setText(courseDetails.term.name);
                    }

                    List<String> lecturers = $.collect(courseDetails.lecturers, it -> it.lastName + " " + it.firstName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseLecturers, lecturers, position -> {
                        Lecturer lecturer = courseDetails.lecturers.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, lecturer.userId);
                    });

                    List<String> coordinators = $.collect(courseDetails.coordinators, it -> it.lastName + " " + it.firstName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseCoordinators, coordinators, position -> {
                        Coordinator coordinator = courseDetails.coordinators.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, coordinator.userId);
                    });

                    handler.post(() -> scrollView.scrollTo(0, 0));

                    List<String> participants = $.collect(courseDetails.participants, participant -> participant.lastName + " " + participant.firstName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseStudents, participants,
                            position -> StudentDetailsActivity.showStudentDetails(CourseDetailsActivity.this, courseDetails.participants.get(position).userId));

                    courseClassType.setText($.join($.collect(courseDetails.groups, it -> it.classType + ", numer grupy: " + it.groupNumber), "\n"));
                }
            }

            @Override public void onFailure(Call<KujonResponse<CourseDetails>> call, Throwable t) {
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    public static void showCourseDetails(Activity activity, String courseId, String termId) {
        Intent intent = new Intent(activity, CourseDetailsActivity.class);
        intent.putExtra(COURSE_ID, courseId);
        intent.putExtra(TERM_ID, termId);
        activity.startActivity(intent);
    }

    @OnClick(R.id.course_fac)
    public void navigate() {
        if (courseDetails != null && courseDetails.facId != null) {
            FacultyDetailsActivity.showFacultyDetails(this, courseDetails.facId.facId);
        }
    }

    @OnClick(R.id.description)
    public void showDesc() {
        TextViewActivity.showText(this, "Opis", description.getText().toString());
    }

    @OnClick(R.id.bibliography)
    public void showBibliography() {
        TextViewActivity.showText(this, "Bibliografia", bibliography.getText().toString());
    }

    @OnClick(R.id.assessment_criteria)
    public void showAssessmentCriteria() {
        TextViewActivity.showText(this, "Kryteria oceny", assessmentCriteria.getText().toString());
    }

    @OnClick(R.id.course_term_name)
    public void termDesc() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        View termView = getLayoutInflater().inflate(R.layout.row_terms, null);
        TermsFragment.ViewHolder holder = new TermsFragment.ViewHolder(termView);

        Term2 term = courseDetails.term;
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

