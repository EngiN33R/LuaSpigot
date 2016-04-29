package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.type.*;
import org.bukkit.event.vehicle.*;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;

public class LuaVehicleEventFactory {
    public static void build(VehicleEvent ev, LuaEvent lev) {
        lev.registerField("vehicle", new LuaEntity(ev.getVehicle()));

        if (ev instanceof VehicleBlockCollisionEvent) {
            lev.registerField("block", new LuaBlock(
                    ((VehicleBlockCollisionEvent) ev).getBlock()));
        }

        if (ev instanceof VehicleEntityCollisionEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((VehicleEntityCollisionEvent) ev).getEntity()));
            lev.registerLinkedField("collide", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((VehicleEntityCollisionEvent) ev).setCollisionCancelled(
                            val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((VehicleEntityCollisionEvent) ev)
                            .isCollisionCancelled());
                }
            });
            lev.registerLinkedField("pickup", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((VehicleEntityCollisionEvent) ev).setPickupCancelled(
                            val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((VehicleEntityCollisionEvent) ev)
                            .isPickupCancelled());
                }
            });
        }

        if (ev instanceof VehicleDamageEvent) {
            lev.registerField("attacker", new LuaEntity(
                    ((VehicleDamageEvent) ev).getAttacker()));

            lev.registerLinkedField("damage", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((VehicleDamageEvent) ev).setDamage(val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((VehicleDamageEvent) ev)
                            .getDamage());
                }
            });
        }

        if (ev instanceof VehicleDestroyEvent) {
            lev.registerField("attacker", new LuaEntity(
                    ((VehicleDestroyEvent) ev).getAttacker()));
        }

        if (ev instanceof VehicleEnterEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((VehicleEnterEvent) ev).getEntered()));
        }

        if (ev instanceof VehicleExitEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((VehicleExitEvent) ev).getExited()));
        }

        if (ev instanceof VehicleMoveEvent) {
            lev.registerField("from", new LuaLocation(((VehicleMoveEvent) ev)
                    .getFrom()));
            lev.registerField("to", new LuaLocation(((VehicleMoveEvent) ev)
                    .getTo()));
        }
    }
}
