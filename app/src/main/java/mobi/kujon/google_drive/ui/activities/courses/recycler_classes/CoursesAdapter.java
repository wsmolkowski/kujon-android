package mobi.kujon.google_drive.ui.activities.courses.recycler_classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.model.dto.TermWithCourseDTO;
import mobi.kujon.google_drive.ui.util.OnDtoClick;

/**
 *
 */

public class CoursesAdapter extends SectionedRecyclerViewAdapter<CoursesAdapter.ViewHolder> {


    private List<TermWithCourseDTO> termWithCourseDTOs;
    private OnDtoClick<CourseDTO> onDtoClick;

    public CoursesAdapter(List<TermWithCourseDTO> courseDTOs, OnDtoClick<CourseDTO> onDtoClick) {
        this.termWithCourseDTOs = courseDTOs;
        this.onDtoClick = onDtoClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course_file, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getSectionCount() {
        return termWithCourseDTOs.size();
    }

    @Override
    public int getItemCount(int section) {
        return termWithCourseDTOs.get(section).getCourseDTOs().size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        holder.section.setText(sectionName(section));
        holder.termId = sectionName(section);
        holder.section.setVisibility(View.VISIBLE);
        holder.courseLayout.setVisibility(View.GONE);
        holder.itemView.setTag(R.string.no_item_decorator, true);
        holder.itemView.setOnClickListener(null);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        CourseDTO course = termWithCourseDTOs.get(section).getCourseDTOs().get(relativePosition);
        holder.courseName.setText(course.getCourseName());
        holder.courseId = course.getCourseId();
        holder.termId = course.getTermId();
        holder.section.setVisibility(View.GONE);
        holder.courseLayout.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> onDtoClick.onDtoClick(course));
        if(course.getFileNumber()==0){
            holder.icon.setVisibility(View.GONE);
            holder.fileCount.setText("");
        }else {
            holder.icon.setVisibility(View.VISIBLE);
            holder.fileCount.setText(String.valueOf(course.getFileNumber()));
        }
    }


    String sectionName(int section) {
        return termWithCourseDTOs.get(section).getTermName();
    }

    public void setData(List<TermWithCourseDTO> courseDTOs) {
        this.termWithCourseDTOs = courseDTOs;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.course_name)
        TextView courseName;
        @Bind(R.id.section)
        TextView section;
        @Bind(R.id.courseLayout)
        View courseLayout;

        @Bind(R.id.files_count)
        TextView fileCount;

        @Bind(R.id.file_icon)
        View icon;
        String courseId;
        String termId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
