package mobi.kujon.network.json;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("API_URL") public String apiUrl;
    @SerializedName("USER_LOGGED") public boolean userLogged;
    @SerializedName("USOS_PAIRED") public boolean usosPaired;

    @Override public String toString() {
        return "Config{" +
                "apiUrl='" + apiUrl + '\'' +
                ", userLogged=" + userLogged +
                ", usosPaired=" + usosPaired +
                '}';
    }
}
