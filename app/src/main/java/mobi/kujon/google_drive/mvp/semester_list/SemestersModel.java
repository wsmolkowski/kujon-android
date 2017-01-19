package mobi.kujon.google_drive.mvp.semester_list;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.network.json.Term2;
import rx.Observable;

public class SemestersModel implements SemestersMVP.Model {

    private SemesterApi semesterApi;

    public SemestersModel(SemesterApi semesterApi) {
        this.semesterApi = semesterApi;
    }

    @Override
    public Observable<List<SemesterDTO>> getListOfSemesters(boolean refresh) {
        return semesterApi.getSemesters(refresh).map(this::convertTerms2Semesters);
    }

    @NonNull
    private List<SemesterDTO> convertTerms2Semesters(List<Term2> term2s) {
        List<SemesterDTO> semesters = new ArrayList<>();
        for(Term2 term : term2s) {
            semesters.add(new SemesterDTO(term));
        }
        return semesters;
    }
}
