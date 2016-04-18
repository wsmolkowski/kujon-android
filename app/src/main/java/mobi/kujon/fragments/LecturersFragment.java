package mobi.kujon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.underscore.$;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.LecturerDetailsActivity;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturersFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        backendApi.lecturers().enqueue(new Callback<KujonResponse<List<Lecturer>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Lecturer>>> call, Response<KujonResponse<List<Lecturer>>> response) {
                List<Lecturer> data = response.body().data;
                adapter.setData(data);
            }

            @Override public void onFailure(Call<KujonResponse<List<Lecturer>>> call, Throwable t) {
                Crashlytics.logException(t);
            }
        });
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Lecturer> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lecturer, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Lecturer lecturer = data.get(position);
            holder.lecturerName.setText(lecturer.firstName + " " + lecturer.lastName);
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

        @Bind(R.id.lecturer_name) TextView lecturerName;
        String lecturerId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LecturerDetailsActivity.class);
                intent.putExtra(LecturerDetailsActivity.LECTURER_ID, lecturerId);
                startActivity(intent);
            });
        }
    }
}
