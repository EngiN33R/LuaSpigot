package net.engin33r.luaspigot.lua.type.util;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import org.bukkit.util.Vector;
import org.luaj.vm2.LuaValue;

/**
 * Utility type describing a vector.
 */
public class LuaVector extends WeakType {
    private Vector vec;
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaVector(Vector vec) {
        this.vec = vec;

        registerLinkedField("x", new LuaVector.XField(this));
        registerLinkedField("y", new LuaVector.YField(this));
        registerLinkedField("z", new LuaVector.ZField(this));
    }

    public LuaVector(double x, double y, double z) {
        this(new Vector(x, y, z));
    }

    public Vector getVector() {
        return this.vec;
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
        public XField(LuaVector self) { super(self); }

        @Override
        public void update(LuaValue val) {
            vec.setX(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaVector.this.vec.getX());
        }
    }

    private class YField extends LinkedField<LuaVector> {
        public YField(LuaVector self) { super(self); }

        @Override
        public void update(LuaValue val) {
            vec.setY(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaVector.this.vec.getY());
        }
    }

    private class ZField extends LinkedField<LuaVector> {
        public ZField(LuaVector self) { super(self); }

        @Override
        public void update(LuaValue val) {
            vec.setZ(val.checkdouble());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(LuaVector.this.vec.getZ());
        }
    }
}
