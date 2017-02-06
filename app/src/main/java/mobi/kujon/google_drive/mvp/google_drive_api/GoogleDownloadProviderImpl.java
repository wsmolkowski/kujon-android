package mobi.kujon.google_drive.mvp.google_drive_api;


import javax.inject.Inject;

public class GoogleDownloadProviderImpl implements GoogleDowloadProvider {

    private GoogleDriveDowloadMVP.ModelGoogleFiles modelGoogleFiles;
    private GoogleDriveDowloadMVP.ModelOtherFiles modelOtherFiles;

    @Inject
    public GoogleDownloadProviderImpl(GoogleDriveDowloadMVP.ModelGoogleFiles modelGoogleFiles,
                                      GoogleDriveDowloadMVP.ModelOtherFiles modelOtherFiles) {
        this.modelGoogleFiles = modelGoogleFiles;
        this.modelOtherFiles = modelOtherFiles;
    }

    @Override
    public GoogleDriveDowloadMVP.Model getModel(String mimeType) {
        return mimeType.contains("vnd.google-apps") ? this.modelGoogleFiles : this.modelOtherFiles;
    }
}
