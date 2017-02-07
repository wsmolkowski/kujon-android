package mobi.kujon.google_drive.mvp.file_details;


import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import rx.Observable;


public class FileDetailsFacade implements FileDetailsMVP.FileDetailsFacade {

    private ChooseStudentsMVP.Model chooseStudentModel;
    private GetFilesApi getFilesApi;
    private String courseId;
    private String termId;


    public FileDetailsFacade(ChooseStudentsMVP.Model chooseStudentModel,
                             GetFilesApi getFilesApi,
                             String courseId, String termId) {
        this.chooseStudentModel = chooseStudentModel;
        this.getFilesApi = getFilesApi;
        this.courseId = courseId;
        this.termId = termId;
    }

    @Override
    public Observable<List<DisableableStudentShareDTO>> loadStudentShares(String fileId, boolean refresh) {
        return Observable.combineLatest(loadFileDetails(fileId, refresh),
                chooseStudentModel.provideListOfStudents(courseId, termId, refresh),
                (fileDTO, studentShareDtos) -> setDtosState(studentShareDtos, fileDTO));
    }

    private List<DisableableStudentShareDTO> setDtosState(List<StudentShareDto> studentShares, FileDTO fileDTO) {
        List<DisableableStudentShareDTO> resultDtos = new ArrayList<>(studentShares.size());
        boolean isEnabled = !fileDTO.getShareType().equals(ShareFileTargetType.ALL);
        if (!fileDTO.isMy()) isEnabled = false;
        for (StudentShareDto studentShare : studentShares) {
            studentShare.setChosen(checkIfChosen(studentShare.getStudentId(), fileDTO.getShares()));
            resultDtos.add(new DisableableStudentShareDTO(studentShare.getStudentName(),
                    studentShare.getStudentId(),
                    studentShare.isChosen(),
                    isEnabled));
        }
        return resultDtos;
    }

    private boolean checkIfChosen(String studentId, List<String> sharedWith) {
        for (String sharedWithStudent : sharedWith) {
            if (studentId.equals(sharedWithStudent)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Observable<FileDTO> loadFileDetails(String fileId, boolean refresh) {
        return getFilesApi.getFiles(refresh,courseId,termId)
                .flatMap(Observable::from)
                .filter(kujonFile -> kujonFile.fileId.equals(fileId))
                .take(1)
                .map(FileDtoFactory::createFileDto);
    }

}
