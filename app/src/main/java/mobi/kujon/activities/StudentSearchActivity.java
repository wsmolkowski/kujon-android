package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
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
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Item;
import mobi.kujon.network.json.gen.StudentSearchResult;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentSearchActivity extends BaseActivity implements EndlessRecyclerViewAdapter.RequestToLoadMoreListener {

    public static final String QUERY = "QUERY";
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private EndlessRecyclerViewAdapter endlessRecyclerViewAdapter;

    @Inject KujonBackendApi kujonBackendApi;

    private int start = 0;
    private Adapter adapter;
    private String query;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        endlessRecyclerViewAdapter = new EndlessRecyclerViewAdapter(this, adapter, this);
        recyclerView.setAdapter(endlessRecyclerViewAdapter);
        KujonApplication.getComponent().inject(this);
        query = getIntent().getStringExtra(QUERY);
        getSupportActionBar().setTitle("Wyniki wyszukiwania");
    }

    @Override public void onLoadMoreRequested() {
        kujonBackendApi.search(query, start).enqueue(new Callback<KujonResponse<StudentSearchResult>>() {
            @Override
            public void onResponse(Call<KujonResponse<StudentSearchResult>> call, Response<KujonResponse<StudentSearchResult>> response) {
                start += 20;
                StudentSearchResult data = response.body().data;
                if (data != null) {
                    adapter.addItems(data.items);
                    endlessRecyclerViewAdapter.onDataReady(data.next_page);
                } else {
                    endlessRecyclerViewAdapter.onDataReady(false);
                }
            }

            @Override public void onFailure(Call<KujonResponse<StudentSearchResult>> call, Throwable t) {
                endlessRecyclerViewAdapter.onDataReady(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    public static void start(Activity from, String query) {
        Intent intent = new Intent(from, StudentSearchActivity.class);
        intent.putExtra(QUERY, query);
        from.startActivity(intent);
    }

    protected class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<Item> items = new ArrayList<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_textview, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Item item = items.get(position);
            holder.lecturerName.setText(Html.fromHtml(item.match));
            holder.item = item;
            holder.itemView.setBackgroundResource(position % 2 == 1 ? R.color.grey : android.R.color.white);
        }

        @Override public int getItemCount() {
            return items.size();
        }

        public void addItems(@NonNull List<Item> newItems) {
            items.addAll(newItems);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name) TextView lecturerName;
        Item item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if ("Nieaktywny pracownik".equals(item.user.staff_status)) {
                    StudentDetailsActivity.showStudentDetails(StudentSearchActivity.this, item.user.id);
                } else {
                    LecturerDetailsActivity.showLecturerDatails(StudentSearchActivity.this, item.user.id);
                }
            });
        }
    }
}
