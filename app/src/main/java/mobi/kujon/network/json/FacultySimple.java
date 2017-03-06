package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class FacultySimple {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof FacultySimple)) return false;

        FacultySimple that = (FacultySimple) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FacultySimple{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}