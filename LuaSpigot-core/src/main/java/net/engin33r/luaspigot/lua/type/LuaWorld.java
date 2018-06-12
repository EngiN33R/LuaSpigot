package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.DynamicFieldDefinition;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import org.bukkit.World;
import org.luaj.vm2.LuaValue;

/**
 * Wrapper type describing a Minecraft world.
 */
public class LuaWorld extends WrapperType<World> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaWorld(World w) {
        super(w);
    }

    @Override
    public String getName() {
        return "world";
    }

    @Override
    public String toLuaString() {
        World w = getHandle();
        return "world: " + w.getName() + " (" + w.getUID().toString() + ")";
    }

    @DynamicFieldDefinition("value")
    public LuaValue getWName() {
        return LuaValue.valueOf(getHandle().getName());
    }

    @DynamicFieldDefinition("uuid")
    public LuaValue getUUID() {
        return new LuaUUID(getHandle().getUID());
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
