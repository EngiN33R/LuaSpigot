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
    public static void validateOf(LuaTable tbl, String type) {
        for (int i = 1; i <= tbl.length(); i++) {
            LuaTable val = tbl.get(i).checktable(i);
            LuaValue ttype = val.get("type");
            if (ttype == LuaValue.NIL) {
                LuaValue.error("table of " + type + " expected, got errant" +
                        " table");
            } else {
                String vtype = ttype.tojstring();
                if (!vtype.equals(type))
                    LuaValue.error("table of " + type + " expected, got" +
                            " errant " + vtype);
            }
        }
    }

    public static boolean is(LuaValue val, String type) {
        return val.type() == LuaValue.TTABLE &&
                val.get("type").tojstring().equals(type);
    }

    // Anton Pavlovich #checkOf
    public static boolean checkOf(LuaTable tbl, String type) {
        for (int i = 1; i <= tbl.length(); i++) {
            LuaTable val = tbl.get(i).checktable(i);
            LuaValue ttype = val.get("type");
            if (ttype == LuaValue.NIL) {
                return false;
            } else {
                String vtype = ttype.tojstring();
                if (!vtype.equals(type))
                    return false;
            }
        }
        return true;
    }

    public static boolean strongCheckOf(LuaTable tbl, int type) {
        for (int i = 1; i <= tbl.length(); i++) {
            if (tbl.get(i).type() != type) {
                return false;
            }
        }
        return true;
    }
}
