package net.engin33r.luaspigot.lua.lib;

import lombok.RequiredArgsConstructor;
import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.VarargBuilder;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.LuaEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.*;

import java.util.*;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Library for listening to and interacting with gameplay events.
 */
@SuppressWarnings("unused")
public class EventLibrary extends Library {
    private final Map<String, Set<LuaFunction>> handlers = new HashMap<>();
    private final JavaPlugin plugin;

    @RequiredArgsConstructor
    private class InternalListener implements Listener {
        private final EventLibrary lib;

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerJoin(PlayerJoinEvent ev) {
            lib.callEvent("PlayerJoin", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerQuit(PlayerQuitEvent ev) {
            lib.callEvent("PlayerQuit", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerMove(PlayerMoveEvent ev) {
            lib.callEvent("PlayerMove", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onProjectileHit(ProjectileHitEvent ev) {
            lib.callEvent("ProjectileHit", ev);
        }
    }

    public EventLibrary(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new InternalListener(this),
                plugin);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void callEvent(String name, Event ev) {
        Varargs result = call(VarargBuilder.build(LuaValue.valueOf(name),
                new LuaEvent(ev)));
        if (result.checkboolean(1)) {
            if (ev instanceof Cancellable) {
                ((Cancellable) ev).setCancelled(true);
            }
        }
    }

    @Override
    public String getName() {
        return "event";
    }

    @LibFunctionDef(name = "addHandler")
    public Varargs addHandler(Varargs args) {
        String eventName = args.checkjstring(1);
        LuaFunction func = args.checkfunction(2);
        System.out.println("attempting to add handler");

        Set<LuaFunction> set = handlers.get(eventName);
        if (set == null) {
            set = new HashSet<>();
            handlers.put(eventName, set);
        }

        set.add(func);

        return NIL;
    }

    @LibFunctionDef(name = "removeHandler")
    public Varargs removeHandler(Varargs args) {
        String eventName = args.checkjstring(1);
        LuaFunction func = args.checkfunction(2);

        Set<LuaFunction> set = handlers.get(eventName);
        if (set != null) {
            set.remove(func);
        }

        return NIL;
    }

    @LibFunctionDef(name = "clearHandlers")
    public Varargs clearHandlers(Varargs args) {
        String eventName = args.checkjstring(1);
        handlers.remove(eventName);
        return NIL;
    }

    @LibFunctionDef(name = "call")
    public Varargs call(Varargs args) {
        String eventName = args.checkjstring(1);
        LuaTable tbl = args.checktable(2);

        boolean cancel = false;

        Set<LuaFunction> set = handlers.get(eventName);
        if (set != null) {
            for (LuaFunction func : set) {
                LuaBoolean res = (LuaBoolean) func.call(tbl);
                if (res.equals(LuaBoolean.TRUE)) {
                    cancel = true;
                }
            }
        }

        return cancel ? LuaBoolean.TRUE : LuaBoolean.FALSE;
    }
}
