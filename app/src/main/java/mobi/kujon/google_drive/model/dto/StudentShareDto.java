package mobi.kujon.google_drive.model.dto;

import mobi.kujon.network.json.Participant;

/**
 *
 */

public class StudentShareDto {

    private String studentName,studentId;
    private boolean isChoosen;

    public StudentShareDto(String name, String id, boolean isChoosen) {
        this.studentId = id;
        this.studentName = name;
        this.isChoosen = isChoosen;
    }

    public StudentShareDto(Participant participant, boolean isChoosen) {
        this.studentName = participant.getName();
        this.studentId = participant.userId;
        this.isChoosen = isChoosen;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean isChoosen() {
        return isChoosen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentShareDto)) return false;

        StudentShareDto that = (StudentShareDto) o;

        if (isChoosen != that.isChoosen) return false;
        if (studentName != null ? !studentName.equals(that.studentName) : that.studentName != null)
            return false;
        return studentId != null ? studentId.equals(that.studentId) : that.studentId == null;

    }

    @Override
    public int hashCode() {
        int result = studentName != null ? studentName.hashCode() : 0;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (isChoosen ? 1 : 0);
        return result;
    }
}
