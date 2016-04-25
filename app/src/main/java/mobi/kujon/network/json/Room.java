package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("building_id")
    @Expose
    public String buildingId;
    @SerializedName("building_name")
    @Expose
    public BuildingName buildingName;
    @SerializedName("number")
    @Expose
    public String number;
    @SerializedName("id")
    @Expose
    public String id;

}