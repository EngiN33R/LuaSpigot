package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaValue;

/**
 * Abstract class representing a strong Lua type for interfacing various Spigot
 * objects with scripts via a wholly new data type.
 */
public abstract class StrongType extends LuaValue implements IStrongType {
    @Override
    public int type() {
        return LuaValue.TNONE; // Hacky representation as "unknown type"
    }

    @Override
    public String typename() {
        return this.getName();
    }
}
