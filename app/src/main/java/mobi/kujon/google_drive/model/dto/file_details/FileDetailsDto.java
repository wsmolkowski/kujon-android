package mobi.kujon.google_drive.model.dto.file_details;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */
public class FileDetailsDto {

    private FileDTO fileDTO;
    private List<StudentShareDto> studentShareDto;

    public FileDetailsDto(FileDTO fileDTO, List<StudentShareDto> studentShareDtoo) {
        this.fileDTO = fileDTO;
        this.studentShareDto = studentShareDtoo;
        List<String> fileSharedWith = new ArrayList<>(fileDTO.getShares());
        setUpChoosen(fileSharedWith);
    }

    public FileDetailsDto(FileDTO fileDTO, FileDetailsHelp fileDetailsHelp) {
        this(fileDTO, fileDetailsHelp.getList());
        this.fileDTO.setCourseName(fileDetailsHelp.courseName);
    }

    public void setUpChoosen(List<String> fileSharedWith) {
        for (StudentShareDto studentShareDto : this.getStudentShareDto()) {
            List<String> strings = new ArrayList<>(fileSharedWith);
            studentShareDto.setChosen(false);
            for (String studentId : strings) {
                if (studentShareDto.getStudentId().equals(studentId)) {
                    studentShareDto.setChosen(true);
                    fileSharedWith.remove(studentId);
                    break;
                }
            }
        }
    }


    public FileDTO getFileDTO() {
        return fileDTO;
    }

    public void setFileDTO(FileDTO fileDTO) {
        this.fileDTO = fileDTO;
    }

    public List<StudentShareDto> getStudentShareDto() {
        return studentShareDto;
    }

    public void setStudentShareDto(List<StudentShareDto> studentShareDto) {
        this.studentShareDto = studentShareDto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDetailsDto)) return false;

        FileDetailsDto that = (FileDetailsDto) o;

        if (fileDTO != null ? !fileDTO.equals(that.fileDTO) : that.fileDTO != null) return false;
        return studentShareDto != null ? studentShareDto.equals(that.studentShareDto) : that.studentShareDto == null;

    }

    @Override
    public int hashCode() {
        int result = fileDTO != null ? fileDTO.hashCode() : 0;
        result = 31 * result + (studentShareDto != null ? studentShareDto.hashCode() : 0);
        return result;
    }
}
