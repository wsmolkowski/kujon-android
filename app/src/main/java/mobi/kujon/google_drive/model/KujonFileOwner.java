package mobi.kujon.google_drive.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KujonFileOwner {

    @SerializedName("first_name") @Expose
    public String firstName;

    @SerializedName("last_name") @Expose
    public String lastName;

    @SerializedName("user_id") @Expose
    public String userId;
}
