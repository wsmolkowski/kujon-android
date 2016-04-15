
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lecturer {

    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("id")
    @Expose
    public String id;

}
