package mobi.kujon.utils.user_data;


import mobi.kujon.utils.shared_preferences.SharedPreferencesFacade;

public class UserDataFacedImpl implements UserDataFacade {

    private final static String USER_ID = "USER_ID";
    private String userId;
    private SharedPreferencesFacade sharedPreferencesFacade;

    public UserDataFacedImpl(SharedPreferencesFacade sharedPreferencesFacade) {
        this.sharedPreferencesFacade = sharedPreferencesFacade;
    }

    @Override
    public void saveUserId(String id) {
        this.userId = id;
        sharedPreferencesFacade.putString(USER_ID, userId);
    }

    @Override
    public String getUserId() {
        return this.userId != null ? this.userId : sharedPreferencesFacade.getObject(USER_ID, String.class);
    }
}
