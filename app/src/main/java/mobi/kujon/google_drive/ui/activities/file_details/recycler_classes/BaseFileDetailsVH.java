package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseFileDetailsVH<T> extends RecyclerView.ViewHolder {

    public BaseFileDetailsVH(View itemView) {
        super(itemView);
    }

    public abstract void bind(T t);
}
