package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Location;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing a location in a world.
 */
public class LuaLocation extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Location loc;
    private LuaWorld worldCache;

    public LuaLocation(Location loc) {
        this.loc = loc;
        this.worldCache = new LuaWorld(loc.getWorld());

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
        return "location: ["+loc.getX()+","+loc.getY()+","+loc.getZ()+"]@" +
                loc.getWorld().getName()+"@("+loc.getPitch()+"," +
                loc.getYaw()+")";
    }

    @MethodDef(name = "getBlock")
    public Varargs getBlock(Varargs args) {
        return new LuaBlock(this.loc);
    }

    @MethodDef(name = "distance")
    public Varargs distance(Varargs args) {
        Location loc2 = ((LuaLocation) args.checktable(1)).getLocation();
        return LuaNumber.valueOf(loc.distance(loc2));
    }

    private class WorldField extends LinkedField<LuaLocation> {
        public WorldField(LuaLocation self) { super(self); }

        @Override
        public void update(LuaValue val) {
            loc.setWorld(((LuaWorld) val.checktable()).getWorld());
        }

        @Override
        public LuaValue query() {
            if (!worldCache.getWorld().equals(loc.getWorld())) {
                worldCache = new LuaWorld(loc.getWorld());
            }
            return worldCache;
        }
    }

    private class XField extends LinkedField<LuaLocation> {
        public XField(LuaLocation self) { super(self); }

        @Override
        public void update(LuaValue val) {
            loc.setX(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaLocation.this.loc.getX());
        }
    }

    private class YField extends LinkedField<LuaLocation> {
        public YField(LuaLocation self) { super(self); }

        @Override
        public void update(LuaValue val) {
            loc.setY(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaLocation.this.loc.getY());
        }
    }

    private class ZField extends LinkedField<LuaLocation> {
        public ZField(LuaLocation self) { super(self); }

        @Override
        public void update(LuaValue val) {
            LuaLocation.this.loc.setZ(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaLocation.this.loc.getZ());
        }
    }

    private class PitchField extends LinkedField<LuaLocation> {
        public PitchField(LuaLocation self) { super(self); }

        @Override
        public void update(LuaValue val) {
            LuaLocation.this.loc.setPitch((float) val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaLocation.this.loc.getPitch());
        }
    }

    private class YawField extends LinkedField<LuaLocation> {
        public YawField(LuaLocation self) { super(self); }

        @Override
        public void update(LuaValue val) {
            LuaLocation.this.loc.setYaw((float) val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaLocation.this.loc.getYaw());
        }
    }

    public Location getLocation() {
        return this.loc;
    }

    @Override
    public String getName() {
        return "location";
    }
}
