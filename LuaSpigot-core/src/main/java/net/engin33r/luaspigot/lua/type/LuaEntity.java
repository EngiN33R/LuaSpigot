package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TypeValidator;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
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

        registerLinkedField("location", new LocationField());
        registerLinkedField("velocity", new VelocityField());
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

    @DynFieldDef("name")
    public LuaValue getEName() {
        return LuaValue.valueOf(getHandle().getName());
    }

    @MethodDef("teleport")
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

    private class LocationField extends LinkedField<LuaEntity> {
        @Override
        public void update(LuaValue val) {
            TypeValidator.validate(val.checktable(), "location");
            getHandle().teleport(((LuaLocation) val.checktable()).getHandle());
        }

        @Override
        public LuaValue query() {
            return new LuaLocation(getHandle().getLocation());
        }
    }

    private class VelocityField extends LinkedField<LuaEntity> {
        @Override
        public void update(LuaValue val) {
            LuaVector vec;
            LuaTable tbl = val.checktable();
            if (tbl.get("type").optjstring("").equals("vector"))
                vec = (LuaVector) tbl;
            else
                vec = new LuaVector(tbl.get(1).checkdouble(),
                        tbl.get(2).checkdouble(), tbl.get(3).checkdouble());
            getHandle().setVelocity(vec.getVector());
        }

        @Override
        public LuaValue query() {
            return new LuaVector(getHandle().getVelocity());
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
