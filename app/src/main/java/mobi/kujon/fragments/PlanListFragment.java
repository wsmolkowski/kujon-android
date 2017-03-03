package mobi.kujon.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.github.underscore.$;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.EventUtils;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.PlanEventsDownloader;


public class PlanListFragment extends BaseFragment implements EndlessRecyclerViewAdapter.RequestToLoadMoreListener {

    @Inject KujonApplication application;
    @Inject KujonUtils utils;

    Handler handler = new Handler();

    private static final String TAG = "PlanListFragment";

    PlanEventsDownloader planEventsDownloader = new PlanEventsDownloader();

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.empty_info) TextView emptyInfo;
    private BaseActivity activity;
    private Adapter adapter;
    private LinearLayoutManager layoutManager;
    private DateTime current;
    private String[] months;
    private EndlessRecyclerViewAdapter endlessRecyclerViewAdapter;

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

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = ((BaseActivity) getActivity());
        activity.getSupportActionBar().setTitle(R.string.plan_title);

        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        endlessRecyclerViewAdapter = new EndlessRecyclerViewAdapter(getActivity(), this.adapter, this);
        recyclerView.setAdapter(endlessRecyclerViewAdapter);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            loadData(true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @DebugLog
    private void loadData(boolean refresh) {
        if (refresh) {
            utils.invalidateEntry("/tt/");
            planEventsDownloader.setRefresh(true);
        }
        adapter.data.clear();
        adapter.notifyDataSetChanged();
        current = DateTime.now();
        current = current.minusMonths(1);
        downloadNext(true);

        DateTime previous = current;

        previous = previous.minusMonths(1);
        addMonth(true, previous.getYear(), previous.getMonthOfYear());
    }

    @DebugLog @Override public void onLoadMoreRequested() {
        downloadNext(false);
    }

    @DebugLog
    private synchronized void downloadNext(boolean first) {
        current = current.plusMonths(1);
        int year = current.getYear();
        int monthOfYear = current.getMonthOfYear();
        if (Months.monthsBetween(DateTime.now(), current).getMonths() > 12) {
            Log.d(TAG, "downloadNext: more then 12 months");
            handler.post(() -> endlessRecyclerViewAdapter.onDataReady(false));
            return;
        }
        addMonth(first, year, monthOfYear);
    }

    private void addMonth(boolean first, int year, int monthOfYear) {
        planEventsDownloader.prepareMonth(year, monthOfYear).onSuccessTask(task -> {
            endlessRecyclerViewAdapter.onDataReady(true);
            SortedMap<PlanEventsDownloader.CalendarSection, List<CalendarEvent>> result = task.getResult();
            adapter.addData(result);
            if (first) {
                gotoToday();
                if (result.size() == 0) emptyInfo.setVisibility(View.VISIBLE);
            }
            return null;
        }, Task.UI_THREAD_EXECUTOR).continueWith(ErrorHandlerUtil.ERROR_HANDLER);
    }

    @Override public void onStart() {
        super.onStart();
        planEventsDownloader.setRefresh(false);
        loadData(false);
    }

    @OnClick(R.id.fab) public void gotoToday() {
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
        final int finalIndex = index - 1;
        handler.postDelayed(() -> layoutManager.scrollToPositionWithOffset(finalIndex, 0), 200);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.plan_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected class Adapter extends SectionedRecyclerViewAdapter<ViewHolder> {

        SortedMap<PlanEventsDownloader.CalendarSection, List<CalendarEvent>> data = new TreeMap<>();
        private final int contentHeight;

        public Adapter() {
            View view = getActivity().findViewById(R.id.placeholder);
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
            contentHeight = view.getHeight() - (int) px;
        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_event, parent, false);
            return new ViewHolder(v, contentHeight);
        }

        @Override public int getSectionCount() {
            return data.keySet().size();
        }

        @Override public int getItemCount(int section) {
            return coursesInSection(section).size();
        }

        @Override public void onBindHeaderViewHolder(ViewHolder holder, int section) {
            PlanEventsDownloader.CalendarSection calendarSection = getCalendarSection(section);
            int itemCount = getItemCount(section);
            LocalDate localDate = calendarSection.localDate;

            if (localDate.dayOfMonth().get() == 1) {
                holder.sectionMonth.setText(months[localDate.monthOfYear().get() - 1]);
                holder.sectionMonth.setVisibility(View.VISIBLE);
                if (emptyMonth(localDate)) {
                    holder.noEvents.setVisibility(itemCount == 0 ? View.VISIBLE : View.GONE);
                }
            } else {
                holder.noEvents.setVisibility(View.GONE);
                holder.sectionMonth.setVisibility(View.GONE);
            }

            if (itemCount > 0) {
                holder.section.setText(localDate.dayOfWeek().getAsText() + "  " + localDate.toString());
                holder.section.setVisibility(View.VISIBLE);
            } else {
                holder.section.setVisibility(View.GONE);
            }

            holder.dataLayout.setVisibility(View.GONE);
            holder.event = null;
        }

        private boolean emptyMonth(LocalDate localDate) {
            Set<PlanEventsDownloader.CalendarSection> sections = $.filter(data.keySet(), it ->
                    it.localDate.getMonthOfYear() == localDate.getMonthOfYear() && it.localDate.getYear() == localDate.getYear());
            Set<List<CalendarEvent>> events = $.collect(sections, it -> data.get(it));
            return !$.any(events, it -> it.size() > 0);
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            CalendarEvent event = coursesInSection(section).get(relativePosition);
            String lecturers = "";
//            if (event.lecturers != null) {
//                lecturers = $.join($.collect(event.lecturers, (LecturerLong it) -> String.format("%s %s %s", it.titles.before, it.firstName, it.lastName)), ", ");
//            }
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

        @BindView(R.id.event_name) TextView eventName;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.section) TextView section;
        @BindView(R.id.sectionMonth) TextView sectionMonth;
        @BindView(R.id.dataLayout) View dataLayout;
        @BindView(R.id.no_events) View noEvents;
        CalendarEvent event;

        public ViewHolder(View itemView, int contentHeight) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ViewGroup.LayoutParams params = noEvents.getLayoutParams();
            params.height = contentHeight;
            noEvents.setLayoutParams(params);

            itemView.setOnClickListener(v -> {
                if (event != null) {
                    AlertDialog eventDialog = EventUtils.getEventDialog(getActivity(), event);
                    eventDialog.show();
                }
            });
        }
    }
}
