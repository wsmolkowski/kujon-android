package mobi.kujon.utils.predicates;

import com.github.underscore.Predicate;

import mobi.kujon.network.json.CourseGrades;

/**
 *
 */

public class CourseGradesPredicate implements Predicate<CourseGrades> {
    private String query;

    public CourseGradesPredicate(String query) {
        this.query = query.toLowerCase();
    }


    @Override
    public Boolean apply(CourseGrades arg) {
        return arg.courseName.toLowerCase().contains(query);
    }
}
