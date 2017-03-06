package mobi.kujon.network;

import javax.inject.Inject;

import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;


public class SettingsApi {

    private KujonBackendApi kujonBackendApi;
    private static final String GOOGLE_CALENDAR = "googlecalendar";
    private static final String EVENT = "event";
    private static final String EVENT_FILE = "eventfiles";

    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";

    @Inject
    public SettingsApi(KujonBackendApi kujonBackendApi) {
        this.kujonBackendApi = kujonBackendApi;
    }

    public Call<KujonResponse<String>> setGoogleCalendar(boolean state) {
        return getKujonResponseCall(state, GOOGLE_CALENDAR);
    }

    public Call<KujonResponse<String>> setEvents(boolean state) {
        return getKujonResponseCall(state, EVENT);
    }

    public Call<KujonResponse<String>> setEventsFiles(boolean state) {
        return getKujonResponseCall(state, EVENT_FILE);
    }

    private Call<KujonResponse<String>> getKujonResponseCall(boolean state, String eventFile) {
        if (state) {
            return kujonBackendApi.setSetting(eventFile, ENABLE);
        } else {
            return kujonBackendApi.setSetting(eventFile, DISABLE);
        }
    }
}
