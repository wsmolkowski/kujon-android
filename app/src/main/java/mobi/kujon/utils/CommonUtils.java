package mobi.kujon.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;

public class CommonUtils {

    public static void showOnMap(Activity activity, String query) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(mapIntent);
        }
    }

    public static void showList(LayoutInflater layoutInflater, LinearLayout layout, List<String> data, OnPositionClickListener listener) {
        KujonApplication application = KujonApplication.getApplication();
        for (int i = 0; i < data.size(); i++) {
            String rowData = data.get(i);
            TextView row = (TextView) layoutInflater.inflate(R.layout.list_row, null);
            row.setText(rowData);
            final int finalI = i;
            row.setOnClickListener(v -> listener.onClick(finalI));
            row.setCompoundDrawablesWithIntrinsicBounds(null, null, application.getResources().getDrawable(R.drawable.navigate_next), null);
            layout.addView(row);
        }
    }
}
