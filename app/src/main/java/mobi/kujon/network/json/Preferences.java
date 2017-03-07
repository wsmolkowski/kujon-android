package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Preferences {

    @SerializedName("event_enable")
    @Expose
    public Boolean notificationsEnabled;

    @SerializedName("google_callendar_enable")
    @Expose
    public Boolean googleCalendarEnabled;


    @SerializedName("event_files_enable")
    @Expose
    public Boolean notificationFilesEnable;

}
