package mobi.kujon.google_drive.ui.activities.files.dagger;

import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.ui.activities.files.FilesActivity;

/**
 *
 */

public interface FilesActivityComponent {
    void inject(FilesActivity filesActivity);
    FileListMVP.Model provideModel();
}
