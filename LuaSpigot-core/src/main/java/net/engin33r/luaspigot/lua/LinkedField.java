package net.engin33r.luaspigot.lua;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

/**
 * Abstract class defining a linked field - technically a table field in Lua
 * that updates every time it's queried for and performs an action whenever
 * it is updated.
 */
@RequiredArgsConstructor
@Getter
@Setter
public class LinkedField<T extends WeakType> {
    private final T handle;
    private Accessor<T, LuaValue> accessor;
    private Mutator<T, LuaValue> mutator;

    public void mutate(LuaValue arg) {
        if (getMutator() != null) {
            getMutator().mutate(getHandle(), arg);
        }
        throw new LuaError("may not mutate immutable field");
    }

    public LuaValue access() {
        if (getAccessor() != null) return getAccessor().access(getHandle());
        return LuaValue.NIL;
    }
}
