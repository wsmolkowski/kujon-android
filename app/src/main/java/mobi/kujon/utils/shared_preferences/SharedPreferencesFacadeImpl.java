package mobi.kujon.utils.shared_preferences;

import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 *
 */
public class SharedPreferencesFacadeImpl implements SharedPreferencesFacade {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public SharedPreferencesFacadeImpl(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void putString(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public void removeKey(String key) {
        editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    @Override
    public String retriveString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    @Override
    public boolean retrieveBoolean(String key) {
        return  preferences.getBoolean(key, false);
    }

    @Override
    public Boolean contaisKey(String key) {
        return preferences.contains(key);
    }

    @Override
    public void putObject(String key, Object any) {
        Gson gson = new Gson();
        putString(key, gson.toJson(any));
    }

    @Override
    public <T> T getObject(String key, Class<T> object) {
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString(key, ""), object);
    }
}
