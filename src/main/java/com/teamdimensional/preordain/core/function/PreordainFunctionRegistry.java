package com.teamdimensional.preordain.core.function;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.api.IPreordainFunctionFactory;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.Map;

public class PreordainFunctionRegistry {
    public static final Map<String, IPreordainFunctionFactory> factories = new Object2ObjectOpenHashMap<>();

    public static void register(String name, IPreordainFunctionFactory factory) {
        if (factories.containsKey(name)) {
            Preordain.LOGGER.error("Attempted redeclaration of the factory {}!", name);
            Preordain.LOGGER.error("Traceback: {}", Arrays.toString(Thread.currentThread().getStackTrace()).replace(',', '\n'));
        }
        factories.put(name, factory);
    }

    public static PreordainFunction create(String key, JsonElement data) throws JsonParseException {
        IPreordainFunctionFactory factory = factories.get(key);
        if (factory == null) {
            throw new IllegalArgumentException("Preordain function '" + key + "' not found");
        }
        return factory.deserialize(data);
    }
}
