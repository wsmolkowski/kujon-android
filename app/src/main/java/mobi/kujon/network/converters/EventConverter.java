package mobi.kujon.network.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mobi.kujon.network.json.CalendarEvent;

public class EventConverter {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static KujonWeekViewEvent from(CalendarEvent calendarEvent) {
        Date startDate = getDate(calendarEvent.startTime);
        Date endDate = getDate(calendarEvent.endTime);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        String desc = String.format("%s\n%s", calendarEvent.getTime(), calendarEvent.name);
        KujonWeekViewEvent event = new KujonWeekViewEvent(startDate.getTime(), desc, start, end, calendarEvent);
        return event;
    }

    public static Date getDate(String date) {
        try {
            return SIMPLE_DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
