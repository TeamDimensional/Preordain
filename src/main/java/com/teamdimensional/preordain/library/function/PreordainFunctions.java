package com.teamdimensional.preordain.library.function;

import com.teamdimensional.preordain.core.function.PreordainFunctionRegistry;

public class PreordainFunctions {

    public static void init() {
        PreordainFunctionRegistry.register("preordain:block", FunctionBlock::create);
        PreordainFunctionRegistry.register("preordain:fill", FunctionFill::create);
        PreordainFunctionRegistry.register("preordain:checkerboard", FunctionCheckerboard::create);
        PreordainFunctionRegistry.register("preordain:subdocument", FunctionSubdocument::create);
    }

}
