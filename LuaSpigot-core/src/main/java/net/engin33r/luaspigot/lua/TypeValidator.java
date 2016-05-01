package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * Utility class for validating arguments against {@link WeakType} types.
 */
public class TypeValidator {
    public static void validate(LuaTable val, String type) {
        LuaValue ttype = val.get("type");
        if (ttype == LuaValue.NIL) {
            LuaValue.error(type + " expected, got table");
        } else {
            String vtype = ttype.tojstring();
            if (!vtype.equals(type))
                LuaValue.error(type + " expected, got " + vtype);
        }
    }
}
