package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Faculty2 {

    @SerializedName("fac_id")
    @Expose
    public String facId;
    @SerializedName("homepage_url")
    @Expose
    public String homepageUrl;
    @SerializedName("logo_urls")
    @Expose
    public LogoUrls logoUrls;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("phone_numbers")
    @Expose
    public List<String> phoneNumbers = new ArrayList<String>();
    @SerializedName("postal_address")
    @Expose
    public String postalAddress;
    @SerializedName("usos_id")
    @Expose
    public String usosId;
    @SerializedName("stats")
    @Expose
    public FacultyStats stats;

}