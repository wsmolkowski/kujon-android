package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Item__;
import mobi.kujon.network.json.gen.Programme;
import mobi.kujon.network.json.gen.ProgrammeSearchResult;
import retrofit2.Call;

import static android.text.TextUtils.isEmpty;

public class ProgrammeSearchActivity extends AbstractSearchActivity<ProgrammeSearchResult, Item__> {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
    }

    protected boolean getNextPage(ProgrammeSearchResult data) {
        return data.next_page;
    }

    protected List<Item__> getItems(ProgrammeSearchResult data) {
        return data.items;
    }

    @Override protected Call<KujonResponse<ProgrammeSearchResult>> getKujonResponseCall() {
        return kujonBackendApi.searchProgrammes(query, start);
    }

    protected String getMatch(Item__ item) {
        return item.match;
    }

    protected void handeClick(Item__ item) {
        Programme programme = item.programme;
        String name = !isEmpty(programme.name) ? programme.name.split(",")[0] : "";
        ProgrammeDetailsActivity.showProgrammeDetails(this, programme, name);
    }

    public static void start(Activity from, String query) {
        Intent intent = new Intent(from, ProgrammeSearchActivity.class);
        intent.putExtra(QUERY, query);
        from.startActivity(intent);
    }
}
