package mobi.kujon.google_drive.ui.dialogs.share_target;


import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;

public interface ChooseShareStudentsListener {
    void shareWith(@ShareFileTargetType String targetType, List<String> chosenStudentIds);
}
