package mobi.kujon.google_drive.ui.activities.semesters.recycler_classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.ui.util.OnDtoClick;

/**
 *
 */

public class SemesterAdapter extends RecyclerView.Adapter<SemesterVH> {

    private List<SemesterDTO> semesterDTOs;
    private OnDtoClick<SemesterDTO> onSemesterClick;

    public SemesterAdapter(List<SemesterDTO> semesterDTOs, OnDtoClick<SemesterDTO> onSemesterClick) {
        this.semesterDTOs = semesterDTOs;
        this.onSemesterClick = onSemesterClick;
    }


    @Override
    public SemesterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_semester, parent, false);
        return new SemesterVH(v);
    }

    @Override
    public void onBindViewHolder(SemesterVH holder, int position) {
        SemesterDTO currentSemester = semesterDTOs.get(position);
        holder.bind(currentSemester);
        holder.itemView.setOnClickListener(view -> this.onSemesterClick.onDtoClick(currentSemester));
    }

    @Override
    public int getItemCount() {
        return semesterDTOs.size();
    }

    public void setSemesterDTOs(List<SemesterDTO> semesterDTOs) {
        this.semesterDTOs = semesterDTOs;
        this.notifyDataSetChanged();
    }


}
