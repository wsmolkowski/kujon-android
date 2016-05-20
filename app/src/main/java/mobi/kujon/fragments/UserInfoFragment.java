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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.FacultyDetailsActivity;
import mobi.kujon.activities.ImageActivity;
import mobi.kujon.activities.TermsActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.Faculty2;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.Programme_;
import mobi.kujon.network.json.StudentProgramme;
import mobi.kujon.network.json.Term2;
import mobi.kujon.network.json.User;
import mobi.kujon.network.json.Usos;
import mobi.kujon.ui.CircleTransform;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class UserInfoFragment extends BaseFragment {

    public static final String USER_ID = "USER_ID";
    @Bind(R.id.student_status) TextView studentStatus;
    @Bind(R.id.student_account_number) TextView studentAccountNumber;
    @Bind(R.id.student_programmes) LinearLayout studentProgrammes;
    @Bind(R.id.usosName) TextView usosName;
    @Bind(R.id.firstLastName) TextView firstLastName;
    @Bind(R.id.index) TextView index;
    @Bind(R.id.terms) TextView terms;
    @Bind(R.id.picture) ImageView picture;
    @Bind(R.id.usosLogo) ImageView usosLogo;
    @Bind(R.id.student_faculties) LinearLayout studentFaculties;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Inject KujonUtils utils;
    @Inject KujonBackendApi kujonBackendApi;
    @Inject Picasso picasso;

    private BaseActivity activity;
    private AlertDialog alertDialog;
    private User user;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.app_name);
        swipeContainer.setOnRefreshListener(() -> loadData(true));
        handler.post(() -> loadData(false));
    }

    private void loadData(boolean refresh) {
        swipeContainer.setRefreshing(true);

        if (refresh) {
            utils.invalidateEntry("users");
            utils.invalidateEntry("faculties");
            utils.invalidateEntry("terms");
            if (user != null && user.picture != null) {
                picasso.invalidate(user.picture);
            }
        }

        Call<KujonResponse<User>> users = refresh ? kujonBackendApi.usersRefresh() : kujonBackendApi.users();
        users.enqueue(new Callback<KujonResponse<User>>() {
            @Override
            public void onResponse(Call<KujonResponse<User>> call, Response<KujonResponse<User>> response) {
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    user = response.body().data;
                    usosName.setText(user.usos_name);
                    index.setText(user.student_number);
                    String name = user.first_name + " " + user.last_name;
                    firstLastName.setText(name);
                    picasso.load(user.picture)
                            .transform(new CircleTransform())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.user_placeholder)
                            .into(picture);
                    showUsosLogo(user.usos_id, usosLogo);
                    picture.setOnClickListener(v -> ImageActivity.show(getActivity(), user.picture, name));
                    studentStatus.setText(user.student_status);
                    studentAccountNumber.setText(user.id);
                    List<String> collect = $.collect(user.student_programmes, it -> it.programme.description.split(",")[0]);
                    studentProgrammes.removeAllViews();
                    CommonUtils.showList(activity.getLayoutInflater(), studentProgrammes, collect, position -> {
                        activity.showProgress(true);
                        StudentProgramme studentProgramme = user.student_programmes.get(position);
                        Programme programme = studentProgramme.programme;
                        kujonBackendApi.programmes().enqueue(new Callback<KujonResponse<List<Programme>>>() {
                            @Override
                            public void onResponse(Call<KujonResponse<List<Programme>>> call, Response<KujonResponse<List<Programme>>> response) {
                                activity.showProgress(false);
                                if (ErrorHandlerUtil.handleResponse(response)) {
                                    List<Programme> data = response.body().data;
                                    Optional<Programme> programmeOptional = $.find(data, it -> it.programme.id.equals(programme.id));
                                    if (!programmeOptional.isPresent()) {
                                        Toast.makeText(KujonApplication.getApplication(), "Brak opisu programu", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Programme prog = programmeOptional.get();
                                        Programme_ programmeFull = prog.programme;
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                                        dlgAlert.setTitle("Kierunek: " + programmeFull.description.split(",")[0]);
                                        dlgAlert.setMessage(String.format("identyfikator: %s\ntryb: %s\nczas trwania: %s\npoziom: %s\nopis: %s",
                                                programmeFull.id, programmeFull.modeOfStudies, programmeFull.duration, programmeFull.levelOfStudies, programmeFull.description));
                                        dlgAlert.setCancelable(false);
                                        dlgAlert.setNegativeButton("OK", (dialog, which) -> {
                                            dialog.dismiss();
                                        });
                                        alertDialog = dlgAlert.create();
                                        alertDialog.show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<KujonResponse<List<Programme>>> call, Throwable t) {
                                activity.showProgress(true);
                                ErrorHandlerUtil.handleError(t);
                            }
                        });
                    });
                }
            }

            @Override public void onFailure(Call<KujonResponse<User>> call, Throwable t) {
                activity.showProgress(true);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });

        Call<KujonResponse<List<Faculty2>>> faculties = refresh ? kujonBackendApi.facultiesRefresh() : kujonBackendApi.faculties();
        faculties.enqueue(new Callback<KujonResponse<List<Faculty2>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Faculty2>>> call, Response<KujonResponse<List<Faculty2>>> response) {
//                activity.showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Faculty2> data = response.body().data;
                    List<String> facultyNames = $.collect(data, it -> it.name);
                    studentFaculties.removeAllViews();
                    CommonUtils.showList(activity.getLayoutInflater(), studentFaculties, facultyNames, position -> {
                        FacultyDetailsActivity.showFacultyDetails(getActivity(), data.get(position).facId);
                    });

                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Faculty2>>> call, Throwable t) {
//                activity.showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });

        Call<KujonResponse<List<Term2>>> terms = refresh ? kujonBackendApi.termsRefresh() : kujonBackendApi.terms();
        terms.enqueue(new Callback<KujonResponse<List<Term2>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Term2>>> call, Response<KujonResponse<List<Term2>>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Term2> data = response.body().data;
                    UserInfoFragment.this.terms.setText(String.format("Cykle (%s)", data.size()));
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Term2>>> call, Throwable t) {
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @OnClick(R.id.terms)
    public void terms() {
        Intent intent = new Intent(getActivity(), TermsActivity.class);
        startActivity(intent);
    }

    @Override public void onStop() {
        super.onStop();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        swipeContainer.setRefreshing(false);
        swipeContainer.destroyDrawingCache();
        swipeContainer.clearAnimation();
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
                                    .fit()
                                    .centerInside()
                                    .into(imageView);
                        }
                    }
                }

                @Override public void onFailure(Call<KujonResponse<List<Usos>>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                }
            });
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
