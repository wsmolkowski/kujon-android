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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class AbstractSearchActivity<S, T> extends BaseActivity implements EndlessRecyclerViewAdapter.RequestToLoadMoreListener {

    public static final String QUERY = "QUERY";
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    protected EndlessRecyclerViewAdapter endlessRecyclerViewAdapter;

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
        getSupportActionBar().setTitle("Wyniki wyszukiwania");
    }

    @Override public void onLoadMoreRequested() {
        getKujonResponseCall().enqueue(new Callback<KujonResponse<S>>() {
            @Override
            public void onResponse(Call<KujonResponse<S>> call, Response<KujonResponse<S>> response) {
                start += 20;
                S data = response.body().data;
                if (data != null) {
                    adapter.addItems(getItems(data));
                    endlessRecyclerViewAdapter.onDataReady(getNextPage(data));
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
            holder.item = item;
            holder.itemView.setBackgroundResource(position % 2 == 1 ? R.color.grey : android.R.color.white);
        }

        @Override public int getItemCount() {
            return items.size();
        }

        public void addItems(@NonNull List<T> newItems) {
            items.addAll(newItems);
            notifyDataSetChanged();
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
