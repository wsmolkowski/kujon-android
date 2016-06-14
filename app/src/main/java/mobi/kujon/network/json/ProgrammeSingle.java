package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ProgrammeSingle {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mode_of_studies")
    @Expose
    public String modeOfStudies;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("programme_id")
    @Expose
    public String programmeId;
    @SerializedName("faculty")
    @Expose
    public Faculty faculty;
    @SerializedName("level_of_studies")
    @Expose
    public String levelOfStudies;

}