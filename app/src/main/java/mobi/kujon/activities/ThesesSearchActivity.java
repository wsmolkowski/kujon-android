package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Item____;
import mobi.kujon.network.json.gen.ThesesSearchResult;
import retrofit2.Call;

public class ThesesSearchActivity extends AbstractSearchActivity<ThesesSearchResult, Item____> {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
    }

    protected boolean getNextPage(ThesesSearchResult data) {
        return data.next_page;
    }

    protected List<Item____> getItems(ThesesSearchResult data) {
        return data.items;
    }

    @Override protected Call<KujonResponse<ThesesSearchResult>> getKujonResponseCall() {
        return kujonBackendApi.searchTheses(query, start);
    }

    protected String getMatch(Item____ item) {
        return item.match;
    }

    protected void handeClick(Item____ item) {
        ThesesActivity.openWithThese(this,item.thesis);
    }

    @Override protected boolean isClickable() {
        return false;
    }

    public static void start(Activity from, String query) {
        Intent intent = new Intent(from, ThesesSearchActivity.class);
        intent.putExtra(QUERY, query);
        from.startActivity(intent);
    }
}
