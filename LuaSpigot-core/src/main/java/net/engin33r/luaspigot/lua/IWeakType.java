package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaValue;

/**
 * Interface defining mandatory methods for Lua weak types to override.
 */
public interface IWeakType {
    String getName();
    default LuaValue index(LuaValue key) {
        return null;
    }
}
