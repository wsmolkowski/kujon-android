package mobi.kujon.google_drive.mvp.google_drive_api;

/**
 *
 */

public interface GoogleDowloadProvider {

    GoogleDriveDowloadMVP.Model getModel(String mimeType);

}
