package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.CoursersSearchResult;
import mobi.kujon.network.json.gen.Item;
import retrofit2.Call;

public class CoursesSearchActivity extends AbstractSearchActivity<CoursersSearchResult, Item> {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
    }

    @Override protected boolean getNextPage(CoursersSearchResult data) {
        return data.next_page;
    }

    protected List<Item> getItems(CoursersSearchResult data) {
        return data.items;
    }

    @Override protected Call<KujonResponse<CoursersSearchResult>> getKujonResponseCall() {
        return kujonBackendApi.searchCourses(query, start);
    }

    protected String getMatch(Item item) {
        return item.match;
    }

    protected void handeClick(Item item) {
        CourseDetailsActivity.showCourseDetails(this, item.course_id, null);
    }

    public static void start(Activity from, String query) {
        Intent intent = new Intent(from, CoursesSearchActivity.class);
        intent.putExtra(QUERY, query);
        from.startActivity(intent);
    }
}
