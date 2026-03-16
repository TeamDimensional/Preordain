package com.teamdimensional.preordain.core.function;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.document.DocumentLoader;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.Map;

public class PreordainFunctionRegistry {
    public static final Map<String, Class<? extends PreordainFunction>> factories = new Object2ObjectOpenHashMap<>();

    public static void register(String name, Class<? extends PreordainFunction> clazz) {
        if (factories.containsKey(name)) {
            Preordain.LOGGER.error("Attempted redeclaration of the factory {}!", name);
            Preordain.LOGGER.error("Traceback: {}", Arrays.toString(Thread.currentThread().getStackTrace()).replace(',', '\n'));
        }
        factories.put(name, clazz);
    }

    public static PreordainFunction create(String key, JsonElement data) throws JsonParseException {
        Class<? extends PreordainFunction> clazz = factories.get(key);
        if (clazz == null) {
            throw new IllegalArgumentException("Preordain function '" + key + "' not found");
        }
        return DocumentLoader.gson.fromJson(data, clazz);
    }
}
