package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;


public class PlanFragment extends Fragment implements MonthLoader.MonthChangeListener {

    @Bind(R.id.weekView) WeekView weekView;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, rootView);

        weekView.setMonthChangeListener(this);
        weekView.setFirstDayOfWeek(Calendar.MONDAY);

        return rootView;
    }

    @Override public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        System.out.println("newYear = [" + newYear + "], newMonth = [" + newMonth + "]");
        List<WeekViewEvent> events = new ArrayList<>();
        if (newMonth == 4) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.add(Calendar.HOUR, 1);
            end.add(Calendar.HOUR, 2);
            events.add(new WeekViewEvent(1, "Język włoski - gr. zerowa - Lektorat", start, end));

            start = Calendar.getInstance();
            end = Calendar.getInstance();
            start.add(Calendar.HOUR, 3);
            start.add(Calendar.DAY_OF_YEAR, 1);
            end.add(Calendar.HOUR, 5);
            end.add(Calendar.DAY_OF_YEAR, 1);
            WeekViewEvent event = new WeekViewEvent(1, "Historia kultury francuskiej i włoskiej - część pierwsza - Wykład", start, end);
            event.setColor(0xFF689F38);
            events.add(event);
        }
        return events;
    }

    @Override public void onStart() {
        super.onStart();
        Calendar instance = Calendar.getInstance();
        weekView.goToDate(instance);
        weekView.goToHour(7);
        weekView.setShowNowLine(true);
    }
}
