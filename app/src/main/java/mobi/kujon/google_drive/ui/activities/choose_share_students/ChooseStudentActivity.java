package mobi.kujon.google_drive.ui.activities.choose_share_students;


import android.support.v7.app.AppCompatActivity;

import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;

public class ChooseStudentActivity extends AppCompatActivity implements HandleException, ChooseStudentsMVP.View {



    @Override
    public void handleException(Throwable throwable) {

    }

    @Override
    public void showStudentList(List<StudentShareDto> studentShareDtos) {

    }
}
