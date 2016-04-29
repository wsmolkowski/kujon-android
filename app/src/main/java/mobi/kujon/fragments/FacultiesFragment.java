package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import mobi.kujon.activities.FacultyDetailsActivity;
import mobi.kujon.network.json.Faculty2;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultiesFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);
        backendApi.faculties().enqueue(new Callback<KujonResponse<List<Faculty2>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Faculty2>>> call, Response<KujonResponse<List<Faculty2>>> response) {
                activity.showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Faculty2> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Faculty2>>> call, Throwable t) {
                activity.showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        activity.getSupportActionBar().setTitle("Jednostki");
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Faculty2> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_textview, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Faculty2 faculty = data.get(position);
            holder.name.setText(faculty.name.pl);
            holder.facultyId = faculty.facId;
            holder.itemView.setBackgroundResource(position % 2 == 1 ? R.color.grey : android.R.color.white);
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Faculty2> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name) TextView name;
        String facultyId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> FacultyDetailsActivity.showFacultyDetails(getActivity(), facultyId));
        }
    }

}
