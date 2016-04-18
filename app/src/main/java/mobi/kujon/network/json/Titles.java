package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Titles {

    @SerializedName("after")
    @Expose
    public String after;
    @SerializedName("before")
    @Expose
    public String before;

}