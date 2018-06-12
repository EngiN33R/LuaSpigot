package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.annotation.DynamicFieldDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldAccessorDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldMutatorDefinition;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
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
public class LuaEntity extends WrapperType<Entity> {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaEntity(Entity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return "entity";
    }

    @Override
    public String toLuaString() {
        return "entity: " + getHandle().getName() + " ("
                + getHandle().getUniqueId().toString() + ")";
    }

    @DynamicFieldDefinition("value")
    public LuaValue getEName() {
        return LuaValue.valueOf(getHandle().getName());
    }

    @MethodDefinition("teleport")
    public Varargs teleport(Varargs args) {
        if (args.narg() == 1) {
            LuaTable tbl = args.checktable(1);
            if (tbl.get("type").tojstring().equals("entity")) {
                getHandle().teleport(((LuaEntity) tbl).getHandle());
            } else if (tbl.get("type").tojstring().equals("location")) {
                getHandle().teleport(((LuaLocation) tbl).getHandle());
            }
        } else if (args.narg() < 3) {
            error("not enough arguments");
            return NIL;
        } else {
            World w = getHandle().getWorld();
            String wname = args.optjstring(4, "");
            if (!wname.equals("")) {
                w = Bukkit.getWorld(wname);
            }
            getHandle().teleport(new Location(w, args.checkdouble(1),
                    args.checkdouble(2), args.checkdouble(3)));
        }
        return NIL;
    }

    @LinkedFieldMutatorDefinition("location")
    public void setLocation(LuaLocation location) {
        getHandle().teleport(location.getHandle());
    }

    @LinkedFieldAccessorDefinition("location")
    public LuaLocation getLocation() {
        return new LuaLocation(getHandle().getLocation());
    }

    @LinkedFieldMutatorDefinition("velocity")
    public void setVelocity(LuaVector velocity) {
        getHandle().setVelocity(velocity.getHandle());
    }

    @LinkedFieldAccessorDefinition("velocity")
    public LuaVector getVelocity() {
        return new LuaVector(getHandle().getVelocity());
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
