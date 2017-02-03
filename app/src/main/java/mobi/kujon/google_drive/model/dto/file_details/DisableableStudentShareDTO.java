package mobi.kujon.google_drive.model.dto.file_details;


import mobi.kujon.google_drive.model.dto.StudentShareDto;

public class DisableableStudentShareDTO {
    private StudentShareDto studentShareDto;
    private boolean enabled;

    public DisableableStudentShareDTO(StudentShareDto studentShareDto) {
        this.studentShareDto = studentShareDto;
    }

    public DisableableStudentShareDTO(StudentShareDto studentShareDto, boolean enabled) {
        this.studentShareDto = studentShareDto;
        this.enabled = enabled;
    }

    public StudentShareDto getStudentShareDto() {
        return studentShareDto;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisableableStudentShareDTO that = (DisableableStudentShareDTO) o;

        if (enabled != that.enabled) return false;
        return studentShareDto != null ? studentShareDto.equals(that.studentShareDto) : that.studentShareDto == null;

    }

    @Override
    public int hashCode() {
        int result = studentShareDto != null ? studentShareDto.hashCode() : 0;
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }
}
