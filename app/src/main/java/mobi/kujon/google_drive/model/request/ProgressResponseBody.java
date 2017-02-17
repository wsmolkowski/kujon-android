package mobi.kujon.google_drive.model.request;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 *
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final  ProgressRequestBody.UploadCallbacks  progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, final ProgressRequestBody.UploadCallbacks listener) {
        this.responseBody = responseBody;
        this.progressListener = listener;
    }

    @Override public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override public long contentLength() {
        return responseBody.contentLength();
    }

    @Override public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }
    private int lastValue = 0;

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long bytesWritten = 0L;
            final long totalBytes = contentLength();
            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                bytesWritten += bytesRead != -1 ? bytesRead : 0;

                int newValue = Math.round(100 * bytesWritten / totalBytes);
                if (newValue > lastValue && newValue < 100) {
                    progressListener.onProgressUpdate(newValue);
                    lastValue = newValue;
                }
                return bytesRead;
            }
        };
    }
}
