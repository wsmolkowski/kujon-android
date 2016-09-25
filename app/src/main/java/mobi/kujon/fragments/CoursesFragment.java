package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);
        swipeContainer.setRefreshing(true);

        loadData(false);
    }

    @Override protected String getRequestUrl() {
        return backendApi.coursesEditionsByTerm().request().url().toString();
    }

    @Override public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.lectures);
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<SortedMap<String, List<Course>>>>> kujonResponseCall =
                refresh ? backendApi.coursesEditionsByTermRefresh() : backendApi.coursesEditionsByTerm();
        kujonResponseCall.enqueue(new Callback<KujonResponse<List<SortedMap<String, List<Course>>>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<SortedMap<String, List<Course>>>>> call, Response<KujonResponse<List<SortedMap<String, List<Course>>>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<SortedMap<String, List<Course>>> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<SortedMap<String, List<Course>>>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        List<SortedMap<String, List<Course>>> data = new ArrayList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
            return new ViewHolder(v);
        }

        @Override public int getSectionCount() {
            return data.size();
        }

        @Override public int getItemCount(int section) {
            return coursesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            holder.section.setText(sectionName(section));
            holder.termId = sectionName(section);
            holder.section.setVisibility(View.VISIBLE);
            holder.courseLayout.setVisibility(View.GONE);
            holder.itemView.setTag(R.string.no_item_decorator, true);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Course course = coursesInSection(section).get(relativePosition);
            holder.courseName.setText(course.courseName);
            holder.courseId = course.courseId;
            holder.termId = sectionName(section);
            holder.section.setVisibility(View.GONE);
            holder.courseLayout.setVisibility(View.VISIBLE);
            holder.itemView.setTag(R.string.no_item_decorator, false);
        }

        public void setData(List<SortedMap<String, List<Course>>> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        List<Course> coursesInSection(int section) {
            return data.get(section).get(data.get(section).firstKey());
        }

        String sectionName(int section) {
            return data.get(section).firstKey();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.course_name) TextView courseName;
        @Bind(R.id.section) TextView section;
        @Bind(R.id.courseLayout) LinearLayout courseLayout;
        String courseId;
        String termId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> showCourseOrTerm(courseId, termId));
        }
    }
}
