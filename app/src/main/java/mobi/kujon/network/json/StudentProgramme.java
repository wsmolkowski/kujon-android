package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentProgramme {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("programme")
    @Expose
    public Programme programme;

}