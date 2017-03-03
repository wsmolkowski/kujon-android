package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

/**
 *
 */


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;


public class SortDialog extends DialogFragment {

    public static final String SORT_KEY = "SORT_KEY";


    private SortStrategyListener sortStrategyListener;

    public static SortDialog newInstance(@SortStrategyType int sortStrategyType) {
        SortDialog fragment = new SortDialog();
        Bundle args = new Bundle();
        args.putInt(SORT_KEY, sortStrategyType);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.date_sort)
    RadioButton dateSort;

    @BindView(R.id.author_sort)
    RadioButton authorSort;

    @BindView(R.id.file_name_sort)
    RadioButton fileSort;

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_DialogStyle);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_sort, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        setUpFirstState();
        dateSort.setOnCheckedChangeListener(getOnCheckedChangeListener(new DateSortStrategy()));
        authorSort.setOnCheckedChangeListener(getOnCheckedChangeListener(new AuthorSortStrategy()));
        fileSort.setOnCheckedChangeListener(getOnCheckedChangeListener(new FileNameSortStrategy()));
        return builder.create();
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(SortStrategy sortStrategy) {
        return (buttonView, isChecked) -> {
            if(isChecked){
                if(sortStrategyListener !=null){
                    sortStrategyListener.setSortStrategy(sortStrategy);
                    this.dismiss();
                }
            }
        };
    }

    private void setUpFirstState() {
        if(getArguments() != null ){
            switch (getArguments().getInt(SORT_KEY,0)){
                case SortStrategyType.DATE:
                    dateSort.setChecked(true);
                    break;
                case SortStrategyType.FILE_NAME:
                    fileSort.setChecked(true);
                    break;
                case SortStrategyType.AUTHOR:
                    authorSort.setChecked(true);
                    break;
            }
        }
    }


    public void setUpListener(SortStrategyListener sortStrategyListener) {
        this.sortStrategyListener = sortStrategyListener;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        sortStrategyListener = null;
    }
}
