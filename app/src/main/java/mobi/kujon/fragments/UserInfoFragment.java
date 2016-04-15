package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
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

public class UserInfoFragment extends Fragment {

    private KujonBackendApi kujonBackendApi;

    @Bind(R.id.userName) TextView userName;
    @Bind(R.id.usosName) TextView usosName;
    @Bind(R.id.firstLastName) TextView firstLastName;
    @Bind(R.id.index) TextView index;
    @Bind(R.id.picture) ImageView picture;

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
                // TODO handle error
                User user = response.body().data;
                userName.setText(user.name);
                usosName.setText(user.usos_name);
                index.setText(user.student_number);
                firstLastName.setText(user.first_name + " " + user.last_name);
                Picasso.with(getActivity()).load(user.picture).fit().centerInside().placeholder(R.drawable.user_placeholder).into(picture);
            }

            @Override public void onFailure(Call<KujonResponse<User>> call, Throwable t) {
                Crashlytics.logException(t);
            }
        });
    }
}
