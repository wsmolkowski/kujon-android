package mobi.kujon.google_drive.ui.activities.file_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import butterknife.Bind;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsView;
import mobi.kujon.google_drive.ui.activities.files.FilesActivity;

public class FileDetailsActivity extends AppCompatActivity implements FileDetailsView, ChooseStudentsMVP.View{

    private String coursId;
    private String termId;
    private String fileId;
    public static final String COURSE_ID_KEY = "COURSE_ID";
    public static final String TERM_ID_KEY = "TERM_ID";
    public static final String FILE_ID_KEY = "FILE_ID";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static void openActivity(Activity context, String courseId, String termId, String fileId) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(TERM_ID_KEY, termId);
        intent.putExtra(FILE_ID_KEY, fileId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_file_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share_file){

            finish();
            return true;
        }
        return false;
    }

    public String getCoursId() {
        return coursId;
    }

    public String getTermId() {
        return termId;
    }

    @Override
    public void displayFileProperties(FileDTO fileDTO) {

    }

    @Override
    public void fileShared() {

    }

    @Override
    public void displayFileShares(List<DisableableStudentShareDTO> shares) {

    }

    @Override
    public void handleException(Throwable throwable) {

    }

    @Override
    public void showStudentList(List<StudentShareDto> studentShareDtos) {

    }
}
