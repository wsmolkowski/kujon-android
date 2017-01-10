package mobi.kujon.utils.predicates;


import com.github.underscore.Predicate;

import mobi.kujon.network.json.Lecturer;

/**
 *
 */

public class LecutrerPredicate implements Predicate<Lecturer> {
    private String query;

    public LecutrerPredicate(String query) {
        this.query = query.toLowerCase();
    }

    @Override
    public Boolean apply(Lecturer arg) {
        if (query.length() == 0) return true;
        return (arg.firstName.toLowerCase().startsWith(query) || arg.lastName.toLowerCase().startsWith(query) || (arg.lastName.toLowerCase() + " " + arg.firstName.toLowerCase()).startsWith(query));

    }
}
