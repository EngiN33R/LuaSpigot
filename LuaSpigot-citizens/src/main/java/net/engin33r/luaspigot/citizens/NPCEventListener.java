package net.engin33r.luaspigot.citizens;

import net.engin33r.luaspigot.EventListener;
import net.engin33r.luaspigot.citizens.lua.LuaNPC;
import net.citizensnpcs.api.ai.speech.event.NPCSpeechEvent;
import net.citizensnpcs.api.event.*;
import net.citizensnpcs.api.npc.NPC;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.lib.EventLibrary;
import net.engin33r.luaspigot.lua.type.*;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.luaj.vm2.*;

public class NPCEventListener extends EventListener {
    public NPCEventListener(EventLibrary lib) {
        super(lib);
    }

    private LuaEvent buildEvent(NPCEvent ev) {
        NPC npc = ev.getNPC();
        LuaNPC lnpc = new LuaNPC(npc);

        LuaEvent lev = new LuaEvent(ev);
        lev.registerField("npc", lnpc);

        return lev;
    }

    private void processSender(CommandSender sender, LuaEvent lev) {
        lev.registerField("name", LuaString.valueOf(sender.getName()));
        if (sender instanceof Player) {
            lev.registerField("player", new LuaPlayer((Player) sender));
        } else if (sender instanceof CommandMinecart) {
            lev.registerField("entity", new LuaEntity((Entity) sender));
        } else if (sender instanceof BlockCommandSender) {
            lev.registerField("block", new LuaBlock(
                    ((BlockCommandSender) sender).getBlock()));
        } else {
            lev.registerField("console", LuaBoolean.valueOf(true));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityTargetNPC(EntityTargetNPCEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("entity", new LuaEntity(ev.getEntity()));
        lev.registerField("reason", LuaString.valueOf(ev.getReason()
                .toString()));
        lib.callEvent("EntityTargetNPC", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCLeftClick(NPCLeftClickEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("player", new LuaPlayer(ev.getClicker()));
        lib.callEvent("NPCLeftClick", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCRightClick(NPCRightClickEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("player", new LuaPlayer(ev.getClicker()));
        lib.callEvent("NPCRightClick", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCCollision(NPCCollisionEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("entity", new LuaEntity(ev.getCollidedWith()));
        lib.callEvent("NPCCollision", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCCombust(NPCCombustEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerLinkedField("duration", new LinkedField<LuaNPC>() {
            @Override
            public void update(LuaValue val) {
                ev.setDuration(val.checkint());
            }

            @Override
            public LuaValue query() {
                return LuaNumber.valueOf(ev.getDuration());
            }
        });
        lib.callEvent("NPCCombust", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCCreate(NPCCreateEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lib.callEvent("NPCCreate", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCDamage(NPCDamageEvent ev) {
        LuaEvent lev = buildEvent(ev);

        lev.registerField("cause", LuaString.valueOf(ev.getCause().name()
                .toLowerCase()));
        lev.registerLinkedField("damage", new LinkedField<LuaNPC>() {
            @Override
            public void update(LuaValue val) {
                ev.setDamage(val.checkint());
            }

            @Override
            public LuaValue query() {
                return LuaNumber.valueOf(ev.getDamage());
            }
        });
        lib.callEvent("NPCDamage", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCDamageByBlock(NPCDamageByBlockEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("block", new LuaBlock(ev.getDamager()));
        lib.callEvent("NPCDamageByBlock", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCDamageByEntity(NPCDamageByEntityEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("entity", new LuaEntity(ev.getDamager()));
        lib.callEvent("NPCDamageByEntity", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCDamageEntity(NPCDamageEntityEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("entity", new LuaEntity(ev.getDamaged()));
        lib.callEvent("NPCDamageEntity", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCDeath(NPCDeathEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerLinkedField("exp", new LinkedField<LuaNPC>() {
            @Override
            public void update(LuaValue val) {
                ev.setDroppedExp(val.checkint());
            }

            @Override
            public LuaValue query() {
                return LuaNumber.valueOf(ev.getDroppedExp());
            }
        });
        lib.callEvent("NPCDeath", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCDespawn(NPCDespawnEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("reason", LuaString.valueOf(ev.getReason()
                .toString()));
        lib.callEvent("NPCDespawn", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCPush(NPCPushEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerLinkedField("collisionVector", new LinkedField<LuaNPC>() {
            @Override
            public void update(LuaValue val) {
                ev.setCollisionVector(((LuaVector) val.checktable())
                        .getVector());
            }

            @Override
            public LuaValue query() {
                return new LuaVector(ev.getCollisionVector());
            }
        });
        lib.callEvent("NPCPush", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCRemove(NPCRemoveEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lib.callEvent("NPCRemove", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCSelect(NPCSelectEvent ev) {
        LuaEvent lev = buildEvent(ev);
        CommandSender sender = ev.getSelector();
        processSender(sender, lev);
        lib.callEvent("NPCSelect", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCSpawn(NPCSpawnEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("location", new LuaLocation(ev.getLocation()));
        lib.callEvent("NPCSpawn", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCSpeech(NPCSpeechEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerLinkedField("vocalChord", new LinkedField<LuaNPC>() {
            @Override
            public void update(LuaValue val) {
                ev.setVocalChord(val.checkjstring());
            }

            @Override
            public LuaValue query() {
                return LuaString.valueOf(ev.getVocalChordName());
            }
        });
        lib.callEvent("NPCSpeech", lev);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNPCTraitCommandAttach(NPCTraitCommandAttachEvent ev) {
        LuaEvent lev = buildEvent(ev);
        CommandSender sender = ev.getCommandSender();
        processSender(sender, lev);
        lev.registerField("trait", LuaString.valueOf(ev.getTraitClass()
                .getSimpleName()));
        lib.callEvent("NPCTraitCommandAttach", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCAddTrait(NPCAddTraitEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("trait", LuaString.valueOf(ev.getTrait().getName()));
        lib.callEvent("NPCAddTrait", lev);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCRemoveTrait(NPCRemoveTraitEvent ev) {
        LuaEvent lev = buildEvent(ev);
        lev.registerField("trait", LuaString.valueOf(ev.getTrait().getName()));
        lib.callEvent("NPCAddTrait", lev);
    }
}