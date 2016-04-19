package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarEvent {

    @SerializedName("course_name")
    @Expose
    public String courseName;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("group_number")
    @Expose
    public Integer groupNumber;
    @SerializedName("room_number")
    @Expose
    public String roomNumber;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("building_name")
    @Expose
    public String buildingName;

}