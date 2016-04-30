package net.engin33r.luaspigot.citizens.lua;

import net.citizensnpcs.api.trait.Trait;
import net.engin33r.luaspigot.lua.WeakType;
import org.luaj.vm2.LuaValue;

public class LuaNPCTrait extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Trait trait;

    // Lombok doesn't work here and I don't know why
    public LuaNPCTrait(Trait trait) {
        this.trait = trait;
    }

    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    public String getName() {
        return "npctrait";
    }

    @Override
    public String toLuaString() {
        return "npctrait: "+trait.getName();
    }
}
