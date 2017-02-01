package mobi.kujon.google_drive.model.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private byte[] bytes;
    private String mPath;
    private UploadCallbacks mListener;
    private MediaType mediaType;
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);
    }

    public ProgressRequestBody(DataForFileUpload file, final  UploadCallbacks listener) {
        bytes = file.getBytes();
        mListener = listener;
        this.mediaType = file.getMediaType();
    }

    @Override
    public MediaType contentType() {
        return this.mediaType;
    }

    @Override
    public long contentLength() throws IOException {
      return bytes.length;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = bytes.length;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        InputStream in = new ByteArrayInputStream(bytes);
        long uploaded = 0;

        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                uploaded += read;
                sink.write(buffer, 0, read);
                mListener.onProgressUpdate((int)(100 * uploaded / fileLength));
            }
        } finally {
            in.close();
        }
    }
}