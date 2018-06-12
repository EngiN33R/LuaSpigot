package net.engin33r.luaspigot.lua.type.util;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WrapperType;
import org.bukkit.util.Vector;
import org.luaj.vm2.LuaValue;

/**
 * Utility type describing a vector.
 */
public class LuaVector extends WrapperType<Vector> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaVector(Vector vec) {
        super(vec);

        registerLinkedField("x", new LuaVector.XField());
        registerLinkedField("y", new LuaVector.YField());
        registerLinkedField("z", new LuaVector.ZField());
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

    private class XField extends LinkedField<LuaVector> {
        @Override
        public void update(LuaValue val) {
            getHandle().setX(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getX());
        }
    }

    private class YField extends LinkedField<LuaVector> {
        @Override
        public void update(LuaValue val) {
            getHandle().setY(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getY());
        }
    }

    private class ZField extends LinkedField<LuaVector> {
        @Override
        public void update(LuaValue val) {
            getHandle().setZ(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(getHandle().getZ());
        }
    }
}
