package mobi.kujon.google_drive.ui.activities.courses.recycler_classes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.CourseDTO;

/**
 *
 */
public class CourseVH  extends RecyclerView.ViewHolder{
    @Bind(R.id.course_text_view)
    TextView textView;

    public CourseVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(CourseDTO courseDTO) {
        textView.setText(courseDTO.getCourseName());
    }
}
