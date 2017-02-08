package mobi.kujon.google_drive.ui.dialogs.file_info_dialog;


import android.app.Activity;
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
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.ui.dialogs.share_target.ChooseShareStudentsListener;

public class FileActionDialog extends DialogFragment {

    public static final String FILE_ID_KEY = "fileIdTralala";
    private static final String FILE_TYPE_KEY = "fileTYpe_key";
    private static final String FILE_NAME_KEY = "fileNmaeKey";
    @Bind(R.id.file_details)
    TextView fileDetails;

    @Bind(R.id.add_to_google_drive)
    TextView addToGoogleDrive;

    @Bind(R.id.delete_file)
    TextView deleteFile;

    private static final String IS_OWNED = "IS_OWNED";
    public static final String NAME = "FILE_ACTION_DIALOG";
    private FileActionListener fileActionListener;

    public static FileActionDialog newInstance(FileDTO dto) {
        FileActionDialog fragment = new FileActionDialog();
        Bundle args = new Bundle();
        args.putBoolean(IS_OWNED, dto.isMy());
        args.putString(FILE_ID_KEY, dto.getFileId());
        args.putString(FILE_TYPE_KEY, dto.getMimeType());
        args.putString(FILE_NAME_KEY, dto.getFileName());
        fragment.setArguments(args);
        return fragment;
    }


    private FileUploadInfoDto file;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_DialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_file_action, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        boolean fileOwned = getArguments() != null && getArguments().getBoolean(IS_OWNED);
        String fileId = getArguments().getString(FILE_ID_KEY);
        String fileType = getArguments().getString(FILE_TYPE_KEY);
        String fileName = getArguments().getString(FILE_NAME_KEY);
        file = new FileUploadInfoDto(fileType, fileName, fileId);
        setUpViews(fileOwned);
        return builder.create();
    }

    private void setUpViews(boolean isFileOwned) {
        fileDetails.setOnClickListener(v -> dismiss());
        if (isFileOwned) {

            deleteFile.setOnClickListener(v -> {
                        dismiss();
                        if (fileActionListener != null) {
                            fileActionListener.onFileDelete(file.getId());
                        }
                    }
            );
            deleteFile.setVisibility(View.VISIBLE);
        } else {
            deleteFile.setVisibility(View.GONE);
            fileDetails.setPadding(fileDetails.getPaddingLeft(), fileDetails.getPaddingTop(),
                    fileDetails.getPaddingRight(), fileDetails.getPaddingTop());
        }

        fileDetails.setOnClickListener(v -> {
            if (fileActionListener != null) {
                fileActionListener.onFileDetails(file.getId());
                this.dismiss();
            }
        });
        addToGoogleDrive.setOnClickListener(v -> {
            if (fileActionListener != null) {
                fileActionListener.onFileAddToGoogleDrive(file);
                this.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setUpListener(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setUpListener(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fileActionListener = null;
    }

    private void setUpListener(Context context) {
        if (context instanceof ChooseShareStudentsListener) {
            fileActionListener = (FileActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FileActionListener");
        }
    }
}
