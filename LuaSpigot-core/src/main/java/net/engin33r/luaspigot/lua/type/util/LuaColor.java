package net.engin33r.luaspigot.lua.type.util;

import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import org.bukkit.Color;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.List;

public class LuaColor extends WrapperType<Color> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaColor(Color color) {
        super(color);
    }

    public LuaColor(int r, int g, int b) {
        super(Color.fromRGB(r, g, b));
    }

    @MethodDefinition("mix")
    public Varargs mix(Varargs args) {
        LuaTable tbl = args.checktable(1);
        if (TypeUtils.is(tbl, "color")) {
            return new LuaColor(getHandle().mixColors(
                    ((LuaColor) tbl).getHandle()));
        } else {
            TypeUtils.validateOf(tbl, "color");
            List<Color> colors = TableUtils.listFrom(tbl, true,
                    o -> ((LuaColor) o).getHandle());
            Color[] vararg = (Color[]) colors.toArray();
            return new LuaColor(getHandle().mixColors(vararg));
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "color";
    }

    @Override
    public String toLuaString() {
        return "color: (#" + String.format("%06X", getHandle().asRGB()) + ")";
    }
}
