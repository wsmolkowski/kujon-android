package mobi.kujon.network.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public class GradeClassType {
    public String name;

    public GradeClassType(String name) {
        this.name = name;
    }

    public static class GradeClassTypeDeserializer implements JsonDeserializer<GradeClassType> {

        @Override public GradeClassType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonPrimitive) {
                return new GradeClassType(json.getAsString());
            } else if (json instanceof JsonObject) {
                return new GradeClassType(((JsonObject) json).get("pl").getAsString());
            } else {
                return new GradeClassType("");
            }
        }
    }
}
