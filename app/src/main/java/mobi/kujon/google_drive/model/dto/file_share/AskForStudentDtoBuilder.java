package mobi.kujon.google_drive.model.dto.file_share;

import mobi.kujon.google_drive.model.dto.StudentShareDto;

public class AskForStudentDtoBuilder {
    private String courseId;
    private String termId;
    private StudentShareDto studentShareDto;

    public AskForStudentDtoBuilder setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public AskForStudentDtoBuilder setTermId(String termId) {
        this.termId = termId;
        return this;
    }

    public AskForStudentDtoBuilder setStudentShareDto(StudentShareDto studentShareDto) {
        this.studentShareDto = studentShareDto;
        return this;
    }

    public AskForStudentDto createAskForStudentDto() {
        return new AskForStudentDto(courseId, termId, studentShareDto);
    }
}