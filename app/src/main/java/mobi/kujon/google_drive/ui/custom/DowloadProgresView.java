package mobi.kujon.google_drive.ui.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;

/**
 *
 */

public class DowloadProgresView extends RelativeLayout {
    private ProgressBar progressBar;
    private TextView textView;

    private FileStreamUpdateMVP.CancelModel cancelModel;
    private View cancelButton;

    public DowloadProgresView(Context context) {
        super(context);
        init();
    }

    public DowloadProgresView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DowloadProgresView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DowloadProgresView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setCancelModel(FileStreamUpdateMVP.CancelModel cancelModel) {
        this.cancelModel = cancelModel;
    }

    private void init() {
        inflate(getContext(), R.layout.update_layout, this);
        this.setPivotY(0.0f);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.textView = (TextView) findViewById(R.id.tilte_text_view);
        cancelButton = findViewById(R.id.cancel_button);
        int padding = getResources().getDimensionPixelSize(R.dimen.progress_margins);
        this.setPadding(padding, padding, padding, padding);
    }

    public void updateProggress(FileUpdateDto fileUpdateDto) {
        this.textView.setText(fileUpdateDto.getFileName());
        this.progressBar.setProgress(fileUpdateDto.getProgress());

        cancelButton.setOnClickListener(v -> {
            if (cancelModel != null) {
                this.cancelModel.updateStream(fileUpdateDto.getFileName());
            }
        });
        if (fileUpdateDto.getProgress() > 99) {
            cancelButton.setVisibility(GONE);
        }
    }

    public void setClick(ClickMeListener click) {
        this.setOnClickListener(view -> click.iWasClicked());
    }

    interface ClickMeListener {
        void iWasClicked();
    }
}
