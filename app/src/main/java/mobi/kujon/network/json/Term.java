
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {

    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("finish_date")
    @Expose
    public String finishDate;

}
