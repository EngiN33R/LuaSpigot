package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.LuaEvent;
import net.engin33r.luaspigot.lua.type.LuaInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryLibrary extends Library {
    private Set<LuaInventoryView> openInventories = new HashSet<>();

    public static class CustomInventory implements Inventory {
        private Map<Integer, ItemStack> contents = new HashMap<>();
        private List<HumanEntity> viewers = new ArrayList<>();

        private int size = 1;
        private String title = "Inventory";

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }

        @Override
        public void setMaxStackSize(int i) {}

        @Override
        public String getName() {
            return "InventoryView";
        }

        @Override
        public ItemStack getItem(int i) {
            return contents.get(i);
        }

        @Override
        public void setItem(int i, ItemStack itemStack) {
            contents.put(i, itemStack);
        }

        @Override
        public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks)
                throws IllegalArgumentException {
            return null;
        }

        @Override
        public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks)
                throws IllegalArgumentException {
            return null;
        }

        @Override
        public ItemStack[] getContents() {
            return new ItemStack[0];
        }

        @Override
        public void setContents(ItemStack[] itemStacks)
                throws IllegalArgumentException {
            setStorageContents(itemStacks);
        }

        @Override
        public ItemStack[] getStorageContents() {
            return new ItemStack[0];
        }

        @Override
        public void setStorageContents(ItemStack[] itemStacks)
                throws IllegalArgumentException {
            if (itemStacks.length > getSize()) {
                throw new IllegalArgumentException();
            }

            contents.clear();
            for (int i = 0; i < itemStacks.length; i++) {
                contents.put(i, itemStacks[i]);
            }
        }

        @Override
        public boolean contains(int materialId) {
            return contains(Material.getMaterial(materialId));
        }

        @Override
        public boolean contains(Material material) throws IllegalArgumentException {
            return contents.values().stream()
                    .anyMatch(i -> i.getType().equals(material));
        }

        @Override
        public boolean contains(ItemStack itemStack) {
            return contents.containsValue(itemStack);
        }

        @Override
        public boolean contains(int materialId, int amount) {
            return contains(Material.getMaterial(materialId), amount);
        }

        @Override
        public boolean contains(Material material, int amount) throws IllegalArgumentException {
            return contents.values().stream()
                    .filter(i -> i.getType().equals(material))
                    .mapToInt(ItemStack::getAmount).sum() >= amount;
        }

        @Override
        public boolean contains(ItemStack itemStack, int amount) {
            return contents.values().stream()
                    .filter(i -> i.equals(itemStack))
                    .count() >= amount;
        }

        @Override
        public boolean containsAtLeast(ItemStack itemStack, int amount) {
            return contents.values().stream()
                    .filter(i -> i.getType().equals(itemStack.getType()))
                    .mapToInt(ItemStack::getAmount).sum() >= amount;
        }

        @Override
        public HashMap<Integer, ? extends ItemStack> all(int materialId) {
            return all(Material.getMaterial(materialId));
        }

        @Override
        public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
            return (HashMap<Integer, ItemStack>) contents.entrySet().stream()
                    .filter(i -> i.getValue().getType().equals(material))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue));
        }

        @Override
        public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
            return (HashMap<Integer, ItemStack>) contents.entrySet().stream()
                    .filter(i -> i.getValue().equals(itemStack))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue));
        }

        @Override
        public int first(int materialId) {
            return first(Material.getMaterial(materialId));
        }

        @Override
        public int first(Material material) throws IllegalArgumentException {
            for (Integer k : contents.keySet()) {
                if (contents.get(k).getType().equals(material)) {
                    return k;
                }
            }
            return -1;
        }

        @Override
        public int first(ItemStack itemStack) {
            for (Integer k : contents.keySet()) {
                if (contents.get(k).equals(itemStack)) {
                    return k;
                }
            }
            return -1;
        }

        @Override
        public int firstEmpty() {
            for (int i = 0; i < getSize(); i++) {
                if (contents.get(i) == null) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void remove(int materialId) {
            remove(Material.getMaterial(materialId));
        }

        @Override
        public void remove(Material material) throws IllegalArgumentException {
            contents.keySet().stream()
                    .filter(k -> contents.get(k).getType().equals(material))
                    .forEach(k -> contents.remove(k));
        }

        @Override
        public void remove(ItemStack itemStack) {
            contents.keySet().stream()
                    .filter(k -> contents.get(k).equals(itemStack))
                    .forEach(k -> contents.remove(k));
        }

        @Override
        public void clear(int i) {
            contents.remove(i);
        }

        @Override
        public void clear() {
            contents.clear();
        }

        @Override
        public List<HumanEntity> getViewers() {
            return viewers;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public InventoryType getType() {
            return InventoryType.CHEST;
        }

        @Override
        @Nonnull
        public ListIterator<ItemStack> iterator() {
            return new ArrayList<>(contents.values()).listIterator();
        }

        @Override
        public ListIterator<ItemStack> iterator(int i) {
            List<ItemStack> list = new ArrayList<>(contents.values());
            return list.subList(i, list.size()).listIterator();
        }

        @Override
        public InventoryHolder getHolder() {
            return null;
        }

        @Override
        public Location getLocation() {
            return null;
        }
    }

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

    public static class View extends InventoryView {
        private CustomInventory topInventory;
        private HumanEntity player;

        public View(Inventory inv) {
            this.topInventory = (CustomInventory) inv;
        }

        @Override
        public Inventory getTopInventory() {
            return topInventory;
        }

        @Override
        public Inventory getBottomInventory() {
            return player.getInventory();
        }

        @Override
        public HumanEntity getPlayer() {
            return player;
        }

        @Override
        public InventoryType getType() {
            return InventoryType.CHEST;
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

    @LibFunctionDef(name = "create")
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
