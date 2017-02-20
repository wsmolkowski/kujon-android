package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class AbstractSearchActivity<S, T> extends BaseActivity implements EndlessRecyclerViewAdapter.RequestToLoadMoreListener {

    public static final String QUERY = "QUERY";
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    protected EndlessRecyclerViewAdapter endlessRecyclerViewAdapter;

    @Bind(R.id.empty_text) TextView emptyTextView;

    @Inject KujonBackendApi kujonBackendApi;

    protected int start = 0;
    protected Adapter adapter;
    protected String query;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        query = getIntent().getStringExtra(QUERY);
        endlessRecyclerViewAdapter = new EndlessRecyclerViewAdapter(this, adapter, this);
        recyclerView.setAdapter(endlessRecyclerViewAdapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        getSupportActionBar().setTitle(R.string.search_results);
    }

    @Override public void onLoadMoreRequested() {
        getKujonResponseCall().enqueue(new Callback<KujonResponse<S>>() {
            @Override
            public void onResponse(Call<KujonResponse<S>> call, Response<KujonResponse<S>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    start += 20;
                    S data = response.body().data;
                    if (data != null) {
                        adapter.addItems(getItems(data));
                        endlessRecyclerViewAdapter.onDataReady(getNextPage(data));
                    } else {
                        endlessRecyclerViewAdapter.onDataReady(false);
                    }
                } else {
                    endlessRecyclerViewAdapter.onDataReady(false);
                }
            }

            @Override public void onFailure(Call<KujonResponse<S>> call, Throwable t) {
                endlessRecyclerViewAdapter.onDataReady(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    protected abstract boolean getNextPage(S data);

    protected abstract List<T> getItems(S data);

    protected abstract Call<KujonResponse<S>> getKujonResponseCall();

    protected abstract void handeClick(T item);

    protected boolean isClickable() {
        return true;
    }

    protected abstract String getMatch(T item);

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<T> items = new ArrayList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_textview, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            T item = items.get(position);
            holder.lecturerName.setText(Html.fromHtml(getMatch(item)));
            if (!isClickable()) holder.lecturerName.setCompoundDrawables(null, null, null, null);
            holder.item = item;
        }

        @Override public int getItemCount() {
            return items.size();
        }

        public void addItems(@NonNull List<T> newItems) {
            items.addAll(newItems);
            if(items.isEmpty()) {
                emptyTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name) TextView lecturerName;
        T item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> handeClick(item));
        }
    }
}
