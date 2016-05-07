package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.LuaPlayer;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import org.bukkit.Bukkit;
import org.luaj.vm2.Varargs;

/**
 * Library for interacting with players on and off the server.
 * Mostly deals with creating {@link LuaPlayer} instances.
 * @see LuaPlayer
 */
@SuppressWarnings("unused")
public class PlayerLibrary extends Library {
    @Override
    public String getName() {
        return "player";
    }

    @LibFunctionDef(name = "getByName")
    public Varargs getByName(Varargs args) {
        return new LuaPlayer(Bukkit.getOfflinePlayer(args.checkjstring(1)));
    }

    @LibFunctionDef(name = "getByUUID")
    public Varargs getByUUID(Varargs args) {
        return new LuaPlayer(Bukkit.getOfflinePlayer(
                new LuaUUID(args.checkjstring(1)).getHandle()));
    }
}
