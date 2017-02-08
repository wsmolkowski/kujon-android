package mobi.kujon.google_drive.dagger.injectors;


import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;
import mobi.kujon.google_drive.ui.activities.file_details.dagger.DaggerRuntimeFileDetailsActivityComponent;
import mobi.kujon.google_drive.ui.activities.file_details.dagger.FileDetailsModule;

public class FileDetailsInjector extends AbstractInjector<FileDetailsActivity> {
    @Override
    public void inject(FileDetailsActivity injectTo) {
        DaggerRuntimeFileDetailsActivityComponent.builder()
                .fileDetailsModule(new FileDetailsModule(injectTo, injectTo.getCoursId(), injectTo.getTermId()))
                .filesComponent(getFilesComponent(injectTo))
                .build().inject(injectTo);
    }
}
