package mobi.kujon.google_drive.ui.fragments.files;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import mobi.kujon.google_drive.ui.dialogs.file_info_dialog.FileActionDialog;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.DateSortStrategy;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortDialog;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortStrategy;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortStrategyListener;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortStrategyType;
import mobi.kujon.google_drive.ui.fragments.BaseFileFragment;
import mobi.kujon.google_drive.ui.fragments.files.recycler_classes.FilesRecyclerAdapter;


public class FilesListFragment extends BaseFileFragment<FilesListFragment> implements FileListMVP.FilesView, FilesRecyclerAdapter.OnFileClick, SortStrategyListener {

    private static final String OWNER_ID = "param1";
    public static final String SORT_DIALOG = "SortDialog";


    private
    @FilesOwnerType
    int fileOwnerType;
    private FilesRecyclerAdapter adapter;

    public FilesListFragment() {

    }


    @Bind(R.id.file_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.empty_folder_id)
    View emptyView;

    @Inject
    FileListMVP.LoadPresenter presenter;


    private
    @SortStrategyType
    int sortType = SortStrategyType.DATE;
    private SortStrategy sortStrategy = new DateSortStrategy();

    public static FilesListFragment newInstance(@FilesOwnerType int param1) {
        FilesListFragment fragment = new FilesListFragment();
        Bundle args = new Bundle();
        args.putInt(OWNER_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //noinspection ResourceType
            fileOwnerType = getArguments().getInt(OWNER_ID);
        }
        setHasOptionsMenu(true);
    }


    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files_list, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new FilesRecyclerAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.loadListOfFiles(false, fileOwnerType, sortStrategy);
        this.setProgress(true);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadListOfFiles(true, fileOwnerType, sortStrategy));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_files, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sort_action) {
            SortDialog sortDialog = SortDialog.newInstance(sortType);
            sortDialog.setUpListener(this);
            sortDialog.show(getFragmentManager(), SORT_DIALOG);
        }
        return false;
    }

    @Override
    protected void setProgress(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    protected void injectYourself(Injector<FilesListFragment> injector) {
        injector.inject(this);
    }


    @Override
    public void listOfFilesLoaded(List<FileDTO> fileDTOs) {

        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setFileDTOs(fileDTOs);
        this.setProgress(false);
    }

    @Override
    public void noFilesAdded() {
        this.setProgress(false);

        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }


    public void reload( boolean t) {
        this.setProgress(t);
        presenter.loadListOfFiles(t, fileOwnerType, sortStrategy);
    }

    @Override
    public void onFileClick(FileDTO fileDTO) {
        FileActionDialog dialog = FileActionDialog.newInstance(fileDTO);
        dialog.show(getActivity().getFragmentManager(), FileActionDialog.NAME);
    }

    @Override
    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
        this.sortType = sortStrategy.getSortStrategy();
        presenter.sortList(this.sortStrategy);

    }
}
