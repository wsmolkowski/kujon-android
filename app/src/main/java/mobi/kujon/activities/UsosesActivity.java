package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

import static mobi.kujon.KujonApplication.getContext;

public class UsosesActivity extends BaseActivity {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private UsosesAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usoses);
        ButterKnife.bind(this);

        requestUsoses();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsosesAdapter();
        recyclerView.setAdapter(adapter);
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
                adapter.setItems(response.body().data);
                for (mobi.kujon.network.json.Usos usos : response.body().data) {
                    System.out.println(usos);
                }
            }

            @Override public void onFailure(Call<UsosesResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    private static class UsosesAdapter extends RecyclerView.Adapter<UsosViewHolder> {

        private List<Usos> items = new ArrayList<>();

        @Override public UsosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usos_row, parent, false);
            return new UsosViewHolder(v);
        }

        @Override public void onBindViewHolder(UsosViewHolder holder, int position) {
            Usos usos = items.get(position);
            holder.name.setText(usos.name);
            Picasso.with(getContext()).load(usos.logo).into(holder.logo);
        }

        @Override public int getItemCount() {
            return items.size();
        }

        public void setItems(List<Usos> items) {
            if (items != null) {
                this.items = items;
                notifyDataSetChanged();
            }
        }
    }

    static class UsosViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.usos_name) TextView name;
        @Bind(R.id.usos_logo) ImageView logo;

        public UsosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
