package mobi.kujon.google_drive.model.request;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 *
 */

public class ProgressRequestBodyFile extends RequestBody {

    public interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

    private final RequestBody requestBody;

    private final ProgressRequestBody.UploadCallbacks progressListener;

    private BufferedSink bufferedSink;

    public ProgressRequestBodyFile(RequestBody requestBody,  final ProgressRequestBody.UploadCallbacks listener) {
        this.requestBody = requestBody;
        this.progressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }

        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();

    }
    private int lastValue = 0;

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {

            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                int newValue =Math.round(100 * bytesWritten / contentLength);
                if(newValue>lastValue){
                    progressListener.onProgressUpdate(newValue);
                    lastValue =newValue;
                }
            }
        };
    }
}