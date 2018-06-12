package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.TypeUtils;
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
            registerLinkedField("cancelled",
                    val -> ((Cancellable) ev).setCancelled(val.checkboolean()),
                    () -> LuaBoolean.valueOf(((Cancellable) ev).isCancelled()));
        }

        registerField("async", LuaBoolean.valueOf(ev.isAsynchronous()));

        if (ev instanceof AsyncPlayerPreLoginEvent) {
            AsyncPlayerPreLoginEvent cev = (AsyncPlayerPreLoginEvent) ev;
            registerField("address", LuaString.valueOf(
                    ((AsyncPlayerPreLoginEvent) ev).getAddress()
                            .getCanonicalHostName()));
            registerField("value", LuaString.valueOf(
                    ((AsyncPlayerPreLoginEvent) ev).getName()));
            registerField("uuid", new LuaUUID(
                    ((AsyncPlayerPreLoginEvent) ev).getUniqueId()));
            registerLinkedField("result",
                    val -> cev.setLoginResult(
                            TypeUtils.getEnum(val,
                                    AsyncPlayerPreLoginEvent.Result.class)),
                    () -> TypeUtils.checkEnum(cev.getLoginResult()));
            registerLinkedField("message",
                    val -> cev.setKickMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getKickMessage()));
            registerMethod("allow", cev::allow);
            registerMethod("disallow", (args) -> {
                cev.disallow(
                        TypeUtils.getEnum(args.checkstring(1),
                                AsyncPlayerPreLoginEvent.Result.class),
                        args.optjstring(2, ""));
            });
        }

        if (ev instanceof InventoryMoveItemEvent) {
            InventoryMoveItemEvent cev = (InventoryMoveItemEvent) ev;
            registerField("destination", new LuaInventory(
                    cev.getDestination()));
            registerField("initiator", new LuaInventory(cev.getInitiator()));
            registerField("source", new LuaInventory(cev.getSource()));
            registerLinkedField("item",
                    val -> cev.setItem(TypeUtils.handleOf(val, LuaItem.class)),
                    () -> new LuaItem(cev.getItem()));
        }

        if (ev instanceof InventoryPickupItemEvent) {
            InventoryPickupItemEvent cev = (InventoryPickupItemEvent) ev;
            registerField("inventory", new LuaInventory(cev.getInventory()));
            registerField("item", new LuaItem(cev.getItem()));
        }

        if (ev instanceof PlayerLeashEntityEvent) {
            PlayerLeashEntityEvent cev = (PlayerLeashEntityEvent) ev;
            registerField("entity", new LuaEntity(cev.getEntity()));
            registerField("holder", new LuaEntity(cev.getLeashHolder()));
            registerField("player", new LuaPlayer(cev.getPlayer()));
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

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
