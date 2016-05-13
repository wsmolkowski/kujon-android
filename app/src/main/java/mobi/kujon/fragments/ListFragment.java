package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public abstract class ListFragment extends BaseFragment {

    @Inject protected KujonBackendApi backendApi;
    @Inject protected KujonUtils utils;

    protected @Bind(R.id.recyclerView) RecyclerView recyclerView;
    protected @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Bind(R.id.empty_info) TextView emptyInfo;
    protected BaseActivity activity;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeContainer.setOnRefreshListener(() -> {
            invalidate();
            loadData();
        });
    }

    private void invalidate() {
        String requestUrl = getRequestUrl();
        utils.invalidateEntry(requestUrl);
    }

    protected abstract String getRequestUrl();

    protected abstract void loadData();

    protected void showCourseOrTerm(String courseId, String termId) {
        if (!isEmpty(courseId) && !isEmpty(termId)) {
            CourseDetailsActivity.showCourseDetails(getActivity(), courseId, termId);
        } else if (!isEmpty(termId)) {
            backendApi.terms(termId).enqueue(new Callback<KujonResponse<Term2>>() {
                @Override public void onResponse(Call<KujonResponse<Term2>> call, Response<KujonResponse<Term2>> response) {
                    if (ErrorHandlerUtil.handleResponse(response)) {
                        Term2 term = response.body().data;
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
                        dlgAlert.setMessage(String.format("Numer: %s\nData początkowa: %s\nData końcowa: %s\nData zakończenia: %s",
                                term.termId, term.startDate, term.endDate, term.finishDate));
                        dlgAlert.setTitle(term.name.pl);
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        AlertDialog alertDialog = dlgAlert.create();
                        alertDialog.show();
                    }
                }

                @Override public void onFailure(Call<KujonResponse<Term2>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                }
            });
        }
    }
}
