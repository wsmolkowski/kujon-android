
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term2 {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("finish_date")
    @Expose
    public String finishDate;
    @SerializedName("term_id")
    @Expose
    public String termId;
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("start_date")
    @Expose
    public String startDate;

}
