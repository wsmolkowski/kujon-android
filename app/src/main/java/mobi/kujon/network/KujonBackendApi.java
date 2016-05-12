package mobi.kujon.network;

import java.util.List;
import java.util.SortedMap;

import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.Faculty2;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.Term2;
import mobi.kujon.network.json.User;
import mobi.kujon.network.json.Usos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KujonBackendApi {

    @GET("usoses") Call<KujonResponse<List<Usos>>> usoses();

    @GET("config") Call<KujonResponse<Config>> config();

    @POST("authentication/register") Call<KujonResponse> register();

    @GET("users") Call<KujonResponse<User>> users();

    @GET("users/{userId}") Call<KujonResponse<User>> users(@Path("userId") String userId);

    @GET("grades") Call<KujonResponse<List<Grade>>> grades();

    @GET("gradesbyterm") Call<KujonResponse<SortedMap<String, List<Grade>>>> gradesByTerm();

    @GET("terms") Call<KujonResponse<List<Term2>>> terms();

    @GET("terms/{termId}") Call<KujonResponse<Term2>> terms(@Path("termId") String termId);

    @GET("programmes") Call<KujonResponse<List<Programme>>> programmes();

    @GET("faculties") Call<KujonResponse<List<Faculty2>>> faculties();

    @GET("faculties/{facultyId}") Call<KujonResponse<Faculty2>> faculty(@Path("facultyId") String facultyId);

    @GET("lecturers") Call<KujonResponse<List<Lecturer>>> lecturers();

    @GET("lecturers/{lecturerId}") Call<KujonResponse<LecturerLong>> lecturer(@Path("lecturerId") String lecturerId);

    @GET("courseseditions") Call<KujonResponse<List<Course>>> coursesEditions();

    @GET("courseseditionsbyterm") Call<KujonResponse<List<SortedMap<String, List<Course>>>>> coursesEditionsByTerm();

    @GET("courseseditions/{courseId}/{termId}")
    Call<KujonResponse<CourseDetails>> courseDetails(@Path("courseId") String courseId, @Path("termId") String termId);

    @GET("tt/{day}") Call<KujonResponse<List<CalendarEvent>>> plan(@Path("day") String day);

    @GET("authentication/archive") Call<Object> deleteAccount();
}
