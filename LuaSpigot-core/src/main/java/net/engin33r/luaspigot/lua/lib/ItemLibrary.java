package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.LuaItem;
import org.luaj.vm2.Varargs;

/**
 * Library for interacting with items.
 */
public class ItemLibrary extends Library {
    @Override
    public String getName() {
        return "item";
    }

    @LibFunctionDef(name = "create")
    public Varargs create(Varargs args) {
        return new LuaItem(args.checkjstring(1), args.optint(2, 1));
    }
}
