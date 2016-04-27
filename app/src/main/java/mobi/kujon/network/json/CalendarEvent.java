package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobi.kujon.network.converters.EventConverter;

public class CalendarEvent {

    public static final SimpleDateFormat HOURS = new SimpleDateFormat("HH:mm");

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

    public String getTime() {
        Date startDate = EventConverter.getDate(startTime);
        Date endDate = EventConverter.getDate(endTime);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        return String.format("%s - %s", HOURS.format(startDate), HOURS.format(endDate));
    }
}