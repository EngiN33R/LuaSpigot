package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldAccessorDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldMutatorDefinition;
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

    @LinkedFieldMutatorDefinition("world")
    public void setWorld(LuaWorld world) {
        getHandle().setWorld(world.getHandle());
    }

    @LinkedFieldAccessorDefinition("world")
    public LuaWorld getWorld() {
        return new LuaWorld(getHandle().getWorld());
    }

    @LinkedFieldMutatorDefinition("x")
    public void setX(LuaNumber x) {
        getHandle().setX(x.todouble());
    }

    @LinkedFieldAccessorDefinition("x")
    public LuaNumber getX() {
        return LuaNumber.valueOf(getHandle().getX());
    }

    @LinkedFieldMutatorDefinition("y")
    public void setY(LuaNumber y) {
        getHandle().setY(y.todouble());
    }

    @LinkedFieldAccessorDefinition("y")
    public LuaNumber getY() {
        return LuaNumber.valueOf(getHandle().getY());
    }

    @LinkedFieldMutatorDefinition("z")
    public void setZ(LuaNumber z) {
        getHandle().setZ(z.todouble());
    }

    @LinkedFieldAccessorDefinition("z")
    public LuaNumber getZ() {
        return LuaNumber.valueOf(getHandle().getZ());
    }

    @LinkedFieldMutatorDefinition("pitch")
    public void setPitch(LuaNumber z) {
        getHandle().setPitch((float) z.todouble());
    }

    @LinkedFieldAccessorDefinition("pitch")
    public LuaNumber getPitch() {
        return LuaNumber.valueOf(getHandle().getPitch());
    }

    @LinkedFieldMutatorDefinition("yaw")
    public void setYaw(LuaNumber yaw) {
        getHandle().setYaw((float) yaw.todouble());
    }

    @LinkedFieldAccessorDefinition("yaw")
    public LuaNumber getYaw() {
        return LuaNumber.valueOf(getHandle().getYaw());
    }

    @Override
    public String getName() {
        return "location";
    }
}
