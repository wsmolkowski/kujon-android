package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.underscore.$;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Faculty2;
import mobi.kujon.network.json.gen.Path;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class FacultyDetailsActivity extends BaseActivity {

    public static final String FACULTY_ID = "FACULTY_ID";

    @Bind(R.id.faculty_name) TextView facultyName;
    @Bind(R.id.logo) ImageView logo;
    @Bind(R.id.postal_address) TextView postalAddress;
    @Bind(R.id.phone) TextView phone;
    @Bind(R.id.homepage) TextView homepage;
    @Bind(R.id.programmeCount) TextView programmeCount;
    @Bind(R.id.courseCount) TextView courseCount;
    @Bind(R.id.staffCount) TextView staffCount;
    @Bind(R.id.parent_faculties) LinearLayout parentFaculties;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.faculty_title);

        String facultyId = getIntent().getStringExtra(FACULTY_ID);

        showProgress(true);
        kujonBackendApi.faculty(facultyId).enqueue(new Callback<KujonResponse<Faculty2>>() {
            @Override public void onResponse(Call<KujonResponse<Faculty2>> call, Response<KujonResponse<Faculty2>> response) {
                showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    Faculty2 faculty = response.body().data;

                    facultyName.setText(faculty.name);
                    postalAddress.setText(faculty.postal_address);
                    phone.setText($.join(faculty.phone_numbers, ", "));
                    homepage.setText(faculty.homepage_url);
                    picasso.load(faculty.logo_urls._100x100).into(logo);
                    programmeCount.setText("Liczba programów: " + faculty.stats.programme_count);
                    courseCount.setText("Liczba kursów: " + faculty.stats.course_count);
                    staffCount.setText("Liczba pracowników: " + faculty.stats.staff_count);

                    List<Path> reveertedPath = $.reverse(faculty.path);
                    List<String> parents = $.collect(reveertedPath, path -> path.name);

                    CommonUtils.showList(getLayoutInflater(), parentFaculties, parents, position -> {
                        showFacultyDetails(FacultyDetailsActivity.this, reveertedPath.get(position).id);
                    });
                }
            }

            @Override public void onFailure(Call<KujonResponse<Faculty2>> call, Throwable t) {
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    public static void showFacultyDetails(Activity activity, String facultyId) {
        if (!isEmpty(facultyId)) {
            Intent intent = new Intent(activity, FacultyDetailsActivity.class);
            intent.putExtra(FACULTY_ID, facultyId);
            activity.startActivity(intent);
        } else {
            Toast.makeText(KujonApplication.getApplication(), "Brak informacji o jednostce", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.postal_address)
    public void navigate() {
        if (isEmpty(postalAddress.getText().toString())) {
            Toast.makeText(FacultyDetailsActivity.this, "Brak adresu", Toast.LENGTH_SHORT).show();
        } else {
            CommonUtils.showOnMap(this, postalAddress.getText().toString());
        }
    }
}
