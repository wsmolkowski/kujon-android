package mobi.kujon.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mobi.kujon.R;
import mobi.kujon.network.json.StudentProgramme;
import mobi.kujon.utils.dto.SimpleUser;

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
        layout.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            String rowData = data.get(i);
            View view = layoutInflater.inflate(R.layout.list_row, null);
            TextView row = (TextView) view.findViewById(R.id.text1);
            row.setText(rowData);
            final int finalI = i;
            view.setOnClickListener(v -> listener.onClick(finalI));
            layout.addView(view);
        }
    }

    public static void showListUserWithImage(LayoutInflater layoutInflater, LinearLayout layout, List<String> data, OnPositionClickListener listener) {
        layout.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            String rowData = data.get(i);
            View view = layoutInflater.inflate(R.layout.list_row, null);
            TextView row = (TextView) view.findViewById(R.id.text1);
            row.setText(rowData);
            final int finalI = i;
            view.setOnClickListener(v -> listener.onClick(finalI));
            layout.addView(view);
        }
    }

    public static void showListProgrammes(LayoutInflater layoutInflater, LinearLayout layout, List<StudentProgramme> data, OnPositionClickListener listener) {
        layout.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            StudentProgramme studentProgramme = data.get(i);
            String rowData = studentProgramme.programme.description.split(",")[0];
            View view = layoutInflater.inflate(R.layout.list_row_programmes, null);
            TextView row = (TextView) view.findViewById(R.id.text1);
            TextView statusText = (TextView) view.findViewById(R.id.status_text);
            try{
                statusText.setText(studentProgramme.getGraduateText());
                statusText.setCompoundDrawablesWithIntrinsicBounds(studentProgramme.getImage(), 0, 0, 0);
            }catch (NullPointerException npe){
                statusText.setVisibility(View.GONE);
            }

            row.setText(rowData);
            final int finalI = i;
            view.setOnClickListener(v -> listener.onClick(finalI));
            layout.addView(view);
        }
    }

    public static boolean stringEquals(String left, String right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        return left.trim().toLowerCase().equals(right.trim().toLowerCase());
    }

    public static void showListUser(LayoutInflater layoutInflater, LinearLayout layout, List<SimpleUser> data, OnPositionClickListener listener) {
        layout.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            SimpleUser simpleUser = data.get(i);

            View view = layoutInflater.inflate(R.layout.list_row_user, null);
            TextView row = (TextView) view.findViewById(R.id.text1);
            row.setText(simpleUser.getName());
            final int finalI = i;
            view.setOnClickListener(v -> listener.onClick(finalI));
            layout.addView(view);
        }
    }
}
