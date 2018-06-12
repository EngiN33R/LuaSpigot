package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.annotation.DynamicFieldDefinition;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing a player on (or off) the server.
 */
public class LuaPlayer extends WrapperType<OfflinePlayer> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    private LuaInventory inv;

    public LuaPlayer(OfflinePlayer p) {
        super(p);

        registerField("uuid", new LuaUUID(p.getUniqueId()));
        registerField("value", LuaValue.valueOf(p.getName()));

        registerLinkedField("inventory", new InventoryField());
        registerLinkedField("location", new LocationField());
        registerLinkedField("velocity", new VelocityField());

        if (p.getPlayer() != null)
            inv = new LuaInventory(p.getPlayer().getInventory());
    }

    public LuaPlayer(Player p) {
        this((OfflinePlayer) p);
    }

    @Override
    public String toLuaString() {
        OfflinePlayer p = getHandle();
        return "player: "+p.getName()+" ("+p.getUniqueId()+")";
    }

    @Override
    public String getName() {
        return "player";
    }

    @DynamicFieldDefinition("online")
    public LuaValue getOnline() {
        return LuaValue.valueOf(getHandle().isOnline());
    }

    @DynamicFieldDefinition("location")
    public LuaValue getLocation() {
        Player p = getHandle().getPlayer();
        return p == null ? NIL : new LuaLocation(p.getLocation());
    }

    @MethodDefinition("message")
    public Varargs message(Varargs args) {
        Player p = getHandle().getPlayer();
        if (p != null) p.sendMessage(args.checkjstring(1));
        return NIL;
    }

    @MethodDefinition("teleport")
    public Varargs teleport(Varargs args) {
        Player p = getHandle().getPlayer();
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
                        p.teleport(((LuaLocation) tbl).getHandle());
                        break;
                    case "entity":
                        p.teleport(((LuaEntity) tbl).getHandle());
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

    @DynamicFieldDefinition("inventory")
    public Varargs inventory() {
        Player p = getHandle().getPlayer();
        if (p == null) return NIL;

        return new LuaInventory(p.getInventory());
    }

    private class LocationField extends LinkedField<LuaPlayer> {
        @Override
        public void update(LuaValue val) {
            Player pp = getHandle().getPlayer();
            if (pp == null) return;

            LuaTable tbl = val.checktable(1);
            TypeUtils.validate(tbl, "location");
            pp.teleport(((LuaLocation) tbl).getHandle());
        }

        @Override
        public LuaValue query() {
            return null;
        }
    }

    private class InventoryField extends LinkedField<LuaPlayer> {
        @Override
        public void update(LuaValue val) {
            Player p = getHandle().getPlayer();
            if (p == null) return;

            TypeUtils.validate(val.checktable(), "inventory");
            p.getInventory().setContents(
                    TypeUtils.checkOf(val, LuaInventory.class)
                            .getContents());
        }

        @Override
        public LuaValue query() {
            Player p = getHandle().getPlayer();
            if (p == null) return NIL;

            if (!inv.getHandle().equals(p.getInventory()))
                inv = new LuaInventory(p.getInventory());

            return inv;
        }
    }

    private class VelocityField extends LinkedField<LuaEntity> {
        @Override
        public void update(LuaValue val) {
            if (getHandle().getPlayer() == null) return;

            LuaVector vec;
            LuaTable tbl = val.checktable();
            if (tbl.get("type").optjstring("").equals("vector"))
                vec = (LuaVector) tbl;
            else
                vec = new LuaVector(tbl.get(1).checkdouble(),
                        tbl.get(2).checkdouble(), tbl.get(3).checkdouble());
            getHandle().getPlayer().setVelocity(vec.getVector());
        }

        @Override
        public LuaValue query() {
            if (getHandle().getPlayer() == null) return NIL;
            return new LuaVector(getHandle().getPlayer().getVelocity());
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
