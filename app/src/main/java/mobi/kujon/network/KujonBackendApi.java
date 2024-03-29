package mobi.kujon.network;

import java.util.List;
import java.util.SortedMap;

import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.network.json.Message;
import mobi.kujon.network.json.Preferences;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.ProgrammeSingle;
import mobi.kujon.network.json.Term2;
import mobi.kujon.network.json.TermGrades;
import mobi.kujon.network.json.Thesis;
import mobi.kujon.network.json.User;
import mobi.kujon.network.json.Usos;
import mobi.kujon.network.json.gen.CoursersSearchResult;
import mobi.kujon.network.json.gen.FacultiesSearchResult;
import mobi.kujon.network.json.gen.Faculty2;
import mobi.kujon.network.json.gen.ProgrammeSearchResult;
import mobi.kujon.network.json.gen.StudentSearchResult;
import mobi.kujon.network.json.gen.ThesesSearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KujonBackendApi {

    String X_KUJONREFRESH_TRUE = "X-Kujonrefresh: true";
    String X_KUJONREFRESH = "X-Kujonrefresh";

    @GET("usoses") Call<KujonResponse<List<Usos>>> usoses();

    @GET("usosesall") Call<KujonResponse<List<Usos>>> usosesAll();

    @GET("config") Call<KujonResponse<Config>> config();

    @GET("usersinfoall") Call<KujonResponse<User>> users();

    @GET("usersinfoall") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<User>> usersRefresh();

    @GET("users/{userId}") Call<KujonResponse<User>> users(@Path("userId") String userId);

    @GET("users/{userId}") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<User>> usersRefresh(@Path("userId") String userId);

    @GET("grades") Call<KujonResponse<List<Grade>>> grades();

    @GET("gradesbyterm") Call<KujonResponse<List<TermGrades>>> gradesByTerm();

    @GET("gradesbyterm") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<TermGrades>>> gradesByTermRefresh();

    @GET("terms") Call<KujonResponse<List<Term2>>> terms();

    @GET("terms") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<Term2>>> termsRefresh();

    @GET("terms/{termId}") Call<KujonResponse<List<Term2>>> terms(@Path("termId") String termId);

    @GET("programmes") Call<KujonResponse<List<Programme>>> programmes();

    @GET("programmes/{id}") Call<KujonResponse<List<ProgrammeSingle>>> programmes(@Path("id") String id,@Header(X_KUJONREFRESH) String refresh);

    @GET("faculties") Call<KujonResponse<List<Faculty2>>> faculties();

    @GET("faculties") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<Faculty2>>> facultiesRefresh();

    @GET("theses") Call<KujonResponse<List<Thesis>>> theses();

    @GET("theses") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<Thesis>>> thesesRefresh();

    @GET("faculties/{facultyId}") Call<KujonResponse<Faculty2>> faculty(@Path("facultyId") String facultyId);

    @GET("lecturers") Call<KujonResponse<List<Lecturer>>> lecturers();

    @GET("lecturers") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<Lecturer>>> lecturersRefresh();

    @GET("lecturers/{lecturerId}") Call<KujonResponse<LecturerLong>> lecturer(@Path("lecturerId") String lecturerId);

    @GET("lecturers/{lecturerId}") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<LecturerLong>> lecturerRefresh(@Path("lecturerId") String lecturerId);

    @GET("courseseditions") Call<KujonResponse<List<Course>>> coursesEditions();

    @GET("courseseditionsbyterm") Call<KujonResponse<List<SortedMap<String, List<Course>>>>> coursesEditionsByTerm();

    @GET("courseseditionsbyterm") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<SortedMap<String, List<Course>>>>> coursesEditionsByTermRefresh();

    @GET("courseseditions/{courseId}/{termId}")
    Call<KujonResponse<CourseDetails>> courseDetails(@Path("courseId") String courseId, @Path("termId") String termId);

    @GET("courses/{courseId}")
    Call<KujonResponse<CourseDetails>> courseDetails(@Path("courseId") String courseId);

    @GET("courseseditions/{courseId}/{termId}")
    @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<CourseDetails>> courseDetailsRefresh(@Path("courseId") String courseId, @Path("termId") String termId);

    @GET("courses/{courseId}")
    @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<CourseDetails>> courseDetailsRefresh(@Path("courseId") String courseId);

    @GET("ttusers/{day}?lecturers_info=False") Call<KujonResponse<List<CalendarEvent>>> plan(@Path("day") String day);

    @GET("ttusers/{day}?lecturers_info=False") @Headers(X_KUJONREFRESH_TRUE) Call<KujonResponse<List<CalendarEvent>>> planRefresh(@Path("day") String day);

    @GET("ttlecturers/{lecturerId}/{day}") Call<KujonResponse<List<CalendarEvent>>> lecturerPlan(@Header(X_KUJONREFRESH) String refresh, @Path("lecturerId") String lecturerId, @Path("day") String day);

    @POST("authentication/archive") Call<Object> deleteAccount();

    @GET("search/users/{query}")
    Call<KujonResponse<StudentSearchResult>> searchStudent(@Path(value = "query", encoded = true) String query, @Query("start") Integer start);

    @GET("search/faculties/{query}")
    Call<KujonResponse<FacultiesSearchResult>> searchFaculty(@Path(value = "query", encoded = true) String query, @Query("start") Integer start);

    @GET("search/programmes/{query}")
    Call<KujonResponse<ProgrammeSearchResult>> searchProgrammes(@Path(value = "query", encoded = true) String query, @Query("start") Integer start);

    @GET("search/courses/{query}")
    Call<KujonResponse<CoursersSearchResult>> searchCourses(@Path(value = "query", encoded = true) String query, @Query("start") Integer start);

    @GET("search/theses/{query}")
    Call<KujonResponse<ThesesSearchResult>> searchTheses(@Path(value = "query", encoded = true) String query, @Query("start") Integer start);

    @GET("settings")
    Call<KujonResponse<Preferences>> getUserPreferences();

    @POST("settings/{setting}/{action}")
    Call<KujonResponse<String>> setSetting(@Path(value = "setting") String setting, @Path(value = "action") String action);

    @GET("messages")
    Call<KujonResponse<List<Message>>> getMessages();

    @GET("messages") @Headers(X_KUJONREFRESH_TRUE)
    Call<KujonResponse<List<Message>>> getMessagesRefresh();

    @POST("authentication/logout")
    Call<KujonResponse<String>> logout();
}
