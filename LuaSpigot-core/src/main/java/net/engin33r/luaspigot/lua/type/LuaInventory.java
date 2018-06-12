package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.Arrays;
import java.util.Map;

/**
 * Wrapper type describing an inventory (chests, players etc.)
 */
public class LuaInventory extends WrapperType<Inventory> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaInventory(Inventory inv) {
        super(inv);

        registerField("title", LuaValue.valueOf(inv.getTitle()));
        registerField("value", LuaValue.valueOf(inv.getName()));
        registerField("type", LuaValue.valueOf(inv.getType().name()
                .toLowerCase()));
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    public ItemStack[] getContents() {
        return getHandle().getContents();
    }

    @Override
    public String getName() {
        return "inventory";
    }

    @MethodDefinition("get")
    public Varargs get(Varargs arg) {
        ItemStack stack = getHandle().getItem(arg.checkint(1));

        return new LuaItem(stack);
    }

    @MethodDefinition("getAll")
    public Varargs getAll(Varargs arg) {
        return TableUtils.tableFrom(
                Arrays.asList(getHandle().getStorageContents()), LuaItem::new);
    }

    @MethodDefinition("add")
    public Varargs add(Varargs arg) {
        TypeUtils.validate(arg.checktable(1), "item");
        Map<Integer, ItemStack> failed = getHandle().addItem(((LuaItem) arg
                .checktable(1)).getHandle());
        LuaTable tbl = LuaValue.tableOf();
        for (Integer key : failed.keySet()) {
            tbl.set(LuaValue.valueOf(key), new LuaItem(failed.get(key)));
        }
        return tbl;
    }

    @MethodDefinition("set")
    public Varargs set(Varargs arg) {
        TypeUtils.validate(arg.checktable(2), "item");
        getHandle().setItem(arg.checkint(1), ((LuaItem) arg.checktable(2))
                .getHandle());
        return NIL;
    }

    @MethodDefinition("setAll")
    public Varargs setAll(Varargs arg) {
        LuaTable tbl = arg.checktable(1);
        int size = tbl.length();
        ItemStack[] contents = new ItemStack[size];
        for (int i = 1; i <= size; i++) {
            if (tbl.get(i).isnil()) contents[i-1] = null;
            else {
                TypeUtils.validate(tbl.get(i).checktable(), "item");
                contents[i-1] = ((LuaItem) tbl.get(i).checktable()).getHandle();
            }
        }
        getHandle().setContents(contents);
        return NIL;
    }

    @MethodDefinition("clear")
    public Varargs clear(Varargs arg) {
        getHandle().clear();
        return NIL;
    }
}
