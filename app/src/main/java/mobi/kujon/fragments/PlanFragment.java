package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.utils.EventUtils;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.KujonWeekViewEvent;
import mobi.kujon.utils.plan_fragment.ActivityChange;
import mobi.kujon.utils.plan_fragment.AskForDataOnPlan;
import mobi.kujon.utils.plan_fragment.AskForLecturerDataOnPlan;
import mobi.kujon.utils.plan_fragment.AskForStudentDataOnPlan;


public class PlanFragment extends BaseFragment implements MonthLoader.MonthChangeListener,ActivityChange {

    @Bind(R.id.weekView) WeekView weekView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @BindColor(R.color.event_color) int eventColor;
    @BindColor(R.color.dark_blue_sky) int darkBlueSky;
    @BindColor(R.color.dark) int dark;


    @Inject KujonBackendApi backendApi;
    @Inject KujonUtils utils;
    private AlertDialog alertDialog;
    private BaseActivity activity;
    private AskForDataOnPlan askForDataOnPlan;

    private static final String LECTURER_ID = "LECTURER_ID";

    public static PlanFragment newLecturerPlanInstance(String lecturerId) {
        PlanFragment planFragment = new PlanFragment();

        Bundle args = new Bundle();
        args.putString(LECTURER_ID, lecturerId);
        planFragment.setArguments(args);

        return planFragment;
    }

    private AskForDataOnPlan getDataPlanProvider() {
        Bundle args = getArguments();
        if (args == null) {
            return new AskForStudentDataOnPlan(this, utils, backendApi);
        }
        return getArguments().containsKey(LECTURER_ID) ? new AskForLecturerDataOnPlan(this, utils, backendApi, getArguments().getString(LECTURER_ID))
                : new AskForStudentDataOnPlan(this, utils, backendApi);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        askForDataOnPlan = getDataPlanProvider();
        weekView.setMonthChangeListener(this);
        weekView.setFirstDayOfWeek(Calendar.MONDAY);
        weekView.setShowNowLine(true);
        weekView.setLimitTime(7, 21);
        weekView.setEventCornerRadius(4);
        weekView.setDefaultEventColor(eventColor);
        weekView.setNowLineColor(darkBlueSky);
        weekView.setTodayHeaderTextColor(darkBlueSky);
        weekView.setEventTextColor(dark);
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            public String interpretDate(Calendar date) {
                try {
                    SimpleDateFormat e = new SimpleDateFormat("EEE dd.M", Locale.getDefault());
                    return e.format(date.getTime()).toUpperCase();
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return "";
                }
            }

            public String interpretTime(int hour) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);

                try {
                    SimpleDateFormat e = DateFormat.is24HourFormat(getActivity()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                    return e.format(calendar.getTime());
                } catch (Exception var4) {
                    var4.printStackTrace();
                    return "";
                }
            }
        });
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
        gotoNow();

        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = ((BaseActivity) getActivity());
    }

    @DebugLog @Override public List<? extends WeekViewEvent> onMonthChange(int year, int month) {
        return askForDataOnPlan.getEvents(year,month);
    }

    @Override public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.plan_title);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        askForDataOnPlan.destroy();
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            askForDataOnPlan.reload();
            gotoNow();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void dataDowloaded() {
        weekView.notifyDatasetChanged();
    }

    @Override
    public void startLoading() {
        activity.showProgress(true);
    }

    @Override
    public void stopLoading() {
        activity.showProgress(false);
    }

    @Override
    public void showToast(@StringRes int id, String text) {
        Toast.makeText(getContext(),getString(id) + " " + text,Toast.LENGTH_SHORT).show();
    }
}
