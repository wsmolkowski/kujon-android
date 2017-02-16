package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Course {

    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("term_id")
    @Expose
    public String termId;
    @SerializedName("course_name")
    @Expose
    public String courseName;
    @SerializedName("groups")
    @Expose
    public List<Group> groups = new ArrayList<>();
    public int fileCount = 25;
}