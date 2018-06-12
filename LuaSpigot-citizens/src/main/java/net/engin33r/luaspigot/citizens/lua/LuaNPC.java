package net.engin33r.luaspigot.citizens.lua;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.DynamicFieldDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldAccessorDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldMutatorDefinition;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
import net.engin33r.luaspigot.lua.type.LuaEntity;
import net.engin33r.luaspigot.lua.type.LuaLocation;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.luaj.vm2.*;

import java.util.HashMap;
import java.util.Map;

public class LuaNPC extends WrapperType<NPC> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    private static final Map<String, Class<? extends LuaNPCTrait>>
            traitAdapters = new HashMap<>();

    public LuaNPC(NPC npc) {
        super(npc);

        registerField("uuid", new LuaUUID(npc.getUniqueId()));
        registerField("fullName", LuaString.valueOf(npc.getFullName()));
    }

    @SuppressWarnings("unused")
    public static void registerTraitAdapter(String name, Class<? extends
            LuaNPCTrait> clazz) {
        traitAdapters.put(name, clazz);
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    public String getName() {
        return "npc";
    }

    @Override
    public String toLuaString() {
        return "npc: " + getHandle().getName() + " (" + getHandle()
                .getUniqueId() + ")";
    }

    @DynamicFieldDefinition("id")
    public LuaValue getID() {
        return LuaNumber.valueOf(getHandle().getId());
    }

    @DynamicFieldDefinition("entity")
    public LuaValue getEntity() {
        return new LuaEntity(getHandle().getEntity());
    }

    @DynamicFieldDefinition("traits")
    public LuaValue getTraits() {
        return TableUtils.tableFrom(getHandle().getTraits(), trait ->
                LuaString.valueOf(CitizensAPI.getTraitFactory().getTrait
                        (trait.getClass()).getName()));
    }

    @DynamicFieldDefinition("spawned")
    public LuaValue getSpawned() {
        return LuaBoolean.valueOf(getHandle().isSpawned());
    }

    @DynamicFieldDefinition("lastLocation")
    public LuaValue getLastLocation() {
        return new LuaLocation(getHandle().getStoredLocation());
    }

    @MethodDefinition("spawn")
    public Varargs spawn(Varargs args) {
        return LuaBoolean.valueOf(getHandle().spawn(((LuaLocation) args
                .checktable(1)).getHandle()));
    }

    @MethodDefinition("despawn")
    public Varargs despawn(Varargs args) {
        return LuaBoolean.valueOf(getHandle().despawn(DespawnReason.valueOf
                (args.optjstring(1, "PLUGIN"))));
    }

    @MethodDefinition("teleport")
    public Varargs teleport(Varargs args) {
        getHandle().teleport(((LuaLocation) args.checktable(1)).getHandle(),
                PlayerTeleportEvent.TeleportCause.valueOf(
                        args.optjstring(2, "PLUGIN")));
        return NIL;
    }

    @MethodDefinition("face")
    public Varargs face(Varargs args) {
        getHandle().faceLocation(
                ((LuaLocation) args.checktable(1)).getHandle());
        return NIL;
    }

    @MethodDefinition("setEntityType")
    public Varargs setEntityType(Varargs args) {
        getHandle().setBukkitEntityType(
                EntityType.valueOf(args.checkjstring(1)));
        return NIL;
    }

    @MethodDefinition("addTrait")
    public Varargs addTrait(Varargs args) {
        getHandle().addTrait(
                CitizensAPI.getTraitFactory().getTrait(args.checkjstring(1)));
        return NIL;
    }

    @MethodDefinition("removeTrait")
    public Varargs removeTrait(Varargs args) {
        getHandle().removeTrait(
                CitizensAPI.getTraitFactory().getTrait(args.checkjstring(1))
                        .getClass());
        return NIL;
    }

    @MethodDefinition("hasTrait")
    public Varargs hasTrait(Varargs args) {
        Trait t = CitizensAPI.getTraitFactory().getTrait(args.checkjstring(1));
        if (t == null) return FALSE;
        return LuaBoolean.valueOf(getHandle().hasTrait(t.getClass()));
    }

    @MethodDefinition("getTrait")
    public Varargs getTrait(Varargs args) {
        String name = args.checkjstring(1);

        Class<? extends Trait> clazz = CitizensAPI.getTraitFactory().getTrait
                (name).getClass();
        Trait trait = getHandle().getTrait(clazz);

        try {
            Class<? extends LuaNPCTrait> lclass = traitAdapters.get(name);
            if (lclass == null) lclass = LuaNPCTrait.class;
            return lclass.getConstructor(clazz).newInstance(trait);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NIL;
    }

    @LinkedFieldMutatorDefinition("name")
    public void setNpcName(LuaValue name) {
        getHandle().setName(name.checkjstring());
    }

    @LinkedFieldAccessorDefinition("name")
    public LuaValue getNpcName() {
        return LuaString.valueOf(getHandle().getName());
    }

    @LinkedFieldMutatorDefinition("flyable")
    public void setFlyable(LuaValue flyable) {
        getHandle().setFlyable(flyable.checkboolean());
    }

    @LinkedFieldAccessorDefinition("flyable")
    public LuaValue isFlyable() {
        return LuaBoolean.valueOf(getHandle().isFlyable());
    }

    @LinkedFieldMutatorDefinition("protected")
    public void setProtected(LuaValue protect) {
        getHandle().setProtected(protect.checkboolean());
    }

    @LinkedFieldAccessorDefinition("protected")
    public LuaValue isProtected() {
        return LuaBoolean.valueOf(getHandle().isProtected());
    }
}
