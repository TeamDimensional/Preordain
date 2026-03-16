package com.teamdimensional.preordain.library.function;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.document.DocumentChecker;
import com.teamdimensional.preordain.core.document.PreordainDocument;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;

public class FunctionSubdocument extends PreordainFunction {

    String document;

    public FunctionSubdocument(String doc) {
        document = doc;
    }

    @Override
    public void apply(WorldPonder world) {
        PreordainDocument doc = Preordain.loader.getDocument(document);
        if (doc == null) {
            Preordain.LOGGER.error("Document {} does not exist! This shouldn't happen.");
            return;
        }
        doc.initialize(world.planner, delay);
    }

    public void check(DocumentChecker checker) throws DocumentChecker.DocumentCheckingException {
        checker.check(document);
    }

    public static FunctionSubdocument create(JsonElement e) throws JsonParseException {
        if (!e.isJsonPrimitive()) throw new JsonParseException("Expected a primitive");
        try {
            return new FunctionSubdocument(e.getAsString());
        } catch (ClassCastException exc) {
            throw new JsonParseException("Expected a string", exc);
        }
    }
    
}
