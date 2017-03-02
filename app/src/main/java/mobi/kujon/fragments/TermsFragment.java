package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.github.underscore.$;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Term2;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends ListFragment {

    private Adapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        showSpinner(true);
        loadData(false);
    }


    @Override
    protected String getRequestUrl() {
        return backendApi.terms().request().url().toString();
    }

    @Override
    protected void loadData(boolean refresh) {
        Call<KujonResponse<List<Term2>>> terms = refresh ? backendApi.termsRefresh() : backendApi.terms();
        terms.enqueue(new Callback<KujonResponse<List<Term2>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<Term2>>> call, Response<KujonResponse<List<Term2>>> response) {
                showSpinner(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<Term2> data = response.body().data;

                    List<Term2> active = $.filter(data, arg -> arg.active);
                    List<Term2> inactivie = $.filter(data, arg -> !arg.active);
                    adapter.setData(active, inactivie);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<Term2>>> call, Throwable t) {
                showSpinner(false);
                ErrorHandlerUtil.handleError(t);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.study_cycles);
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        List<Term2> data1 = new LinkedList<>();
        List<Term2> data2 = new LinkedList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_terms, parent, false);
            return new ViewHolder(v);
        }


        @Override
        public int getSectionCount() {
            return 2;
        }

        @Override
        public int getItemCount(int section) {
            if (section == 0) {
                return data1.size();
            } else return data2.size();
        }


        @Override
        public void onBindHeaderViewHolder(ViewHolder holder, int section) {

            holder.section.setText(section == 0 ? getString(R.string.active) : getString(R.string.inactive));
            holder.section.setVisibility(View.VISIBLE);
            holder.termsView.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            Term2 term = section == 0 ? data1.get(relativePosition) : data2.get(relativePosition);
            holder.section.setVisibility(View.GONE);
            holder.termsView.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
            if (section == 0) {
                holder.finishDate.setVisibility(View.GONE);
                holder.finishLabel.setVisibility(View.GONE);
            } else {
                holder.finishDate.setVisibility(View.VISIBLE);
                holder.finishLabel.setVisibility(View.VISIBLE);
                holder.finishDate.setText(term.finishDate);
            }
            holder.termName.setText(term.name);
            holder.termId.setText(term.termId);
            holder.startDate.setText(term.startDate);
            holder.endDate.setText(term.endDate);
        }

        public void setData(List<Term2> active, List<Term2> inactive) {
            this.data1 = active;
            this.data2 = inactive;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public
        @Bind(R.id.section)
        TextView section;
        public
        @Bind(R.id.term_layout)
        View termsView;
        public
        @Bind(R.id.term_name_text)
        TextView termName;
        public
        @Bind(R.id.term_id_text_view)
        TextView termId;
        public
        @Bind(R.id.start_date_text)
        TextView startDate;
        public
        @Bind(R.id.end_date_text)
        TextView endDate;
        public
        @Bind(R.id.finish_date_text)
        TextView finishDate;
        public
        @Bind(R.id.finish_date_label)
        View finishLabel;
        @Bind(R.id.divider)
        View divider;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
