package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;

public class FileDetailsAdapter extends RecyclerView.Adapter<BaseFileDetailsVH>{
    private final static int TYPE_HEADER = 345;
    private final static int TYPE_STUDENT = 999;
    private List<DisableableStudentShareDTO> studentShareDTOs;
    private OnEveryoneSwitchClicked onEveryoneSwitchClicked;
    private FileDTO fileDTO;

    public FileDetailsAdapter(List<DisableableStudentShareDTO> studentShareDTOs, OnEveryoneSwitchClicked onEveryoneSwitchClicked, FileDTO fileDTO) {
        this.studentShareDTOs = studentShareDTOs;
        this.onEveryoneSwitchClicked = onEveryoneSwitchClicked;
        this.fileDTO = fileDTO;
    }

    @Override
    public BaseFileDetailsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case TYPE_HEADER:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_file_details_header, parent, false);
                return new HeaderFileDetailsVH(v, onEveryoneSwitchClicked);
            case TYPE_STUDENT:
                View v2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_choose_student, parent, false);
                return new StudentVH(v2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseFileDetailsVH holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                if (fileDTO != null) {
                    ((HeaderFileDetailsVH) holder).bind(fileDTO);
                }
                break;
            case TYPE_STUDENT:
                ((StudentVH) holder).bind(studentShareDTOs.get(position - 1));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return studentShareDTOs.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_STUDENT;
        }
    }

    public void addFileDTO(FileDTO fileDTO) {
        this.fileDTO = fileDTO;
        notifyDataSetChanged();
    }

    public void addStudents(List<DisableableStudentShareDTO> students) {
        this.studentShareDTOs = students;
        notifyDataSetChanged();
    }

    public List<DisableableStudentShareDTO> getStudentShareDTOs() {
        return studentShareDTOs;
    }

    public interface OnEveryoneSwitchClicked {
        void onEveryoneClicked(boolean isEveryone);
    }
}
