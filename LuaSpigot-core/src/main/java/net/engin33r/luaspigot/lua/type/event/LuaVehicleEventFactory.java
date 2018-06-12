package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.type.*;
import org.bukkit.event.vehicle.*;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;

public class LuaVehicleEventFactory {
    public static void build(VehicleEvent ev, LuaEvent lev) {
        lev.registerField("vehicle", new LuaEntity(ev.getVehicle()));

        if (ev instanceof VehicleBlockCollisionEvent) {
            VehicleBlockCollisionEvent cev = (VehicleBlockCollisionEvent) ev;
            lev.registerField("block", new LuaBlock(cev.getBlock()));
        }

        if (ev instanceof VehicleEntityCollisionEvent) {
            VehicleEntityCollisionEvent cev = (VehicleEntityCollisionEvent) ev;
            lev.registerField("entity", new LuaEntity(
                    ((VehicleEntityCollisionEvent) ev).getEntity()));
            lev.registerLinkedField("collide",
                    val -> cev.setCollisionCancelled(!val.checkboolean()),
                    () -> LuaBoolean.valueOf(!cev.isCollisionCancelled()));
            lev.registerLinkedField("pickup",
                    val -> cev.setPickupCancelled(!val.checkboolean()),
                    () -> LuaBoolean.valueOf(!cev.isPickupCancelled()));
        }

        if (ev instanceof VehicleDamageEvent) {
            VehicleDamageEvent cev = (VehicleDamageEvent) ev;
            lev.registerField("attacker", new LuaEntity((cev.getAttacker())));

            lev.registerLinkedField("damage",
                    val -> cev.setDamage(val.checkdouble()),
                    () -> LuaNumber.valueOf(cev.getDamage()));
        }

        if (ev instanceof VehicleDestroyEvent) {
            VehicleDestroyEvent cev = (VehicleDestroyEvent) ev;
            lev.registerField("attacker", new LuaEntity(cev.getAttacker()));
        }

        if (ev instanceof VehicleEnterEvent) {
            VehicleEnterEvent cev = (VehicleEnterEvent) ev;
            lev.registerField("entity", new LuaEntity(cev.getEntered()));
        }

        if (ev instanceof VehicleExitEvent) {
            VehicleExitEvent cev = (VehicleExitEvent) ev;
            lev.registerField("entity", new LuaEntity(cev.getExited()));
        }

        if (ev instanceof VehicleMoveEvent) {
            VehicleMoveEvent cev = (VehicleMoveEvent) ev;
            lev.registerField("from", new LuaLocation(cev.getFrom()));
            lev.registerField("to", new LuaLocation(cev.getTo()));
        }
    }
}
