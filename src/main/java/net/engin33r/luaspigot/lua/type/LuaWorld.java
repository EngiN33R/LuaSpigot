package net.engin33r.luaspigot.lua.type;

import lombok.RequiredArgsConstructor;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import org.bukkit.World;
import org.luaj.vm2.LuaValue;

/**
 * Wrapper type describing a Minecraft world.
 */
@RequiredArgsConstructor
public class LuaWorld extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final World world;

    @Override
    public String getName() {
        return "world";
    }

    @Override
    public String toLuaString() {
        return "world: "+ world.getName()+" ("+ world.getUID().toString()+")";
    }

    public World getWorld() {
        return this.world;
    }

    @DynFieldDef(name = "name")
    public LuaValue getWName() {
        return LuaValue.valueOf(world.getName());
    }

    @DynFieldDef(name = "uuid")
    public LuaValue getUUID() {
        return new LuaUUID(world.getUID());
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
