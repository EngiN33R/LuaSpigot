package net.engin33r.luaspigot.lua.type.util;

import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.TypeValidator;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Color;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.List;

public class LuaColor extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Color color;

    public LuaColor(Color color) {
        this.color = color;
    }

    public LuaColor(int r, int g, int b) {
        this.color = Color.fromRGB(r, g, b);
    }

    @MethodDef(name = "mix")
    public Varargs mix(Varargs args) {
        LuaTable tbl = args.checktable(1);
        if (TypeValidator.is(tbl, "color")) {
            return new LuaColor(color.mixColors(((LuaColor) tbl).getColor()));
        } else {
            TypeValidator.validateOf(tbl, "color");
            List<Color> colors = TableUtils.listFrom(tbl, true,
                    o -> ((LuaColor) o).getColor());
            Color[] vararg = (Color[]) colors.toArray();
            return new LuaColor(color.mixColors(vararg));
        }
    }

    public Color getColor() {
        return color;
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
        return "color: (#" + String.format("%06X", color.asRGB()) + ")";
    }
}
