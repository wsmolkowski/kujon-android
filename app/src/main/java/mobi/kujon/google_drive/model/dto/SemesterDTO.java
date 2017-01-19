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
}
