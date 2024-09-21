package com.teamdimensional.preordain.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.teamdimensional.preordain.core.function.PreordainFunction;

public interface IPreordainFunctionFactory {

    PreordainFunction deserialize(JsonElement element);

    default JsonElement serialize(PreordainFunction object) {
        return JsonNull.INSTANCE;
    }

}
