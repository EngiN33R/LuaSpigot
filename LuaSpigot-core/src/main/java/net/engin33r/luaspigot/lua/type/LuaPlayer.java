package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing a player on (or off) the server.
 */
public class LuaPlayer extends WeakType {
    private final OfflinePlayer p;
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private LuaInventory inv;

    public LuaPlayer(OfflinePlayer p) {
        super();

        this.p = p;

        registerField("uuid", new LuaUUID(p.getUniqueId()));
        registerField("name", LuaValue.valueOf(p.getName()));

        registerLinkedField("inventory", new InventoryField(this));

        if (p.getPlayer() != null)
            inv = new LuaInventory(p.getPlayer().getInventory());
    }

    public LuaPlayer(Player p) {
        this((OfflinePlayer) p);
    }

    @Override
    public String toLuaString() {
        return "player: "+p.getName()+" ("+p.getUniqueId()+")";
    }

    @Override
    public String getName() {
        return "player";
    }

    public OfflinePlayer getPlayer() {
        return this.p;
    }

    @DynFieldDef(name = "online")
    public LuaValue getOnline() {
        return LuaValue.valueOf(this.p.isOnline());
    }

    @MethodDef(name = "message")
    public Varargs message(Varargs args) {
        Player p = this.p.getPlayer();
        if (p != null) p.sendMessage(args.checkjstring(1));
        return NIL;
    }

    @MethodDef(name = "teleport")
    public Varargs teleport(Varargs args) {
        Player p = this.p.getPlayer();
        if (p == null) return NIL;

        Location loc;
        if (args.narg() == 1) {
            loc = ((LuaLocation) args.checktable(1)).getLocation();
        } else if (args.narg() < 3) {
            error("not enough arguments");
            return NIL;
        } else {
            World w = p.getWorld();
            String wname = args.optjstring(4, "");
            if (!wname.equals("")) {
                w = Bukkit.getWorld(wname);
            }
            loc = new Location(w, args.checkdouble(1), args.checkdouble(2),
                    args.checkdouble(3));
        }
        p.teleport(loc);
        return NIL;
    }

    @DynFieldDef(name = "inventory")
    public Varargs inventory() {
        Player p = this.p.getPlayer();
        if (p == null) return NIL;

        return new LuaInventory(p.getInventory());
    }

    private class InventoryField extends LinkedField<LuaPlayer> {
        public InventoryField(LuaPlayer self) { super(self); }

        @Override
        public void update(LuaValue val) {
            Player p = getPlayer().getPlayer();
            if (p == null) return;

            p.getInventory().setContents(((LuaInventory) val).getContents());
        }

        @Override
        public LuaValue query() {
            Player p = getPlayer().getPlayer();
            if (p == null) return NIL;

            if (!inv.getInventory().equals(p.getInventory()))
                inv = new LuaInventory(p.getInventory());

            return inv;
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
