package mobi.kujon.google_drive.ui.fragments;

import mobi.kujon.google_drive.dagger.injectors.Injector;

/**
 *
 */

public interface ProvideInjector<T> {

    Injector<T> provideInjector();
}
