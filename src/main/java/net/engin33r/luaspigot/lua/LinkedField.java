package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaValue;

/**
 * Abstract class defining a linked field - technically a table field in Lua
 * that updates every time it's queried for and performs an action whenever
 * it is updated.
 */
public abstract class LinkedField<T extends WeakType> extends DynamicField {
    public LinkedField(WeakType self) { super(self); }

    public abstract void update(LuaValue val);

    public abstract LuaValue query();
}
