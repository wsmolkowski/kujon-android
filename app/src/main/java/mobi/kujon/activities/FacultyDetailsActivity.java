package mobi.kujon.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.underscore.$;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Faculty2;
import mobi.kujon.network.json.gen.Path;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.geocoding.GeocodeTask;
import mobi.kujon.utils.geocoding.OnLocationSeted;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class FacultyDetailsActivity extends BaseActivity implements OnMapReadyCallback, OnLocationSeted {

    public static final String FACULTY_ID = "FACULTY_ID";

    @BindView(R.id.faculty_name) TextView facultyName;
    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.postal_address) TextView postalAddress;
    @BindView(R.id.phone) TextView phone;
    @BindView(R.id.homepage) TextView homepage;
    @BindView(R.id.programmeCount) TextView programmeCount;
    @BindView(R.id.courseCount) TextView courseCount;
    @BindView(R.id.staffCount) TextView staffCount;
    @BindView(R.id.parent_faculties) LinearLayout parentFaculties;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.scrollView) ScrollView scrollView;
    private GoogleMap googleMap;
    private boolean loaded;
    private String addressLine;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.faculty_title);

        String facultyId = getIntent().getStringExtra(FACULTY_ID);

        showProgress(true);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadData(facultyId);
    }

    private void loadData(String facultyId) {
        kujonBackendApi.faculty(facultyId).enqueue(new Callback<KujonResponse<Faculty2>>() {
            @Override
            public void onResponse(Call<KujonResponse<Faculty2>> call, Response<KujonResponse<Faculty2>> response) {
                showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    Faculty2 faculty = response.body().data;
                    fillUpWithFacultyData(faculty);
                    loaded = true;
                    setUpMarker();
                }else {
                    loaded = false;
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<Faculty2>> call, Throwable t) {
                showProgress(false);
                loaded = false;
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void fillUpWithFacultyData(Faculty2 faculty) {
        facultyName.setText(faculty.name);
        addressLine = faculty.postal_address;
        postalAddress.setText(addressLine);
        phone.setText($.join(faculty.phone_numbers, ", "));
        homepage.setText(faculty.homepage_url);
        picasso.load(faculty.logo_urls._100x100).into(logo);
        programmeCount.setText(String.format("%s: %d", getString(R.string.number_of_programmes), faculty.stats.programme_count));
        courseCount.setText(String.format("%s: %d", getString(R.string.number_of_courses), faculty.stats.course_count));
        staffCount.setText(String.format("%s: %d", getString(R.string.number_of_staff), faculty.stats.staff_count));

        List<Path> reveertedPath = $.reverse(faculty.path);
        List<String> parents = $.collect(reveertedPath, path -> path.name);

        CommonUtils.showList(getLayoutInflater(), parentFaculties, parents, position -> {
            showFacultyDetails(FacultyDetailsActivity.this, reveertedPath.get(position).id);
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnInfoWindowClickListener(marker -> {
            CommonUtils.showOnMap(this, marker.getTitle());
        });
        setUpMarker();
    }

    private void setUpMarker() {
        if(conditionToShowMarkerOnMap()){
            (new GeocodeTask(new WeakReference<>(this),addressLine)).execute();
        }
    }

    private boolean conditionToShowMarkerOnMap() {
        return this.googleMap !=  null && loaded && !isEmpty(addressLine);
    }

    public static void showFacultyDetails(Activity activity, String facultyId) {
        if (!isEmpty(facultyId)) {
            Intent intent = new Intent(activity, FacultyDetailsActivity.class);
            intent.putExtra(FACULTY_ID, facultyId);
            activity.startActivity(intent);
        } else {
            Toast.makeText(KujonApplication.getApplication(), R.string.no_faculty_information, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setLatLng(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(addressLine);
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

    }

    @Override
    public void cantSetLocation() {
        Toast.makeText(FacultyDetailsActivity.this, R.string.unprocessable_address, Toast.LENGTH_SHORT).show();
    }
}
