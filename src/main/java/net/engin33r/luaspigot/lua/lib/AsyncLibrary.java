package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.Varargs;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Library for creating and managing Spigot async tasks.
 */
public class AsyncLibrary extends Library {
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;

    public AsyncLibrary(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
    }

    @Override
    public String getName() {
        return "async";
    }

    @LibFunctionDef(name = "create")
    public Varargs create(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        LuaFunction sync = args.optfunction(2, null);
        scheduler.runTaskAsynchronously(plugin, () -> {
            run.call();
            if (sync != null) {
                scheduler.runTask(plugin, sync::call);
            }
        });
        return NIL;
    }
}
