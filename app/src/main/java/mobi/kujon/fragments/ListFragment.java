package mobi.kujon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.CourseDetailsActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Term2;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.ShowTermDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.text.TextUtils.isEmpty;

public abstract class ListFragment extends BaseFragment {

    public static final int REQUEST_CODE = 1765;
    @Inject
    protected KujonBackendApi backendApi;
    @Inject
    protected KujonUtils utils;

    protected
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    protected
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.empty_info)
    TextView emptyInfo;
    protected BaseActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        return rootView;
    }

    public void showSpinner(boolean show) {
        activity.showProgress(show);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        swipeContainer.setOnRefreshListener(() -> {
            invalidate();
            loadData(true);
        });
    }

    private void invalidate() {
        String requestUrl = getRequestUrl();
        utils.invalidateEntry(requestUrl);
    }

    protected abstract String getRequestUrl();

    protected abstract void loadData(boolean refresh);


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            this.loadData(false,true);
        }
    }

    protected void loadData(boolean refresh, boolean invalidate) {
        this.loadData(refresh);
    }

    protected void showCourseOrTerm(String courseId, String termId) {
        if (!isEmpty(courseId) && !isEmpty(termId)) {
            CourseDetailsActivity.showCourseDetails(this, courseId, termId, REQUEST_CODE);
        } else if (!isEmpty(termId)) {
            backendApi.terms(termId).enqueue(new Callback<KujonResponse<List<Term2>>>() {
                @Override
                public void onResponse(Call<KujonResponse<List<Term2>>> call, Response<KujonResponse<List<Term2>>> response) {
                    if (ErrorHandlerUtil.handleResponse(response)) {
                        ShowTermDialog.showTermDialog(getActivity(),response.body().data.get(0));
                    }
                }

                @Override
                public void onFailure(Call<KujonResponse<List<Term2>>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                }
            });
        }
    }
}
