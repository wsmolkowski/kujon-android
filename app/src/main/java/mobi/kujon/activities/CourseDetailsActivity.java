package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.underscore.$;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.KujonResponse;
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
    @Bind(R.id.course_lecturers) TextView courseLecturers;
    @Bind(R.id.course_coordinators) TextView courseCoordinators;
    @Bind(R.id.course_class_type) TextView courseClassType;
    @Bind(R.id.course_students) TextView courseStudents;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        ButterKnife.bind(this);

        String courseId = getIntent().getStringExtra(COURSE_ID);
        String termId = getIntent().getStringExtra(TERM_ID);

        kujonBackendApi.courseDetails(courseId, termId).enqueue(new Callback<KujonResponse<CourseDetails>>() {
            @Override public void onResponse(Call<KujonResponse<CourseDetails>> call, Response<KujonResponse<CourseDetails>> response) {
                CourseDetails data = response.body().data;
                courseFac.setText(data.facId.name + ", " + data.facId.postalAddress);
                courseLang.setText(data.langId);
                courseIsConducted.setText(data.isCurrentlyConducted);
                description.setText(Html.fromHtml(data.description));
                courseName.setText(String.format("%s, (%s)", data.name, data.courseId));
                bibliography.setText(data.bibliography);
                assessmentCriteria.setText(data.assessmentCriteria);
                courseTermName.setText(data.term.name);
                courseTermDates.setText(String.format("%s - %s", data.term.startDate, data.term.endDate));
                courseLecturers.setText($.join($.collect(data.lecturers, it -> it.firstName + " " + it.lastName), "\n"));
                courseCoordinators.setText($.join($.collect(data.coordinators, it -> it.firstName + " " + it.lastName), "\n"));
                courseStudents.setText($.join($.collect(data.participants, it -> it.firstName + " " + it.lastName), "\n"));
                courseClassType.setText($.join($.collect(data.groups, it -> it.classType + ", numer grupy: " + it.groupNumber), "\n"));
            }

            @Override public void onFailure(Call<KujonResponse<CourseDetails>> call, Throwable t) {
                Crashlytics.logException(t);
            }
        });
    }
}
