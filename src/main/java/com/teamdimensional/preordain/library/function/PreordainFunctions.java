package com.teamdimensional.preordain.library.function;

import com.teamdimensional.preordain.core.function.PreordainFunctionRegistry;

public class PreordainFunctions {

    public static void init() {
        PreordainFunctionRegistry.register("preordain:block", FunctionBlock.class);
        PreordainFunctionRegistry.register("preordain:fill", FunctionFill.class);
        PreordainFunctionRegistry.register("preordain:checkerboard", FunctionCheckerboard.class);
        PreordainFunctionRegistry.register("preordain:subdocument", FunctionSubdocument.class);
    }

}
