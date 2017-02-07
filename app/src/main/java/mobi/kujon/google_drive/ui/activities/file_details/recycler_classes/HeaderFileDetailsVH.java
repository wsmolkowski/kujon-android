package mobi.kujon.google_drive.ui.activities.file_details.recycler_classes;


import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;

public class HeaderFileDetailsVH extends BaseFileDetailsVH<FileDTO> {

    @Bind(R.id.file_type)
    TextView fileType;
    @Bind(R.id.file_size)
    TextView fileSize;
    @Bind(R.id.file_creation_date)
    TextView fileCreationDate;
    @Bind(R.id.share_with_everyone)
    SwitchCompat shareWithEveryone;

    private FileDetailsAdapter.OnEveryoneSwitchClicked onEveryoneSwitchClicked;

    public HeaderFileDetailsVH(View itemView, FileDetailsAdapter.OnEveryoneSwitchClicked onEveryoneSwitchClicked) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.onEveryoneSwitchClicked = onEveryoneSwitchClicked;
    }

    @Override
    public void bind(FileDTO fileDTO) {
        fileType.setText(fileDTO.getContentType());
        fileSize.setText(new StringBuilder().append(fileDTO.getFileSize()).append(" MB"));
        fileCreationDate.setText(fileDTO.getDateCreated());
        shareWithEveryone.setChecked(ShareFileTargetType.ALL.equals(fileDTO.getShareType()));
        shareWithEveryone.setOnCheckedChangeListener((buttonView, isChecked) -> onEveryoneSwitchClicked.onEveryoneClicked(isChecked));
    }
}
