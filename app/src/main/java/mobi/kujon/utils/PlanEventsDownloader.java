package mobi.kujon.utils;

import com.github.underscore.$;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import bolts.Task;
import bolts.TaskCompletionSource;
import mobi.kujon.KujonApplication;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanEventsDownloader {

    public static final SimpleDateFormat REST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @Inject KujonBackendApi kujonBackendApi;

    private boolean refresh = false;

    public PlanEventsDownloader() {
        KujonApplication.getComponent().inject(this);
    }

    public Task<List<CalendarEvent>> downloadEventsFor(int year, int month) {

        List<Task<List<CalendarEvent>>> tasks = new ArrayList<>();

        DateTime day = new DateTime(year, month, 1, 12, 0, 0);
        while (day.getMonthOfYear() == month) {
            String restSuffix = REST_DATE_FORMAT.format(day.toDate());
            System.out.println(restSuffix);
            day = day.plusDays(7);

            final TaskCompletionSource<List<CalendarEvent>> tcs = new TaskCompletionSource<>();

            Call<KujonResponse<List<CalendarEvent>>> call = refresh ? kujonBackendApi.planRefresh(restSuffix) : kujonBackendApi.plan(restSuffix);
            call.enqueue(new Callback<KujonResponse<List<CalendarEvent>>>() {
                @Override public void onResponse(Call<KujonResponse<List<CalendarEvent>>> call, Response<KujonResponse<List<CalendarEvent>>> response) {
                    if (ErrorHandlerUtil.handleResponse(response)) {
                        List<CalendarEvent> data = response.body().data;
                        tcs.setResult(data);
                    } else {
                        tcs.setError(new Exception(response.message() + response.code()));
                    }
                }

                @Override public void onFailure(Call<KujonResponse<List<CalendarEvent>>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                    tcs.setError(new Exception(t));
                }
            });
            tasks.add(tcs.getTask());
        }

        return Task.whenAllResult(tasks).onSuccess(task -> {
            List<CalendarEvent> events = $.flatten(task.getResult());
            List<CalendarEvent> onlyThisMonthEvents = $.filter(events, it -> {
                DateTime time = new DateTime(it.getStartDate().getTime());
                return time.getMonthOfYear() == month;
            });
            return onlyThisMonthEvents;
        });
    }

    public Task<SortedMap<CalendarSection, List<CalendarEvent>>> prepareMonth(int year, int month) {
        Task<List<CalendarEvent>> monthEvents = downloadEventsFor(year, month);
        return monthEvents.onSuccess(task -> {
            SortedMap<CalendarSection, List<CalendarEvent>> result = new TreeMap<>();
            result.put(new CalendarSection(new LocalDate(year, month, 1), true), Collections.emptyList());
            SortedMap<LocalDate, List<CalendarEvent>> grouped = groupEventsByDay(task.getResult());

            for (LocalDate localDate : grouped.keySet()) {
                result.put(new CalendarSection(localDate), grouped.get(localDate));
            }
            return result;
        });
    }

    public SortedMap<LocalDate, List<CalendarEvent>> groupEventsByDay(List<CalendarEvent> events) {
        Map<LocalDate, List<CalendarEvent>> map = $.groupBy(events, it -> {
            DateTime dateTime = new DateTime(it.getStartDate().getTime());
            return dateTime.toLocalDate();
        });

        SortedMap<LocalDate, List<CalendarEvent>> result = new TreeMap<>();
        result.putAll(map);

        return result;
    }

    public static class CalendarSection implements Comparable<CalendarSection> {
        public final LocalDate localDate;
        public final boolean monthName;

        public CalendarSection(LocalDate localDate) {
            this(localDate, false);
        }

        public CalendarSection(LocalDate localDate, boolean monthName) {
            this.localDate = localDate;
            this.monthName = monthName;
        }

        @Override public int compareTo(CalendarSection another) {
            return localDate.compareTo(another.localDate);
        }

        @Override public String toString() {
            return localDate.toString();
        }
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
