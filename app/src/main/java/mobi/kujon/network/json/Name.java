package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name {

    @SerializedName("en")
    @Expose
    public String en;
    @SerializedName("pl")
    @Expose
    public String pl;

}