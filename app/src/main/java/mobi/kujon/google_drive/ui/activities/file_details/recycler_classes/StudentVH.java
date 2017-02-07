package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;

public class StudentVH extends BaseFileDetailsVH<DisableableStudentShareDTO> {


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
    public void bind(DisableableStudentShareDTO studentShareDTO) {
        studentName.setText(studentShareDTO.getStudentName());
        studentChoiceCheckbox.setChecked(studentShareDTO.isChosen());
        if(studentShareDTO.isEnabled()) {
            studentChoiceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> studentShareDTO.setChosen(isChecked));
            chooseStudentContainer.setOnClickListener(v -> studentChoiceCheckbox.setChecked(!studentChoiceCheckbox.isChecked()));
        } else {
            chooseStudentContainer.setAlpha((float) 0.6);
        }
    }
}
