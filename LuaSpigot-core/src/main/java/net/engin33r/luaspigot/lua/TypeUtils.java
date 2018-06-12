package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.InvocationTargetException;

/**
 * Utility class for validating arguments against {@link WeakType} types.
 */
public class TypeUtils {
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
    @SuppressWarnings("unchecked")
    public static <V extends LuaValue, T extends IWeakType> T checkOf(
            V val, Class<? extends T> cls) {
        if (cls.isInstance(val)) {
            return (T) val;
        } else {
            throw new LuaError("expected " + cls.getCanonicalName()
                    + ", got " + val.getClass().getCanonicalName());
        }
    }

    public static <V extends LuaValue, W, T extends WrapperType<W>> W handleOf(
            V val, Class<T> cls) {
        return checkOf(val, cls).getHandle();
    }

    public static boolean isTableOf(LuaTable tbl, String type) {
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

    public static boolean strongIsTableOf(LuaTable tbl, int type) {
        for (int i = 1; i <= tbl.length(); i++) {
            if (tbl.get(i).type() != type) {
                return false;
            }
        }
        return true;
    }

    public static <E extends Enum> LuaString checkEnum(E enumValue) {
        return LuaString.valueOf(enumValue.name());
    }

    public static <E extends Enum> E getEnum(LuaValue str,
                                             Class<E> enumClass) {
        String value = str.tojstring().toUpperCase();
        try {
            return enumClass.cast(enumClass.getMethod("valueOf", String.class)
                    .invoke(null, value));
        } catch (NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException e) {
            throw new LuaError("cannot restore enum of class "
                    + enumClass.getName() + " from string " + value);
        }
    }
}
