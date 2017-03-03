package mobi.kujon.google_drive.ui.activities.choose_share_students.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;

public class ChooseStudentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.student_choice_checkbox)
    CheckBox studentChoiceCheckbox;

    @BindView(R.id.student_name)
    TextView studentName;

    @BindView(R.id.choose_student_container)
    RelativeLayout chooseStudentContainer;

    public ChooseStudentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(StudentShareDto studentShareDto) {
        studentName.setText(studentShareDto.getStudentName());
        studentChoiceCheckbox.setChecked(studentShareDto.isChosen());
        studentChoiceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> studentShareDto.setChosen(isChecked));
        chooseStudentContainer.setOnClickListener(v -> studentChoiceCheckbox.setChecked(!studentChoiceCheckbox.isChecked()));
    }

}
