package mobi.kujon.google_drive.ui.activities.files.dagger;

import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;
import mobi.kujon.google_drive.ui.activities.files.FilesActivity;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public interface FilesActivityComponent {
    void inject(FilesActivity filesActivity);
    void inject(DowloadFileService injectTo);
    FileListMVP.Model provideModel();
    SchedulersHolder provideSchedulerHolder();
}
