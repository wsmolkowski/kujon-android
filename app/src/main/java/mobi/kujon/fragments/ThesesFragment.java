package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.underscore.$;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.ThesesActivity;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Thesis_;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mobi.kujon.activities.FacultyDetailsActivity.showFacultyDetails;
import static mobi.kujon.activities.LecturerDetailsActivity.showLecturerDatails;
import static mobi.kujon.utils.CommonUtils.showList;

public class ThesesFragment extends ListFragment {

    private Adapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);

        loadData(false);
    }

    @Override
    protected String getRequestUrl() {
        return backendApi.theses().request().url().toString();
    }

    @Override
    protected void loadData(boolean refresh) {
        if (getActivity().getIntent().getStringExtra(ThesesActivity.THESE_KEY) != null) {
            loadDataFromBundle();

        } else {
            loadDataFromApi(refresh);
        }

    }

    private void loadDataFromBundle() {
        String thesisJson = getActivity().getIntent().getStringExtra(ThesesActivity.THESE_KEY);
        Gson gson = new Gson();
        try {
            activity.showProgress(false);
            swipeContainer.setRefreshing(false);
            Thesis_ thesis = gson.fromJson(thesisJson, Thesis_.class);
            List<Thesis_> thesises = new ArrayList<>();
            thesises.add(thesis);
            ((ThesesActivity) getActivity()).setToolbarTitle(R.string.thesiss);
            adapter.setData(thesises);
        } catch (JsonSyntaxException e) {

            activity.showProgress(false);
            swipeContainer.setRefreshing(false);
            adapter.setData(new ArrayList<>());
        }
    }

    private void loadDataFromApi(boolean refresh) {
        ((ThesesActivity) getActivity()).setToolbarTitle(R.string.thesiss);
        Call<KujonResponse<List<Thesis_>>> theses = refresh ? backendApi.thesesRefresh() : backendApi.theses();
        theses.enqueue(new Callback<KujonResponse<List<Thesis_>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Thesis_>>> call, Response<KujonResponse<List<Thesis_>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Thesis_> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<Thesis_>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Thesis_> data = new LinkedList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thesis, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Thesis_ thesis = data.get(position);
            holder.title.setText(thesis.title);
            holder.type.setText(thesis.type);
            holder.authors.setText($.join($.collect(thesis.authors, author -> author.first_name + " " + author.last_name), ","));

            List<String> names = $.collect(thesis.supervisors, supervisor -> supervisor.first_name + " " + supervisor.last_name);
            showList(activity.getLayoutInflater(), holder.supervisors, names, index -> showLecturerDatails(activity, thesis.supervisors.get(index).id));

            showList(activity.getLayoutInflater(), holder.faculty, Arrays.asList(thesis.faculty.name), index -> showFacultyDetails(activity, thesis.faculty.id));
            if (position == getItemCount() - 1) {
                holder.greyView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<Thesis_> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.authors)
        TextView authors;
        @Bind(R.id.supervisors)
        LinearLayout supervisors;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.faculty)
        LinearLayout faculty;
        @Bind(R.id.last_grey)
        View greyView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
