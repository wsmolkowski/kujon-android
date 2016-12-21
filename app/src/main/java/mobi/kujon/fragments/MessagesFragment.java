package mobi.kujon.fragments;


import mobi.kujon.R;

public class MessagesFragment extends ListFragment {


    @Override
    protected String getRequestUrl() {
        return null;
    }

    @Override
    protected void loadData(boolean refresh) {

    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setToolbarTitle(R.string.messages);
    }
}
