package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_details.FileDetailsDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;

public class FileDetailsAdapter extends RecyclerView.Adapter<BaseFileDetailsVH> {
    private final static int TYPE_HEADER = 345;
    private final static int TYPE_STUDENT = 999;
    private FileDetailsDto fileDetailsDto;
    private OnEveryoneSwitchClicked onEveryoneSwitchClicked;
    private boolean shouldBeEnabled;
    private boolean changed;

    public FileDetailsAdapter(OnEveryoneSwitchClicked onEveryoneSwitchClicked) {
        this.onEveryoneSwitchClicked = onEveryoneSwitchClicked;
    }

    @Override
    public BaseFileDetailsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
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
                if (fileDetailsDto != null)
                    ((HeaderFileDetailsVH) holder).bind(fileDetailsDto.getFileDTO());
                break;
            case TYPE_STUDENT:

                StudentShareDto studentShareDTO = fileDetailsDto.getStudentShareDto().get(position - 1);
                StudentVH holder1 = (StudentVH) holder;
                holder1.studentChoiceCheckbox.setOnCheckedChangeListener(null);
                holder1.bind(studentShareDTO, shouldBeEnabled);
                holder1.studentChoiceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    this.changed = true;
                    studentShareDTO.setChosen(isChecked);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        try {

            return fileDetailsDto.getStudentShareDto().size() + 1;
        } catch (NullPointerException npe) {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_STUDENT;
        }
    }


    public void setFileDetailsDto(FileDetailsDto fileDetailsDto) {
        this.fileDetailsDto = fileDetailsDto;
        shouldBeEnabled = !fileDetailsDto.getFileDTO().getShareType().equals(ShareFileTargetType.ALL);
        this.notifyDataSetChanged();
    }

    public List<StudentShareDto> getStudentShareDTOs() {
        return this.fileDetailsDto.getStudentShareDto();
    }

    public void setShareType(@ShareFileTargetType String shareType, List<String> fileSharedWith) {
        this.fileDetailsDto.getFileDTO().setShareType(shareType);
        shouldBeEnabled = !fileDetailsDto.getFileDTO().getShareType().equals(ShareFileTargetType.ALL);
        this.fileDetailsDto.setUpChoosen(fileSharedWith);
        this.notifyDataSetChanged();
    }

    public interface OnEveryoneSwitchClicked {
        void onEveryoneClicked(boolean isEveryone);
    }

    public boolean isChanged() {
        return changed;
    }
}
