package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.FacultiesSearchResult;
import mobi.kujon.network.json.gen.Item_;
import retrofit2.Call;

public class FacultySearchActivity extends AbstractSearchActivity<FacultiesSearchResult, Item_> {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
    }

    @Override protected boolean getNextPage(FacultiesSearchResult data) {
        return data.next_page;
    }

    protected List<Item_> getItems(FacultiesSearchResult data) {
        return data.items;
    }

    @Override protected Call<KujonResponse<FacultiesSearchResult>> getKujonResponseCall() {
        return kujonBackendApi.searchFaculty(query, start);
    }

    protected String getMatch(Item_ item) {
        return item.match;
    }

    protected void handeClick(Item_ item) {
        FacultyDetailsActivity.showFacultyDetails(this, item.id);
    }

    public static void start(Activity from, String query) {
        Intent intent = new Intent(from, FacultySearchActivity.class);
        intent.putExtra(QUERY, query);
        from.startActivity(intent);
    }
}
