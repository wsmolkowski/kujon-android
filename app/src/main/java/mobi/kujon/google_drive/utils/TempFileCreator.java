package mobi.kujon.google_drive.utils;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import mobi.kujon.google_drive.dagger.scopes.GoogleDriveScope;
import okhttp3.ResponseBody;

/**
 *
 */
@GoogleDriveScope
public class TempFileCreator {

    private Application kujonApplication;

    @Inject
    public TempFileCreator(Application kujonApplication) {
        this.kujonApplication = kujonApplication;
    }


    public File writeToTempFile(ResponseBody body, UpdateListener updateListener) {
        File outputDir = kujonApplication.getCacheDir(); // context being the Activity pointer
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int lastProcent = 0;
        try {
            File outputFile = File.createTempFile(randomIdentifier(), null, outputDir);

            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(outputFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;
                int percent = (int) (100 * fileSizeDownloaded / fileSize);
                if (percent > lastProcent) {
                    updateListener.onUpdate(percent);
                    lastProcent = percent;
                }

            }

            outputStream.flush();
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File writeToTempFile(InputStream inputStream, String fileName) {
        File outputDir = kujonApplication.getCacheDir(); // context being the Activity pointer
        OutputStream outputStream = null;
        try {
            File outputFile = new File(outputDir, fileName);

            byte[] fileReader = new byte[4096];


            outputStream = new FileOutputStream(outputFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }

            outputStream.flush();
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File writeToDowload(ResponseBody body, String fileName, UpdateListener updateListener) {
        File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int lastProcent = 0;
        try {
            File outputFile = new File(outputDir, fileName);

            byte[] fileReader = new byte[4096];

            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(outputFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;
                int percent = (int) (100 * fileSizeDownloaded / fileSize);
                if (percent > lastProcent && percent < 100) {
                    updateListener.onUpdate(percent);
                    lastProcent = percent;
                }

            }

            outputStream.flush();
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    private final java.util.Random rand = new java.util.Random();


    private String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(10) + 5;
            for (int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
        }
        return builder.toString();
    }
}
