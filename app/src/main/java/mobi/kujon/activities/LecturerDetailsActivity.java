package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.underscore.$;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.LecturerLong;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerDetailsActivity extends BaseActivity {

    public static final String LECTURER_ID = "LECTURER_ID";

    @Bind(R.id.lecturer_name) TextView lecturerName;
    @Bind(R.id.lecurerTitle) TextView lecurerTitle;
    @Bind(R.id.lecturer_status) TextView lecturerStatus;
    @Bind(R.id.lecturer_room) TextView lecturerRoom;
    @Bind(R.id.lecturer_email) TextView lecturerEmail;
    @Bind(R.id.lecturer_courses) TextView lecturerCourses;
    @Bind(R.id.picture) ImageView picture;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_details);
        ButterKnife.bind(this);

        String lecturerId = getIntent().getStringExtra(LECTURER_ID);

        kujonBackendApi.lecturer(lecturerId).enqueue(new Callback<KujonResponse<LecturerLong>>() {
            @Override public void onResponse(Call<KujonResponse<LecturerLong>> call, Response<KujonResponse<LecturerLong>> response) {
                LecturerLong lecturer = response.body().data;
                lecturerName.setText(lecturer.firstName + " " + lecturer.lastName);
                lecurerTitle.setText(lecturer.titles.before);
                lecturerStatus.setText(lecturer.staffStatus);
                lecturerRoom.setText(lecturer.room);
                lecturerEmail.setText(lecturer.emailUrl);
                lecturerCourses.setText($.join($.collect(lecturer.courseEditionsConducted, it -> it.courseName), "\n"));
                picasso.load(lecturer.hasPhoto).placeholder(R.drawable.user_placeholder).into(picture);
            }

            @Override public void onFailure(Call<KujonResponse<LecturerLong>> call, Throwable t) {
                Crashlytics.logException(t);
            }
        });
    }
}
