package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import org.bukkit.entity.Entity;
import org.luaj.vm2.LuaValue;

/**
 * Wrapper type describing an Entity.
 */
public class LuaEntity extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Entity entity;

    public LuaEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getName() {
        return "entity";
    }

    @Override
    public String toLuaString() {
        return "entity: " + entity.getName() + " (" + entity.getUniqueId()
                .toString() + ")";
    }

    @DynFieldDef(name = "name")
    public LuaValue getEName() {
        return LuaValue.valueOf(this.entity.getName());
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
