package mobi.kujon.google_drive.model.dto.file_share;

import mobi.kujon.google_drive.model.dto.StudentShareDto;

/**
 *
 */

public class AskForStudentDto {

    private String courseId,termId;
    private StudentShareDto studentShareDto;

    public AskForStudentDto(String courseId, String termId, StudentShareDto studentShareDto) {
        this.courseId = courseId;
        this.termId = termId;
        this.studentShareDto = studentShareDto;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public StudentShareDto getStudentShareDto() {
        return studentShareDto;
    }

    public void setStudentShareDto(StudentShareDto studentShareDto) {
        this.studentShareDto = studentShareDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AskForStudentDto)) return false;

        AskForStudentDto that = (AskForStudentDto) o;

        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null)
            return false;
        if (termId != null ? !termId.equals(that.termId) : that.termId != null) return false;
        return studentShareDto != null ? studentShareDto.equals(that.studentShareDto) : that.studentShareDto == null;

    }

    @Override
    public int hashCode() {
        int result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (termId != null ? termId.hashCode() : 0);
        result = 31 * result + (studentShareDto != null ? studentShareDto.hashCode() : 0);
        return result;
    }
}
