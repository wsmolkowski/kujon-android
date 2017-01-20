package mobi.kujon.google_drive.ui.activities.courses.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;

/**
 *
 */


@ActivityScope
@Component(modules = CoursesInSemesterModule.class,dependencies = FilesComponent.class)
public interface RuntimeCourseInSemesterActivityComponent extends CoursesInSemesterActivityComponent {
}
