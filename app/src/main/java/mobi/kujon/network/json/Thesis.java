package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class Thesis {

    @SerializedName("authors")
    @Expose
    public List<SimpleUserJson> authors = new ArrayList<>();
    @SerializedName("faculty")
    @Expose
    public FacultySimple faculty;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("supervisors")
    @Expose
    public List<SimpleUserJson> supervisors = new ArrayList<>();
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("type")
    @Expose
    public String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thesis)) return false;

        Thesis thesis = (Thesis) o;

        if (authors != null ? !authors.equals(thesis.authors) : thesis.authors != null)
            return false;
        if (faculty != null ? !faculty.equals(thesis.faculty) : thesis.faculty != null)
            return false;
        if (id != null ? !id.equals(thesis.id) : thesis.id != null) return false;
        if (supervisors != null ? !supervisors.equals(thesis.supervisors) : thesis.supervisors != null)
            return false;
        if (title != null ? !title.equals(thesis.title) : thesis.title != null) return false;
        return type != null ? type.equals(thesis.type) : thesis.type == null;

    }

    @Override
    public int hashCode() {
        int result = authors != null ? authors.hashCode() : 0;
        result = 31 * result + (faculty != null ? faculty.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (supervisors != null ? supervisors.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Thesis{" +
                "authors=" + authors +
                ", faculty=" + faculty +
                ", id='" + id + '\'' +
                ", supervisors=" + supervisors +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}