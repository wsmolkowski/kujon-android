
package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FacId {

    @SerializedName("logo_urls")
    @Expose
    public LogoUrls logoUrls;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("postal_address")
    @Expose
    public String postalAddress;
    @SerializedName("fac_id")
    @Expose
    public String facId;
    @SerializedName("homepage_url")
    @Expose
    public String homepageUrl;
    @SerializedName("phone_numbers")
    @Expose
    public List<String> phoneNumbers = new ArrayList<String>();

}
