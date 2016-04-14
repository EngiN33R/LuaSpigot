package net.engin33r.luaspigot.lua;

import lombok.RequiredArgsConstructor;
import org.luaj.vm2.LuaValue;

/**
 * Abstract class defining a dynamic field - technically a table field in Lua
 * that updates every time it's queried for.
 */
@RequiredArgsConstructor
public abstract class DynamicField<T extends WeakType> {
    public final T self;

    public abstract String getName();

    public abstract LuaValue update();
}
