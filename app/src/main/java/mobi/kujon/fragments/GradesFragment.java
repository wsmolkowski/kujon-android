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

import java.util.LinkedList;
import java.util.List;

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
        backendApi.grades().enqueue(new Callback<KujonResponse<List<Grade>>>() {
            @Override public void onResponse(Call<KujonResponse<List<Grade>>> call, Response<KujonResponse<List<Grade>>> response) {
                activity.showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Grade> data = response.body().data;
                    adapter.setData(data);
                }
            }

            @Override public void onFailure(Call<KujonResponse<List<Grade>>> call, Throwable t) {
                activity.showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Oceny");
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Grade> data = new LinkedList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grade, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Grade grade = data.get(position);
            holder.title.setText(grade.courseName + " - " + grade.termId);
            holder.desc.setText(Html.fromHtml(String.format("Ocena %s, termin: %s", grade.classType, grade.examSessionNumber)));
            holder.gradeDesc.setText(grade.valueDescription);
            holder.gradeSymbol.setText(grade.valueSymbol);
            holder.courseId = grade.courseId;
            holder.termId = grade.termId;
            holder.itemView.setBackgroundResource(position % 2 == 1 ? R.color.grey : android.R.color.white);
        }

        @Override public int getItemCount() {
            return data.size();
        }

        public void setData(List<Grade> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title) TextView title;
        @Bind(R.id.desc) TextView desc;
        @Bind(R.id.grade_value_symbol) TextView gradeSymbol;
        @Bind(R.id.grade_value_desc) TextView gradeDesc;
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
