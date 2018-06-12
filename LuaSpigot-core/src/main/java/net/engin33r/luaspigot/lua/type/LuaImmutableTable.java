package net.engin33r.luaspigot.lua.type;

import org.luaj.vm2.*;

public class LuaImmutableTable extends LuaTable {
    public boolean getLocked() {
        LuaTable mt = this.getmetatable().checktable();
        return mt.get("__immutable").optboolean(false);
    }

    public void lock() {
        LuaTable mt = this.getmetatable().checktable();
        mt.set("__immutable", LuaBoolean.TRUE);
    }

    public void unlock() {
        LuaTable mt = this.getmetatable().checktable();
        mt.set("__immutable", LuaBoolean.FALSE);
    }

    @Override
    public void set(int i, LuaValue value) {
        if (getLocked()) {
            throw new LuaError("changing values of an immutable table is forbidden");
        } else {
            super.set(i, value);
        }
    }

    @Override
    public void set(LuaValue key, LuaValue value) {
        if (getLocked()) {
            throw new LuaError("changing values of an immutable table is forbidden");
        } else {
            super.set(key, value);
        }
    }

    @Override
    public void rawset(int i, LuaValue value) {
        if (getLocked()) {
            throw new LuaError("changing values of an immutable table is forbidden");
        } else {
            super.rawset(i, value);
        }
    }

    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (getLocked()) {
            throw new LuaError("changing values of an immutable table is forbidden");
        } else {
            super.rawset(key, value);
        }
    }

    @Override
    public void insert(int i, LuaValue value) {
        if (getLocked()) {
            throw new LuaError("changing values of an immutable table is forbidden");
        } else {
            super.insert(i, value);
        }
    }
}
