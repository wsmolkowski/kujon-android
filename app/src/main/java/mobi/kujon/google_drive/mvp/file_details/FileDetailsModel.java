package mobi.kujon.google_drive.mvp.file_details;


import android.support.annotation.NonNull;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.model.dto.file_details.FileDetailsDto;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import rx.Observable;


public class FileDetailsModel implements FileDetailsMVP.FileDetailsModel {

    private ChooseStudentsMVP.Model chooseStudentModel;
    private GetFilesApi getFilesApi;
    private String courseId;
    private String termId;


    public FileDetailsModel(ChooseStudentsMVP.Model chooseStudentModel,
                            GetFilesApi getFilesApi,
                            String courseId, String termId) {
        this.chooseStudentModel = chooseStudentModel;
        this.getFilesApi = getFilesApi;
        this.courseId = courseId;
        this.termId = termId;
    }


    @Override
    public Observable<FileDetailsDto> loadFileDetails(String fileId, boolean refresh) {
        return Observable.combineLatest(getFileDto(fileId, refresh), chooseStudentModel.profileFileDetailsInfo(courseId, termId, refresh),
                FileDetailsDto::new);
    }

    @NonNull
    private Observable<FileDTO> getFileDto(String fileId, boolean refresh) {
        return getFilesApi.getFiles(refresh, courseId, termId)
                .flatMap(Observable::from)
                .filter(kujonFile -> kujonFile.fileId.equals(fileId))
                .take(1)
                .map(FileDtoFactory::createFileDto);
    }

}
