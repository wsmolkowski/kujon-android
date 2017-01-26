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

public class FileActionDialog extends DialogFragment {

    @Bind(R.id.file_details)
    TextView fileDetails;

    @Bind(R.id.add_to_google_drive)
    TextView addToGoogleDrive;

    @Bind(R.id.delete_file)
    TextView deleteFile;

    private static final String IS_OWNED = "IS_OWNED";
    public static final String NAME = "FILE_ACTION_DIALOG";

    public static FileActionDialog newInstance(boolean isOwned) {
        FileActionDialog fragment = new FileActionDialog();
        Bundle args = new Bundle();
        args.putBoolean(IS_OWNED, isOwned);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_DialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_file_action, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        boolean fileOwned = getArguments() != null && getArguments().getBoolean(IS_OWNED);
        setUpViews(fileOwned);
        return builder.create();
    }

    private void setUpViews(boolean isFileOwned) {
        fileDetails.setOnClickListener(v -> dismiss());
        if (isFileOwned) {
            addToGoogleDrive.setOnClickListener(v -> dismiss());
            deleteFile.setOnClickListener(v -> dismiss());
        } else {
            addToGoogleDrive.setVisibility(View.GONE);
            deleteFile.setVisibility(View.GONE);
            fileDetails.setPadding(fileDetails.getPaddingLeft(), fileDetails.getPaddingTop(),
                    fileDetails.getPaddingRight(), fileDetails.getPaddingTop());
        }
    }
}
