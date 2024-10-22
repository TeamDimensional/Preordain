package com.teamdimensional.preordain.renderer;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.document.PreordainDocument;
import org.apache.commons.lang3.NotImplementedException;

public class PreordainRenderingManager {
    public static void showDocument(PreordainDocument doc) {
        Preordain.LOGGER.info("Showing document: {}", doc.getKey());
        throw new NotImplementedException("Preordains are NYI");
    }
}
