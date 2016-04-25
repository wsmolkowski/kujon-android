package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuildingName {

    @SerializedName("en")
    @Expose
    public Object en;
    @SerializedName("pl")
    @Expose
    public String pl;

}