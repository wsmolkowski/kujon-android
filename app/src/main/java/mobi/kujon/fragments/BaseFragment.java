package mobi.kujon.fragments;

import android.os.Handler;
import android.support.v4.app.Fragment;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

public abstract class BaseFragment extends Fragment {

    protected Handler handler = new Handler();

    @Override public void onStart() {
        super.onStart();
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(this.getClass().getSimpleName()));
    }
}
