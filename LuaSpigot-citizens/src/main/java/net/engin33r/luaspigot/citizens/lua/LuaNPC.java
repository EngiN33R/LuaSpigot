package net.engin33r.luaspigot.citizens.lua;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import net.engin33r.luaspigot.lua.type.LuaEntity;
import net.engin33r.luaspigot.lua.type.LuaLocation;
import net.engin33r.luaspigot.lua.type.LuaUUID;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.luaj.vm2.*;

public class LuaNPC extends WeakType {
    private final NPC npc;
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaNPC(NPC npc) {
        this.npc = npc;

        registerField("uuid", new LuaUUID(npc.getUniqueId()));
        registerField("fullName", LuaString.valueOf(npc.getFullName()));
        registerLinkedField("name", new NameField(this));
        registerLinkedField("flyable", new FlyableField(this));
        registerLinkedField("protected", new ProtectedField(this));
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "npc";
    }

    public NPC getNPC() {
        return this.npc;
    }

    @Override
    public String toLuaString() {
        return "npc: "+npc.getName()+" ("+npc.getUniqueId()+")";
    }

    @DynFieldDef(name = "id")
    public LuaValue getID() {
        return LuaNumber.valueOf(this.npc.getId());
    }

    @DynFieldDef(name = "entity")
    public LuaValue getEntity() {
        return new LuaEntity(this.npc.getEntity());
    }

    @DynFieldDef(name = "traits")
    public LuaValue getTraits() {
        LuaTable traits = LuaTable.tableOf();
        for (Trait trait : npc.getTraits()) {
            traits.set(traits.length()+1, LuaString.valueOf(trait
                    .getName()));
        }
        return traits;
    }

    @DynFieldDef(name = "spawned")
    public LuaValue getSpawned() {
        return LuaBoolean.valueOf(npc.isSpawned());
    }

    @DynFieldDef(name = "lastLocation")
    public LuaValue getLastLocation() {
        return new LuaLocation(npc.getStoredLocation());
    }

    @MethodDef(name = "spawn")
    public Varargs spawn(Varargs args) {
        return LuaBoolean.valueOf(npc.spawn(((LuaLocation) args.checktable(1))
                .getLocation()));
    }

    @MethodDef(name = "despawn")
    public Varargs despawn(Varargs args) {
        return LuaBoolean.valueOf(npc.despawn(DespawnReason.valueOf(
                args.optjstring(1, "PLUGIN"))));
    }

    @MethodDef(name = "teleport")
    public Varargs teleport(Varargs args) {
        npc.teleport(((LuaLocation) args.checktable(1)).getLocation(),
                PlayerTeleportEvent.TeleportCause.valueOf(args.optjstring(2,
                        "PLUGIN")));
        return NIL;
    }

    @MethodDef(name = "face")
    public Varargs face(Varargs args) {
        npc.faceLocation(((LuaLocation) args.checktable(1)).getLocation());
        return NIL;
    }

    @MethodDef(name = "setEntityType")
    public Varargs setEntityType(Varargs args) {
        npc.setBukkitEntityType(EntityType.valueOf(args.checkjstring(1)));
        return NIL;
    }

    @MethodDef(name = "addTrait")
    public Varargs addTrait(Varargs args) {
        npc.addTrait(CitizensAPI.getTraitFactory().getTrait(args
                .checkjstring(1)));
        return NIL;
    }

    @MethodDef(name = "removeTrait")
    public Varargs removeTrait(Varargs args) {
        npc.removeTrait(CitizensAPI.getTraitFactory().getTrait(args
                .checkjstring(1)).getClass());
        return NIL;
    }

    @MethodDef(name = "hasTrait")
    public Varargs hasTrait(Varargs args) {
        return LuaBoolean.valueOf(npc.hasTrait(CitizensAPI.getTraitFactory()
                .getTrait(args.checkjstring(1)).getClass()));
    }

    private class NameField extends LinkedField<LuaNPC> {
        NameField(LuaNPC self) { super(self); }

        @Override
        public void update(LuaValue val) {
            npc.setName(val.tojstring());
        }

        @Override
        public LuaValue query() {
            return LuaString.valueOf(npc.getName());
        }
    }

    private class FlyableField extends LinkedField<LuaNPC> {
        FlyableField(LuaNPC self) { super(self); }

        @Override
        public void update(LuaValue val) {
            npc.setFlyable(val.checkboolean());
        }

        @Override
        public LuaValue query() {
            return LuaBoolean.valueOf(npc.isFlyable());
        }
    }

    private class ProtectedField extends LinkedField<LuaNPC> {
        ProtectedField(LuaNPC self) { super(self); }

        @Override
        public void update(LuaValue val) {
            npc.setProtected(val.checkboolean());
        }

        @Override
        public LuaValue query() {
            return LuaBoolean.valueOf(npc.isProtected());
        }
    }
}
