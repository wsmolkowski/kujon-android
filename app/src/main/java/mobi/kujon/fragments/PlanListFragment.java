package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.crashlytics.android.Crashlytics;
import com.github.underscore.$;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.EventUtils;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.PlanEventsDownloader;


public class PlanListFragment extends BaseFragment {

    @Inject KujonApplication application;
    @Inject KujonUtils utils;

    private static final String TAG = "PlanListFragment";

    PlanEventsDownloader planEventsDownloader = new PlanEventsDownloader();

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.empty_info) TextView emptyInfo;
    private BaseActivity activity;
    private Adapter adapter;
    private LinearLayoutManager layoutManager;
    private DateTime current;
    private AtomicBoolean readyToDownloadNext = new AtomicBoolean(true);
    private String[] months;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plan_list, container, false);
        ButterKnife.bind(this, rootView);
        KujonApplication.getComponent().inject(this);
        adapter = new Adapter();
        adapter.shouldShowHeadersForEmptySections(true);
        setHasOptionsMenu(true);

        months = application.getResources().getStringArray(R.array.months);
        return rootView;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadData(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private synchronized void downloadNext(boolean first) {
        System.out.println("downloadNext. readyToDownloadNext = " + readyToDownloadNext);
        if (readyToDownloadNext.get()) {
            Log.d(TAG, "downloadNext: downloading next");
            readyToDownloadNext.set(false);

            current = current.plusMonths(1);
            int year = current.getYear();
            int monthOfYear = current.getMonthOfYear();
            if (Months.monthsBetween(DateTime.now(), current).getMonths() > 12) {
                Log.d(TAG, "downloadNext: more then 12 months");
                return;
            }
            if (first) activity.showProgress(true);
            planEventsDownloader.prepareMonth(year, monthOfYear).continueWith(task -> {
                activity.showProgress(false);
                Exception error = task.getError();
                if ((error == null)) {
                    SortedMap<PlanEventsDownloader.CalendarSection, List<CalendarEvent>> result = task.getResult();
                    System.out.println(result);
                    adapter.addData(result);
                    if (first) {
                        fab();
                        if (result.size() == 1) emptyInfo.setVisibility(View.VISIBLE);
                    }
                } else {
                    System.out.println(error);
                    error.printStackTrace();
                    Crashlytics.logException(error);
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
                readyToDownloadNext.set(true);
                return null;
            }, Task.UI_THREAD_EXECUTOR).continueWith(ErrorHandlerUtil.ERROR_HANDLER);
        }
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = ((BaseActivity) getActivity());
        activity.getSupportActionBar().setTitle(R.string.plan_title);

        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData(boolean refresh) {
        if (refresh) {
            utils.invalidateEntry("tt");
            planEventsDownloader.setRefresh(true);
        }
        readyToDownloadNext.set(true);
        adapter.data.clear();
        adapter.notifyDataSetChanged();
        current = DateTime.now();
        current = current.minusMonths(1);
        downloadNext(true);
    }

    @Override public void onStart() {
        super.onStart();
        activity.showProgress(true);
        planEventsDownloader.setRefresh(false);
        loadData(false);
    }

    @OnClick(R.id.fab) public void fab() {
        int index = 0;
        DateTime now = DateTime.now();
        for (Object key : adapter.data.keySet().toArray()) {
            PlanEventsDownloader.CalendarSection calendarSection = (PlanEventsDownloader.CalendarSection) key;
            LocalDate localDate = calendarSection.localDate;
            if (now.toLocalDate().isAfter(localDate)) {
                index += 1;
                index += adapter.data.get(calendarSection).size();
            } else {
                break;
            }
        }
        layoutManager.scrollToPositionWithOffset(index, 0);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.plan_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        SortedMap<PlanEventsDownloader.CalendarSection, List<CalendarEvent>> data = new TreeMap<>();

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_event, parent, false);
            return new ViewHolder(v);
        }

        @Override public int getSectionCount() {
            return data.keySet().size();
        }

        @Override public int getItemCount(int section) {
            return coursesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            PlanEventsDownloader.CalendarSection calendarSection = getCalendarSection(section);
            LocalDate localDate = calendarSection.localDate;
            if (calendarSection.monthName) {
                holder.sectionMonth.setText(months[localDate.monthOfYear().get() - 1]);
                holder.sectionMonth.setVisibility(View.VISIBLE);
                holder.section.setVisibility(View.GONE);
            } else {
                holder.section.setText(localDate.dayOfWeek().getAsText() + "  " + localDate.toString());
                holder.section.setVisibility(View.VISIBLE);
                holder.sectionMonth.setVisibility(View.GONE);
            }
            holder.dataLayout.setVisibility(View.GONE);
            holder.event = null;
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            System.out.println("holder = [" + holder + "], section = [" + section + "], relativePosition = [" + relativePosition + "], absolutePosition = [" + absolutePosition + "]");
            System.out.println("getSectionCount() = " + getSectionCount());
            System.out.println("getItemCount = " + getItemCount());
            if (getSectionCount() + absolutePosition + 3 > getItemCount()) {
                downloadNext(false);
            }
            CalendarEvent event = coursesInSection(section).get(relativePosition);
            String lecturers = "";
            if (event.lecturers != null) {
                lecturers = $.join($.collect(event.lecturers, (LecturerLong it) -> String.format("%s %s %s", it.titles.before, it.firstName, it.lastName)), ", ");
            }
            holder.eventName.setText(event.name + "\n" + lecturers);
            holder.time.setText(event.getHoursFromTo("\n") + "\ns. " + event.roomNumber);
            holder.section.setVisibility(View.GONE);
            holder.sectionMonth.setVisibility(View.GONE);
            holder.dataLayout.setVisibility(View.VISIBLE);
            holder.event = event;
        }

        public void addData(SortedMap<PlanEventsDownloader.CalendarSection, List<CalendarEvent>> data) {
            this.data.putAll(data);
            notifyDataSetChanged();
        }

        List<CalendarEvent> coursesInSection(int section) {
            List<CalendarEvent>[] courses = data.values().toArray(new List[0]);
            return courses[section];
        }

        private PlanEventsDownloader.CalendarSection getCalendarSection(int section) {
            return (PlanEventsDownloader.CalendarSection) data.keySet().toArray()[section];
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.event_name) TextView eventName;
        @Bind(R.id.time) TextView time;
        @Bind(R.id.section) TextView section;
        @Bind(R.id.sectionMonth) TextView sectionMonth;
        @Bind(R.id.dataLayout) View dataLayout;
        CalendarEvent event;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (event != null) {
                    AlertDialog eventDialog = EventUtils.getEventDialog(getActivity(), event);
                    eventDialog.show();
                }
            });
        }
    }
}
