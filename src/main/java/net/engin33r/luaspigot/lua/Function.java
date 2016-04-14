package net.engin33r.luaspigot.lua;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Abstract class representing a Lua function.
 */
public abstract class Function implements IFunction {
    private final VarArgFunction function;

    public Function() {
        this.function = new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                return Function.this.call(varargs);
            }
        };
    }

    public VarArgFunction getFunction() {
        return function;
    }
}
