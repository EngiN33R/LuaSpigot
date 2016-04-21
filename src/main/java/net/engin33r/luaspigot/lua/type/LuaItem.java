package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WeakType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaValue;

/**
 * Wrapper type that represents an item stack.
 */
public class LuaItem extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private ItemStack item;

    public LuaItem(ItemStack item) {
        this.item = item;
    }

    public LuaItem(String name) {
        this(name, 1);
    }

    public LuaItem(String name, int n) {
        this(new ItemStack(Material.getMaterial(name.toUpperCase()), n));
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "item";
    }
}
