package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;

public class StudentVH extends BaseFileDetailsVH<StudentShareDto> {


    @Bind(R.id.student_choice_checkbox)
    CheckBox studentChoiceCheckbox;

    @Bind(R.id.student_name)
    TextView studentName;


    @Bind(R.id.choose_student_container)
    RelativeLayout chooseStudentContainer;

    public StudentVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(StudentShareDto studentShareDto) {

    }


    public void bind(StudentShareDto studentShareDTO,boolean enable) {
        studentName.setText(studentShareDTO.getStudentName());
        studentChoiceCheckbox.setChecked(studentShareDTO.isChosen());
        if(enable) {
            studentName.setAlpha(1.0f);
            studentChoiceCheckbox.setChecked(studentShareDTO.isChosen());
            studentChoiceCheckbox.setEnabled(true);
            studentChoiceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> studentShareDTO.setChosen(isChecked));
            chooseStudentContainer.setOnClickListener(v -> studentChoiceCheckbox.setChecked(!studentChoiceCheckbox.isChecked()));
        } else {
            studentChoiceCheckbox.setChecked(true);
            studentChoiceCheckbox.setEnabled(false);
            studentName.setAlpha(0.5f);

        }
    }
}
