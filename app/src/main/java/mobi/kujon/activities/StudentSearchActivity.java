package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.gen.Item___;
import mobi.kujon.network.json.gen.StudentSearchResult;
import retrofit2.Call;

import static mobi.kujon.utils.CommonUtils.stringEquals;

public class StudentSearchActivity extends AbstractSearchActivity<StudentSearchResult, Item___> {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);
    }

    protected boolean getNextPage(StudentSearchResult data) {
        return data.next_page;
    }

    protected List<Item___> getItems(StudentSearchResult data) {
        return data.items;
    }

    @Override protected Call<KujonResponse<StudentSearchResult>> getKujonResponseCall() {
        return kujonBackendApi.searchStudent(query, start);
    }

    protected String getMatch(Item___ item) {
        return item.match;
    }

    protected void handeClick(Item___ item) {
        if (stringEquals("Pracownik", item.user.staff_status) || stringEquals("Nauczyciel akademicki", item.user.staff_status)) {
            LecturerDetailsActivity.showLecturerDatails(this, item.user.id);
        } else if (stringEquals("aktywny student", item.user.student_status)) {
            StudentDetailsActivity.showStudentDetails(this, item.user.id);
        } else {
            StudentDetailsActivity.showStudentDetails(this, item.user.id);
        }
    }

    public static void start(Activity from, String query) {
        Intent intent = new Intent(from, StudentSearchActivity.class);
        intent.putExtra(QUERY, query);
        from.startActivity(intent);
    }
}
