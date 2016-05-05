package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;

public abstract class ListFragment extends Fragment {

    protected @Bind(R.id.recyclerView) RecyclerView recyclerView;
    protected @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    protected KujonBackendApi backendApi;
    @Bind(R.id.empty_info) TextView emptyInfo;
    protected BaseActivity activity;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        backendApi = KujonBackendService.getInstance().getKujonBackendApi();

        swipeContainer.setOnRefreshListener(() -> loadData());
    }

    protected abstract void loadData();
}
