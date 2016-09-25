package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.github.underscore.$;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.TermGrades;
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
        List<List<Grade>> sections = new ArrayList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grade, parent, false);
            return new ViewHolder(v);
        }

        @Override public int getSectionCount() {
            return data.size();
        }

        @Override public int getItemCount(int section) {
            return gradesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            holder.section.setText(sectionName(section));
            holder.section.setVisibility(View.VISIBLE);
            holder.dataLayout.setVisibility(View.GONE);
            holder.termId = sectionName(section);
            holder.itemView.setTag(R.string.no_item_decorator, true);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Grade grade = gradesInSection(section).get(relativePosition);
            TermGrades termGrades = data.get(section);
            holder.title.setText(grade.courseName);
            holder.desc.setText(Html.fromHtml(String.format("%s, termin: %s", grade.classType, grade.examSessionNumber)));
            holder.gradeDesc.setText(grade.valueDescription);
            holder.gradeSymbol.setText(grade.valueSymbol);
            holder.courseId = grade.courseId;
            holder.termId = termGrades.termId;
            holder.section.setVisibility(View.GONE);
            holder.dataLayout.setVisibility(View.VISIBLE);
            holder.itemView.setTag(R.string.no_item_decorator, false);
        }

        List<Grade> gradesInSection(int section) {
            return sections.get(section);
        }

        String sectionName(int section) {
            return data.get(section).termId;
        }

        public void setData(List<TermGrades> data) {
            this.data = data;
            sections.clear();
            for (TermGrades termGrades : data) {
                List<List<Grade>> nestedGrades = $.collect(termGrades.courses, it -> {
                    $.each(it.grades, grade -> {
                        grade.courseName = it.courseName;
                        grade.courseId = it.courseId;
                    });
                    return it.grades;
                });
                List<Grade> grades = $.flatten(nestedGrades);
                sections.add(grades);
            }
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
            itemView.setOnClickListener(v -> showCourseOrTerm(courseId, termId));
        }
    }
}
