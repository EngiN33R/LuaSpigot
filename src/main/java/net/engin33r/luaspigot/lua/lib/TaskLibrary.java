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
public class TaskLibrary extends Library {
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;

    public TaskLibrary(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = Bukkit.getScheduler();
    }

    @Override
    public String getName() {
        return "tasks";
    }

    @LibFunctionDef(name = "async")
    public Varargs async(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        scheduler.runTaskAsynchronously(plugin, run::call);
        return NIL;
    }

    @LibFunctionDef(name = "sync")
    public Varargs sync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        scheduler.runTask(plugin, run::call);
        return NIL;
    }
}
