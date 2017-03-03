package mobi.kujon.google_drive.ui.fragments.files.recycler_classes;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class FileVH extends RecyclerView.ViewHolder implements ShowShareIcon {

    @BindView(R.id.file_layout)
    RelativeLayout fileLayout;

    @BindView(R.id.vh_file_icon)
    ImageView imageView;

    @BindView(R.id.vh_file_header)
    TextView fileHeader;
    @BindView(R.id.vh_file_date)
    TextView fileDate;
    @BindView(R.id.vh_file_owner)
    TextView fileOwner;
    @BindView(R.id.vh_file_file_size)
    TextView fileSize;
    @BindView(R.id.vh_file_share)
    TextView shareFile;

    public FileVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(FileDTO fileDTO, FilesRecyclerAdapter.OnFileClick onFileClick) {
        Drawable drawable = getDrawable(fileDTO.getImageIcon());
        imageView.setImageDrawable(drawable);
        fileHeader.setText(fileDTO.getFileName());
        fileDate.setText(fileDTO.getDateCreated(itemView.getResources()));
        fileSize.setText(new StringBuilder().append(fileDTO.getFileSize()).append(" MB").toString());
        fileOwner.setText(fileDTO.getAuthorString(itemView.getResources()));
        fileDTO.setShareFile(this);
        fileLayout.setOnClickListener(v -> onFileClick.onFileClick(fileDTO));
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
        shareFile.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
    }

    private Drawable getDrawable(@DrawableRes int res) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                itemView.getResources().getDrawable(res, null) : itemView.getResources().getDrawable(res);
    }
}
