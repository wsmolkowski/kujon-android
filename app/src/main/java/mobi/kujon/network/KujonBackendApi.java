package mobi.kujon.network;

import java.util.List;

import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.Event;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
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

    @GET("grades") Call<KujonResponse<List<Grade>>> grades();

    @GET("courseseditions") Call<KujonResponse<List<Course>>> coursesEditions();

    @GET("courseseditions/{courseId}") Call<KujonResponse<CourseDetails>> courseDetails(@Path("courseId") String courseId);

    @GET("tt/2016-04-10") Call<KujonResponse<List<Event>>> plan();
}
