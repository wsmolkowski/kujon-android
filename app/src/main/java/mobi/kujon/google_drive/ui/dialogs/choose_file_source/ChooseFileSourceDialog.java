package mobi.kujon.google_drive.ui.dialogs.choose_file_source;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;

/**
 *
 */

public class ChooseFileSourceDialog extends DialogFragment {

    public static final String SORT_KEY = "SORT_KEY";


    private ChooseFileSourceListener chooseFileSourceListener;

    public static ChooseFileSourceDialog newInstance() {
        ChooseFileSourceDialog fragment = new ChooseFileSourceDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.google_drive_source)
    TextView googleSource;

    @Bind(R.id.this_device_file)
    TextView deviceSource;


    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_DialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_source, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        googleSource.setOnClickListener(v -> {
            dismiss();
            if (chooseFileSourceListener != null) {

                chooseFileSourceListener.onGoogleDriveChoose();
            }
        });
        deviceSource.setOnClickListener(v -> {
            dismiss();
            if (chooseFileSourceListener != null) {
                chooseFileSourceListener.onDeviceFileChoose();
            }
        });

        return builder.create();
    }

    public void setUpListener(ChooseFileSourceListener sortStrategyListener) {
        this.chooseFileSourceListener = sortStrategyListener;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        chooseFileSourceListener = null;
    }


}