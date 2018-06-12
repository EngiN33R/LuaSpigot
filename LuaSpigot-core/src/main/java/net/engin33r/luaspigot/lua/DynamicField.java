package net.engin33r.luaspigot.lua;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.luaj.vm2.LuaValue;

import java.util.function.Supplier;

/**
 * Abstract class defining a dynamic field - technically a table field in Lua
 * that updates every time it's queried for.
 */
@RequiredArgsConstructor
@Getter
@Setter
public class DynamicField<T extends WeakType> {
    private final T handle;
    private Accessor<T, ? extends LuaValue> accessor;

    public LuaValue access() {
        return getAccessor().access(getHandle());
    }
}
