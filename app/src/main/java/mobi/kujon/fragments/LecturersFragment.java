package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.underscore.$;
import com.github.underscore.Predicate;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.LecturerDetailsActivity;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.predicates.LecutrerPredicate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturersFragment extends AbstractSearchFragment<Lecturer> {

    private Adapter adapter;
    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        showSpinner(true);

        loadData(false);
    }

    @Override
    protected void setDataToAdapter(List<Lecturer> filter) {
        adapter.setData(filter);
    }

    @Override
    protected Predicate<Lecturer> createPredicate(String query) {
        return new LecutrerPredicate(query);
    }

    @Override protected String getRequestUrl() {
        return backendApi.lecturers().request().url().toString();
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<Lecturer>>> lecturers = refresh ? backendApi.lecturersRefresh() : backendApi.lecturers();
        lecturers.enqueue(new Callback<KujonResponse<List<Lecturer>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Lecturer>>> call, Response<KujonResponse<List<Lecturer>>> response) {
                showSpinner(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Lecturer> data = response.body().data;
                    dataFromApi = data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Lecturer>>> call, Throwable t) {
                showSpinner(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.teachers);
    }




    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Lecturer> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_textview, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Lecturer lecturer = data.get(position);
            holder.lecturerName.setText(lecturer.lastName + " " + lecturer.firstName);
            holder.lecturerId = lecturer.userId;
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Lecturer> data) {
            this.data = $.sortBy(data, it -> it.lastName);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name) TextView lecturerName;
        String lecturerId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                LecturerDetailsActivity.showLecturerDatails(getActivity(), lecturerId);
            });
        }
    }

}
