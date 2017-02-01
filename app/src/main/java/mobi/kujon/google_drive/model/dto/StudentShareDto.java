package mobi.kujon.google_drive.model.dto;

import mobi.kujon.network.json.Participant;

/**
 *
 */

public class StudentShareDto {

    private String studentName,studentId;
    private boolean isChosen;

    public StudentShareDto(String name, String id, boolean isChosen) {
        this.studentId = id;
        this.studentName = name;
        this.isChosen = isChosen;
    }

    public StudentShareDto(Participant participant, boolean isChosen) {
        this.studentName = participant.getName();
        this.studentId = participant.userId;
        this.isChosen = isChosen;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean isChosen) {
        this.isChosen = isChosen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentShareDto)) return false;

        StudentShareDto that = (StudentShareDto) o;

        if (isChosen != that.isChosen) return false;
        if (studentName != null ? !studentName.equals(that.studentName) : that.studentName != null)
            return false;
        return studentId != null ? studentId.equals(that.studentId) : that.studentId == null;

    }

    @Override
    public int hashCode() {
        int result = studentName != null ? studentName.hashCode() : 0;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (isChosen ? 1 : 0);
        return result;
    }
}
