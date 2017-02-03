package mobi.kujon.google_drive.ui.custom;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.ui.util.AbstractAnimatorListener;

/**
 *
 */

public class UploadLayout extends LinearLayout {

    private List<String> filesList;
    private UpdateFileListener updateFileListener;

    public UploadLayout(Context context) {
        super(context);
        init();
    }

    public UploadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UploadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UploadLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        filesList = new ArrayList<>();
    }


    public void setUpdateFileListener(UpdateFileListener updateFileListener) {
        this.updateFileListener = updateFileListener;
    }

    public void update(FileUpdateDto fileUpdateDto) {
        String fileName = fileUpdateDto.getFileName();
        if (filesList.contains(fileName)) {
            doIfAlreadyExist(fileUpdateDto, fileName);
        } else {
            this.filesList.add(fileUpdateDto.getFileName());
            DowloadProgresView dowloadProgresView = new DowloadProgresView(getContext());
            this.addView(dowloadProgresView);
            dowloadProgresView.setScaleY(0.0f);
            dowloadProgresView.setAlpha(0.0f);
            dowloadProgresView.updateProggress(fileUpdateDto);
            dowloadProgresView.animate().scaleY(1.0f).alpha(1.0f).setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }
    }

    private void doIfAlreadyExist(FileUpdateDto fileUpdateDto, String fileName) {
        DowloadProgresView childAt = (DowloadProgresView) this.getChildAt(filesList.indexOf(fileName));
        childAt.updateProggress(fileUpdateDto);
        if (fileUpdateDto.isEnded() || fileUpdateDto.getProgress() == 100) {
            UploadLayout.this.updateFileListener.onFileUploaded();
            childAt.setClick(() -> doOnProgressEnd(childAt,fileName));
        }
    }

    private void doOnProgressEnd(final DowloadProgresView childAt,String filename) {
        childAt.animate()
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AbstractAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        UploadLayout.this.removeView(childAt);
                        UploadLayout.this.filesList.remove(filename);

                    }
                }).start();
    }


}
