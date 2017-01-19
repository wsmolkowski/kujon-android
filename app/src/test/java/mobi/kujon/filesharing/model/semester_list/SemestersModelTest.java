package mobi.kujon.filesharing.model.semester_list;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.mvp.semester_list.SemestersModel;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.network.json.Term2;
import rx.Observable;

import static org.junit.Assert.assertEquals;

public class SemestersModelTest extends UnitTest {
    @Mock
    SemesterApi semesterApi;

    private SemestersModel semestersModel;

    @Override
    protected void onSetup() {
        semestersModel = new SemestersModel(semesterApi);
    }

    @Test
    public void testGetSemesters() {
        List<Term2> mockTerms = SemestersModelTestHelper.mockTerms();
        Mockito.when(semesterApi.getSemesters(Mockito.anyBoolean())).thenReturn(Observable.just(mockTerms));
        semestersModel.getListOfSemesters(false).subscribe(semesterDTOs -> {
            assertEquals(semesterDTOs.size(), 1);
            assertEquals(semesterDTOs.get(0), new SemesterDTO(mockTerms.get(0)));
        });
    }

}
