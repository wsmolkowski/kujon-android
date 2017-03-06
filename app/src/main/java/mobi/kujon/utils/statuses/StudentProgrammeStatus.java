package mobi.kujon.utils.statuses;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@StringDef({StudentProgrammeStatus.ACTIVE,StudentProgrammeStatus.CANCELLED,StudentProgrammeStatus.GRADUATE_DIPLOMA,StudentProgrammeStatus.GRADUATE_BEFORE_DIPLOMA,StudentProgrammeStatus.GRADUATE_END_OF_STUDY})
@Retention(RetentionPolicy.SOURCE)
public @interface StudentProgrammeStatus {
    String CANCELLED = "cancelled";
    String ACTIVE  =  "active";
    String GRADUATE_END_OF_STUDY = "graduated_end_of_study";
    String GRADUATE_BEFORE_DIPLOMA = "graduated_before_diploma";
    String GRADUATE_DIPLOMA = "graduated_diploma";

}
