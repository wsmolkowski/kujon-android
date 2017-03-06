package mobi.kujon.network.json;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import mobi.kujon.R;
import mobi.kujon.utils.statuses.StudentProgrammeStatus;

public class StudentProgramme {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("programme")
    @Expose
    public Programme programme;


    @Expose
    public @StudentProgrammeStatus  String status;



    public @StringRes int getGraduateText(){
        switch (status){
            case StudentProgrammeStatus.CANCELLED:
                return R.string.cancelled_study;
            case StudentProgrammeStatus.ACTIVE:
                return R.string.active_study;
            case StudentProgrammeStatus.GRADUATE_END_OF_STUDY:
                return R.string.graduated_end_of_study;
            case StudentProgrammeStatus.GRADUATE_BEFORE_DIPLOMA:
                return R.string.graduated_before;
            case StudentProgrammeStatus.GRADUATE_DIPLOMA:
                return R.string.graduated_diploma;
            default:
                return R.string.active_study;
        }
    }

    public @DrawableRes int getImage(){
        switch (status){
            case StudentProgrammeStatus.CANCELLED:
                return R.drawable.ic_canceled_study;
            case StudentProgrammeStatus.ACTIVE:
                return R.drawable.ic_active_study;
            case StudentProgrammeStatus.GRADUATE_END_OF_STUDY:
                return R.drawable.ic_graduated_end_of_study;
            case StudentProgrammeStatus.GRADUATE_BEFORE_DIPLOMA:
                return R.drawable.ic_graduated_before_diploma;
            case StudentProgrammeStatus.GRADUATE_DIPLOMA:
                return R.drawable.ic_graduated_diploma;
            default:
                return R.drawable.ic_active_study;
        }
    }


}