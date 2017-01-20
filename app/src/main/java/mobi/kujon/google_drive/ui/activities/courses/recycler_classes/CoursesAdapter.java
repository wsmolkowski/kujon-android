package mobi.kujon.google_drive.ui.activities.courses.recycler_classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.ui.util.OnDtoClick;

/**
 *
 */

public class CoursesAdapter extends RecyclerView.Adapter<CourseVH> {


    private List<CourseDTO> courseDTOs;
    private OnDtoClick<CourseDTO> onDtoClick;

    public CoursesAdapter(List<CourseDTO> courseDTOs, OnDtoClick<CourseDTO> onDtoClick) {
        this.courseDTOs = courseDTOs;
        this.onDtoClick = onDtoClick;
    }

    @Override
    public CourseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_course, parent, false);
        return new CourseVH(v);

    }

    @Override
    public void onBindViewHolder(CourseVH holder, int position) {
        CourseDTO courseDTO = courseDTOs.get(position);
        holder.bind(courseDTO);
        holder.itemView.setOnClickListener(view -> onDtoClick.onDtoClick(courseDTO));
    }

    @Override
    public int getItemCount() {
        return courseDTOs.size();
    }

    public void setCourseDTOs(List<CourseDTO> courseDTOs) {
        this.courseDTOs = courseDTOs;
        this.notifyDataSetChanged();
    }
}
