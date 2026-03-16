package com.teamdimensional.preordain.core.function;

import com.teamdimensional.preordain.core.document.DocumentChecker;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;

public abstract class PreordainFunction {

    protected long delay = 0;

    public abstract void apply(WorldPonder world);

    public long getDelay() {
        return delay;
    }

    public boolean requiresMeshUpdate() {
        return true;
    }

    public void check(DocumentChecker checker) throws DocumentChecker.DocumentCheckingException {
        // Override to implement a check
    }

}
