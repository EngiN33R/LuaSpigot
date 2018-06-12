package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.Arrays;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Utility class for creating Varargs from arrays.
 */
public class VarargBuilder {
    public static Varargs build(LuaValue... values) {
        return new Varargs() {
            @Override
            public LuaValue arg(int i) {
                return values.length < i ? NIL : values[i-1];
            }

            @Override
            public int narg() {
                return values.length;
            }

            @Override
            public LuaValue arg1() {
                return arg(0);
            }

            @Override
            public Varargs subargs(int start) {
                return VarargBuilder.build(Arrays
                        .copyOfRange(values, start, values.length-1));
            }
        };
    }
}
