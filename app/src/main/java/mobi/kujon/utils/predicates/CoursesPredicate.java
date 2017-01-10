package mobi.kujon.utils.predicates;


import com.github.underscore.Predicate;

import mobi.kujon.network.json.Course;

/**
 *
 */

public class CoursesPredicate implements Predicate<Course> {
    private String query;

    public CoursesPredicate(String query) {
        this.query = query.toLowerCase();
    }

    @Override
    public Boolean apply(Course arg) {
        return arg.courseName.toLowerCase().startsWith(query);
    }
}
