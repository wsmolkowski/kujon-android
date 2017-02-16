package mobi.kujon.google_drive.model.dto.file_details;

import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;

/**
 *
 */
public class FileDetailsHelp {
    List<StudentShareDto> list;
    String courseName;

    public FileDetailsHelp(List<StudentShareDto> list, String courseName) {
        this.list = list;
        this.courseName = courseName;
    }

    public List<StudentShareDto> getList() {
        return list;
    }

    public String getCourseName() {
        return courseName;
    }
}
