package mobi.kujon.google_drive.mvp.file_details;


import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import rx.Observable;


public class FileDetailsFacade implements FileDetailsMVP.FileDetailsFacade {

    private ChooseStudentsMVP.Model chooseStudentModel;
    private FileListMVP.Model fileListModel;
    private String courseId;
    private String termId;


    public FileDetailsFacade(ChooseStudentsMVP.Model chooseStudentModel, FileListMVP.Model fileListModel,
                             String courseId, String termId) {
        this.chooseStudentModel = chooseStudentModel;
        this.fileListModel = fileListModel;
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
        return fileListModel.getFilesDto(refresh, FilesOwnerType.ALL)
                .map(fileDTOs -> getFileDTOById(fileDTOs, fileId));
    }


    private FileDTO getFileDTOById(List<FileDTO> fileDTOs, String fileId) {
        for (FileDTO dto : fileDTOs) {
            if (dto.getFileId().equals(fileId)) {
                return dto;
            }
        }
        return null;
    }
}
