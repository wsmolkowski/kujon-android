package mobi.kujon.google_drive.mvp.choose_students;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.network.unwrapped_api.CourseDetailsApi;
import mobi.kujon.network.json.Participant;
import rx.Observable;

public class ChooseStudentsModel implements ChooseStudentsMVP.Model {

    private CourseDetailsApi courseDetailsApi;

    @Inject
    public ChooseStudentsModel(CourseDetailsApi courseDetailsApi) {
        this.courseDetailsApi = courseDetailsApi;
    }

    @Override
    public Observable<List<StudentShareDto>> provideListOfStudents(String courseId, String termId, boolean refresh) {
        return courseDetailsApi.getCourseDetails(refresh, courseId, termId)
                .map(courseDetails -> getStudentShareDtos(courseDetails.participants));
    }

    private List<StudentShareDto> getStudentShareDtos(List<Participant> participants) {
        List<StudentShareDto> studentShareDtos = new ArrayList<>(participants.size());
        for(Participant participant : participants) {
            studentShareDtos.add(new StudentShareDto(participant, false));
        }
        return studentShareDtos;
    }
}
