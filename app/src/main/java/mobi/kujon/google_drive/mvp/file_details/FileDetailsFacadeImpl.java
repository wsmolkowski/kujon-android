package mobi.kujon.google_drive.mvp.file_details;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mobi.kujon.google_drive.model.json.ShareFileTarget;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFile;
import rx.Observable;

public class FileDetailsFacadeImpl implements FileDetailsFacade {

    private ChooseStudentsMVP.Model chooseStudentModel;
    private FileListMVP.Model fileListModel;
    private ShareFile shareFile;
    private String courseId;
    private String termId;

    @Inject
    public FileDetailsFacadeImpl(ChooseStudentsMVP.Model chooseStudentModel, FileListMVP.Model fileListModel, ShareFile shareFile, String courseId, String termId) {
        this.chooseStudentModel = chooseStudentModel;
        this.fileListModel = fileListModel;
        this.courseId = courseId;
        this.termId = termId;
        this.shareFile = shareFile;
    }

    @Override
    public Observable<List<DisableableStudentShareDTO>> loadStudentShares(String fileId, boolean refresh) {
        return Observable.combineLatest(getSharedWith(fileId, refresh),
                chooseStudentModel.provideListOfStudents(courseId, termId, refresh),
                (sharedWith, studentShareDtos) -> setDtosState(studentShareDtos, sharedWith));
    }

    private Observable<List<String>> getSharedWith(String fileId, boolean refresh) {
        return loadFileProperties(fileId, refresh).map(FileDTO::getShares);
    }

    private FileDTO getFileDTO(List<FileDTO> fileDTOs, String fileId) {
        for (FileDTO dto : fileDTOs) {
            if (dto.getFileId().equals(fileId)) {
                return dto;
            }
        }
        throw new RuntimeException("File Id passed in does not match any of downloaded files");
    }

    private List<DisableableStudentShareDTO> setDtosState(List<StudentShareDto> studentShares, List<String> sharedWith) {
        List<DisableableStudentShareDTO> resultDtos = new ArrayList<>(studentShares.size());
        boolean isEnabled = studentShares.size() != sharedWith.size();
        for (StudentShareDto studentShare : studentShares) {
            studentShare.setChosen(checkIfChosen(studentShare.getStudentId(), sharedWith));
            resultDtos.add(new DisableableStudentShareDTO(studentShare, isEnabled));
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
    public Observable<FileDTO> loadFileProperties(String fileId, boolean refresh) {
        return fileListModel.getFilesDto(refresh, FilesOwnerType.ALL)
                .map(fileDTOs -> getFileDTO(fileDTOs, fileId));
    }

    @Override
    public Observable<SharedFile> shareFile(FileShareDto fileShareDto) {
        ShareFileTarget target = prepareShareFileTarget(fileShareDto);
        return shareFile.shareFile(target);
    }

    private ShareFileTarget prepareShareFileTarget(FileShareDto fileShareDto) {
        ShareFileTarget target = new ShareFileTarget();
        target.shareWithTargetType = fileShareDto.getShareType();
        target.fileId = fileShareDto.getFileId();
        target.shareWithTarget = new ArrayList<>(fileShareDto.getStudentsListToShare().size());
        for(String studentId : fileShareDto.getStudentsListToShare()) {
            target.shareWithTarget.add(Integer.valueOf(studentId));
        }
        return target;
    }
}
