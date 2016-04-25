package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.underscore.$;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.CourseEditionsConducted;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerDetailsActivity extends BaseActivity {

    public static final String LECTURER_ID = "LECTURER_ID";

    @Bind(R.id.lecturer_name) TextView lecturerName;
    @Bind(R.id.lecturer_status) TextView lecturerStatus;
    @Bind(R.id.lecturer_room) TextView lecturerRoom;
    @Bind(R.id.lecturer_email) TextView lecturerEmail;
    @Bind(R.id.lecturer_courses) ListView lecturerCourses;
    @Bind(R.id.picture) ImageView picture;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_details);
        ButterKnife.bind(this);

        String lecturerId = getIntent().getStringExtra(LECTURER_ID);

        kujonBackendApi.lecturer(lecturerId).enqueue(new Callback<KujonResponse<LecturerLong>>() {
            @Override public void onResponse(Call<KujonResponse<LecturerLong>> call, Response<KujonResponse<LecturerLong>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    LecturerLong lecturer = response.body().data;
                    String title = TextUtils.isEmpty(lecturer.titles.before) ? "" : lecturer.titles.before;
                    lecturerName.setText(title + " " + lecturer.firstName + " " + lecturer.lastName);
                    lecturerStatus.setText(lecturer.staffStatus);
                    if (lecturer.room != null) {
                        lecturerRoom.setText(lecturer.room.buildingName.pl + ", pokój " + lecturer.room.number);
                    }
                    String email = String.format("<a href=\"%s\">Sprawdź w USOS</a>", lecturer.emailUrl);
                    lecturerEmail.setText(Html.fromHtml(email));
                    lecturerEmail.setMovementMethod(LinkMovementMethod.getInstance());
                    List<String> courses = $.collect(lecturer.courseEditionsConducted, it -> it.courseName);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(LecturerDetailsActivity.this, android.R.layout.simple_list_item_1, courses);
                    lecturerCourses.setAdapter(adapter);
                    lecturerCourses.setOnItemClickListener((parent, view, position, id) -> {
                        CourseEditionsConducted course = lecturer.courseEditionsConducted.get(position);
                        CourseDetailsActivity.showCourseDetails(LecturerDetailsActivity.this, course.courseId, course.termId);
                    });
                    picasso.load(lecturer.hasPhoto).placeholder(R.drawable.user_placeholder).into(picture);
                }
            }

            @Override public void onFailure(Call<KujonResponse<LecturerLong>> call, Throwable t) {
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    public static void showLecturerDatails(Activity activity, String lecturerId) {
        Intent intent = new Intent(activity, LecturerDetailsActivity.class);
        intent.putExtra(LECTURER_ID, lecturerId);
        activity.startActivity(intent);
    }
}
