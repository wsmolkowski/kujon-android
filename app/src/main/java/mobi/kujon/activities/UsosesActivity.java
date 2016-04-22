package mobi.kujon.activities;

import android.content.Intent;
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
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        kujonBackendApi.usoses().enqueue(new Callback<KujonResponse<List<Usos>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Usos>>> call, Response<KujonResponse<List<Usos>>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    adapter.setItems(response.body().data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Usos>>> call, Throwable t) {
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private class UsosesAdapter extends RecyclerView.Adapter<UsosViewHolder> {

        private List<Usos> items = new ArrayList<>();

        @Override public UsosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usos_row, parent, false);
            return new UsosViewHolder(v);
        }

        @Override public void onBindViewHolder(UsosViewHolder holder, int position) {
            Usos usos = items.get(position);
            holder.name.setText(usos.name);
            holder.usosId = usos.usosId;
            Picasso.with(KujonApplication.getApplication()).load(usos.logo).into(holder.logo);
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

    class UsosViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.usos_name) TextView name;
        @Bind(R.id.usos_logo) ImageView logo;
        String usosId;

        public UsosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(UsosesActivity.this, UsoswebLoginActivity.class);
                intent.putExtra(UsoswebLoginActivity.USOS_ID, usosId);
                startActivity(intent);
                finish();
            });
        }
    }
}
