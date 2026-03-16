package com.teamdimensional.preordain.library.function;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.document.DocumentChecker;
import com.teamdimensional.preordain.core.document.PreordainDocument;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;

public class FunctionSubdocument extends PreordainFunction {

    String document;

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
    
}
