package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.underscore.$;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoFragment extends ErrorHandlerFragment {

    @Bind(R.id.student_status) TextView studentStatus;
    @Bind(R.id.student_account_number) TextView studentAccountNumber;
    @Bind(R.id.student_programmes) TextView studentProgrammes;
    @Bind(R.id.userName) TextView userName;
    @Bind(R.id.usosName) TextView usosName;
    @Bind(R.id.firstLastName) TextView firstLastName;
    @Bind(R.id.index) TextView index;
    @Bind(R.id.picture) ImageView picture;

    private KujonBackendApi kujonBackendApi;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, rootView);
        kujonBackendApi = KujonBackendService.getInstance().getKujonBackendApi();
        return rootView;
    }

    @Override public void onStart() {
        super.onStart();
        kujonBackendApi.users().enqueue(new Callback<KujonResponse<User>>() {
            @Override public void onResponse(Call<KujonResponse<User>> call, Response<KujonResponse<User>> response) {
                if (handleResponse(response)) {
                    User user = response.body().data;
                    userName.setText(user.name);
                    usosName.setText(user.usos_name);
                    index.setText(user.student_number);
                    firstLastName.setText(user.first_name + " " + user.last_name);
                    Picasso.with(getActivity()).load(user.picture).fit().centerInside().placeholder(R.drawable.user_placeholder).into(picture);
                    studentStatus.setText(user.student_status);
                    studentAccountNumber.setText(user.student_number);
                    studentProgrammes.setText($.join($.collect(user.student_programmes, it -> String.format("%s - %s (%s)", it.id, it.programme.description, it.programme.id)), "\n\n"));
                }
            }

            @Override public void onFailure(Call<KujonResponse<User>> call, Throwable t) {
                handleError(t);
            }
        });
    }
}
