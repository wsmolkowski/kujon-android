package mobi.kujon.google_drive.mvp.choose_students;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.network.unwrapped_api.CourseDetailsApi;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.Participant;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChooseStudentsModelTest extends UnitTest {

    public static final String FIRST_NAME = "Bill";
    public static final String LAS_NAME = "Robin";
    public static final String ID = "id";
    public static final String USER_ID = "uid";
    public static String NAME;

    @Mock
    CourseDetailsApi courseDetailsApi;

    private ChooseStudentsMVP.Model model;

    @Override
    protected void onSetup() {
        model = new ChooseStudentsModel(courseDetailsApi);
    }

    @Test
    public void testGetStudentsShared() {
        Mockito.when(courseDetailsApi.getCourseDetails(Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Observable.just(provideCourseDetails()));
        model.provideListOfStudents("any", "any", true)
                .subscribe(studentShareDtos -> {
                    assertEquals(studentShareDtos.size(), 1);
                    assertEquals(studentShareDtos.get(0).isChoosen(), false);
                    assertEquals(studentShareDtos.get(0).getStudentId(), USER_ID);
                    assertEquals(studentShareDtos.get(0).getStudentName(), NAME);
                }, throwable -> fail());
    }

    public static CourseDetails provideCourseDetails() {
        CourseDetails courseDetails = new CourseDetails();
        courseDetails.participants = new ArrayList<>();
        Participant participant = new Participant();
        participant.firstName = FIRST_NAME;
        participant.lastName = LAS_NAME;
        participant.id = ID;
        participant.userId = USER_ID;
        courseDetails.participants.add(participant);
        NAME = participant.getName();
        return courseDetails;
    }
}