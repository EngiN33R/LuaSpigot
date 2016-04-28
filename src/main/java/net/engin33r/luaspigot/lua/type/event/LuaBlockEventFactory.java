package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.type.*;
import org.bukkit.Instrument;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.luaj.vm2.*;

public class LuaBlockEventFactory {
    public static void build(BlockEvent ev, LuaEvent lev) {
        LuaBlock lb = new LuaBlock(ev.getBlock());
        lev.registerField("block", lb);

        if (ev instanceof BrewEvent) {

        }

        if (ev instanceof BlockCanBuildEvent) {
            lev.registerField("material", LuaString.valueOf(
                    ((BlockCanBuildEvent) ev).getMaterial().name()));

            lev.registerLinkedField("buildable", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockCanBuildEvent) ev).setBuildable(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((BlockCanBuildEvent) ev)
                            .isBuildable());
                }
            });
        }

        if (ev instanceof BlockDamageEvent) {
            lev.registerField("item", new LuaItem(((BlockDamageEvent) ev)
                    .getItemInHand()));
            lev.registerField("player", new LuaPlayer(((BlockDamageEvent) ev)
                    .getPlayer()));

            lev.registerLinkedField("instabreak", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockDamageEvent) ev).setInstaBreak(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((BlockDamageEvent) ev)
                            .getInstaBreak());
                }
            });
        }

        if (ev instanceof BlockDispenseEvent) {
            lev.registerLinkedField("item", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockDispenseEvent) ev).setItem(((LuaItem)
                            val.checktable()).getItem());
                }

                @Override
                public LuaValue query() {
                    return new LuaItem(((BlockDispenseEvent) ev)
                            .getItem());
                }
            });
            lev.registerLinkedField("velocity", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockDispenseEvent) ev).setVelocity(((LuaVector)
                            val.checktable()).getVector());
                }

                @Override
                public LuaValue query() {
                    return new LuaVector(((BlockDispenseEvent) ev)
                            .getVelocity());
                }
            });
        }

        if (ev instanceof BlockExpEvent) {
            lev.registerLinkedField("velocity", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockExpEvent) ev).setExpToDrop(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((BlockExpEvent) ev)
                            .getExpToDrop());
                }
            });
        }

        if (ev instanceof EntityBlockFormEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((EntityBlockFormEvent) ev).getEntity()));
        }

        if (ev instanceof BlockFromToEvent) {
            lev.registerField("face", LuaString.valueOf(((BlockFromToEvent) ev)
                    .getFace().toString()));
            lev.registerField("to", new LuaBlock(((BlockFromToEvent) ev)
                    .getToBlock()));
        }

        if (ev instanceof BlockGrowEvent) {
            lev.registerField("newState", new LuaBlock(((BlockGrowEvent) ev)
                    .getNewState()));
        }

        if (ev instanceof BlockIgniteEvent) {
            lev.registerField("cause", LuaString.valueOf(((BlockIgniteEvent) ev)
                    .getCause().toString()));
            if (((BlockIgniteEvent) ev).getIgnitingBlock() != null)
                lev.registerField("igniteBlock", new LuaBlock(
                        ((BlockIgniteEvent) ev).getIgnitingBlock()));
            lev.registerField("entity", new LuaEntity(((BlockIgniteEvent) ev)
                    .getIgnitingEntity()));
            if (((BlockIgniteEvent) ev).getPlayer() != null)
                lev.registerField("player", new LuaPlayer(
                        ((BlockIgniteEvent) ev).getPlayer()));
        }

        if (ev instanceof BlockMultiPlaceEvent) {
            LuaTable tbl = LuaTable.tableOf();
            for (BlockState b : ((BlockMultiPlaceEvent) ev)
                    .getReplacedBlockStates()) {
                tbl.set(tbl.length()+1, new LuaBlock(b));
            }
        }

        if (ev instanceof BlockPhysicsEvent) {
            lev.registerField("material", LuaString.valueOf(
                    ((BlockPhysicsEvent) ev).getChangedType().toString()));
        }

        if (ev instanceof BlockPistonEvent) {
            lev.registerField("direction", LuaString.valueOf(
                    ((BlockPistonEvent) ev).getDirection().toString()));
            lev.registerField("sticky", LuaBoolean.valueOf(
                    ((BlockPistonEvent) ev).isSticky()));
        }

        if (ev instanceof BlockPistonExtendEvent) {
            LuaTable tbl = LuaTable.tableOf();
            for (Block b : ((BlockPistonExtendEvent) ev).getBlocks()) {
                tbl.set(tbl.length()+1, new LuaBlock(b));
            }
            lev.registerField("blocks", tbl);
        }

        if (ev instanceof BlockPlaceEvent) {
            lev.registerField("against", new LuaBlock(((BlockPlaceEvent) ev)
                    .getBlockAgainst()));
            lev.registerField("placed", new LuaBlock(((BlockPlaceEvent) ev)
                    .getBlockPlaced()));
            lev.registerField("replaced", new LuaBlock(((BlockPlaceEvent) ev)
                    .getBlockReplacedState()));

            lev.registerField("item", new LuaItem(((BlockPlaceEvent) ev)
                    .getItemInHand()));
            lev.registerField("player", new LuaPlayer(((BlockPlaceEvent) ev)
                    .getPlayer()));

            lev.registerLinkedField("buildable", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockPlaceEvent) ev).setBuild(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((BlockPlaceEvent) ev)
                            .canBuild());
                }
            });
        }

        if (ev instanceof BlockRedstoneEvent) {
            lev.registerField("old", LuaNumber.valueOf(
                    ((BlockRedstoneEvent) ev).getOldCurrent()));
            lev.registerLinkedField("new", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((BlockRedstoneEvent) ev).setNewCurrent(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((BlockRedstoneEvent) ev)
                            .getNewCurrent());
                }
            });
        }

        if (ev instanceof BlockSpreadEvent) {
            lev.registerField("source", new LuaBlock(((BlockSpreadEvent) ev)
                    .getSource()));
        }

        if (ev instanceof EntityBlockFormEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((EntityBlockFormEvent) ev).getEntity()));
        }

        if (ev instanceof FurnaceBurnEvent) {

        }

        if (ev instanceof FurnaceExtractEvent) {

        }

        if (ev instanceof FurnaceSmeltEvent) {

        }

        if (ev instanceof NotePlayEvent) {
            lev.registerLinkedField("instrument", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((NotePlayEvent) ev).setInstrument(Instrument.valueOf(
                            val.checkjstring().toUpperCase()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((NotePlayEvent) ev)
                            .getInstrument().toString());
                }
            });

            // TODO: Note
        }

        if (ev instanceof SignChangeEvent) {
            lev.registerField("lines", new LuaTable() {
                @Override
                public void set(LuaValue key, LuaValue value) {
                    ((SignChangeEvent) ev).setLine(key.checkint(),
                            value.checkjstring());
                }

                @Override
                public LuaValue get(LuaValue key) {
                    return LuaString.valueOf(((SignChangeEvent) ev)
                            .getLine(key.checkint()));
                }
            });
            lev.registerField("player", new LuaPlayer(((SignChangeEvent) ev)
                    .getPlayer()));
        }
    }
}
