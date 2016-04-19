package mobi.kujon.network.converters;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

import mobi.kujon.network.json.CalendarEvent;

public class KujonWeekViewEvent extends WeekViewEvent {

    public KujonWeekViewEvent(long id, String name, Calendar startTime, Calendar endTime, CalendarEvent calendarEvent) {
        super(id, name, startTime, endTime);
        this.calendarEvent = calendarEvent;
    }

    private CalendarEvent calendarEvent;

    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }

}
