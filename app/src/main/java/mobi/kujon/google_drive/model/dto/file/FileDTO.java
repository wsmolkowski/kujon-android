package mobi.kujon.google_drive.model.dto.file;

import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.UploadedFile;
import mobi.kujon.google_drive.ui.fragments.files.recycler_classes.ShowShareIcon;

import static java.util.Locale.getDefault;

/**
 *
 */

public abstract class FileDTO {

    private
    @ShareFileTargetType
    String shareType;
    private int numberOfShares;
    private String fileName;
    private String fileSize;
    private String userName;
    private String fileId;
    private String dateCreated;
    private Date date;
    private List<String> shares;
    private String mimeType;
    private boolean isMy;
    private String courseId;
    private String termId;
    private String courseName;


    public FileDTO(KujonFile kujonFile) {
        this.fileId = kujonFile.fileId;
        this.shareType = kujonFile.shareType;
        this.numberOfShares = kujonFile.fileSharedWith.length;
        this.shares = Arrays.asList(kujonFile.fileSharedWith);
        this.fileName = kujonFile.fileName;
        this.fileSize = kujonFile.fileSize;
        this.userName = new StringBuilder(kujonFile.firstName).append(" ").append(kujonFile.lastName).toString();
        this.isMy = kujonFile.myFile;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", getDefault());
        dateCreated = simpleDateFormat.format(kujonFile.createdTime);
        this.mimeType = kujonFile.contentType;
        this.date = kujonFile.createdTime;
        this.courseId = kujonFile.courseId;
        this.termId = kujonFile.termId;
    }
    public FileDTO(UploadedFile uploadedFile, FileUploadDto fileUploadDto, DataForFileUpload dataForFileUpload) {
        this.fileId = uploadedFile.fileId;
        this.shareType = uploadedFile.shareType;
        this.numberOfShares = uploadedFile.sharedWith.size();
        this.shares = uploadedFile.sharedWith;
        this.fileName = uploadedFile.fileName;
        this.fileSize = String.valueOf(dataForFileUpload.getBytes().length/1024);
//        this.userName = new StringBuilder(kujonFile.firstName).append(" ").append(kujonFile.lastName).toString();
        this.isMy = true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", getDefault());
        dateCreated = simpleDateFormat.format(new Date());
        this.mimeType = dataForFileUpload.getMediaType().toString();
        this.date = new Date();
        this.termId = fileUploadDto.getTermId();
        this.courseId = fileUploadDto.getCourseId();
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getShareType() {
        return shareType;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public List<String> getShares() {
        return shares;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileId() {
        return fileId;
    }

    public Date getDate() {
        return date;
    }

    public String getDateCreated(Resources resources) {
            return dateCreated;
    }


    public String getCourseId() {
        return courseId;
    }

    public String getTermId() {
        return termId;
    }

    public String getAuthorString(Resources resources) {
        return new StringBuilder().append(resources.getString(R.string.owner)).append(" ").append(userName).toString();
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public boolean isMy() {
        return isMy;
    }

    public void setShareFile(ShowShareIcon showShareIcon) {
        switch (shareType) {
            case ShareFileTargetType.ALL:
                showShareIcon.showShareIcon(R.drawable.share_options_icon, R.string.everyone);
                break;
            case ShareFileTargetType.LIST:
                showShareIcon.showShareIcon(R.drawable.share_options_icon, String.valueOf(numberOfShares));
                break;
            case ShareFileTargetType.NONE:
                showShareIcon.hide();
                break;
        }
    }

    public String getMimeType() {
        return mimeType;
    }

    public abstract
    @DrawableRes
    int getImageIcon();

    public
    @StringRes
    int getDateType() {
        return R.string.created_time;
    }

    public abstract
    @StringRes
    int getContentType();


    public void setShareType(String shareType) {
        this.shareType = shareType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileDTO)) return false;

        FileDTO fileDTO = (FileDTO) o;

        if (numberOfShares != fileDTO.numberOfShares) return false;
        if (isMy != fileDTO.isMy) return false;
        if (shareType != null ? !shareType.equals(fileDTO.shareType) : fileDTO.shareType != null)
            return false;
        if (fileName != null ? !fileName.equals(fileDTO.fileName) : fileDTO.fileName != null)
            return false;
        if (fileSize != null ? !fileSize.equals(fileDTO.fileSize) : fileDTO.fileSize != null)
            return false;
        if (userName != null ? !userName.equals(fileDTO.userName) : fileDTO.userName != null)
            return false;
        if (fileId != null ? !fileId.equals(fileDTO.fileId) : fileDTO.fileId != null) return false;
        if (dateCreated != null ? !dateCreated.equals(fileDTO.dateCreated) : fileDTO.dateCreated != null)
            return false;
        if (date != null ? !date.equals(fileDTO.date) : fileDTO.date != null) return false;
        if (shares != null ? !shares.equals(fileDTO.shares) : fileDTO.shares != null) return false;
        if (mimeType != null ? !mimeType.equals(fileDTO.mimeType) : fileDTO.mimeType != null)
            return false;
        if (courseId != null ? !courseId.equals(fileDTO.courseId) : fileDTO.courseId != null)
            return false;
        if (termId != null ? !termId.equals(fileDTO.termId) : fileDTO.termId != null) return false;
        return courseName != null ? courseName.equals(fileDTO.courseName) : fileDTO.courseName == null;

    }

    @Override
    public int hashCode() {
        int result = shareType != null ? shareType.hashCode() : 0;
        result = 31 * result + numberOfShares;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (fileId != null ? fileId.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (shares != null ? shares.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (isMy ? 1 : 0);
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        result = 31 * result + (termId != null ? termId.hashCode() : 0);
        result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
        return result;
    }
}
