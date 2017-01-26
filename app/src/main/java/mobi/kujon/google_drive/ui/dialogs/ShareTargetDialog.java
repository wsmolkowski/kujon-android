package mobi.kujon.google_drive.ui.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;

public class ShareTargetDialog extends DialogFragment {

    public static final String NAME = "SHARE_TARGET_DIALOG";

    @Bind(R.id.share_with_everyone)
    TextView shareWithEveryone;

    @Bind(R.id.share_with_chosen)
    TextView shareWithChosen;

    private Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_DialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_share_target, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        dialog = builder.create();
        setOnClickListeners();
        return dialog;
    }

    private void setOnClickListeners() {
        shareWithChosen.setOnClickListener(v -> dialog.dismiss());

        shareWithEveryone.setOnClickListener(v -> dialog.dismiss());
    }
}
