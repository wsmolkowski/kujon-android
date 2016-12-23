package mobi.kujon.utils.shared_preferences;

/**
 *
 */
public interface SharedPreferencesFacade {

    void putString(String key, String value);
    void putBoolean(String key, boolean value);

    void removeKey(String key);

    String retriveString(String key, String defaultValue);
    boolean retrieveBoolean(String key);
    Boolean contaisKey(String key);

    void putObject(String key, Object any);

    <T> T getObject(String key, Class<T> object);
}
