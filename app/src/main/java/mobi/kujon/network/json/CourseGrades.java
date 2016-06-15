package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseGrades {
    @SerializedName("course_id") @Expose public String courseId;
    @SerializedName("course_name") @Expose public String courseName;
    @SerializedName("term_id") @Expose public String termId;
    @SerializedName("grades") @Expose public List<Grade> grades;
}
