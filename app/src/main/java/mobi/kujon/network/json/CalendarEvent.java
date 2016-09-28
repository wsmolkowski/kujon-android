package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobi.kujon.utils.EventUtils;

public class CalendarEvent {

    public static final SimpleDateFormat HOURS = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

//    @SerializedName("lecturers") @Expose public List<LecturerLong> lecturers;

    public String getHoursFromTo(String separator) {
        Date startDate = getStartDate();
        Date endDate = getEndDate();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        return String.format("%s%s%s", HOURS.format(startDate), separator, HOURS.format(endDate));
    }

    public String getHoursFromTo() {
        return getHoursFromTo(" - ");
    }

    public Date getEndDate() {
        return EventUtils.getDate(endTime);
    }

    public Date getStartDate() {
        return EventUtils.getDate(startTime);
    }

    public String getStartDateFormatted() {
        Date startDate = getStartDate();
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        return HOURS.format(startDate);
    }

    @Override public String toString() {
        return "CalendarEvent{" +
                "courseName='" + courseName + '\'' +
                ", name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", groupNumber=" + groupNumber +
                ", roomNumber='" + roomNumber + '\'' +
                ", endTime='" + endTime + '\'' +
                ", courseId='" + courseId + '\'' +
                ", type='" + type + '\'' +
                ", buildingName='" + buildingName + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarEvent that = (CalendarEvent) o;

        if (!name.equals(that.name)) return false;
        if (!startTime.equals(that.startTime)) return false;
        if (!endTime.equals(that.endTime)) return false;
        return courseId.equals(that.courseId);

    }

    @Override public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + courseId.hashCode();
        return result;
    }
}