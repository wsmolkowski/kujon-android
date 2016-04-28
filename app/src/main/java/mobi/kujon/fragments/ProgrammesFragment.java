package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Programme;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgrammesFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        backendApi.programmes().enqueue(new Callback<KujonResponse<List<Programme>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Programme>>> call, Response<KujonResponse<List<Programme>>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Programme> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Programme>>> call, Throwable t) {
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Kierunki");
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Programme> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_programme, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Programme programme = data.get(position);
            holder.description.setText(programme.programme.description);
            holder.id.setText(programme.programme.id);
            holder.levelOfStudies.setText(programme.programme.levelOfStudies);
            holder.modeOfStudies.setText(programme.programme.modeOfStudies);
            holder.duration.setText(programme.programme.duration);
            holder.itemView.setBackgroundResource(position % 2 == 1 ? R.color.grey : android.R.color.white);
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Programme> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.description) TextView description;
        @Bind(R.id.id) TextView id;
        @Bind(R.id.level_of_studies) TextView levelOfStudies;
        @Bind(R.id.mode_of_studies) TextView modeOfStudies;
        @Bind(R.id.duration) TextView duration;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
