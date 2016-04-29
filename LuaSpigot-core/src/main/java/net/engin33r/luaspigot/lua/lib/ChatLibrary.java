package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.LuaValue;

/**
 * Library for interacting with the chat.
 */
@SuppressWarnings("unused")
public class ChatLibrary extends Library {
    @Override
    public String getName() {
        return "chat";
    }

    @LibFunctionDef(name = "broadcast")
    public Varargs broadcast(Varargs args) {
        int n = args.narg();
        if (n == 0) return LuaValue.NIL;

        String str = "";
        for (int i = 1; i <= args.narg(); i++) {
            if (i > 1) str += "\t";
            str += args.checkjstring(i);
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(str);
        }
        return LuaValue.NIL;
    }
}
