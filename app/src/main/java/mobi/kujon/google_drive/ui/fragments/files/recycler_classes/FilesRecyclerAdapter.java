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

    public FilesRecyclerAdapter(List<FileDTO> fileDTOs) {
        this.fileDTOs = fileDTOs;
    }

    @Override
    public FileVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_file, parent, false);
        return new FileVH(v);

    }

    @Override
    public void onBindViewHolder(FileVH holder, int position) {
        holder.bind(fileDTOs.get(position));
    }

    public void setFileDTOs(List<FileDTO> fileDTOs) {
        this.fileDTOs = fileDTOs;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fileDTOs.size();
    }
}
