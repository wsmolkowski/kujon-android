package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.underscore.$;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Thesis;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThesesFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);

        loadData(false);
    }

    @Override protected String getRequestUrl() {
        return backendApi.theses().request().url().toString();
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<Thesis>>> theses = refresh ? backendApi.thesesRefresh() : backendApi.theses();
        theses.enqueue(new Callback<KujonResponse<List<Thesis>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Thesis>>> call, Response<KujonResponse<List<Thesis>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Thesis> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Thesis>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });

    }

    @Override public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Prace dyplomowe");
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Thesis> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thesis, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Thesis thesis = data.get(position);
            holder.title.setText(thesis.title);
            holder.type.setText(thesis.type);
            holder.faculty.setText(thesis.faculty.name);
            holder.authors.setText($.join($.collect(thesis.authors, author -> author.first_name + " " + author.last_name), ","));
            holder.supervisors.setText($.join($.collect(thesis.supervisors, supervisor -> supervisor.first_name + " " + supervisor.last_name), ","));
            holder.itemView.setBackgroundResource(position % 2 == 1 ? R.color.grey : android.R.color.white);
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Thesis> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title) TextView title;
        @Bind(R.id.authors) TextView authors;
        @Bind(R.id.supervisors) TextView supervisors;
        @Bind(R.id.type) TextView type;
        @Bind(R.id.faculty) TextView faculty;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
