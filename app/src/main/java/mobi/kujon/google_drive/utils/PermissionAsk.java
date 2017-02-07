package mobi.kujon.google_drive.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 *
 */

public class PermissionAsk {

    public static Boolean askForPermission(Activity context, String permission, int code) {
        if (Build.VERSION.SDK_INT > 21) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context,new String[]{permission},code);
                return false;
            }
        }
        return true;
    }

    public static boolean doWeHavePermission(Context activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}

