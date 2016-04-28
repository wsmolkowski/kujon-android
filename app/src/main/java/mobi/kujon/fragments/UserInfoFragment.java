package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.ImageActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.User;
import mobi.kujon.network.json.Usos;
import mobi.kujon.ui.CircleTransform;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class UserInfoFragment extends Fragment {

    public static final String USER_ID = "USER_ID";
    @Bind(R.id.student_status) TextView studentStatus;
    @Bind(R.id.student_account_number) TextView studentAccountNumber;
    @Bind(R.id.student_programmes) TextView studentProgrammes;
    @Bind(R.id.usosName) TextView usosName;
    @Bind(R.id.firstLastName) TextView firstLastName;
    @Bind(R.id.index) TextView index;
    @Bind(R.id.picture) ImageView picture;
    @Bind(R.id.usosLogo) ImageView usosLogo;

    private KujonBackendApi kujonBackendApi;
    private BaseActivity activity;

    public static UserInfoFragment getFragment(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, rootView);
        kujonBackendApi = KujonBackendService.getInstance().getKujonBackendApi();
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
    }

    @Override public void onStart() {
        super.onStart();
        activity.getSupportActionBar().setTitle(R.string.app_name);
        activity.showProgress(true);
        Call<KujonResponse<User>> users = getArguments() != null && getArguments().getString(USER_ID) != null ? kujonBackendApi.users(getArguments().getString(USER_ID)) : kujonBackendApi.users();
        users.enqueue(new Callback<KujonResponse<User>>() {
            @Override public void onResponse(Call<KujonResponse<User>> call, Response<KujonResponse<User>> response) {
                activity.showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    User user = response.body().data;
                    usosName.setText(user.usos_name);
                    index.setText(user.student_number);
                    String name = user.first_name + " " + user.last_name;
                    firstLastName.setText(name);
                    Picasso.with(getActivity()).load(user.picture)
                            .transform(new CircleTransform())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.user_placeholder)
                            .into(picture);
                    showUsosLogo(user.usos_id, usosLogo);
                    picture.setOnClickListener(v -> ImageActivity.show(getActivity(), user.picture, name));
                    studentStatus.setText(user.student_status);
                    studentAccountNumber.setText(user.student_number);
                    studentProgrammes.setText($.join($.collect(user.student_programmes, it -> String.format("%s - %s (%s)", it.id, it.programme.description, it.programme.id)), "\n\n"));
                }
            }

            @Override public void onFailure(Call<KujonResponse<User>> call, Throwable t) {
                activity.showProgress(true);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void showUsosLogo(String usosId, ImageView imageView) {
        if (!isEmpty(usosId)) {
            kujonBackendApi.usoses().enqueue(new Callback<KujonResponse<List<Usos>>>() {
                @Override public void onResponse(Call<KujonResponse<List<Usos>>> call, Response<KujonResponse<List<Usos>>> response) {
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
