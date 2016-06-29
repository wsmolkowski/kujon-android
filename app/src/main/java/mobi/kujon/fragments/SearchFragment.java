package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import bolts.CancellationTokenSource;
import bolts.Task;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.StudentSearchActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.StudentSearchResult;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends BaseFragment {

    @Bind(R.id.student_query) EditText studentQuery;
    @BindString(R.string.search_student_hint) String searchStudentHint;

    @Bind(R.id.student_query_input_layout) TextInputLayout studentQueryInputLayout;
    @Bind(R.id.student_search_message) TextView studentSearchMessage;
    private CancellationTokenSource cts;

    @Inject KujonBackendApi kujonBackendApi;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);

        return rootView;
    }

    @OnClick(R.id.student_search)
    public void studentSearch() {
        StudentSearchActivity.start(getActivity(), studentQuery.getText().toString());
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("Szukaj");
        studentQuery.addTextChangedListener(new TextWatcher() {

            private Call<KujonResponse<StudentSearchResult>> searchCall;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override public void afterTextChanged(Editable s) {
                String query = s.toString();
                System.out.println("New query: " + query);
                if (cts != null) {
                    cts.cancel();
                    cts = null;
                }
                if (searchCall != null) {
                    searchCall.cancel();
                    searchCall = null;
                }
                if (query.length() == 0) {
                    studentSearchMessage.setText("");
                } else if (query.length() < 4) {
                    studentSearchMessage.setText("Minimum 4 znaki");
                } else {
                    studentSearchMessage.setText("");

                    cts = new CancellationTokenSource();
                    Task.delay(500, cts.getToken()).onSuccess(task -> {
                        studentSearchMessage.setText("Szukam...");
                        System.out.println("Searching " + query);
                        searchCall = kujonBackendApi.search(query);
                        searchCall.enqueue(new Callback<KujonResponse<StudentSearchResult>>() {
                            @Override
                            public void onResponse(Call<KujonResponse<StudentSearchResult>> call, Response<KujonResponse<StudentSearchResult>> response) {
                                StudentSearchResult data = response.body().data;
                                if (data != null) {
                                    int size = data.items.size();
                                    studentSearchMessage.setText(String.format("Znaleziono %s wyników", size < 20 ? size : "20+"));
                                } else {
                                    studentSearchMessage.setText("Nie znaleziono wyników");
                                }
                            }

                            @Override public void onFailure(Call<KujonResponse<StudentSearchResult>> call, Throwable t) {
                                if (!call.isCanceled()) {
                                    ErrorHandlerUtil.handleError(t);
                                }
                            }
                        });
                        return null;
                    }, Task.UI_THREAD_EXECUTOR);
                }
            }
        });
    }
}
