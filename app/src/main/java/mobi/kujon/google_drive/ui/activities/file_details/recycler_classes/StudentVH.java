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

    @Bind(R.id.enable_layout)
    View enableLayout;

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
            studentChoiceCheckbox.setChecked(studentShareDTO.isChosen());
            enableLayout.setVisibility(View.GONE);
            studentChoiceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> studentShareDTO.setChosen(isChecked));
            chooseStudentContainer.setOnClickListener(v -> studentChoiceCheckbox.setChecked(!studentChoiceCheckbox.isChecked()));
        } else {
            studentChoiceCheckbox.setChecked(true);
            enableLayout.setVisibility(View.VISIBLE);

        }
    }
}
