package net.engin33r.luaspigot.lua.type.util;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldAccessorDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldMutatorDefinition;
import org.bukkit.util.Vector;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;

/**
 * Utility type describing a vector.
 */
public class LuaVector extends WrapperType<Vector> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaVector(Vector vec) {
        super(vec);
    }

    public LuaVector(double x, double y, double z) {
        this(new Vector(x, y, z));
    }

    public Vector getVector() {
        return getHandle();
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "vector";
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
}
