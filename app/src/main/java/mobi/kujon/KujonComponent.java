package mobi.kujon;

import javax.inject.Singleton;

import dagger.Component;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.CongratulationsActivity;
import mobi.kujon.activities.CourseDetailsActivity;
import mobi.kujon.activities.FacultyDetailsActivity;
import mobi.kujon.activities.ImageActivity;
import mobi.kujon.activities.LecturerDetailsActivity;
import mobi.kujon.activities.LoginActivity;
import mobi.kujon.activities.MainActivity;
import mobi.kujon.activities.PreferencesActivity;
import mobi.kujon.activities.StudentDetailsActivity;
import mobi.kujon.activities.StudentSearchActivity;
import mobi.kujon.activities.UsosesActivity;
import mobi.kujon.activities.UsoswebLoginActivity;
import mobi.kujon.activities.WebViewAcitivty;
import mobi.kujon.fragments.CoursesFragment;
import mobi.kujon.fragments.GradesFragment;
import mobi.kujon.fragments.LecturersFragment;
import mobi.kujon.fragments.ListFragment;
import mobi.kujon.fragments.PlanFragment;
import mobi.kujon.fragments.PlanListFragment;
import mobi.kujon.fragments.SearchFragment;
import mobi.kujon.fragments.StudentInfoFragment;
import mobi.kujon.fragments.TermsFragment;
import mobi.kujon.fragments.UserInfoFragment;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.PlanEventsDownloader;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface KujonComponent {
    void inject(MainActivity activity);

    void inject(BaseActivity activity);

    void inject(StudentSearchActivity activity);

    void inject(CongratulationsActivity activity);

    void inject(CourseDetailsActivity activity);

    void inject(FacultyDetailsActivity activity);

    void inject(ImageActivity activity);

    void inject(LecturerDetailsActivity activity);

    void inject(LoginActivity activity);

    void inject(PreferencesActivity activity);

    void inject(StudentDetailsActivity activity);

    void inject(UsosesActivity activity);

    void inject(UsoswebLoginActivity activity);

    void inject(WebViewAcitivty activity);

    void inject(CoursesFragment fragment);

    void inject(GradesFragment fragment);

    void inject(LecturersFragment fragment);

    void inject(ListFragment fragment);

    void inject(PlanFragment fragment);

    void inject(PlanListFragment fragment);

    void inject(TermsFragment fragment);

    void inject(UserInfoFragment fragment);

    void inject(KujonUtils kujonUtils);

    void inject(PlanEventsDownloader kujonUtils);

    void inject(KujonApplication kujonApplication);

    void inject(StudentInfoFragment studentInfoFragment);

    void inject(SearchFragment searchFragment);
}
