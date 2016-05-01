package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing an Entity.
 */
public class LuaEntity extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Entity entity;

    public LuaEntity(Entity entity) {
        this.entity = entity;

        registerLinkedField("location", new LocationField());
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

    public Entity getEntity() {
        return this.entity;
    }

    @DynFieldDef(name = "name")
    public LuaValue getEName() {
        return LuaValue.valueOf(this.entity.getName());
    }

    @MethodDef(name = "teleport")
    public Varargs teleport(Varargs args) {
        if (args.narg() == 1) {
            LuaTable tbl = args.checktable(1);
            if (tbl.get("type").tojstring().equals("entity")) {
                entity.teleport(((LuaEntity) tbl).getEntity());
            } else if (tbl.get("type").tojstring().equals("location")) {
                entity.teleport(((LuaLocation) tbl).getLocation());
            }
        } else if (args.narg() < 3) {
            error("not enough arguments");
            return NIL;
        } else {
            World w = entity.getWorld();
            String wname = args.optjstring(4, "");
            if (!wname.equals("")) {
                w = Bukkit.getWorld(wname);
            }
            entity.teleport(new Location(w, args.checkdouble(1),
                    args.checkdouble(2), args.checkdouble(3)));
        }
        return NIL;
    }

    private class LocationField extends LinkedField<LuaEntity> {
        @Override
        public void update(LuaValue val) {
            entity.teleport(((LuaLocation) val.checktable()).getLocation());
        }

        @Override
        public LuaValue query() {
            return new LuaLocation(entity.getLocation());
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
