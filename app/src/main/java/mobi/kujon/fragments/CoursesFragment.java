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

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.CourseDetailsActivity;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class CoursesFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        activity.showProgress(true);

        backendApi.coursesEditionsByTerm().enqueue(new Callback<KujonResponse<SortedMap<String, List<Course>>>>() {
            @Override
            public void onResponse(Call<KujonResponse<SortedMap<String, List<Course>>>> call, Response<KujonResponse<SortedMap<String, List<Course>>>> response) {
                activity.showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    SortedMap<String, List<Course>> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<SortedMap<String, List<Course>>>> call, Throwable t) {
                activity.showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        activity.getSupportActionBar().setTitle("Przedmioty");
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        SortedMap<String, List<Course>> data = new TreeMap<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
            return new ViewHolder(v);
        }

        @Override public int getSectionCount() {
            return data.keySet().size();
        }

        @Override public int getItemCount(int section) {
            return coursesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            holder.section.setText(sectionName(section));
            holder.section.setVisibility(View.VISIBLE);
            holder.courseLayout.setVisibility(View.GONE);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Course course = coursesInSection(section).get(relativePosition);
            holder.courseName.setText(course.courseName);
            holder.courseTerm.setText(course.termId);
            holder.courseId = course.courseId;
            holder.termId = sectionName(section);
            holder.itemView.setBackgroundResource(relativePosition % 2 == 1 ? R.color.grey : android.R.color.white);
            holder.section.setVisibility(View.GONE);
            holder.courseLayout.setVisibility(View.VISIBLE);
        }

        public void setData(SortedMap<String, List<Course>> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        List<Course> coursesInSection(int section) {
            List<Course>[] courses = data.values().toArray(new List[0]);
            return courses[section];
        }

        String sectionName(int section) {
            return ((String) data.keySet().toArray()[section]);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.course_name) TextView courseName;
        @Bind(R.id.course_term) TextView courseTerm;
        @Bind(R.id.section) TextView section;
        @Bind(R.id.courseLayout) LinearLayout courseLayout;
        String courseId;
        String termId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (!isEmpty(courseId) && !isEmpty(termId)) {
                    CourseDetailsActivity.showCourseDetails(getActivity(), courseId, termId);
                }
            });
        }
    }

}
