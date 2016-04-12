package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.json.Usos;
import mobi.kujon.network.json.UsosesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsosesActivity extends BaseActivity {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usoses);
        ButterKnife.bind(this);

        requestUsoses();
    }

    private void requestUsoses() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kujon.mobi")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KujonBackendService kujonBackendService = retrofit.create(KujonBackendService.class);

        kujonBackendService.usoses().enqueue(new Callback<UsosesResponse>() {
            @Override public void onResponse(Call<UsosesResponse> call, Response<UsosesResponse> response) {
                System.out.println(response);
                for (Usos usos : response.body().data) {
                    System.out.println(usos);
                }
            }

            @Override public void onFailure(Call<UsosesResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
