package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.underscore.$;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.network.json.CourseEditionsConducted;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.ui.CircleTransform;
import mobi.kujon.utils.CommonUtils;
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
    @Bind(R.id.lecturer_courses) LinearLayout lecturerCourses;
    @Bind(R.id.picture) ImageView picture;
    @Bind(R.id.lecturer_homepage) TextView lecturerHomepage;
    @Bind(R.id.lecturer_employment_positions) TextView lecturerEmploymentPositions;
    @Bind(R.id.lecturer_office_hours) TextView lecturerOfficeHours;
    @Bind(R.id.lecturer_interests) TextView lecturerInterests;
    private LayoutInflater layoutInflater;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_details);
        ButterKnife.bind(this);
        layoutInflater = getLayoutInflater();

        String lecturerId = getIntent().getStringExtra(LECTURER_ID);

        kujonBackendApi.lecturer(lecturerId).enqueue(new Callback<KujonResponse<LecturerLong>>() {
            @Override public void onResponse(Call<KujonResponse<LecturerLong>> call, Response<KujonResponse<LecturerLong>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    LecturerLong lecturer = response.body().data;
                    String title = TextUtils.isEmpty(lecturer.titles.before) ? "" : lecturer.titles.before;
                    String name = title + " " + lecturer.firstName + " " + lecturer.lastName;
                    lecturerName.setText(name);
                    getSupportActionBar().setTitle(name);
                    lecturerStatus.setText(lecturer.staffStatus);
                    if (lecturer.room != null) {
                        lecturerRoom.setText(lecturer.room.buildingName.pl + ", pokój " + lecturer.room.number);
                    }
                    String email = String.format("<a href=\"%s\">Sprawdź w USOS</a>", lecturer.emailUrl);
                    lecturerEmail.setText(Html.fromHtml(email));
                    lecturerEmail.setMovementMethod(LinkMovementMethod.getInstance());
                    lecturerInterests.setText(lecturer.interests);
                    lecturerOfficeHours.setText(lecturer.officeHours);

                    List<String> data = $.collect(lecturer.courseEditionsConducted, it -> it.courseName);
                    CommonUtils.showList(layoutInflater, lecturerCourses, data, position -> {
                        CourseEditionsConducted course = lecturer.courseEditionsConducted.get(position);
                        CourseDetailsActivity.showCourseDetails(LecturerDetailsActivity.this, course.courseId, course.termId);
                    });

                    picasso.load(lecturer.hasPhoto)
                            .transform(new CircleTransform())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.user_placeholder)
                            .into(picture);

                    picture.setOnClickListener(v -> ImageActivity.show(LecturerDetailsActivity.this, lecturer.hasPhoto, name));

                    lecturerHomepage.setText(lecturer.homepageUrl);
                    lecturerEmploymentPositions.setText($.join($.collect(lecturer.employmentPositions, it -> it.faculty.name + ", " + it.position.name), "\n\n"));
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

    @OnClick(R.id.lecturer_room)
    public void navigate() {
        if (TextUtils.isEmpty(lecturerRoom.getText().toString())) {
            Toast.makeText(LecturerDetailsActivity.this, "Brak adresu", Toast.LENGTH_SHORT).show();
        } else {
            CommonUtils.showOnMap(this, lecturerRoom.getText().toString());
        }
    }
}
