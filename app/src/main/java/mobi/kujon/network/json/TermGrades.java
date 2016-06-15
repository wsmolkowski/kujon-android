package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TermGrades {

    @SerializedName("term_id") @Expose public String termId;
    @SerializedName("courses") @Expose public List<CourseGrades> courses;
}
