package mobi.kujon.utils.plan_fragment;


import java.util.List;

import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;

public class AskForLecturerDataOnPlan extends AskForDataOnPlan {

    private String lecturerId;

    public AskForLecturerDataOnPlan(ActivityChange activityChange, KujonUtils utils, KujonBackendApi backendApi, String lecturerId) {
        super(activityChange, utils, backendApi);
        this.lecturerId = lecturerId;
    }

    @Override
    protected Call<KujonResponse<List<CalendarEvent>>> getKujonResponseCall(String restSuffix) {
        return backendApi.lecturerPlan(String.valueOf(refresh), lecturerId, restSuffix);
    }
}
