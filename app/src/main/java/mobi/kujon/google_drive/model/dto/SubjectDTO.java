package mobi.kujon.google_drive.model.dto;

/**
 *
 */

public class SubjectDTO {

    private String subjectName,subjectId;

    public SubjectDTO(String subjectName, String subjectId) {
        this.subjectName = subjectName;
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectDTO)) return false;

        SubjectDTO that = (SubjectDTO) o;

        if (subjectName != null ? !subjectName.equals(that.subjectName) : that.subjectName != null)
            return false;
        return subjectId != null ? subjectId.equals(that.subjectId) : that.subjectId == null;

    }

    @Override
    public int hashCode() {
        int result = subjectName != null ? subjectName.hashCode() : 0;
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        return result;
    }
}
