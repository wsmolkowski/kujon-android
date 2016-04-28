package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.underscore.$;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.network.json.Coordinator;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mobi.kujon.utils.CommonUtils.showList;

public class CourseDetailsActivity extends BaseActivity {

    public static final String COURSE_ID = "COURSE_ID";
    public static final String TERM_ID = "TERM_ID";

    @Bind(R.id.course_fac) TextView courseFac;
    @Bind(R.id.course_lang) TextView courseLang;
    @Bind(R.id.course_is_conducted) TextView courseIsConducted;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.course_name) TextView courseName;
    @Bind(R.id.bibliography) TextView bibliography;
    @Bind(R.id.assessment_criteria) TextView assessmentCriteria;
    @Bind(R.id.course_term_name) TextView courseTermName;
    @Bind(R.id.course_term_dates) TextView courseTermDates;
    @Bind(R.id.course_lecturers) LinearLayout courseLecturers;
    @Bind(R.id.course_coordinators) LinearLayout courseCoordinators;
    @Bind(R.id.course_class_type) TextView courseClassType;
    @Bind(R.id.course_students) LinearLayout courseStudents;
    @Bind(R.id.scrollView) ScrollView scrollView;

    private LayoutInflater layoutInflater;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        ButterKnife.bind(this);
        layoutInflater = getLayoutInflater();

        Handler handler = new Handler();

        String courseId = getIntent().getStringExtra(COURSE_ID);
        String termId = getIntent().getStringExtra(TERM_ID);

        showProgress(true);
        kujonBackendApi.courseDetails(courseId, termId).enqueue(new Callback<KujonResponse<CourseDetails>>() {
            @Override public void onResponse(Call<KujonResponse<CourseDetails>> call, Response<KujonResponse<CourseDetails>> response) {
                showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    CourseDetails data = response.body().data;

                    getSupportActionBar().setTitle(data.name);

                    courseFac.setText(data.facId.name + ", " + data.facId.postalAddress);
                    courseLang.setText(data.langId);
                    courseIsConducted.setText(data.isCurrentlyConducted);
                    description.setText(Html.fromHtml(data.description.replace("\n", "<br>")));
                    courseName.setText(String.format("%s, (%s)", data.name, data.courseId));
                    bibliography.setText(data.bibliography.replace("\n", "\n\n"));
                    assessmentCriteria.setText(data.assessmentCriteria.replace("\n", "\n\n"));
                    if (data.term != null) {
                        courseTermName.setText(data.term.name);
                        courseTermDates.setText(String.format("%s - %s", data.term.startDate, data.term.endDate));
                    }

                    List<String> lecturers = $.collect(data.lecturers, it -> it.firstName + " " + it.lastName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseLecturers, lecturers, position -> {
                        Lecturer lecturer = data.lecturers.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, lecturer.userId);
                    });

                    List<String> coordinators = $.collect(data.coordinators, it -> it.firstName + " " + it.lastName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseCoordinators, coordinators, position -> {
                        Coordinator coordinator = data.coordinators.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, coordinator.userId);
                    });

                    handler.post(() -> scrollView.scrollTo(0, 0));

                    List<String> participants = $.collect(data.participants, participant -> participant.firstName + " " + participant.lastName);
                    showList(CourseDetailsActivity.this.layoutInflater, courseStudents, participants,
                            position -> StudentDetailsActivity.showStudentDetails(CourseDetailsActivity.this, data.participants.get(position).userId));

                    courseClassType.setText($.join($.collect(data.groups, it -> it.classType + ", numer grupy: " + it.groupNumber), "\n"));
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
        CommonUtils.showOnMap(this, courseFac.getText().toString());
    }
}
