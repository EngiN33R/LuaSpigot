package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;

/**
 * Wrapper type that represents an item stack.
 */
public class LuaItem extends WrapperType<ItemStack> {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaItem(ItemStack item) {
        super(item);

        registerLinkedField("material", new MaterialField());
        registerLinkedField("durability", new DurabilityField());
        registerLinkedField("amount", new AmountField());
    }

    public LuaItem(String name) {
        this(name, 1);
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
