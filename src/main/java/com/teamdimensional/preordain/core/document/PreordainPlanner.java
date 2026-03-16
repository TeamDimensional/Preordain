package com.teamdimensional.preordain.core.document;

import java.util.PriorityQueue;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;

public class PreordainPlanner {

    /**
     * Note: this class implements a natural ordering that is inconsistent with equals().
     */
    static class FunctionWithDelay implements Comparable<FunctionWithDelay> {
        long delay;
        PreordainFunction function;

        FunctionWithDelay(long delay, PreordainFunction function) {
            this.delay = delay;
            this.function = function;
        }

        @Override
        public int compareTo(FunctionWithDelay o) {
            if (delay < o.delay) return -1;
            if (delay == o.delay) return 0;
            return 1;
        }
    }

    final PriorityQueue<FunctionWithDelay> functions = new PriorityQueue<>();

    public void register(PreordainFunction function, long delay) {
        functions.add(new FunctionWithDelay(delay, function));
    }

    public boolean tick(WorldPonder world, long ticks) {
        boolean requiresUpdate = false;
        FunctionWithDelay func;
        while ((func = functions.peek()) != null && func.delay <= ticks) {
            Preordain.LOGGER.debug("Applying function: " + func);
            // We need to be careful here - applying the function can create a new function, so we need to remove it first.
            functions.poll();
            func.function.apply(world);
            requiresUpdate = requiresUpdate || func.function.requiresMeshUpdate();
        }
        return requiresUpdate;
    }

}
