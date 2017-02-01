package mobi.kujon.google_drive.ui.dialogs.share_target;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.network.json.Participant;

import static mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity.CHOOSE_STUDENTS_REQUEST;
import static mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity.CHOOSE_STUDENTS_RESPONSE;
import static mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity.CHOSEN;

public class ShareTargetDialog extends DialogFragment {

    public static final String NAME = "SHARE_TARGET_DIALOG";
    public static final String COURSE_ID = "COURSE_ID";
    public static final String TERM_ID = "TERM_ID";
    public static final String TITLE = "TITLE";


    @Bind(R.id.share_with_everyone)
    TextView shareWithEveryone;

    @Bind(R.id.share_with_chosen)
    TextView shareWithChosen;

    private ChooseShareStudentsListener chooseShareTargetListener;

    public static ShareTargetDialog newInstance(String courseId, String termId, String title) {
        ShareTargetDialog dialog = new ShareTargetDialog();
        Bundle args = new Bundle();
        args.putString(COURSE_ID, courseId);
        args.putString(TERM_ID, termId);
        args.putString(TITLE, title);
        dialog.setArguments(args);
        return dialog;
    }

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
            showStudentChoiceList();
        });

        shareWithEveryone.setOnClickListener(v -> {
            chooseShareTargetListener.shareWith(ShareFileTargetType.ALL, null);
            dismiss();
        });
    }

    private void showStudentChoiceList() {
        Intent chooseStudentsIntent = new Intent(getActivity(), ChooseStudentActivity.class);
        chooseStudentsIntent.putExtras(getArguments());
        startActivityForResult(chooseStudentsIntent, CHOOSE_STUDENTS_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHOOSE_STUDENTS_REQUEST) {
            if(resultCode == CHOSEN) {
                String chosenStudentIds[] = data.getStringArrayExtra(CHOOSE_STUDENTS_RESPONSE);
                if(chosenStudentIds.length == 0) {
                    chooseShareTargetListener.shareWith(ShareFileTargetType.NONE, null);
                } else {
                    chooseShareTargetListener.shareWith(ShareFileTargetType.LIST, Arrays.asList(chosenStudentIds));
                }
            }
        }
        dismiss();
    }

    private void setUpListener(Context context) {
        if (context instanceof ChooseShareStudentsListener) {
            chooseShareTargetListener = (ChooseShareStudentsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ChooseShareTarget");
        }
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
        chooseShareTargetListener = null;
    }
}
