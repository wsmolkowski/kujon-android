package mobi.kujon.google_drive.ui.activities.files;

import com.google.android.gms.common.api.GoogleApiClient;

import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.upload_file.UploadFileMVP;
import mobi.kujon.google_drive.ui.dialogs.choose_file_source.ChooseFileSourceListener;
import mobi.kujon.google_drive.ui.dialogs.file_info_dialog.FileActionListener;
import mobi.kujon.google_drive.ui.dialogs.share_target.ChooseShareStudentsListener;
import mobi.kujon.google_drive.ui.fragments.ProvideInjector;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;

/**
 *
 */

public interface FileActivityView extends  ProvideInjector<FilesListFragment>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        FileStreamUpdateMVP.View,
        UploadFileMVP.View,
        ChooseShareStudentsListener, FileActionListener, FileListMVP.DeleteView, ChooseFileSourceListener {
}
