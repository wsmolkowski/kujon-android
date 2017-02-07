package mobi.kujon.google_drive.dagger.injectors;


import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;
import mobi.kujon.google_drive.ui.activities.file_details.dagger.DaggerRuntimeFileDetailsActivityComponent;
import mobi.kujon.google_drive.ui.activities.file_details.dagger.FileDetailsActivityComponent;
import mobi.kujon.google_drive.ui.activities.file_details.dagger.FileDetailsModule;
import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityModule;

public class FileDetailsInjector extends AbstractInjector<FileDetailsActivity> {

    FileDetailsActivityComponent fileDetailsActivityComponent;

    @Override
    public void inject(FileDetailsActivity injectTo) {
        DaggerRuntimeFileDetailsActivityComponent.builder()
                .filesActivityModule(new FilesActivityModule(injectTo.getCoursId(), injectTo.getTermId()))
                .fileDetailsModule(new FileDetailsModule(injectTo, injectTo.getCoursId(), injectTo.getTermId()))
                .filesComponent(getFilesComponent(injectTo))
                .build().inject(injectTo);

    }

}
