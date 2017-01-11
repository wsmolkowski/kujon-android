package mobi.kujon.utils.plan_fragment;


import java.util.List;

import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;

public class AskForStudentDataOnPlan extends AskForDataOnPlan {

    public AskForStudentDataOnPlan(ActivityChange activityChange, KujonUtils utils, KujonBackendApi backendApi) {
        super(activityChange, utils, backendApi);
    }

    @Override
    protected Call<KujonResponse<List<CalendarEvent>>> getKujonResponseCall(String restSuffix) {
            return refresh ? backendApi.planRefresh(restSuffix) : backendApi.plan(restSuffix);
    }
}
