package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.crashlytics.android.Crashlytics;
import com.github.underscore.$;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.converters.EventConverter;
import mobi.kujon.network.converters.KujonWeekViewEvent;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlanFragment extends Fragment implements MonthLoader.MonthChangeListener {

    public static final SimpleDateFormat REST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Bind(R.id.weekView) WeekView weekView;

    private Map<String, List<WeekViewEvent>> eventsForDate = new HashMap<>();
    private List<String> downloaded = new ArrayList<>();
    private KujonBackendApi backendApi;
    private AlertDialog alertDialog;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, rootView);

        weekView.setMonthChangeListener(this);
        weekView.setFirstDayOfWeek(Calendar.MONDAY);

        weekView.setOnEventClickListener((event, eventRect) -> {
            KujonWeekViewEvent viewEvent = (KujonWeekViewEvent) event;
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            CalendarEvent calendarEvent = viewEvent.getCalendarEvent();
            dlgAlert.setMessage(String.format("Sala: %s\nBudynek: %s\nGrupa: %s\nTyp: %s",
                    calendarEvent.roomNumber, calendarEvent.buildingName, calendarEvent.groupNumber, calendarEvent.type));
            dlgAlert.setTitle(calendarEvent.name);
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            alertDialog = dlgAlert.create();
            alertDialog.show();
        });

        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backendApi = KujonBackendService.getInstance().getKujonBackendApi();
    }

    @Override public List<? extends WeekViewEvent> onMonthChange(int year, int month) {
        System.out.println("year = [" + year + "], month = [" + month + "]");
        if (!downloaded.contains(getKey(year, month))) {
            downloadEventsFor(year, month);
        }
        return getEvents(year, month);
    }

    private List<WeekViewEvent> getEvents(int year, int month) {
        String key = getKey(year, month);
        if (!eventsForDate.containsKey(key)) {
            eventsForDate.put(key, new ArrayList<>());
        }

        return eventsForDate.get(key);
    }

    private String getKey(int year, int month) {
        return "" + year + month;
    }

    private void downloadEventsFor(int year, int month) {
        String key = getKey(year, month);
        downloaded.add(key);

        DateTime day = new DateTime(year, month, 1, 12, 0, 0);
        while (day.getMonthOfYear() == month) {
            String restSuffix = REST_DATE_FORMAT.format(day.toDate());
            System.out.println(restSuffix);
            day = day.plusDays(7);
            String weekKey = "" + year + day.getWeekOfWeekyear();
            if (downloaded.contains(weekKey)) {
                return;
            }

            downloaded.add(weekKey);

            backendApi.plan(restSuffix).enqueue(new Callback<KujonResponse<List<CalendarEvent>>>() {
                @Override public void onResponse(Call<KujonResponse<List<CalendarEvent>>> call, Response<KujonResponse<List<CalendarEvent>>> response) {
                    List<CalendarEvent> data = response.body().data;
                    List<WeekViewEvent> events = $.map(data, EventConverter::from);
                    getEvents(year, month).addAll(events);
                    weekView.notifyDatasetChanged();
                }

                @Override public void onFailure(Call<KujonResponse<List<CalendarEvent>>> call, Throwable t) {
                    Crashlytics.logException(t);
                }
            });
        }
    }

    @Override public void onStart() {
        super.onStart();
        DateTime now = DateTime.now();

        weekView.goToToday();
        weekView.goToHour(now.getHourOfDay());
        weekView.setShowNowLine(true);
    }

    @Override public void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
