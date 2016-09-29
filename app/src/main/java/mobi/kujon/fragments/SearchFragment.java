package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.underscore.Function1;

import javax.inject.Inject;

import bolts.CancellationTokenSource;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.CoursesSearchActivity;
import mobi.kujon.activities.FacultySearchActivity;
import mobi.kujon.activities.ProgrammeSearchActivity;
import mobi.kujon.activities.StudentSearchActivity;
import mobi.kujon.activities.ThesesSearchActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.CoursersSearchResult;
import mobi.kujon.network.json.gen.FacultiesSearchResult;
import mobi.kujon.network.json.gen.ProgrammeSearchResult;
import mobi.kujon.network.json.gen.StudentSearchResult;
import mobi.kujon.network.json.gen.ThesesSearchResult;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends BaseFragment {

    @Inject KujonBackendApi kujonBackendApi;

    @Bind(R.id.student_query) EditText studentQuery;
    @Bind(R.id.student_search_message) TextView studentSearchMessage;

    @Bind(R.id.faculty_query) EditText facultyQuery;
    @Bind(R.id.faculty_search_message) TextView facultySearchMessage;

    @Bind(R.id.course_query) EditText courseQuery;
    @Bind(R.id.course_search_message) TextView courseSearchMessage;

    @Bind(R.id.programme_query) EditText programmeQuery;
    @Bind(R.id.programme_search_message) TextView programmeSearchMessage;

    @Bind(R.id.thesis_query) EditText thesisQuery;
    @Bind(R.id.thesis_search_message) TextView thesisSearchMessage;

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

    @OnClick(R.id.course_search)
    public void courseSearch() {
        CoursesSearchActivity.start(getActivity(), courseQuery.getText().toString());
    }

    @OnClick(R.id.faculty_search)
    public void facultySearch() {
        FacultySearchActivity.start(getActivity(), facultyQuery.getText().toString());
    }

    @OnClick(R.id.programme_search)
    public void programmeSearch() {
        ProgrammeSearchActivity.start(getActivity(), programmeQuery.getText().toString());
    }

    @OnClick(R.id.thesis_search)
    public void thesisSearch() {
        ThesesSearchActivity.start(getActivity(), thesisQuery.getText().toString());
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).setToolbarTitle(R.string.search);

        studentQuery.addTextChangedListener(getTextWatcher(studentSearchMessage, query -> kujonBackendApi.searchStudent(query, 0)));
        facultyQuery.addTextChangedListener(getTextWatcher(facultySearchMessage, query -> kujonBackendApi.searchFaculty(query, 0)));
        courseQuery.addTextChangedListener(getTextWatcher(courseSearchMessage, query -> kujonBackendApi.searchCourses(query, 0)));
        programmeQuery.addTextChangedListener(getTextWatcher(programmeSearchMessage, query -> kujonBackendApi.searchProgrammes(query, 0)));
        thesisQuery.addTextChangedListener(getTextWatcher(thesisSearchMessage, query -> kujonBackendApi.searchTheses(query, 0)));

        studentQuery.setOnEditorActionListener(startSearch(this::studentSearch));
        facultyQuery.setOnEditorActionListener(startSearch(this::facultySearch));
        courseQuery.setOnEditorActionListener(startSearch(this::courseSearch));
        programmeQuery.setOnEditorActionListener(startSearch(this::programmeSearch));
        thesisQuery.setOnEditorActionListener(startSearch(this::thesisSearch));
    }

    @NonNull private TextView.OnEditorActionListener startSearch(Runnable runnable) {
        return (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                runnable.run();
                return true;
            } else {
                return false;
            }
        };
    }

    private int getItemsSize(Object data) {
        if (data instanceof StudentSearchResult) return ((StudentSearchResult) data).items.size();
        if (data instanceof FacultiesSearchResult) return ((FacultiesSearchResult) data).items.size();
        if (data instanceof CoursersSearchResult) return ((CoursersSearchResult) data).items.size();
        if (data instanceof ProgrammeSearchResult) return ((ProgrammeSearchResult) data).items.size();
        if (data instanceof ThesesSearchResult) return ((ThesesSearchResult) data).items.size();

        return 0;
    }

    @NonNull private <T> TextWatcher getTextWatcher(final TextView searchMessage, Function1<String, Call<KujonResponse<T>>> search) {
        return new TextWatcher() {

            private Call<KujonResponse<T>> searchCall;
            private CancellationTokenSource cts;

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
                    searchMessage.setText("");
                } else if (query.length() < 4) {
                    searchMessage.setText(R.string.search_minimum);
                } else {
                    searchMessage.setText("");

                    cts = new CancellationTokenSource();
                    Task.delay(500, cts.getToken()).onSuccess(task -> {
                        searchMessage.setText(R.string.searching);
                        // System.out.println("Searching " + query);
                        searchCall = search.apply(query);
                        searchCall.enqueue(new Callback<KujonResponse<T>>() {
                            @Override
                            public void onResponse(Call<KujonResponse<T>> call, Response<KujonResponse<T>> response) {
                                T data = response.body().data;
                                if (data != null) {
                                    int size = getItemsSize(data);
                                    searchMessage.setText(String.format("%s wyników", size < 20 ? size : "20+"));
                                } else {
                                    searchMessage.setText(R.string.no_results_found);
                                }
                            }

                            @Override public void onFailure(Call<KujonResponse<T>> call, Throwable t) {
                                if (!call.isCanceled()) {
                                    ErrorHandlerUtil.handleError(t);
                                }
                            }
                        });
                        return null;
                    }, Task.UI_THREAD_EXECUTOR);
                }
            }
        };
    }
}
