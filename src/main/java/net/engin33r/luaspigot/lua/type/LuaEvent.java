package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.DynamicField;
import net.engin33r.luaspigot.lua.Method;
import net.engin33r.luaspigot.lua.WeakType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Wrapper type describing a Spigot event.
 */
public class LuaEvent extends WeakType {
    private final Event ev;
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaEvent(Event ev) {
        this.ev = ev;

        if (ev instanceof Cancellable) {
            registerField("cancellable", TRUE);
            registerDynamicField("cancelled", new CancelledField(this));
            registerMethod(new CancelMethod(this));
        }

        if (ev instanceof BlockEvent) {
            processBlockEvent();
        }

        if (ev instanceof EntityEvent) {
            processEntityEvent();
        }

        if (ev instanceof PlayerEvent) {
            processPlayerEvent();
        }
    }

    private void processBlockEvent() {
        registerField("block", new LuaBlock(((BlockEvent) ev).getBlock()));
    }

    private void processEntityEvent() {
        registerField("entity", new LuaEntity(((EntityEvent) ev).getEntity()));
    }

    private void processInventoryEvent() {

    }

    private void processPlayerEvent() {
        registerField("player", new LuaPlayer(((PlayerEvent) ev).getPlayer()));
    }

    private void processVehicleEvent() {

    }

    @Override
    public String getName() {
        return "event";
    }

    @Override
    public String toLuaString() {
        return "event: "+ev.getEventName();
    }

    private Event getEvent() {
        return this.ev;
    }

    // 100% type-safety guarantee
    private class CancelledField extends DynamicField<LuaEvent> {
        public CancelledField(LuaEvent self) { super(self); }
        @Override public String getName() { return null; }

        @Override
        public LuaValue query() {
            return LuaValue.valueOf(
                    ((Cancellable) self.getEvent()).isCancelled());
        }
    }

    private class CancelMethod extends Method<LuaEvent> {
        public CancelMethod(LuaEvent self) { super(self); }
        @Override public String getName() { return "cancel"; }

        @Override
        public Varargs call(Varargs args) {
            ((Cancellable) self.getEvent()).setCancelled(args.checkboolean(1));
            return NIL;
        }
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
