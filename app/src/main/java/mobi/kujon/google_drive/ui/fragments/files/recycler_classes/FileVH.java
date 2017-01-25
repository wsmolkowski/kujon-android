package mobi.kujon.google_drive.ui.fragments.files.recycler_classes;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class FileVH extends RecyclerView.ViewHolder {

    @Bind(R.id.file_icon)
    ImageView imageView;

    @Bind(R.id.file_text_view)
    TextView textView;
    public FileVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(FileDTO fileDTO){
        Drawable drawable =Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP?
            itemView.getResources().getDrawable(fileDTO.getImageIcon(),null):itemView.getResources().getDrawable(fileDTO.getImageIcon());
        imageView.setImageDrawable(drawable);
        textView.setText(fileDTO.getFileName() + " " + fileDTO.getUserName() + " " + fileDTO.getFileSize());
    }
}
