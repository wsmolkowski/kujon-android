package mobi.kujon.google_drive.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.kujon.google_drive.dagger.injectors.Injector;

/**
 *
 */

public abstract class BaseFileFragment<T> extends Fragment {


    private Injector<T> filesListFragmentInjector;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injectYourself(filesListFragmentInjector);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract void injectYourself(Injector<T> injector);


    protected abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            filesListFragmentInjector = ((ProvideInjector<T>)this.getActivity()).provideInjector();
        }catch (ClassCastException e){
            throw new RuntimeException("Activity does not implements Correct ProvideInjector");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            filesListFragmentInjector = ((ProvideInjector<T>)this.getActivity()).provideInjector();
        }catch (ClassCastException e){
            throw new RuntimeException("Activity does not implements Correct ProvideInjector");
        }
    }
}
