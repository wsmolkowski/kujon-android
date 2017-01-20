package mobi.kujon.google_drive.ui.activities.semesters.recycler_classes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.SemesterDTO;

/**
 *
 */
public class SemesterVH extends RecyclerView.ViewHolder {
    @Bind(R.id.semester_name)
    TextView semesterNameTextView;

    public SemesterVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(SemesterDTO semesterDTO) {
        semesterNameTextView.setText(semesterDTO.getSemesterCode());
    }
}
