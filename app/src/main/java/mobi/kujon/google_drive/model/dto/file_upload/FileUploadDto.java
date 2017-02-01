package mobi.kujon.google_drive.model.dto.file_upload;

import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;

/**
 *
 */

public class FileUploadDto {
    private String courseId;
    private String termId;
    private @ShareFileTargetType String shareFileTargetType;
    private List<Integer> listOfShares;


    public FileUploadDto(String courseId, String termId, String shareFileTargetType, List<Integer> listOfShares) {
        this.courseId = courseId;
        this.termId = termId;
        this.shareFileTargetType = shareFileTargetType;
        this.listOfShares = listOfShares;
    }


    public String getCourseId() {
        return courseId;
    }

    public String getTermId() {
        return termId;
    }

    public  @ShareFileTargetType String getShareFileTargetType() {
        return shareFileTargetType;
    }

    public List<Integer> getListOfShares() {
        return listOfShares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileUploadDto)) return false;

        FileUploadDto that = (FileUploadDto) o;

        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null)
            return false;
        if (termId != null ? !termId.equals(that.termId) : that.termId != null) return false;
        if (shareFileTargetType != null ? !shareFileTargetType.equals(that.shareFileTargetType) : that.shareFileTargetType != null)
            return false;
        return listOfShares != null ? listOfShares.equals(that.listOfShares) : that.listOfShares == null;

    }

    @Override
    public int hashCode() {
        int result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (termId != null ? termId.hashCode() : 0);
        result = 31 * result + (shareFileTargetType != null ? shareFileTargetType.hashCode() : 0);
        result = 31 * result + (listOfShares != null ? listOfShares.hashCode() : 0);
        return result;
    }
}
