package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing a player on the server.
 */
public class LuaPlayer extends WeakType {
    private final OfflinePlayer p;
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaPlayer(OfflinePlayer p) {
        super();

        this.p = p;

        registerField("uuid", new LuaUUID(p.getUniqueId()));
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

    private OfflinePlayer getPlayer() {
        return this.p;
    }

    @DynFieldDef(name = "online")
    public LuaValue getOnline() {
        return LuaValue.valueOf(this.p.isOnline());
    }

    @DynFieldDef(name = "name")
    public LuaValue getPlayerName() {
        return LuaValue.valueOf(this.p.getName());
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
        Location loc = null;
        if (args.narg() < 3) {
            error("at least three arguments expected");
            return NIL;
        } else if (args.narg() == 3) {
            loc = new Location(p.getWorld(), args.checkdouble(1),
                    args.checkdouble(2), args.checkdouble(3));
        }
        p.teleport(loc);
        return NIL;
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
