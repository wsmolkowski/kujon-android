package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Grade {

    @SerializedName("course_name")
    @Expose
    public String courseName;
    @SerializedName("value_description")
    @Expose
    public String valueDescription;
    @SerializedName("term_id")
    @Expose
    public String termId;
    @SerializedName("exam_session_number")
    @Expose
    public Integer examSessionNumber;
    @SerializedName("value_symbol")
    @Expose
    public String valueSymbol;
    @SerializedName("exam_id")
    @Expose
    public Integer examId;
    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("class_type")
    @Expose
    public String classType;

}