package net.engin33r.luaspigot.citizens.lua;

import net.citizensnpcs.api.trait.Trait;
import net.engin33r.luaspigot.lua.WrapperType;
import org.luaj.vm2.LuaValue;

public class LuaNPCTrait extends WrapperType<Trait> {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaNPCTrait(Trait trait) {
        super(trait);
    }

    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    public String getName() {
        return "npctrait";
    }

    @Override
    public String toLuaString() {
        return "npctrait: "+getHandle().getName();
    }
}
