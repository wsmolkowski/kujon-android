package mobi.kujon.google_drive.ui.dialogs.share_target;


import java.util.List;

public interface ChooseShareStudentsListener {
    void shareWithAll();
    void shareWithChosen(List<String> chosenStudentIds);
}
