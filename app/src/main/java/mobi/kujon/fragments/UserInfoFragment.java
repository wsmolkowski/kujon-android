package mobi.kujon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.FacultyDetailsActivity;
import mobi.kujon.activities.ImageActivity;
import mobi.kujon.activities.ProgrammeDetailsActivity;
import mobi.kujon.activities.TermsActivity;
import mobi.kujon.activities.ThesesActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.Programme_;
import mobi.kujon.network.json.StudentProgramme;
import mobi.kujon.network.json.User;
import mobi.kujon.network.json.Usos;
import mobi.kujon.network.json.gen.Faculty2;
import mobi.kujon.ui.CircleTransform;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.user_data.UserDataFacade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class UserInfoFragment extends BaseFragment {

    @Inject
    KujonUtils utils;
    @Inject
    KujonBackendApi kujonBackendApi;
    @Inject
    Picasso picasso;
    @Inject
    UserDataFacade userDataFacade;

    private BaseActivity activity;
    private AlertDialog alertDialog;
    private User user;
    private Call<KujonResponse<User>> usersCall;


    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.usosLogo)
    ImageView usosLogo;

    @BindView(R.id.firstLastName)
    TextView firstLastName;

    @BindView(R.id.student_programmes)
    LinearLayout studentProgrammes;

    @BindView(R.id.student_faculties)
    LinearLayout studentFaculties;

    @BindView(R.id.terms)
    TextView terms;


    @BindView(R.id.theses)
    TextView theses;


    @BindView(R.id.index_number)
    TextView indexNumber;

    @BindView(R.id.userId)
    TextView userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        activity.setToolbarTitle(R.string.app_name);
        handler.post(() -> loadData(false));
        swipeContainer.setOnRefreshListener(() -> loadData(true));
    }


    private void loadData(boolean refresh) {
        cancelLastCallIfExist();
        swipeContainer.setRefreshing(true);

        if (refresh) {
            utils.invalidateEntry("users");
            utils.invalidateEntry("faculties");
            utils.invalidateEntry("terms");
            utils.invalidateEntry("theses");
            if (user != null && user.picture != null) picasso.invalidate(user.picture);
            if (user != null && user.photoUrl != null) picasso.invalidate(user.photoUrl);
        }

        usersCall = refresh ? kujonBackendApi.usersRefresh() : kujonBackendApi.users();
        usersCall.enqueue(new Callback<KujonResponse<User>>() {
            @Override
            public void onResponse(Call<KujonResponse<User>> call, Response<KujonResponse<User>> response) {
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    user = response.body().data;
                    userDataFacade.saveUserId(user.id);
                    String name = user.first_name + " " + user.last_name;
                    firstLastName.setText(name);
                    userId.setText(user.id);
                    indexNumber.setText(user.student_number);
                    String pictureUrl = !isEmpty(user.picture) ? user.picture : user.photoUrl;
                    picasso.load(pictureUrl)
                            .transform(new CircleTransform())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.photo_placeholder)
                            .into(picture);
                    showUsosLogo(user.usos_id, usosLogo);
                    picture.setOnClickListener(v -> ImageActivity.show(getActivity(), pictureUrl, name));
                    studentProgrammes.removeAllViews();
                    CommonUtils.showListProgrammes(activity.getLayoutInflater(), studentProgrammes, user.student_programmes, position -> {
                        StudentProgramme studentProgramme = user.student_programmes.get(position);
                        Programme programme = studentProgramme.programme;
                        List<Programme> data = user.programmes;
                        Optional<Programme> programmeOptional = $.find(data, it -> it.programme.id.equals(programme.id));
                        if (!programmeOptional.isPresent()) {
                            Toast.makeText(KujonApplication.getApplication(), R.string.no_programme_desc, Toast.LENGTH_SHORT).show();
                        } else {
                            Programme prog = programmeOptional.get();
                            Programme_ programmeFull = prog.programme;
                            if (getActivity() != null) {
                                String programmeName = programmeFull.description.split(",")[0];
                                ProgrammeDetailsActivity.showProgrammeDetails(getActivity(), studentProgramme,programmeFull, programmeName);
                            }
                        }
                    });

                    List<Faculty2> faculties = user.faculties;
                    List<String> facultyNames = $.collect(faculties, it -> it.name);
                    studentFaculties.removeAllViews();
                    CommonUtils.showList(activity.getLayoutInflater(), studentFaculties, facultyNames, position -> {
                        FacultyDetailsActivity.showFacultyDetails(getActivity(), faculties.get(position).fac_id);
                    });

                    terms.setText(String.format("%s (%s)", getString(R.string.study_cycles), user.terms.size()));
                    theses.setText(String.format("%s (%s)", getString(R.string.thesiss), user.theses.size()));
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<User>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
        backendCall = usersCall;
    }

    @OnClick(R.id.terms)
    public void terms() {
        Intent intent = new Intent(getActivity(), TermsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.theses)
    public void theses() {
        Intent intent = new Intent(getActivity(), ThesesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        swipeContainer.setRefreshing(false);
        swipeContainer.destroyDrawingCache();
        swipeContainer.clearAnimation();
        cancelCall(usersCall);
    }

    private void showUsosLogo(String usosId, ImageView imageView) {
        if (!isEmpty(usosId)) {
            kujonBackendApi.usoses().enqueue(new Callback<KujonResponse<List<Usos>>>() {
                @Override
                public void onResponse(Call<KujonResponse<List<Usos>>> call, Response<KujonResponse<List<Usos>>> response) {
                    if (ErrorHandlerUtil.handleResponse(response)) {
                        List<Usos> usoses = response.body().data;
                        Optional<Usos> usosOpt = $.find(usoses, it -> usosId.equals(it.usosId));
                        if (usosOpt.isPresent()) {
                            Usos usos = usosOpt.get();
                            Picasso.with(getActivity()).load(usos.logo)
                                    .transform(new CircleTransform())
                                    .fit()
                                    .centerInside()
                                    .into(imageView);
                        }
                    }
                }

                @Override
                public void onFailure(Call<KujonResponse<List<Usos>>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                }
            });
        }
    }
}
