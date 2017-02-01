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
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.network.KujonException;
import mobi.kujon.utils.ErrorHandlerUtil;

/**
 *
 */

public abstract class BaseFileFragment<T> extends Fragment implements HandleException {


    private Injector<T> injector;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(injector == null){
            setUpInjector();
        }
        injectYourself(injector);
        return createView(inflater, container, savedInstanceState);
    }

    protected abstract void injectYourself(Injector<T> injector);


    protected abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setUpInjector();
    }

    private void setUpInjector() {
        try {
            injector = ((ProvideInjector<T>)this.getActivity()).provideInjector();
        }catch (ClassCastException e){
            throw new RuntimeException("Activity does not implements Correct ProvideInjector");
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setUpInjector();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        injector = null;
    }


    @Override
    public void handleException(Throwable throwable) {
        this.setProgress(false);
        if(throwable instanceof KujonException){
            ErrorHandlerUtil.handleKujonError((KujonException)throwable);
        }else {
            ErrorHandlerUtil.handleError(throwable);
        }
    }

    protected abstract void setProgress(boolean b);
}
