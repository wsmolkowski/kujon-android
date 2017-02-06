package mobi.kujon.google_drive.mvp.google_drive_api;


public class GoogleDownloadProviderImpl implements GoogleDowloadProvider {

    private static final String VND_GOOGLE_APPS = "vnd.google-apps";
    private GoogleDriveDowloadMVP.ModelGoogleFiles modelGoogleFiles;
    private GoogleDriveDowloadMVP.ModelOtherFiles modelOtherFiles;

    public GoogleDownloadProviderImpl(GoogleDriveDowloadMVP.ModelGoogleFiles modelGoogleFiles,
                                      GoogleDriveDowloadMVP.ModelOtherFiles modelOtherFiles) {
        this.modelGoogleFiles = modelGoogleFiles;
        this.modelOtherFiles = modelOtherFiles;
    }

    @Override
    public GoogleDriveDowloadMVP.Model getModel(String mimeType) {
        return mimeType.contains(VND_GOOGLE_APPS) ? this.modelGoogleFiles : this.modelOtherFiles;
    }
}
