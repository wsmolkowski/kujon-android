package mobi.kujon.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import mobi.kujon.R;
import mobi.kujon.fragments.TermsFragment;
import mobi.kujon.network.json.Term2;

/**
 *
 */

public class ShowTermDialog {


    public static void showTermDialog(Activity context, Term2 term) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        View termView = context.getLayoutInflater().inflate(R.layout.row_terms, null);
        TermsFragment.ViewHolder holder = new TermsFragment.ViewHolder(termView);
        holder.termName.setText(term.name);
        holder.termId.setText(term.termId);
        holder.section.setText(term.active ? context.getString(R.string.active_one) : context.getString(R.string.inactive_one));
        holder.startDate.setText(term.startDate);
        holder.endDate.setText(term.endDate);
        holder.finishDate.setText(term.finishDate);

        dlgAlert.setView(termView);
        dlgAlert.setCancelable(false);
        dlgAlert.setNegativeButton(R.string.ok, (dialog, which) -> {
            dialog.dismiss();
        });
        dlgAlert.create().show();

    }
}
