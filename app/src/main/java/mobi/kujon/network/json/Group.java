package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("class_type_id")
    @Expose
    public String classTypeId;

    @SerializedName("class_type")
    @Expose
    public String classType;
    @SerializedName("course_unit_id")
    @Expose
    public Integer courseUnitId;
    @SerializedName("group_number")
    @Expose
    public Integer groupNumber;

}