package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kacper on 19.12.16.
 */

public class Preferences {

    @SerializedName("event_enable")
    @Expose
    public Boolean notificationsEnabled;

    @SerializedName("google_callendar_enable")
    @Expose
    public Boolean googleCalendarEnabled;

}
