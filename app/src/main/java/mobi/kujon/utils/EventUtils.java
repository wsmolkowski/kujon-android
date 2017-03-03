package mobi.kujon.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import mobi.kujon.R;
import mobi.kujon.activities.CourseDetailsActivity;
import mobi.kujon.network.json.CalendarEvent;

public class EventUtils {

    public static KujonWeekViewEvent from(CalendarEvent calendarEvent) {
        Date startDate = getDate(calendarEvent.startTime);
        Date endDate = getDate(calendarEvent.endTime);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        String desc = String.format("%s\n%s", calendarEvent.getHoursFromTo(), calendarEvent.name);
        return new KujonWeekViewEvent(startDate.getTime(), desc, start, end, calendarEvent);
    }

    public static Date getDate(String date) {
        try {
            return CalendarEvent.SIMPLE_DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog getEventDialog(Activity activity, CalendarEvent calendarEvent) {
        DateTime now = DateTime.now();
        String term = "" + (now.getMonthOfYear() > 9 ? now.getYear() : now.getYear() - 1);
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
        dlgAlert.setMessage(String.format("%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                calendarEvent.getHoursFromTo(),
                activity.getString(R.string.room), calendarEvent.roomNumber,
                activity.getString(R.string.building),calendarEvent.buildingName,
                activity.getString(R.string.group),calendarEvent.groupNumber,
                activity.getString(R.string.type),calendarEvent.type));
        dlgAlert.setTitle(calendarEvent.name);
        dlgAlert.setPositiveButton(R.string.ok, null);
        dlgAlert.setNeutralButton(R.string.view_course, (dialog, which) -> {
            CourseDetailsActivity.showCourseDetails(activity, calendarEvent.courseId,calendarEvent.termId);
        });
        dlgAlert.setCancelable(true);
        return dlgAlert.create();
    }
}
