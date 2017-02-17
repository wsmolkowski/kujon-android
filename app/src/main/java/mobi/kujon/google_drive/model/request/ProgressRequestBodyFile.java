package mobi.kujon.google_drive.model.request;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 *
 */

public class ProgressRequestBodyFile extends RequestBody {


    private final RequestBody requestBody;

    private final ProgressRequestBody.UploadCallbacks progressListener;


    public ProgressRequestBodyFile(RequestBody requestBody, final ProgressRequestBody.UploadCallbacks listener) {
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

    private int lastValue = 0;

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        final long totalBytes = contentLength();
        BufferedSink progressSink = Okio.buffer(new ForwardingSink(sink) {
            private long bytesWritten = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                bytesWritten += byteCount;
                int newValue = Math.round(100 * bytesWritten / totalBytes);
                if (newValue > lastValue && newValue < 100) {
                    progressListener.onProgressUpdate(newValue);
                    lastValue = newValue;
                }
                super.write(source, byteCount);
            }
        });
        requestBody.writeTo(progressSink);
        progressSink.flush();
    }


}