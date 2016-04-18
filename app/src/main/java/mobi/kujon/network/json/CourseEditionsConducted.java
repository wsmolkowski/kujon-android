package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseEditionsConducted {

    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("term_id")
    @Expose
    public String termId;
    @SerializedName("course_name")
    @Expose
    public String courseName;

}