package mobi.kujon.google_drive.model.json;

import java.util.List;
import java.util.TreeMap;

import mobi.kujon.network.json.Course;

/**
 *
 */

public class CourseWithTerms extends TreeMap<String,List<Course>> {


    public String getTermId(){
        return firstKey();
    }

    public List<Course> getCourses(){
        return this.get(firstKey());
    }
}
