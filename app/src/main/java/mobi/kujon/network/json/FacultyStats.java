package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacultyStats {

    @SerializedName("course_count") @Expose public Integer courseCount;
    @SerializedName("programme_count") @Expose public Integer programmeCount;
    @SerializedName("staff_count") @Expose public Integer staffCount;
}
