package mobi.kujon;


import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

public class CommonExceptionCreator {
    public static HttpException provideHTTPException(String text) {
        return new HttpException(Response.error(404, ResponseBody.create(MediaType.parse("JSON"), text)));
    }

    public static NullPointerException provideNullPointer(String text) {
        return new NullPointerException(text);
    }
}
