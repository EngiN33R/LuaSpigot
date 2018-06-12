package net.engin33r.luaspigot.lua.type;

import lombok.Getter;
import net.engin33r.luaspigot.lua.*;
import net.engin33r.luaspigot.lua.annotation.*;
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

    public LuaPlayer(OfflinePlayer p) {
        super(p);

        registerField("uuid", new LuaUUID(p.getUniqueId()));
        registerField("value", LuaValue.valueOf(p.getName()));
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

    @LinkedFieldMutatorDefinition("location")
    public void setLocation(LuaLocation location) {
        Player pl = getHandle().getPlayer();
        if (pl != null) pl.teleport(location.getHandle());
    }

    @LinkedFieldAccessorDefinition("location")
    public LuaValue getLocation() {
        Player pl = getHandle().getPlayer();
        if (pl != null)
            return new LuaLocation(pl.getLocation());
        return NIL;
    }

    @LinkedFieldMutatorDefinition("inventory")
    public void setInventory(LuaInventory inv) {
        Player pl = getHandle().getPlayer();
        if (pl != null) pl.getInventory().setContents(inv.getContents());
    }

    @LinkedFieldAccessorDefinition("inventory")
    public LuaValue getInventory() {
        Player pl = getHandle().getPlayer();
        if (pl != null)
            return new LuaInventory(pl.getInventory());
        return NIL;
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
