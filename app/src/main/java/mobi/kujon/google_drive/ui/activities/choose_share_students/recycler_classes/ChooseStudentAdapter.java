package mobi.kujon.google_drive.ui.activities.choose_share_students.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;

public class ChooseStudentAdapter extends RecyclerView.Adapter<ChooseStudentViewHolder> {

    private List<StudentShareDto> studentsToShare;

    public ChooseStudentAdapter(List<StudentShareDto> studentsToShare) {
        this.studentsToShare = studentsToShare;
    }

    @Override
    public ChooseStudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_choose_student, parent, false);
        return new ChooseStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseStudentViewHolder holder, int position) {
        StudentShareDto dto = studentsToShare.get(position);
        holder.bind(dto);
    }

    @Override
    public int getItemCount() {
        return studentsToShare.size();
    }

    public void setStudentsToShare(List<StudentShareDto> studentsToShare) {
        this.studentsToShare = studentsToShare;
        notifyDataSetChanged();
    }

    public List<StudentShareDto> getStudentsToShare() {
        return studentsToShare;
    }
}
