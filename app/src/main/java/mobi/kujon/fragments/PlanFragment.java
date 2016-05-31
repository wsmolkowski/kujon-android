package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.github.underscore.$;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.EventUtils;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.KujonWeekViewEvent;
import mobi.kujon.utils.PlanEventsDownloader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlanFragment extends BaseFragment implements MonthLoader.MonthChangeListener {

    @Bind(R.id.weekView) WeekView weekView;
    @Bind(R.id.fab) FloatingActionButton fab;

    private Map<String, List<WeekViewEvent>> eventsForDate = new HashMap<>();
    private List<String> downloaded = new ArrayList<>();
    @Inject KujonBackendApi backendApi;
    @Inject KujonUtils utils;
    private AlertDialog alertDialog;
    private BaseActivity activity;
    private boolean refresh;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);

        weekView.setMonthChangeListener(this);
        weekView.setFirstDayOfWeek(Calendar.MONDAY);
        weekView.setShowNowLine(true);
        weekView.setOnEventClickListener((event, eventRect) -> {
            KujonWeekViewEvent viewEvent = (KujonWeekViewEvent) event;
            CalendarEvent calendarEvent = viewEvent.getCalendarEvent();

            alertDialog = EventUtils.getEventDialog(getActivity(), calendarEvent);
            alertDialog.show();
        });

        fab.setOnClickListener(v -> gotoNow());
        fab.setOnLongClickListener(v -> {
            Toast.makeText(getActivity(), "Wróć do \"dzisiaj\"", Toast.LENGTH_SHORT).show();
            return true;
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = ((BaseActivity) getActivity());
    }

    @DebugLog @Override public List<? extends WeekViewEvent> onMonthChange(int year, int month) {
        System.out.println("year = [" + year + "], month = [" + month + "]");
        if (!downloaded.contains(getKey(year, month))) {
            downloadEventsFor(year, month);
        }
        return getEvents(year, month);
    }

    @DebugLog private List<WeekViewEvent> getEvents(int year, int month) {
        String key = getKey(year, month);
        if (!eventsForDate.containsKey(key)) {
            eventsForDate.put(key, new ArrayList<>());
        }

        return eventsForDate.get(key);
    }

    private String getKey(int year, int month) {
        return "" + year + month;
    }

    @DebugLog private void downloadEventsFor(int year, int month) {
        String key = getKey(year, month);
        downloaded.add(key);

        DateTime day = new DateTime(year, month, 1, 12, 0, 0);
        while (day.getMonthOfYear() == month) {
            String restSuffix = PlanEventsDownloader.REST_DATE_FORMAT.format(day.toDate());
            System.out.println(restSuffix);
            day = day.plusDays(7);
            String weekKey = "" + year + day.getWeekOfWeekyear();
            if (downloaded.contains(weekKey)) {
                return;
            }

            downloaded.add(weekKey);

            activity.showProgress(true);
            Call<KujonResponse<List<CalendarEvent>>> call = refresh ? backendApi.planRefresh(restSuffix) : backendApi.plan(restSuffix);
            call.enqueue(new Callback<KujonResponse<List<CalendarEvent>>>() {
                @Override public void onResponse(Call<KujonResponse<List<CalendarEvent>>> call, Response<KujonResponse<List<CalendarEvent>>> response) {
                    activity.showProgress(false);
                    if (ErrorHandlerUtil.handleResponse(response)) {
                        List<CalendarEvent> data = response.body().data;
                        List<WeekViewEvent> events = $.map(data, EventUtils::from);
                        getEvents(year, month).addAll(events);
                        weekView.notifyDatasetChanged();
                    }
                }

                @Override public void onFailure(Call<KujonResponse<List<CalendarEvent>>> call, Throwable t) {
                    activity.showProgress(false);
                    ErrorHandlerUtil.handleError(t);
                }
            });
        }
    }

    @Override public void onStart() {
        super.onStart();
        refresh = false;
        gotoNow();
        activity.getSupportActionBar().setTitle(R.string.plan_title);
    }

    private void gotoNow() {
        DateTime now = DateTime.now();
        weekView.goToToday();
        weekView.goToHour(now.getHourOfDay());
    }

    @Override public void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            gotoNow();
            refresh = true;
            utils.invalidateEntry("tt");
            downloaded.clear();
            eventsForDate.clear();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
