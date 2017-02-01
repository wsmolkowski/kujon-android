package mobi.kujon.google_drive.ui.activities.choose_share_students.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;

public class ChooseStudentViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.student_choice_checkbox)
    CheckBox studentChoiceCheckbox;

    @Bind(R.id.student_name)
    TextView studentName;

    public ChooseStudentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(StudentShareDto studentShareDto) {
        studentName.setText(studentShareDto.getStudentName());
        studentChoiceCheckbox.setChecked(studentShareDto.isChosen());
        studentChoiceCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                studentShareDto.setChosen(isChecked);
            }
        });
    }

}
