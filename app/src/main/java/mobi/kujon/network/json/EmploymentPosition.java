package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmploymentPosition {

    @SerializedName("position")
    @Expose
    public Position position;
    @SerializedName("faculty")
    @Expose
    public Faculty faculty;

}