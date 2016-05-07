package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaNumber;
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

    @LibFunctionDef(name = "sched", module = "async")
    public Varargs schedAsync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskAsynchronously(plugin, run::call);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "repeating", module = "async")
    public Varargs repeatingAsync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskTimerAsynchronously(plugin,
                run::call, args.optlong(3, 0), args.checklong(2));
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "delayed", module = "async")
    public Varargs delayedAsync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskLaterAsynchronously(plugin,
                run::call, args.checklong(2));
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "sched", module = "sync")
    public Varargs schedSync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTask(plugin, run::call);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "repeating", module = "sync")
    public Varargs repeatingSync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskTimer(plugin, run::call,
                args.optlong(3, 0), args.checklong(2));
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "delayed", module = "sync")
    public Varargs delayedSync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskLater(plugin, run::call,
                args.checklong(2));
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "cancel")
    public Varargs cancel(Varargs args) {
        scheduler.cancelTask(args.checkint(1));
        return NIL;
    }

    @LibFunctionDef(name = "cancelAll")
    public Varargs cancelAll(Varargs args) {
        scheduler.cancelAllTasks();
        return NIL;
    }
}
