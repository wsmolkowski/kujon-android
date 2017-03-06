package mobi.kujon.utils.statuses;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */

@StringDef({ThesisType.DOCTORAL, ThesisType.MASTER,ThesisType.LIC,ThesisType.ENGINEER,ThesisType.POSTGRADUATE})
@Retention(RetentionPolicy.SOURCE)
public @interface ThesisType {
    String DOCTORAL  = "doctoral";
    String MASTER = "master";
    String LIC = "licentiate";
    String ENGINEER  = "engineer";
    String POSTGRADUATE = "postgraduate";

}
//doctoral - praca doktorska,
//        master - praca magisterska,
//        licentiate - praca licencjacka,
//        engineer - praca inżynierska
//        postgraduate - praca podyplomowa