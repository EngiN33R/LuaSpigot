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

import java.util.HashSet;
import java.util.Set;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Library for creating and managing Spigot async tasks.
 */
@SuppressWarnings("unused")
public class TaskLibrary extends Library {
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;
    private final Set<BukkitTask> spawnedTasks = new HashSet<>();

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
        spawnedTasks.add(task);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "repeating", module = "async")
    public Varargs repeatingAsync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskTimerAsynchronously(plugin,
                run::call, args.optlong(3, 0), args.checklong(2));
        spawnedTasks.add(task);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "delayed", module = "async")
    public Varargs delayedAsync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskLaterAsynchronously(plugin,
                run::call, args.checklong(2));
        spawnedTasks.add(task);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "sched", module = "sync")
    public Varargs schedSync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTask(plugin, run::call);
        spawnedTasks.add(task);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "repeating", module = "sync")
    public Varargs repeatingSync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskTimer(plugin, run::call,
                args.optlong(3, 0), args.checklong(2));
        spawnedTasks.add(task);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "delayed", module = "sync")
    public Varargs delayedSync(Varargs args) {
        LuaFunction run = args.checkfunction(1);
        BukkitTask task = scheduler.runTaskLater(plugin, run::call,
                args.checklong(2));
        spawnedTasks.add(task);
        return LuaNumber.valueOf(task.getTaskId());
    }

    @LibFunctionDef(name = "cancel")
    public Varargs cancel(Varargs args) {
        int id = args.checkint(1);
        spawnedTasks.stream().filter(t -> t.getTaskId() == id)
                .forEach(BukkitTask::cancel);
        spawnedTasks.removeIf(t -> t.getTaskId() == id);
        scheduler.cancelTask(id);
        return NIL;
    }

    @LibFunctionDef(name = "cancelAll")
    public Varargs cancelAll(Varargs args) {
        scheduler.cancelAllTasks();
        spawnedTasks.clear();
        return NIL;
    }

    public void stop() {
        spawnedTasks.forEach(BukkitTask::cancel);
        spawnedTasks.clear();
    }
}
