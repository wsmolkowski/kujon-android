package mobi.kujon.google_drive.ui.fragments.files.recycler_classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FileVH> {

    private List<FileDTO> fileDTOs;
    private OnFileClick onFileClick;

    public FilesRecyclerAdapter(List<FileDTO> fileDTOs, OnFileClick onFileClick) {
        this.fileDTOs = fileDTOs;
        this.onFileClick = onFileClick;
    }

    @Override
    public FileVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_file, parent, false);
        return new FileVH(v);

    }

    @Override
    public void onBindViewHolder(FileVH holder, int position) {
        holder.bind(fileDTOs.get(position), onFileClick);
    }

    public void setFileDTOs(List<FileDTO> fileDTOs) {
        this.fileDTOs = fileDTOs;
    }

    @Override
    public int getItemCount() {
        return fileDTOs.size();
    }

    public interface OnFileClick {
        void onFileClick(FileDTO fileDTO);
    }

    public List<FileDTO> getFileDTOs() {
        return fileDTOs;
    }
}
