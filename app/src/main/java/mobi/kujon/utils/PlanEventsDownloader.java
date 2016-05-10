package mobi.kujon.utils;

import com.github.underscore.$;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import bolts.Task;
import bolts.TaskCompletionSource;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanEventsDownloader {

    public static final SimpleDateFormat REST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private KujonBackendApi kujonBackendApi;

    public PlanEventsDownloader() {
        kujonBackendApi = KujonBackendService.getInstance().getKujonBackendApi();
    }

    public Task<List<CalendarEvent>> downloadEventsFor(int year, int month) {

        List<Task<List<CalendarEvent>>> tasks = new ArrayList<>();

        DateTime day = new DateTime(year, month, 1, 12, 0, 0);
        while (day.getMonthOfYear() == month) {
            String restSuffix = REST_DATE_FORMAT.format(day.toDate());
            System.out.println(restSuffix);
            day = day.plusDays(7);

            final TaskCompletionSource<List<CalendarEvent>> tcs = new TaskCompletionSource<>();

            kujonBackendApi.plan(restSuffix).enqueue(new Callback<KujonResponse<List<CalendarEvent>>>() {
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

        return Task.whenAllResult(tasks).onSuccess(task -> $.flatten(task.getResult()));
    }

    public Task<List<CalendarEvent>> downloadEventsForThreeMonths() {
        DateTime now = DateTime.now();
        Task<List<CalendarEvent>> events0 = downloadEventsFor(now.getYear(), now.getMonthOfYear());
        DateTime nextMonth = now.plusMonths(1);
        Task<List<CalendarEvent>> events1 = downloadEventsFor(nextMonth.getYear(), nextMonth.getMonthOfYear());
        DateTime another = nextMonth.plusMonths(1);
        Task<List<CalendarEvent>> events2 = downloadEventsFor(another.getYear(), another.getMonthOfYear());

        Task<List<CalendarEvent>> listTask = Task.whenAllResult(Arrays.asList(events0, events1, events2))
                .onSuccess(task -> $.flatten(task.getResult()));
        return listTask.onSuccess(task -> $.uniq(task.getResult()));
    }


    public SortedMap<LocalDate, List<CalendarEvent>> groupEvents(List<CalendarEvent> events) {
        Map<LocalDate, List<CalendarEvent>> map = $.groupBy(events, it -> {
            DateTime dateTime = new DateTime(it.getStartDate().getTime());
            return dateTime.toLocalDate();
        });

        SortedMap<LocalDate, List<CalendarEvent>> result = new TreeMap<>();
        result.putAll(map);

        return result;
    }
}
