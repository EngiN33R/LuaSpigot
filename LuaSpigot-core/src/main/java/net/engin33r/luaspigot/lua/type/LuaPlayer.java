package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TypeValidator;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
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

        registerLinkedField("inventory", new InventoryField());
        registerLinkedField("location", new LocationField());

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

    @DynFieldDef(name = "location")
    public LuaValue getLocation() {
        Player p = this.p.getPlayer();
        return p == null ? NIL : new LuaLocation(p.getLocation());
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

        if (args.narg() == 1) {
            LuaTable tbl = args.checktable(1);
            LuaValue ltype = tbl.get("type");
            if (ltype == NIL) {
                error("location or entity expected, got table");
            } else {
                String type = ltype.tojstring();
                switch (type) {
                    case "location":
                        p.teleport(((LuaLocation) tbl).getLocation());
                        break;
                    case "entity":
                        p.teleport(((LuaEntity) tbl).getEntity());
                        break;
                    default:
                        error("location or entity expected, got " + type);
                        break;
                }
            }
        } else if (args.narg() < 3) {
            error("not enough arguments");
            return NIL;
        } else {
            World w = p.getWorld();
            String wname = args.optjstring(4, "");
            if (!wname.equals("")) {
                w = Bukkit.getWorld(wname);
            }
            p.teleport(new Location(w, args.checkdouble(1), args.checkdouble(2),
                    args.checkdouble(3)));
        }
        return NIL;
    }

    @DynFieldDef(name = "inventory")
    public Varargs inventory() {
        Player p = this.p.getPlayer();
        if (p == null) return NIL;

        return new LuaInventory(p.getInventory());
    }

    private class LocationField extends LinkedField<LuaPlayer> {
        @Override
        public void update(LuaValue val) {
            Player pp = p.getPlayer();
            if (pp == null) return;

            LuaTable tbl = val.checktable(1);
            TypeValidator.validate(tbl, "location");
            pp.teleport(((LuaLocation) tbl).getLocation());
        }

        @Override
        public LuaValue query() {
            return null;
        }
    }

    private class InventoryField extends LinkedField<LuaPlayer> {
        @Override
        public void update(LuaValue val) {
            Player p = getPlayer().getPlayer();
            if (p == null) return;

            TypeValidator.validate(val.checktable(), "inventory");
            p.getInventory().setContents(((LuaInventory) val.checktable())
                    .getContents());
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
