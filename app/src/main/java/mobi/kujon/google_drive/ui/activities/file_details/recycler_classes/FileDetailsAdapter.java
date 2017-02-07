package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_details.FileDetailsDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;

public class FileDetailsAdapter extends RecyclerView.Adapter<BaseFileDetailsVH>{
    private final static int TYPE_HEADER = 345;
    private final static int TYPE_STUDENT = 999;
    private FileDetailsDto fileDetailsDto;
    private OnEveryoneSwitchClicked onEveryoneSwitchClicked;
    private boolean shouldBeEnabled;

    public FileDetailsAdapter(OnEveryoneSwitchClicked onEveryoneSwitchClicked) {
        this.onEveryoneSwitchClicked = onEveryoneSwitchClicked;
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
                if(fileDetailsDto !=null)
                    ((HeaderFileDetailsVH) holder).bind(fileDetailsDto.getFileDTO());
                break;
            case TYPE_STUDENT:

                ((StudentVH) holder).bind(fileDetailsDto.getStudentShareDto().get(position - 1), shouldBeEnabled);
                break;
        }
    }

    @Override
    public int getItemCount() {
        try {

            return fileDetailsDto.getStudentShareDto().size() + 1;
        }catch (NullPointerException npe){
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
        for(StudentShareDto studentShareDto:this.fileDetailsDto.getStudentShareDto()){
            List<String> strings = new ArrayList<>(fileSharedWith);
            studentShareDto.setChosen(false);
            for(String studentId:strings){
                if(studentShareDto.getStudentId().equals(studentId)){
                    studentShareDto.setChosen(true);
                    fileSharedWith.remove(studentId);
                    break;
                }
            }
        }
        this.notifyDataSetChanged();
    }

    public interface OnEveryoneSwitchClicked {
        void onEveryoneClicked(boolean isEveryone);
    }
}
