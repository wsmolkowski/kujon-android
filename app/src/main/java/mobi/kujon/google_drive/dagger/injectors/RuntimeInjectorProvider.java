package mobi.kujon.google_drive.dagger.injectors;

/**
 *
 */

public class RuntimeInjectorProvider implements InjectorProvider {
    @Override
    public SemesterActivityInjector provideInjector() {
        return new SemesterActivityInjector();
    }
}
