package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.Method;
import net.engin33r.luaspigot.lua.type.event.*;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEvent;
import org.luaj.vm2.*;

/**
 * Wrapper type describing a Spigot event.
 */
public class LuaEvent extends WrapperType<Event> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public LuaEvent(Event ev) {
        super(ev);

        if (ev instanceof Cancellable) {
            registerField("cancellable", TRUE);
            registerLinkedField("cancelled", new CancelledField(this));
        }

        registerField("async", LuaBoolean.valueOf(ev.isAsynchronous()));

        if (ev instanceof AsyncPlayerPreLoginEvent) {
            registerField("address", LuaString.valueOf(
                    ((AsyncPlayerPreLoginEvent) ev).getAddress()
                            .getCanonicalHostName()));
            registerField("name", LuaString.valueOf(
                    ((AsyncPlayerPreLoginEvent) ev).getName()));
            registerField("uuid", new LuaUUID(
                    ((AsyncPlayerPreLoginEvent) ev).getUniqueId()));
            registerLinkedField("result", new LinkedField<LuaEvent>(this) {
                @Override
                public void update(LuaValue val) {
                    ((AsyncPlayerPreLoginEvent) ev).setLoginResult(
                            AsyncPlayerPreLoginEvent.Result
                                    .valueOf(val.checkjstring().toUpperCase()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((AsyncPlayerPreLoginEvent) ev)
                            .getLoginResult().name());
                }
            });
            registerLinkedField("message", new LinkedField<LuaEvent>(this) {
                @Override
                public void update(LuaValue val) {
                    ((AsyncPlayerPreLoginEvent) ev).setKickMessage(val
                            .checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((AsyncPlayerPreLoginEvent) ev)
                            .getKickMessage());
                }
            });
            registerMethod("allow", new Method<LuaEvent>(this) {
                @Override
                public Varargs call(Varargs args) {
                    ((AsyncPlayerPreLoginEvent) ev).allow();
                    return NIL;
                }
            });
            registerMethod("disallow", new Method<LuaEvent>(this) {
                @Override
                public Varargs call(Varargs args) {
                    ((AsyncPlayerPreLoginEvent) ev).disallow(
                            AsyncPlayerPreLoginEvent.Result
                                    .valueOf(args.checkjstring(1)),
                            args.optjstring(2, ""));
                    return NIL;
                }
            });
        }

        if (ev instanceof InventoryMoveItemEvent) {
            registerField("destination", new LuaInventory(
                    ((InventoryMoveItemEvent) ev).getDestination()));
            registerField("initiator", new LuaInventory(
                    ((InventoryMoveItemEvent) ev).getInitiator()));
            registerField("source", new LuaInventory(
                    ((InventoryMoveItemEvent) ev).getSource()));

            registerLinkedField("item", new LinkedField<LuaEvent>(this) {
                @Override
                public void update(LuaValue val) {
                    ((InventoryMoveItemEvent) ev).setItem(((LuaItem)
                            val.checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaItem(((InventoryMoveItemEvent) ev)
                            .getItem());
                }
            });
        }

        if (ev instanceof InventoryPickupItemEvent) {
            InventoryPickupItemEvent cev = (InventoryPickupItemEvent) ev;
            registerField("inventory", new LuaInventory(cev.getInventory()));
            registerField("item", new LuaItem(cev.getItem()));
        }

        if (ev instanceof PlayerLeashEntityEvent) {
            registerField("entity", new LuaEntity(((PlayerLeashEntityEvent) ev)
                    .getEntity()));
            registerField("holder", new LuaEntity(((PlayerLeashEntityEvent) ev)
                    .getLeashHolder()));
            registerField("player", new LuaPlayer(((PlayerLeashEntityEvent) ev)
                    .getPlayer()));
        }

        if (ev instanceof BlockEvent) {
            LuaBlockEventFactory.build((BlockEvent) ev, this);
        }

        if (ev instanceof EntityEvent) {
            LuaEntityEventFactory.build((EntityEvent) ev, this);
        }

        if (ev instanceof InventoryEvent) {
            LuaInventoryEventFactory.build((InventoryEvent) ev, this);
        }

        if (ev instanceof PlayerEvent) {
            LuaPlayerEventFactory.build((PlayerEvent) ev, this);
        }

        if (ev instanceof VehicleEvent) {
            LuaVehicleEventFactory.build((VehicleEvent) ev, this);
        }
    }

    @Override
    public String getName() {
        return "event";
    }

    @Override
    public String toLuaString() {
        return "event: " + getHandle().getEventName();
    }

    // 100% type-safety guarantee
    private class CancelledField extends LinkedField<LuaEvent> {
        public CancelledField(LuaEvent self) { super(self); }

        @Override
        public void update(LuaValue val) {
            ((Cancellable) getHandle()).setCancelled(val.checkboolean());
        }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(((Cancellable) getHandle()).isCancelled());
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
