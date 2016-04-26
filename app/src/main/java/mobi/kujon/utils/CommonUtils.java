package mobi.kujon.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CommonUtils {

    public static void showOnMap(Activity activity, String query) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(mapIntent);
        }
    }
}
