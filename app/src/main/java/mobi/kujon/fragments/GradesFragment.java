package mobi.kujon.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.R;
import mobi.kujon.databinding.RowGradeBinding;
import mobi.kujon.network.json.CourseGrades;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.TermGrades;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradesFragment extends ListFragment {

    private Adapter adapter;
    private int dark;
    private int red;

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
        dark = ContextCompat.getColor(activity, R.color.dark);
        red = ContextCompat.getColor(activity, android.R.color.holo_red_light);
        loadData(false);
    }

    @Override protected String getRequestUrl() {
        return backendApi.gradesByTerm().request().url().toString();
    }

    @Override protected void loadData(boolean refresh) {
        Call<KujonResponse<List<TermGrades>>> kujonResponseCall = refresh ? backendApi.gradesByTermRefresh() : backendApi.gradesByTerm();
        kujonResponseCall.enqueue(new Callback<KujonResponse<List<TermGrades>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<TermGrades>>> call, Response<KujonResponse<List<TermGrades>>> response) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<TermGrades> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<TermGrades>>> call, Throwable t) {
                activity.showProgress(false);
                swipeContainer.setRefreshing(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.grades);
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        List<TermGrades> data = new ArrayList<>();
        List<List<Pair<CourseGrades, Grade>>> sections = new ArrayList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RowGradeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_grade, parent, false);
            return new ViewHolder(binding);
        }

        @Override public int getSectionCount() {
            return data.size();
        }

        @Override public int getItemCount(int section) {
            return gradesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            holder.binding.section.setText(sectionName(section));
            holder.binding.section.setVisibility(View.VISIBLE);
            holder.binding.dataLayout.setVisibility(View.GONE);
            holder.termId = sectionName(section);
            holder.itemView.setTag(R.string.no_item_decorator, true);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Pair<CourseGrades, Grade> grade = gradesInSection(section).get(relativePosition);
            TermGrades termGrades = data.get(section);
            holder.binding.title.setText(grade.first.courseName);
            holder.binding.desc.setText(Html.fromHtml(String.format("%s, termin: %s", grade.second.classType.name, grade.second.examSessionNumber)));
            String symbol = grade.second.valueSymbol;
            holder.binding.gradeValueSymbol.setText(symbol);
            int color = "2".equals(symbol) || "nzal".equals(symbol.toLowerCase()) ? red : dark;
            holder.binding.gradeValueSymbol.setTextColor(color);
            holder.binding.gradeValueDesc.setText(grade.second.valueDescription);
            holder.binding.gradeValueDesc.setTextColor(color);
            holder.courseId = grade.first.courseId;
            holder.termId = termGrades.termId;
            holder.binding.section.setVisibility(View.GONE);
            holder.binding.dataLayout.setVisibility(View.VISIBLE);
            holder.itemView.setTag(R.string.no_item_decorator, false);
        }

        List<Pair<CourseGrades, Grade>> gradesInSection(int section) {
            return sections.get(section);
        }

        String sectionName(int section) {
            return data.get(section).termId;
        }

        public void setData(List<TermGrades> data) {
            this.data = data;
            sections.clear();
            for (TermGrades termGrades : data) {
                List<Pair<CourseGrades, Grade>> result = Stream.of(termGrades.courses)
                        .flatMap(course -> Stream.of(course.grades)
                                .map(grade -> Pair.create(course, grade)))
                        .collect(Collectors.toList());

                sections.add(result);
            }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String courseId;
        String termId;
        public final RowGradeBinding binding;

        public ViewHolder(RowGradeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> showCourseOrTerm(courseId, termId));
        }
    }
}
