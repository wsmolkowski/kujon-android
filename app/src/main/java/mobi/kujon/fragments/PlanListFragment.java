package mobi.kujon.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.github.underscore.$;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.utils.EventUtils;
import mobi.kujon.utils.PlanEventsDownloader;


public class PlanListFragment extends Fragment {

    PlanEventsDownloader planEventsDownloader = new PlanEventsDownloader();

    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    private BaseActivity activity;
    private Adapter adapter;
    private LinearLayoutManager layoutManager;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plan_list, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new Adapter();
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = ((BaseActivity) getActivity());
        activity.getSupportActionBar().setTitle(R.string.plan_title);

        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override public void onStart() {
        super.onStart();
        activity.showProgress(true);
        planEventsDownloader.downloadEventsForThreeMonths().continueWith(task -> {
            activity.showProgress(false);
            if ((task.getError() == null)) {
                List<CalendarEvent> result = task.getResult();
                System.out.println(result);
                SortedMap<LocalDate, List<CalendarEvent>> localDateListMap = planEventsDownloader.groupEvents(result);
                System.out.println(localDateListMap);
                adapter.setData(localDateListMap);
                fab();
            } else {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @OnClick(R.id.fab) public void fab() {
        int index = 0;
        DateTime now = DateTime.now();
        for (Object key : adapter.data.keySet().toArray()) {
            LocalDate localDate = (LocalDate) key;
            if (now.toLocalDate().isAfter(localDate)) {
                index += 1;
                index += adapter.data.get(localDate).size();
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

        SortedMap<LocalDate, List<CalendarEvent>> data = new TreeMap<>();

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
            holder.section.setText(sectionName(section));
            holder.section.setVisibility(View.VISIBLE);
            holder.dataLayout.setVisibility(View.GONE);
            holder.event = null;
        }

        @Override public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
            CalendarEvent event = coursesInSection(section).get(relativePosition);
            String lecturers = "";
            if (event.lecturers != null) {
                lecturers = $.join($.collect(event.lecturers, (LecturerLong it) -> String.format("%s %s %s", it.titles.before, it.firstName, it.lastName)), ", ");
            }
            holder.eventName.setText(event.name + "\n" + lecturers);
            holder.time.setText(event.getHoursFromTo("\n") + "\ns. " + event.roomNumber);
            holder.section.setVisibility(View.GONE);
            holder.dataLayout.setVisibility(View.VISIBLE);
            holder.event = event;
        }

        public void setData(SortedMap<LocalDate, List<CalendarEvent>> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        List<CalendarEvent> coursesInSection(int section) {
            List<CalendarEvent>[] courses = data.values().toArray(new List[0]);
            return courses[section];
        }

        String sectionName(int section) {
            LocalDate localDate = (LocalDate) data.keySet().toArray()[section];
            return localDate.dayOfWeek().getAsText() + "  " + localDate.toString();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.event_name) TextView eventName;
        @Bind(R.id.time) TextView time;
        @Bind(R.id.section) TextView section;
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
