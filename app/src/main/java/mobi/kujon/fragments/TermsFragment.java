package mobi.kujon.fragments;

import android.graphics.Typeface;
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
import mobi.kujon.network.json.Term2;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends ListFragment {

    private Adapter adapter;
    private Typeface latoSemiBold;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);
        latoSemiBold = Typeface.createFromAsset(activity.getAssets(), "fonts/Lato-Semibold.ttf");
        loadData(false);
    }


    @Override protected String getRequestUrl() {
        return backendApi.terms().request().url().toString();
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<Term2>>> terms = refresh ? backendApi.termsRefresh() : backendApi.terms();
        terms.enqueue(new Callback<KujonResponse<List<Term2>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Term2>>> call, Response<KujonResponse<List<Term2>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Term2> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Term2>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });

    }

    @Override public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cykle");
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Term2> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_terms, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Term2 term = data.get(position);
            holder.name.setText(term.name);
            holder.termId.setText(term.termId);
            holder.active.setText(term.active ? "TAK" : "NIE");

            if (term.active) {
                holder.name.setTypeface(latoSemiBold);
                holder.termId.setTypeface(latoSemiBold);
                holder.active.setTypeface(latoSemiBold);
                holder.startDate.setTypeface(latoSemiBold);
                holder.endDate.setTypeface(latoSemiBold);
                holder.finishDate.setTypeface(latoSemiBold);
            }
            holder.startDate.setText(term.startDate);
            holder.endDate.setText(term.endDate);
            holder.finishDate.setText(term.finishDate);
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Term2> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public @Bind(R.id.name) TextView name;
        public @Bind(R.id.term_id) TextView termId;
        public @Bind(R.id.active) TextView active;
        public @Bind(R.id.start_date) TextView startDate;
        public @Bind(R.id.end_date) TextView endDate;
        public @Bind(R.id.finish_date) TextView finishDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
