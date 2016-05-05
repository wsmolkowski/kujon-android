package mobi.kujon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.CourseDetailsActivity;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradesFragment extends ListFragment {

    private Adapter adapter;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                boolean haveData = adapter.data != null && adapter.data.size() > 0;
                emptyInfo.setVisibility(haveData ? View.GONE : View.VISIBLE);
            }
        });
        activity.showProgress(true);

        loadData();
    }

    @Override protected void loadData() {
        backendApi.gradesByTerm().enqueue(new Callback<KujonResponse<SortedMap<String, List<Grade>>>>() {
            @Override
            public void onResponse(Call<KujonResponse<SortedMap<String, List<Grade>>>> call, Response<KujonResponse<SortedMap<String, List<Grade>>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    SortedMap<String, List<Grade>> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<SortedMap<String, List<Grade>>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Oceny");
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        SortedMap<String, List<Grade>> data = new TreeMap<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grade, parent, false);
            return new ViewHolder(v);
        }

        @Override public int getSectionCount() {
            return data.keySet().size();
        }

        @Override public int getItemCount(int section) {
            return gradesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            holder.section.setText(sectionName(section));
            holder.section.setVisibility(View.VISIBLE);
            holder.dataLayout.setVisibility(View.GONE);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Grade grade = gradesInSection(section).get(relativePosition);
            holder.title.setText(grade.courseName);
            holder.desc.setText(Html.fromHtml(String.format("%s, termin: %s", grade.classType, grade.examSessionNumber)));
            holder.gradeDesc.setText(grade.valueDescription);
            holder.gradeSymbol.setText(grade.valueSymbol);
            holder.courseId = grade.courseId;
            holder.termId = grade.termId;
            holder.itemView.setBackgroundResource(relativePosition % 2 == 1 ? R.color.grey : android.R.color.white);
            holder.section.setVisibility(View.GONE);
            holder.dataLayout.setVisibility(View.VISIBLE);
        }

        List<Grade> gradesInSection(int section) {
            List<Grade>[] courses = data.values().toArray(new List[0]);
            return courses[section];
        }

        String sectionName(int section) {
            return ((String) data.keySet().toArray()[section]);
        }

        public void setData(SortedMap<String, List<Grade>> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title) TextView title;
        @Bind(R.id.desc) TextView desc;
        @Bind(R.id.grade_value_symbol) TextView gradeSymbol;
        @Bind(R.id.grade_value_desc) TextView gradeDesc;
        @Bind(R.id.section) TextView section;
        @Bind(R.id.dataLayout) View dataLayout;
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
