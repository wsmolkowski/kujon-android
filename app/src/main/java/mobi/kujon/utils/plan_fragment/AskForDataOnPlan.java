package mobi.kujon.utils.plan_fragment;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.github.underscore.$;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import hugo.weaving.DebugLog;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.EventUtils;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.PlanEventsDownloader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public abstract class AskForDataOnPlan {
    private WeakReference<ActivityChange> activityChange;
    protected KujonBackendApi backendApi;
    protected KujonUtils utils;

    public AskForDataOnPlan(ActivityChange activityChange, KujonUtils utils, KujonBackendApi backendApi) {
        this.activityChange = new WeakReference<>(activityChange);
        this.utils = utils;
        this.backendApi = backendApi;
    }

    private List<Call<KujonResponse<List<CalendarEvent>>>> callList = new ArrayList<>();
    private List<String> downloadedMonth = new ArrayList<>();
    private Map<String, List<WeekViewEvent>> eventsForDate = new HashMap<>();
    protected boolean refresh;
    private final AtomicLong counter = new AtomicLong();


    public void reload() {
        this.setRefresh(true);
        utils.invalidateEntry("ttusers");
        utils.invalidateEntry("ttlecturers");
        downloadedMonth.clear();
        eventsForDate.clear();
        stopAllCurrentCalls();
        activityChange.get().startLoading();
    }

    public List<WeekViewEvent> getEvents(int year, int month) throws NoDataDownloaded {

        String key = getKey(year, month);
        if (!eventsForDate.containsKey(key)) {
           dowloadDataIfNeed(year, month);
           throw new NoDataDownloaded();

        }
        return eventsForDate.get(key);
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void destroy() {
        stopAllCurrentCalls();
        activityChange = null;
        backendApi = null;
        utils = null;
    }


    private void dowloadDataIfNeed(int year, int month) {
        if (!downloadedMonth.contains(getKey(year, month))) {
            downloadEventsFor(year, month);
        }
    }


    private String getKey(int year, int month) {
        return "" + year + month;
    }


    @DebugLog
    private void downloadEventsFor(int year, int month) {
        String key = getKey(year, month);
        if(downloadedMonth.contains(key))
            return;
        Log.d("Date asked", "year = [" + year + "], month = [" + month + "]");
        downloadedMonth.add(key);
        activityChange.get().startLoading();
        DateTime day = new DateTime(year, month, 1, 12, 0, 0);
        String restSuffix = PlanEventsDownloader.REST_DATE_FORMAT.format(day.toDate());
        Call<KujonResponse<List<CalendarEvent>>> call = getKujonResponseCall(restSuffix);
        callList.add(call);
        counter.incrementAndGet();
        call.enqueue(new Callback<KujonResponse<List<CalendarEvent>>>() {
            @Override
            public void onResponse(Call<KujonResponse<List<CalendarEvent>>> call, Response<KujonResponse<List<CalendarEvent>>> response) {
                if (ErrorHandlerUtil.handleResponse(response)) {
                    List<CalendarEvent> data = response.body().data;
                    handleIncomingEvents(call, data, year, month);
                } else {
                    checkForRefreshCondition();
                    callList.remove(call);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<List<CalendarEvent>>> call, Throwable t) {
                callList.remove(call);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    protected abstract Call<KujonResponse<List<CalendarEvent>>> getKujonResponseCall(String restSuffix);

    private void handleIncomingEvents(Call<KujonResponse<List<CalendarEvent>>> call, List<CalendarEvent> data, int year, int month) {
        List<WeekViewEvent> events = $.map(data, EventUtils::from);
        String key = getKey(year, month);
        if (!eventsForDate.containsKey(key)) {
            eventsForDate.put(key, new ArrayList<>());
        }
        eventsForDate.get(key).addAll(events);
        activityChange.get().dataDowloaded();
        callList.remove(call);
        checkForRefreshCondition();
    }


    private void checkForRefreshCondition() {
        long value = counter.decrementAndGet();
        if (value == 0) {
            activityChange.get().stopLoading();
            refresh = false;
        }
    }

    private void stopAllCurrentCalls() {
        List<Call<KujonResponse<List<CalendarEvent>>>> calls = new ArrayList<>(callList);
        for (Call<KujonResponse<List<CalendarEvent>>> call : calls) {
            call.cancel();

            callList.remove(call);
        }
        counter.set(0);
    }

}
