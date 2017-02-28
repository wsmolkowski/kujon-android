
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Programme_ {

    @SerializedName("level_of_studies")
    @Expose
    public String levelOfStudies;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("mode_of_studies")
    @Expose
    public String modeOfStudies;
    @SerializedName("ects_used_sum")
    @Expose
    public String ectsUsedSum;
    @SerializedName("id")
    @Expose
    public String id;

}