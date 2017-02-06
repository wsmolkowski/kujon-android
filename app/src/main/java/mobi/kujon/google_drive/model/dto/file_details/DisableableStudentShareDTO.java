package mobi.kujon.google_drive.model.dto.file_details;


import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.network.json.Participant;

public class DisableableStudentShareDTO extends StudentShareDto{
    private boolean enabled;

    public DisableableStudentShareDTO(String name, String id, boolean isChosen, boolean enabled) {
        super(name, id, isChosen);
        this.enabled = enabled;
    }

    public DisableableStudentShareDTO(Participant participant,boolean isChosen, boolean enabled) {
        super(participant, isChosen);
        this.enabled = enabled;
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
        if (!super.equals(o)) return false;

        DisableableStudentShareDTO that = (DisableableStudentShareDTO) o;

        return enabled == that.enabled;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (enabled ? 1 : 0);
        return result;
    }
}
