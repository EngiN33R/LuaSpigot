package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.luaj.vm2.*;

/**
 * Wrapper type that represents an item stack.
 */
public class LuaItem extends WrapperType<ItemStack> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public static class LuaItemMeta extends WrapperType<ItemMeta> {
        private static final LuaValue typeMetatable = LuaValue.tableOf();

        public LuaItemMeta(ItemStack item, ItemMeta handle) {
            super(handle);

            registerLinkedField("name",
                    val -> {
                        handle.setDisplayName(val.checkjstring());
                        item.setItemMeta(handle);
                    },
                    () -> LuaString.valueOf(handle.getDisplayName()));
            registerLinkedField("lore",
                    val -> {
                        handle.setLore(TableUtils.listFrom(
                                val.checktable(), String::valueOf));
                        item.setItemMeta(handle);
                    },
                    () -> TableUtils.tableFrom(handle.getLore(),
                            LuaString::valueOf));
        }

        @MethodDef("addEnchantment")
        public Varargs addEnchantment(Varargs arg) {
            getHandle().addEnchant(Enchantment.getByName(arg.checkjstring(1)),
                    arg.checkint(2), arg.checkboolean(3));
            return NIL;
        }

        @MethodDef("hasEnchantment")
        public Varargs hasEnchantment(Varargs arg) {
            return LuaBoolean.valueOf(getHandle()
                    .hasEnchant(Enchantment.getByName(arg.checkjstring(1))));
        }

        @MethodDef("removeEnchantment")
        public Varargs removeEnchantment(Varargs arg) {
            getHandle().removeEnchant(
                    Enchantment.getByName(arg.checkjstring(1)));
            return NIL;
        }

        @Override
        protected LuaValue getMetatable() {
            return typeMetatable;
        }

        @Override
        public String getName() {
            return "itemmeta";
        }
    }

    public LuaItem(ItemStack item) {
        super(item);

        registerLinkedField("material", new MaterialField());
        registerLinkedField("durability", new DurabilityField());
        registerLinkedField("amount", new AmountField());
        registerField("meta", new LuaItemMeta(item, item.getItemMeta()));
    }

    public LuaItem(Item item) {
        super(item.getItemStack());
    }

    public LuaItem(String name, int n) {
        this(new ItemStack(Material.getMaterial(name.toUpperCase()), n));
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "item";
    }

    private class MaterialField extends LinkedField<LuaItem> {
        @Override
        public void update(LuaValue val) {
            getHandle().setType(Material.getMaterial(val.checkjstring()));
        }

        @Override
        public LuaValue query() {
            ItemStack item = getHandle();
            return (item == null || item.getType() == null) ? NIL :
                    LuaString.valueOf(item.getType().toString());
        }
    }

    private class DurabilityField extends LinkedField<LuaItem> {
        @Override
        public void update(LuaValue val) {
            getHandle().setDurability(val.checknumber().toshort());
        }

        @Override
        public LuaValue query() {
            return LuaNumber.valueOf(getHandle().getDurability());
        }
    }

    private class AmountField extends LinkedField<LuaItem> {
        @Override
        public void update(LuaValue val) {
            getHandle().setAmount(val.checkint());
        }

        @Override
        public LuaValue query() {
            return LuaNumber.valueOf(getHandle().getAmount());
        }
    }
}
