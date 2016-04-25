package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.underscore.$;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.Coordinator;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @Bind(R.id.course_lecturers) ListView courseLecturers;
    @Bind(R.id.course_coordinators) ListView courseCoordinators;
    @Bind(R.id.course_class_type) TextView courseClassType;
    @Bind(R.id.course_students) TextView courseStudents;
    @Bind(R.id.scrollView) ScrollView scrollView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        ButterKnife.bind(this);

        Handler handler = new Handler();

        String courseId = getIntent().getStringExtra(COURSE_ID);
        String termId = getIntent().getStringExtra(TERM_ID);

        kujonBackendApi.courseDetails(courseId, termId).enqueue(new Callback<KujonResponse<CourseDetails>>() {
            @Override public void onResponse(Call<KujonResponse<CourseDetails>> call, Response<KujonResponse<CourseDetails>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    CourseDetails data = response.body().data;
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
                    courseLecturers.setAdapter(new ArrayAdapter<>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1, lecturers));
                    courseLecturers.setOnItemClickListener((parent, view, position, id) -> {
                        Lecturer lecturer = data.lecturers.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, lecturer.userId);
                    });

                    List<String> coordinators = $.collect(data.coordinators, it -> it.firstName + " " + it.lastName);
                    courseCoordinators.setAdapter(new ArrayAdapter<>(CourseDetailsActivity.this, android.R.layout.simple_list_item_1, coordinators));
                    courseCoordinators.setOnItemClickListener((parent, view, position, id) -> {
                        Coordinator coordinator = data.coordinators.get(position);
                        LecturerDetailsActivity.showLecturerDatails(CourseDetailsActivity.this, coordinator.userId);
                    });

                    handler.post(() -> scrollView.scrollTo(0, 0));

                    courseStudents.setText($.join($.collect(data.participants, it -> it.firstName + " " + it.lastName), "\n"));
                    courseClassType.setText($.join($.collect(data.groups, it -> it.classType + ", numer grupy: " + it.groupNumber), "\n"));
                }
            }

            @Override public void onFailure(Call<KujonResponse<CourseDetails>> call, Throwable t) {
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
}
