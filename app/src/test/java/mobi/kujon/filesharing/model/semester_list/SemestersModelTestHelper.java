package mobi.kujon.filesharing.model.semester_list;


import java.util.Collections;
import java.util.List;

import mobi.kujon.network.json.Term2;

public class SemestersModelTestHelper {

    public static final String TERM_NAME = "SEMESTR1";
    public static final String TERM_ID = "1";

    public static List<Term2> mockTerms() {
        Term2 term = new Term2();
        term.name = TERM_NAME;
        term.termId = TERM_ID;
        return Collections.singletonList(term);
    }
}
