package mobi.kujon.google_drive.model.dto;

/**
 *
 */

public class SemesterDTO {

    private String semesterCode;
    private String semesterId;

    public SemesterDTO(String semesterCode, String semesterId) {
        this.semesterCode = semesterCode;
        this.semesterId = semesterId;
    }


    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemesterDTO)) return false;

        SemesterDTO that = (SemesterDTO) o;

        if (semesterCode != null ? !semesterCode.equals(that.semesterCode) : that.semesterCode != null)
            return false;
        return semesterId != null ? semesterId.equals(that.semesterId) : that.semesterId == null;

    }

    @Override
    public int hashCode() {
        int result = semesterCode != null ? semesterCode.hashCode() : 0;
        result = 31 * result + (semesterId != null ? semesterId.hashCode() : 0);
        return result;
    }
}
