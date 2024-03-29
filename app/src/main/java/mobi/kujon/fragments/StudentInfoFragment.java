package mobi.kujon.fragments;

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

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.ImageActivity;
import mobi.kujon.activities.ProgrammeDetailsActivity;
import mobi.kujon.activities.StudentDetailsActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.StudentProgramme;
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

public class StudentInfoFragment extends BaseFragment {

    public static final String USER_ID = "USER_ID";
    @BindView(R.id.student_account_number) TextView studentAccountNumber;
    @BindView(R.id.student_programmes) LinearLayout studentProgrammes;
    @BindView(R.id.firstLastName) TextView firstLastName;
    @BindView(R.id.picture) ImageView picture;
    @BindView(R.id.usosLogo) ImageView usosLogo;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Inject KujonUtils utils;
    @Inject KujonBackendApi kujonBackendApi;
    @Inject Picasso picasso;

    private BaseActivity activity;
    private AlertDialog alertDialog;
    private User user;
    private Call<KujonResponse<User>> users;

    public static StudentInfoFragment getFragment(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        StudentInfoFragment fragment = new StudentInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_student_info, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        swipeContainer.setOnRefreshListener(() -> loadData(true));
        handler.post(() -> loadData(false));
    }

    private void loadData(boolean refresh) {
        cancelLastCallIfExist();
        handler.post(() -> swipeContainer.setRefreshing(true));

        String userId = getArguments().getString(USER_ID);
        if (refresh) {
            utils.invalidateEntry("users/" + userId);
            if (user != null && user.photoUrl != null) {
                picasso.invalidate(user.photoUrl);
            }
        }

        users = refresh ? kujonBackendApi.usersRefresh(userId) : kujonBackendApi.users(userId);
        users.enqueue(new Callback<KujonResponse<User>>() {
            @Override
            public void onResponse(Call<KujonResponse<User>> call, Response<KujonResponse<User>> response) {
                if (swipeContainer != null) swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    user = response.body().data;
                    String name = user.first_name + " " + user.last_name;
                    firstLastName.setText(name);
                    picasso.load(user.photoUrl)
                            .transform(new CircleTransform())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.photo_placeholder)
                            .into(picture);
                    ((StudentDetailsActivity) getActivity()).setToolbarTitle(name);
                    picture.setOnClickListener(v -> ImageActivity.show(getActivity(), user.photoUrl, name));
                    studentAccountNumber.setText(user.id);
                    studentProgrammes.removeAllViews();
                    CommonUtils.showListProgrammes(activity.getLayoutInflater(), studentProgrammes, user.student_programmes, position -> {
                        StudentProgramme studentProgramme = user.student_programmes.get(position);
                        Programme programme = studentProgramme.programme;
                        ProgrammeDetailsActivity.showProgrammeDetailsWithLoad(StudentInfoFragment.this.getActivity(),studentProgramme);
                    });
                }
            }

            @Override public void onFailure(Call<KujonResponse<User>> call, Throwable t) {
                activity.showProgress(false);
                if(swipeContainer != null)
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });

        backendCall = users;
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
}
