package mobi.kujon.google_drive.ui.dialogs.share_target;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
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

    private ChooseShareStudentsListener chooseShareTargetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_DialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_share_target, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        setOnClickListeners();
        return builder.create();
    }

    private void setOnClickListeners() {
        shareWithChosen.setOnClickListener(v -> {
            dismiss();
        });

        shareWithEveryone.setOnClickListener(v -> {
            chooseShareTargetListener.shareWithAll();
            dismiss();
        });
    }

    private void setUpListener(Context context) {
        if (context instanceof ChooseShareStudentsListener) {
            chooseShareTargetListener = (ChooseShareStudentsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ChooseShareTarget");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setUpListener(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        chooseShareTargetListener = null;
    }
}
