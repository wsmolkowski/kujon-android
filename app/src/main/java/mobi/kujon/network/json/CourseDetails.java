
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CourseDetails {

    @SerializedName("lecturers")
    @Expose
    public List<Lecturer> lecturers = new ArrayList<Lecturer>();
    @SerializedName("is_currently_conducted")
    @Expose
    public String isCurrentlyConducted;
    @SerializedName("bibliography")
    @Expose
    public String bibliography;
    @SerializedName("course_name")
    @Expose
    public String name;
    @SerializedName("fac_id")
    @Expose
    public FacId facId;
    @SerializedName("participants")
    @Expose
    public List<Participant> participants = new ArrayList<Participant>();
    @SerializedName("coordinators")
    @Expose
    public List<Coordinator> coordinators = new ArrayList<Coordinator>();
    @SerializedName("homepage_url")
    @Expose
    public String homepageUrl;
    @SerializedName("lang_id")
    @Expose
    public String langId;
    @SerializedName("course_units_ids")
    @Expose
    public List<String> courseUnitsIds = new ArrayList<String>();
    @SerializedName("term")
    @Expose
    public List<Term2> term;
    @SerializedName("groups")
    @Expose
    public List<Group> groups = new ArrayList<Group>();
    @SerializedName("learning_outcomes")
    @Expose
    public String learningOutcomes;
    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("assessment_criteria")
    @Expose
    public String assessmentCriteria;
    @SerializedName("description")
    @Expose
    public String description;

}
