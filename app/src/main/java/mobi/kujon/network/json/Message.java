package mobi.kujon.network.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {

    @SerializedName("created_time")
    @Expose
    public Date createdTime;

    @SerializedName("from")
    @Expose
    public String from;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("typ")
    @Expose
    public String type;

}
