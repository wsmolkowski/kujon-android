package mobi.kujon.network;

import javax.inject.Inject;

import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;


public class SettingsApi {

    private KujonBackendApi kujonBackendApi;
    private static final String GOOGLE_CALENDAR = "googlecalendar";
    private static final String EVENT = "event";

    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";

    @Inject
    public SettingsApi(KujonBackendApi kujonBackendApi) {
        this.kujonBackendApi = kujonBackendApi;
    }

    public Call<KujonResponse<String>> setGoogleCalendar(boolean state) {
        if(state) {
            return kujonBackendApi.setSetting(GOOGLE_CALENDAR, ENABLE);
        } else {
            return kujonBackendApi.setSetting(GOOGLE_CALENDAR, DISABLE);
        }
    }

    public Call<KujonResponse<String>> setEvents(boolean state) {
        if(state) {
            return kujonBackendApi.setSetting(EVENT, ENABLE);
        } else {
            return kujonBackendApi.setSetting(EVENT, DISABLE);
        }
    }
}
