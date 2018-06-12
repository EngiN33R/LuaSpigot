package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibraryFunctionDefinition;
import net.engin33r.luaspigot.lua.type.LuaEvent;
import net.engin33r.luaspigot.lua.type.LuaInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.*;

@SuppressWarnings("unused")
public class InventoryLibrary extends Library {
    private final Set<LuaInventoryView> openInventories = new HashSet<>();

    private class InventoryEventListener implements Listener {
        private Varargs callIfExists(LuaInventoryView inv, String method,
                                     Varargs args) {
            if (inv.get(method).isfunction()) {
                return inv.get(method).invoke(args);
            }
            return LuaValue.varargsOf(new LuaValue[]{ LuaNil.NIL });
        }

        private Varargs callIfExists(LuaInventoryView inv, String method,
                                     LuaValue arg) {
            return callIfExists(inv, method,
                    LuaValue.varargsOf(new LuaValue[]{ arg }));
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onInventoryOpen(InventoryOpenEvent ev) {
            openInventories.forEach(
                    i -> callIfExists(i, "onOpen", new LuaEvent(ev)));
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onInventoryClick(InventoryClickEvent ev) {
            openInventories.forEach(
                    i -> callIfExists(i, "onClick", new LuaEvent(ev)));
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onInventoryDrag(InventoryDragEvent ev) {
            openInventories.forEach(
                    i -> callIfExists(i, "onDrag", new LuaEvent(ev)));
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onInventoryClose(InventoryCloseEvent ev) {
            openInventories.forEach(
                    i -> callIfExists(i, "onClose", new LuaEvent(ev)));
            ev.getInventory().getViewers().remove(ev.getPlayer());
            openInventories.remove(openInventories.stream()
                    .filter(i -> i.getHandle().getHandle()
                            .equals(ev.getInventory()))
                    .findFirst()
                    .orElse(null));
        }
    }

    public InventoryLibrary(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(
                new InventoryEventListener(), plugin);
    }

    public void cleanup() {
        openInventories.clear();
    }

    @Override
    public String getName() {
        return "inventory";
    }

    @LibraryFunctionDefinition(value = "create")
    public Varargs create(Varargs arg) {
        String title = arg.checkjstring(1);
        int size = arg.checkint(2);
        LuaInventoryView inv = new LuaInventoryView(
                size % 9 == 0 ? size : (size / 9 + 1) * 9,
                title);
        openInventories.add(inv);
        return inv;
    }
}
