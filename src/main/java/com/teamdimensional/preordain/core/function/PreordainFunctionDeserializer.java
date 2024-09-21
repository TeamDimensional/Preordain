package com.teamdimensional.preordain.core.function;

import com.google.gson.*;

import java.lang.reflect.Type;

public class PreordainFunctionDeserializer implements JsonDeserializer<PreordainFunction> {
    @Override
    public PreordainFunction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String functionName = jsonObject.get("name").getAsString();
        JsonElement data = jsonObject.get("data");
        return PreordainFunctionRegistry.create(functionName, data);
    }
}
