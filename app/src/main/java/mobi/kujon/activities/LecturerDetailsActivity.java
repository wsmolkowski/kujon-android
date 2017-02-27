package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.underscore.$;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.CourseEditionsConducted;
import mobi.kujon.network.json.EmploymentPosition;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.ui.CircleTransform;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class LecturerDetailsActivity extends BaseActivity {

    public static final String LECTURER_ID = "LECTURER_ID";

    @Bind(R.id.name) TextView lecturerName;
    @Bind(R.id.lecturer_status) TextView lecturerStatus;
    @Bind(R.id.lecturer_room) TextView lecturerRoom;
    @Bind(R.id.lecturer_email) TextView lecturerEmail;
    @Bind(R.id.lecturer_courses) LinearLayout lecturerCourses;
    @Bind(R.id.lecturer_faculty) LinearLayout lecturersFaculty;
    @Bind(R.id.picture) ImageView picture;
    @Bind(R.id.lecturer_homepage) TextView lecturerHomepage;
    @Bind(R.id.lecturer_office_hours) TextView lecturerOfficeHours;
    @Bind(R.id.lecturer_interests) TextView lecturerInterests;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Inject protected KujonUtils utils;

    private LayoutInflater layoutInflater;
    private String lecturerId;
    private LecturerLong lecturer;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        KujonApplication.getComponent().inject(this);
        layoutInflater = getLayoutInflater();
        toolbarTitle.setText(R.string.lecturer_title);

        lecturerId = getIntent().getStringExtra(LECTURER_ID);
        swipeContainer.setOnRefreshListener(() -> loadData(true));
        showProgress(true);
        handler.post(() -> loadData(false));
    }

    private void loadData(boolean refresh) {
        if (refresh) {
            swipeContainer.setRefreshing(true);
        } else {
            showProgress(true);
        }

        if (refresh) {
            utils.invalidateEntry("lecturers/" + lecturerId);
            if (lecturer != null) {
                if (!isEmpty(lecturer.photoUrl)) picasso.invalidate(lecturer.photoUrl);
            }
        }

        Call<KujonResponse<LecturerLong>> lecturerCall = refresh ? kujonBackendApi.lecturerRefresh(lecturerId) : kujonBackendApi.lecturer(lecturerId);
        lecturerCall.enqueue(new Callback<KujonResponse<LecturerLong>>() {
            @Override public void onResponse(Call<KujonResponse<LecturerLong>> call, Response<KujonResponse<LecturerLong>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    lecturer = response.body().data;
                    String titleBefore = "";
                    String titleAfter = "";
                    if (lecturer.titles != null) {
                        titleBefore = isEmpty(lecturer.titles.before) ? "" : lecturer.titles.before;
                        titleAfter = isEmpty(lecturer.titles.after) ? "" : ", " + lecturer.titles.after;
                    }
                    String name = titleBefore + " " + lecturer.firstName + " " + lecturer.lastName + titleAfter;
                    lecturerName.setText(name);
                    lecturerStatus.setText(lecturer.staffStatus);
                    if (lecturer.room != null) {
                        lecturerRoom.setText(lecturer.room.buildingName + ", " + getString(R.string.room).toLowerCase() + " " + lecturer.room.number);
                    }
                    String email = String.format("<a href=\"%s\">%s</a>", lecturer.emailUrl, getString(R.string.check_usos));
                    lecturerEmail.setText(Html.fromHtml(email));
                    lecturerEmail.setMovementMethod(LinkMovementMethod.getInstance());
                    setUpConsultationField();

                    List<String> data = $.collect(lecturer.courseEditionsConducted, it -> String.format("%s (%s)", it.courseName, it.termId));
                    CommonUtils.showList(layoutInflater, lecturerCourses, data, position -> {
                        CourseEditionsConducted course = lecturer.courseEditionsConducted.get(position);
                        CourseDetailsActivity.showCourseDetails(LecturerDetailsActivity.this, course.courseId, course.termId);
                    });

                    setUpPicture(name);
                    lecturerHomepage.setText(lecturer.homepageUrl);
                    showListOfLecturerFaculties();

                }

                handler.postDelayed(() -> {
                    swipeContainer.setRefreshing(false);
                    showProgress(false);
                }, 200);
            }

            @Override public void onFailure(Call<KujonResponse<LecturerLong>> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lecturer_plan, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.lecturer_plan_menu){
            LecturerPlanActivity.showLecturerPlan(LecturerDetailsActivity.this, lecturerId,lecturerName.getText().toString());
            return true;
        }
        return false;
    }

    private void setUpPicture(String name) {
        String photoUrl = lecturer.photoUrl;
        picasso.load(photoUrl)
                .transform(new CircleTransform())
                .fit()
                .centerInside()
                .placeholder(R.drawable.photo_placeholder)
                .into(picture);

        picture.setOnClickListener(v -> {
            if (!isEmpty(photoUrl) && photoUrl.startsWith("http")) {
                ImageActivity.show(LecturerDetailsActivity.this, photoUrl, name);
            } else {
                Toast.makeText(LecturerDetailsActivity.this, R.string.no_photo, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showListOfLecturerFaculties() {
        List<String> dataFacultys = $.collect(lecturer.employmentPositions, it -> it.faculty.name + ", " + it.position.name);
        CommonUtils.showList(layoutInflater, lecturersFaculty, dataFacultys, position -> {
            EmploymentPosition employmentPosition = lecturer.employmentPositions.get(position);
            FacultyDetailsActivity.showFacultyDetails(LecturerDetailsActivity.this, employmentPosition.faculty.id);
        });
    }

    private void setUpConsultationField() {
        lecturerInterests.setText(lecturer.interests);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lecturerOfficeHours.setText(Html.fromHtml(lecturer.officeHours,0));
        }else {
            lecturerOfficeHours.setText(Html.fromHtml(lecturer.officeHours));
        }
        lecturerOfficeHours.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void showLecturerDatails(Activity activity, String lecturerId) {
        Intent intent = new Intent(activity, LecturerDetailsActivity.class);
        intent.putExtra(LECTURER_ID, lecturerId);
        activity.startActivity(intent);
    }
}
