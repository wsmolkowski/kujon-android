package mobi.kujon.google_drive.ui.fragments.files;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import mobi.kujon.google_drive.ui.dialogs.FileActionDialog;
import mobi.kujon.google_drive.ui.fragments.BaseFileFragment;
import mobi.kujon.google_drive.ui.fragments.files.recycler_classes.FilesRecyclerAdapter;


public class FilesListFragment extends BaseFileFragment<FilesListFragment> implements FileListMVP.FilesView, FilesRecyclerAdapter.OnFileClick {

    private static final String OWNER_ID = "param1";


    private @FilesOwnerType int fileOwnerType;
    private FilesRecyclerAdapter adapter;

    public FilesListFragment() {

    }



    @Bind(R.id.file_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    FileListMVP.LoadPresenter presenter;


    public static FilesListFragment newInstance(@FilesOwnerType  int param1) {
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
            fileOwnerType =  getArguments().getInt(OWNER_ID);
        }
    }


    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files_list, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new FilesRecyclerAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.loadListOfFiles(false,fileOwnerType);
        this.setProgress(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadListOfFiles(true,fileOwnerType);
        });
        return rootView;
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
        adapter.setFileDTOs(fileDTOs);
        this.setProgress(false);
    }


    public void reload(){
        this.setProgress(true);
        presenter.loadListOfFiles(true,fileOwnerType);
    }

    @Override
    public void onFileClick(FileDTO fileDTO) {
        FileActionDialog dialog = FileActionDialog.newInstance(fileDTO.isMy());
        dialog.show(getActivity().getFragmentManager(), FileActionDialog.NAME);
    }
}
