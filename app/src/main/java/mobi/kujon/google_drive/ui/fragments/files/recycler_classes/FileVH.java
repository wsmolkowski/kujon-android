package mobi.kujon.google_drive.ui.fragments.files.recycler_classes;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
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

public class FileVH extends RecyclerView.ViewHolder implements ShowShareIcon {

    @Bind(R.id.vh_file_icon)
    ImageView imageView;

    @Bind(R.id.vh_file_header)
    TextView fileHeader;
    @Bind(R.id.vh_file_date)
    TextView fileDate;

    @Bind(R.id.vh_file_file_size)
    TextView fileSize;
    @Bind(R.id.vh_file_share)
    TextView shareFile;
    public FileVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(FileDTO fileDTO){
        Drawable drawable = getDrawable(fileDTO.getImageIcon());
        imageView.setImageDrawable(drawable);
        fileHeader.setText(fileDTO.getFileName());
        fileDate.setText(fileDTO.getDateCreated(itemView.getResources()));
        fileSize.setText(new StringBuilder().append(fileDTO.getFileSize()).append(" MB").toString());
        fileDTO.setShareFile(this);
    }

    @Override
    public void showShareIcon(@DrawableRes int res, @StringRes int stringRes) {
        setVisiabilityAndDrawable(res);
        shareFile.setText(stringRes);
    }

    @Override
    public void showShareIcon(@DrawableRes int res, String stringRes) {
        setVisiabilityAndDrawable(res);
        shareFile.setText(stringRes);
    }

    @Override
    public void hide() {
        shareFile.setVisibility(View.INVISIBLE);
    }

    private void setVisiabilityAndDrawable(@DrawableRes int res) {
        shareFile.setVisibility(View.VISIBLE);
        shareFile.setCompoundDrawablesWithIntrinsicBounds(res,0,0,0);
    }

    private Drawable getDrawable(@DrawableRes int res) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP?
            itemView.getResources().getDrawable(res,null):itemView.getResources().getDrawable(res);
    }
}
