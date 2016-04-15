package mobi.kujon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.CourseDetailsActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesFragment extends Fragment {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private Adapter adapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        KujonBackendApi backendApi = KujonBackendService.getInstance().getKujonBackendApi();
        backendApi.coursesEditions().enqueue(new Callback<KujonResponse<List<Course>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Course>>> call, Response<KujonResponse<List<Course>>> response) {
                List<Course> data = response.body().data;
                adapter.setData(data);
            }

            @Override public void onFailure(Call<KujonResponse<List<Course>>> call, Throwable t) {
                Crashlytics.logException(t);
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Course> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Course course = data.get(position);
            holder.courseName.setText(course.courseName);
            holder.courseTerm.setText(course.termId);
            holder.courseId = course.courseId;
            holder.termId = course.termId;
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Course> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.course_name) TextView courseName;
        @Bind(R.id.course_term) TextView courseTerm;
        String courseId;
        String termId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CourseDetailsActivity.class);
                intent.putExtra(CourseDetailsActivity.COURSE_ID, courseId);
                intent.putExtra(CourseDetailsActivity.TERM_ID, termId);
                startActivity(intent);
            });
        }
    }
}
