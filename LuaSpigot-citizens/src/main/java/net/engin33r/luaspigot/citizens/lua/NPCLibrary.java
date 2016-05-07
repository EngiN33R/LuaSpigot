package net.engin33r.luaspigot.citizens.lua;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.Varargs;

/**
 * Library for NPC discovery and creation
 */
public class NPCLibrary extends Library {
    private final NPCRegistry registry;

    public NPCLibrary(NPCRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String getName() {
        return "npc";
    }

    @LibFunctionDef(name = "create")
    public Varargs create(Varargs args) {
        NPC npc = registry.createNPC(EntityType.valueOf(args.checkjstring(1)),
                args.checkjstring(2));
        return new LuaNPC(npc);
    }

    @LibFunctionDef(name = "fromUUID")
    public Varargs getByUUID(Varargs args) {
        LuaUUID uuid = (LuaUUID) args.checktable(1);
        return new LuaNPC(registry.getByUniqueIdGlobal(uuid.getHandle()));
    }

    @LibFunctionDef(name = "list")
    public Varargs list(Varargs args) {
        LuaTable tbl = LuaTable.tableOf();
        for (NPC npc : registry.sorted()) {
            tbl.set(tbl.length()+1, new LuaNPC(npc));
        }
        return tbl;
    }

    @LibFunctionDef(name = "withTrait")
    public Varargs withTrait(Varargs args) {
        LuaTable tbl = LuaTable.tableOf();
        for (NPC npc : registry.sorted()) {
            if (npc.hasTrait(CitizensAPI.getTraitFactory()
                    .getTrait(args.checkjstring(1)).getClass()))
                tbl.set(tbl.length()+1, new LuaNPC(npc));
        }
        return tbl;
    }
}
