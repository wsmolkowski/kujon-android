package mobi.kujon.network.json;

import com.google.gson.annotations.SerializedName;

public class Usos {
    public String name;
    public String url;
    public @SerializedName("consumer_secret") String consumerSecret;
    public String contact;
    public @SerializedName("usos_id") String usosId;
    public String logo;
    public @SerializedName("consumer_key") String consumerKey;
    public boolean enabled;
    public String comment;

    @Override public String toString() {
        return "Usos{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                ", contact='" + contact + '\'' +
                ", usosId='" + usosId + '\'' +
                ", logo='" + logo + '\'' +
                ", consumerKey='" + consumerKey + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
