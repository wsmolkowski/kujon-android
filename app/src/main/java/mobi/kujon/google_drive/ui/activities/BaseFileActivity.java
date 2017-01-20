package mobi.kujon.google_drive.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mobi.kujon.R;
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.network.KujonException;
import mobi.kujon.utils.ErrorHandlerUtil;

/**
 *
 */

public abstract class BaseFileActivity extends AppCompatActivity implements HandleException{

    @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setBack();
    }

    private void setBack() {
        View backView = findViewById(R.id.back);
        if (backView != null) backView.setOnClickListener(v -> onBackPressed());
    }

    protected abstract void setProgress(boolean t);
    @Override
    public void handleException(Throwable throwable) {
        this.setProgress(false);
        try {
            throw  throwable;
        }catch (KujonException e){
            ErrorHandlerUtil.handleKujonError(e);
        }catch (Throwable t){
            ErrorHandlerUtil.handleError(t);
        }
    }
}