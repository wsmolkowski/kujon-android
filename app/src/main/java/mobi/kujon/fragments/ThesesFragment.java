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
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.LecturerDetailsActivity;
import mobi.kujon.activities.StudentDetailsActivity;
import mobi.kujon.activities.ThesesActivity;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Thesis;
import mobi.kujon.utils.CommonUtils;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.dto.SimpleUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mobi.kujon.activities.FacultyDetailsActivity.showFacultyDetails;

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
            Thesis thesis = gson.fromJson(thesisJson, Thesis.class);
            List<Thesis> thesises = new ArrayList<>();
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
        cancelLastCallIfExist();
        ((ThesesActivity) getActivity()).setToolbarTitle(getString(R.string.thesiss));
        Call<KujonResponse<List<Thesis>>> theses = refresh ? backendApi.thesesRefresh() : backendApi.theses();
        theses.enqueue(new Callback<KujonResponse<List<Thesis>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Thesis>>> call, Response<KujonResponse<List<Thesis>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Thesis> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<Thesis>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
        backendCall = theses;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Thesis> data = new LinkedList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thesis, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Thesis thesis = data.get(position);
            holder.title.setText(thesis.title);
            holder.type.setText(thesis.type);

            List<SimpleUser> supervisors = $.collect(thesis.supervisors, SimpleUser::new);
            List<SimpleUser> authorsList = $.collect(thesis.authors, SimpleUser::new);
            CommonUtils.showListUser(activity.getLayoutInflater(),holder.authors,authorsList, position1 -> StudentDetailsActivity.showStudentDetails(getActivity(), authorsList.get(position1).getId()));
            CommonUtils.showListUser(activity.getLayoutInflater(),holder.supervisors,supervisors, position1 -> LecturerDetailsActivity.showLecturerDatails(getActivity(), supervisors.get(position1).getId()));
            holder.faculty.setText(thesis.faculty.name);
            holder.facultyView.setOnClickListener(v -> showFacultyDetails(activity, thesis.faculty.id));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<Thesis> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.authors)
        LinearLayout authors;
        @BindView(R.id.supervisors)
        LinearLayout supervisors;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.faculty)
        TextView faculty;

        @BindView(R.id.facilty_layout)
        View facultyView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
