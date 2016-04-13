package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Programme {

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("id")
    @Expose
    public String id;
}