package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LecturerLong {

    @SerializedName("course_editions_conducted")
    @Expose
    public List<CourseEditionsConducted> courseEditionsConducted = new ArrayList<CourseEditionsConducted>();
    @SerializedName("interests")
    @Expose
    public String interests;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("employment_positions")
    @Expose
    public List<Object> employmentPositions = new ArrayList<Object>();
    @SerializedName("room")
    @Expose
    public Room room;
    @SerializedName("office_hours")
    @Expose
    public String officeHours;
    @SerializedName("staff_status")
    @Expose
    public String staffStatus;
    @SerializedName("homepage_url")
    @Expose
    public String homepageUrl;
    @SerializedName("titles")
    @Expose
    public Titles titles;
    @SerializedName("email_url")
    @Expose
    public String emailUrl;
    @SerializedName("has_photo")
    @Expose
    public String hasPhoto;
    @SerializedName("id")
    @Expose
    public String id;

}