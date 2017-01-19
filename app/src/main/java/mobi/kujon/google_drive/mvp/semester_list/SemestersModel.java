package mobi.kujon.google_drive.mvp.semester_list;


import java.util.List;

import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import rx.Observable;

public class SemestersModel implements SemestersMVP.Model {

    private SemesterApi semesterApi;


    @Override
    public Observable<List<SemesterDTO>> getListOfSemesters(boolean refresh) {
        return semesterApi.getListOfSubjcetsInSemester(refresh)
                .flatMapIterable()
    }
}
