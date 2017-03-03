package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.underscore.$;
import com.github.underscore.Predicate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.CourseGrades;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.TermGrades;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.predicates.CourseGradesPredicate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradesFragment extends AbstractFragmentSearchWidget<TermGrades> {

    private Adapter adapter;
    private int dark;
    private int red;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                boolean haveData = adapter.data != null && adapter.data.size() > 0;
                emptyInfo.setVisibility(haveData ? View.GONE : View.VISIBLE);
            }
        });
        showSpinner(true);
        dark = ContextCompat.getColor(activity, R.color.dark);
        red = ContextCompat.getColor(activity, android.R.color.holo_red_light);
        loadData(false);
    }

    @Override
    protected void setDataToAdapter(List<TermGrades> filter) {
        this.adapter.setData(filter);
    }

    @Override
    protected Predicate<TermGrades> createPredicate(String query) {
        return arg -> true;
    }

    @Override
    protected List<TermGrades> performFiltering(String query) {
        Predicate<CourseGrades> gradesPredicate = new CourseGradesPredicate(query);
        List<TermGrades> filteredTermGrades = new ArrayList<>();
        List<TermGrades> copyOfData = new ArrayList<>(dataFromApi);
        for (TermGrades termGrades : copyOfData) {
            List<CourseGrades> filteredCourseGrades = new ArrayList<>($.filter(termGrades.courses, gradesPredicate));
            if (filteredCourseGrades.size() > 0) {
                termGrades.courses = filteredCourseGrades;
                filteredTermGrades.add(termGrades);
            }
        }

        return filteredTermGrades;
    }

    @Override
    protected String getRequestUrl() {
        return backendApi.gradesByTerm().request().url().toString();
    }

    @Override
    protected void loadData(boolean refresh) {
        Call<KujonResponse<List<TermGrades>>> kujonResponseCall = refresh ? backendApi.gradesByTermRefresh() : backendApi.gradesByTerm();
        kujonResponseCall.enqueue(new Callback<KujonResponse<List<TermGrades>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<TermGrades>>> call, Response<KujonResponse<List<TermGrades>>> response) {
                showSpinner(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<TermGrades> data = response.body().data;
                    dataFromApi = data;
                    adapter.setData(data);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<TermGrades>>> call, Throwable t) {
                showSpinner(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.grades);
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        List<TermGrades> data = new ArrayList<>();
        List<List<Pair<CourseGrades, Grade>>> sections = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grade, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public int getSectionCount() {
            return data.size();
        }

        @Override
        public int getItemCount(int section) {
            return gradesInSection(section).size();
        }

        @Override
        public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            TermGrades termGrades = data.get(section);
            holder.section.setText(termGrades.termId + ", " + getString(R.string.average_grade) + ": " + termGrades.avrGrades);
            holder.section.setVisibility(View.VISIBLE);
            holder.dataLayout.setVisibility(View.GONE);
            holder.termId = termGrades.termId;
            holder.itemView.setTag(R.string.no_item_decorator, true);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Pair<CourseGrades, Grade> grade = gradesInSection(section).get(relativePosition);
            TermGrades termGrades = data.get(section);
            holder.title.setText(grade.first.courseName);
            holder.description.setText(Html.fromHtml(String.format("%s, %s: %s", grade.second.classType.name, getString(R.string.deadline),grade.second.examSessionNumber)));
            String symbol = grade.second.valueSymbol;
            holder.gradeSymbol.setText(symbol);
            if (symbol != null) {
                int color = "2".equals(symbol) || "nzal".equals(symbol.toLowerCase()) ? red : dark;
                holder.gradeSymbol.setTextColor(color);
                holder.gradeDesc.setTextColor(color);
            }
            holder.gradeDesc.setText(grade.second.valueDescription);
            holder.courseId = grade.first.courseId;
            holder.termId = termGrades.termId;
            holder.section.setVisibility(View.GONE);
            holder.dataLayout.setVisibility(View.VISIBLE);
            holder.itemView.setTag(R.string.no_item_decorator, false);
        }

        List<Pair<CourseGrades, Grade>> gradesInSection(int section) {
            return sections.get(section);
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
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.desc)
        TextView description;

        @BindView(R.id.section)
        TextView section;
        @BindView(R.id.dataLayout)
        View dataLayout;
        @BindView(R.id.grade_value_symbol)
        TextView gradeSymbol;

        @BindView(R.id.grade_value_desc)
        TextView gradeDesc;


        String courseId;
        String termId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> showCourseOrTerm(courseId, termId));
        }
    }
}
