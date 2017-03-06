package mobi.kujon.network.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.network.json.gen.Faculty2;


public class User {

    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("photo_url")
    @Expose
    public String photoUrl;
    @SerializedName("last_name")
    @Expose
    public String last_name;
    @SerializedName("student_status")
    @Expose
    public String student_status;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("usos_name")
    @Expose
    public String usos_name;
    @SerializedName("student_programmes")
    @Expose
    public List<StudentProgramme> student_programmes = new ArrayList<StudentProgramme>();
    @SerializedName("user_type")
    @Expose
    public String user_type;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("usos_id")
    @Expose
    public String usos_id;
    @SerializedName("first_name")
    @Expose
    public String first_name;
    @SerializedName("user_created")
    @Expose
    public String user_created;
    @SerializedName("student_number")
    @Expose
    public String student_number;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("user_email")
    @Expose
    public Object user_email;
    @SerializedName("theses")
    @Expose
    public List<Thesis> theses;
    @SerializedName("terms")
    @Expose
    public List<Term2> terms;
    @SerializedName("programmes")
    @Expose
    public List<Programme> programmes;
    @SerializedName("faculties")
    @Expose
    public List<Faculty2> faculties;

    @SerializedName("avr_grades") @Expose public String avrGrades;
}
