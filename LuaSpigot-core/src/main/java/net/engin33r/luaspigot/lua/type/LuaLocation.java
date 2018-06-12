package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import org.bukkit.Location;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing a location in a world.
 */
public class LuaLocation extends WrapperType<Location> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaLocation(Location loc) {
        super(loc);

        registerLinkedField("world", new WorldField(this));
        registerLinkedField("x", new XField(this));
        registerLinkedField("y", new YField(this));
        registerLinkedField("z", new ZField(this));
        registerLinkedField("pitch", new PitchField(this));
        registerLinkedField("yaw", new YawField(this));
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String toLuaString() {
        Location loc = getHandle();
        return "location: [" + loc.getX() + "," + loc.getY() + ","
                + loc.getZ() + "]@" + loc.getWorld().getName() + "@("
                + loc.getPitch() + "," + loc.getYaw() + ")";
    }

    @MethodDefinition("getBlock")
    public Varargs getBlock(Varargs args) {
        return new LuaBlock(getHandle());
    }

    @MethodDefinition("distance")
    public Varargs distance(Varargs args) {
        TypeUtils.validate(args.checktable(1), "location");
        Location loc2 = ((LuaLocation) args.checktable(1)).getHandle();
        return LuaNumber.valueOf(getHandle().distance(loc2));
    }

    private class WorldField extends LinkedField<LuaLocation> {
        public WorldField(LuaLocation self) {
            super(self);
        }

        @Override
        public void update(LuaValue val) {
            TypeUtils.validate(val.checktable(), "world");
            getHandle().setWorld(((LuaWorld) val.checktable()).getHandle());
        }

        @Override
        public LuaValue query() {
            return new LuaWorld(getHandle().getWorld());
        }
    }

    private class XField extends LinkedField<LuaLocation> {
        public XField(LuaLocation self) {
            super(self);
        }

        @Override
        public void update(LuaValue val) {
            getHandle().setX(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getX());
        }
    }

    private class YField extends LinkedField<LuaLocation> {
        public YField(LuaLocation self) {
            super(self);
        }

        @Override
        public void update(LuaValue val) {
            getHandle().setY(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getY());
        }
    }

    private class ZField extends LinkedField<LuaLocation> {
        public ZField(LuaLocation self) {
            super(self);
        }

        @Override
        public void update(LuaValue val) {
            getHandle().setZ(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getZ());
        }
    }

    private class PitchField extends LinkedField<LuaLocation> {
        public PitchField(LuaLocation self) {
            super(self);
        }

        @Override
        public void update(LuaValue val) {
            getHandle().setPitch((float) val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getPitch());
        }
    }

    private class YawField extends LinkedField<LuaLocation> {
        public YawField(LuaLocation self) {
            super(self);
        }

        @Override
        public void update(LuaValue val) {
            getHandle().setYaw((float) val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getYaw());
        }
    }

    @Override
    public String getName() {
        return "location";
    }
}
