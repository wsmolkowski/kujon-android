
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;

}