package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Function;
import net.engin33r.luaspigot.lua.Library;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.LuaValue;

/**
 * Library for interacting with the chat.
 */
@SuppressWarnings("unused")
public class ChatBinding extends Library {
    public ChatBinding() {
        registerFunction(new BroadcastFunction());
    }

    @Override
    public String getName() {
        return "chat";
    }

    private class BroadcastFunction extends Function {
        @Override
        public String getName() {
            return "broadcast";
        }

        @Override
        public Varargs call(Varargs args) {
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


}
