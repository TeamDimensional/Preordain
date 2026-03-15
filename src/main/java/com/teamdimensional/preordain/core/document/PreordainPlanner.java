package com.teamdimensional.preordain.core.document;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;

public class PreordainPlanner {

    Multimap<Long, PreordainFunction> functions = HashMultimap.create();

    public void register(PreordainFunction function, long delay) {
        functions.put(delay, function);
    }

    public boolean tick(WorldPonder world, long ticks) {
        boolean requiresUpdate = false;
        for (PreordainFunction f : functions.get(ticks)) {
            Preordain.LOGGER.info("Applying function: " + f);
            f.apply(world);
            requiresUpdate = requiresUpdate || f.requiresMeshUpdate();
        }
        return requiresUpdate;
    }

}
