package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.DynamicFieldDefinition;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaInventoryView extends WrapperType<LuaInventory> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaInventoryView(int size, String title) {
        super(new LuaInventory(Bukkit.createInventory(null, size, title)));
    }

    @MethodDefinition("displayTo")
    public Varargs displayTo(Varargs arg) {
        TypeUtils.validate(arg.checktable(1), "player");
        LuaPlayer pl = (LuaPlayer) arg.checktable(1);
        Player bukkitPl = pl.getHandle().getPlayer();
        bukkitPl.openInventory(getInventory());
        return NIL;
    }

    @MethodDefinition("closeFor")
    public Varargs closeFor(Varargs arg) {
        TypeUtils.validate(arg.checktable(1), "player");
        LuaPlayer pl = (LuaPlayer) arg.checktable(1);
        Player bukkitPl = pl.getHandle().getPlayer();
        if (bukkitPl.getOpenInventory().getTopInventory()
                .equals(getInventory())) {
            bukkitPl.closeInventory();
        }
        return NIL;
    }

    @DynamicFieldDefinition("inventory")
    public LuaValue getLuaInventory() {
        return getHandle();
    }

    public Inventory getInventory() {
        return getHandle().getHandle();
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "inventoryview";
    }
}
