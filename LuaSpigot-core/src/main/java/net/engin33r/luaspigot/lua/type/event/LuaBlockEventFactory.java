package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.type.*;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.*;

public class LuaBlockEventFactory {
    public static void build(BlockEvent ev, LuaEvent lev) {
        LuaBlock lb = new LuaBlock(ev.getBlock());
        lev.registerField("block", lb);

        if (ev instanceof BrewEvent) {
            BrewEvent cev = (BrewEvent) ev;
            lev.registerField("contents", new LuaInventory(cev.getContents()));
            lev.registerDynamicField("fuel",
                    () -> LuaNumber.valueOf(cev.getFuelLevel()));
        }

        if (ev instanceof BlockCanBuildEvent) {
            BlockCanBuildEvent cev = (BlockCanBuildEvent) ev;
            lev.registerField("material", LuaString.valueOf(
                    cev.getMaterial().name()));

            lev.registerLinkedField("buildable",
                    val -> cev.setBuildable(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.isBuildable()));
        }

        if (ev instanceof BlockDamageEvent) {
            BlockDamageEvent cev = (BlockDamageEvent) ev;
            lev.registerField("item", new LuaItem(cev.getItemInHand()));
            lev.registerField("player", new LuaPlayer(cev.getPlayer()));

            lev.registerLinkedField("instabreak",
                    val -> cev.setInstaBreak(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.getInstaBreak()));
        }

        if (ev instanceof BlockDispenseEvent) {
            BlockDispenseEvent cev = (BlockDispenseEvent) ev;

            lev.registerLinkedField("item",
                    val -> cev.setItem(TypeUtils.handleOf(val, LuaItem.class)),
                    () -> new LuaItem(cev.getItem()));
            lev.registerLinkedField("velocity",
                    val -> cev.setVelocity(TypeUtils.handleOf(val,
                            LuaVector.class)),
                    () -> new LuaVector(cev.getVelocity()));
        }

        if (ev instanceof BlockExpEvent) {
            BlockExpEvent cev = (BlockExpEvent) ev;
            lev.registerLinkedField("exp",
                    val -> cev.setExpToDrop(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getExpToDrop()));
        }

        if (ev instanceof EntityBlockFormEvent) {
            EntityBlockFormEvent cev = (EntityBlockFormEvent) ev;
            lev.registerField("entity", new LuaEntity(cev.getEntity()));
        }

        if (ev instanceof BlockFromToEvent) {
            BlockFromToEvent cev = (BlockFromToEvent) ev;
            lev.registerField("face", LuaString.valueOf(cev
                    .getFace().toString()));
            lev.registerField("to", new LuaBlock(cev.getToBlock()));
        }

        if (ev instanceof BlockGrowEvent) {
            BlockGrowEvent cev = (BlockGrowEvent) ev;
            lev.registerField("newState", new LuaBlock(cev.getNewState()));
        }

        if (ev instanceof BlockIgniteEvent) {
            BlockIgniteEvent cev = (BlockIgniteEvent) ev;
            lev.registerField("cause", LuaString.valueOf(cev
                    .getCause().toString()));
            if (cev.getIgnitingBlock() != null)
                lev.registerField("igniteBlock", new LuaBlock(
                        cev.getIgnitingBlock()));
            lev.registerField("entity", new LuaEntity(cev.getIgnitingEntity()));
            if (cev.getPlayer() != null)
                lev.registerField("player", new LuaPlayer(cev.getPlayer()));
        }

        if (ev instanceof BlockMultiPlaceEvent) {
            BlockMultiPlaceEvent cev = (BlockMultiPlaceEvent) ev;
            LuaTable tbl = LuaTable.tableOf();
            for (BlockState b : cev.getReplacedBlockStates()) {
                tbl.set(tbl.length()+1, new LuaBlock(b));
            }
        }

        if (ev instanceof BlockPhysicsEvent) {
            BlockPhysicsEvent cev = (BlockPhysicsEvent) ev;
            lev.registerField("material", LuaString.valueOf(
                    cev.getChangedType().toString()));
        }

        if (ev instanceof BlockPistonEvent) {
            BlockPistonEvent cev = (BlockPistonEvent) ev;
            lev.registerField("direction", LuaString.valueOf(
                    cev.getDirection().toString()));
            lev.registerField("sticky", LuaBoolean.valueOf(cev.isSticky()));
        }

        if (ev instanceof BlockPistonExtendEvent) {
            BlockPistonExtendEvent cev = (BlockPistonExtendEvent) ev;
            LuaTable tbl = LuaTable.tableOf();
            for (Block b : cev.getBlocks()) {
                tbl.set(tbl.length()+1, new LuaBlock(b));
            }
            lev.registerField("blocks", tbl);
        }

        if (ev instanceof BlockPlaceEvent) {
            BlockPlaceEvent cev = (BlockPlaceEvent) ev;
            lev.registerField("against", new LuaBlock(cev.getBlockAgainst()));
            lev.registerField("placed", new LuaBlock(cev.getBlockPlaced()));
            lev.registerField("replaced", new LuaBlock(cev
                    .getBlockReplacedState()));

            lev.registerField("item", new LuaItem(cev.getItemInHand()));
            lev.registerField("player", new LuaPlayer(cev.getPlayer()));

            lev.registerLinkedField("buildable",
                    val -> cev.setBuild(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.canBuild()));
        }

        if (ev instanceof BlockRedstoneEvent) {
            BlockRedstoneEvent cev = (BlockRedstoneEvent) ev;
            lev.registerField("old", LuaNumber.valueOf(cev.getOldCurrent()));
            lev.registerLinkedField("new",
                    val -> cev.setNewCurrent(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getNewCurrent()));
        }

        if (ev instanceof BlockSpreadEvent) {
            BlockSpreadEvent cev = (BlockSpreadEvent) ev;
            lev.registerField("source", new LuaBlock(cev.getSource()));
        }

        if (ev instanceof EntityBlockFormEvent) {
            EntityBlockFormEvent cev = (EntityBlockFormEvent) ev;
            lev.registerField("entity", new LuaEntity(cev.getEntity()));
        }

        if (ev instanceof FurnaceBurnEvent) {
            FurnaceBurnEvent cev = (FurnaceBurnEvent) ev;
            lev.registerField("fuel", new LuaItem(cev.getFuel()));
            lev.registerLinkedField("time",
                    val -> cev.setBurnTime(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getBurnTime()));
            lev.registerLinkedField("burning",
                    val -> cev.setBurning(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.isBurning()));
        }

        if (ev instanceof FurnaceExtractEvent) {
            FurnaceExtractEvent cev = (FurnaceExtractEvent) ev;
            lev.registerField("item", new LuaItem(
                    new ItemStack(cev.getItemType(), cev.getItemAmount())));
            lev.registerField("player", new LuaPlayer(cev.getPlayer()));
        }

        if (ev instanceof FurnaceSmeltEvent) {
            FurnaceSmeltEvent cev = (FurnaceSmeltEvent) ev;
            lev.registerField("source", new LuaItem(cev.getSource()));
            lev.registerLinkedField("result",
                    val -> cev.setResult(((LuaItem) val).getHandle()),
                    () -> new LuaItem(cev.getResult()));
        }

        if (ev instanceof NotePlayEvent) {
            NotePlayEvent cev = (NotePlayEvent) ev;
            lev.registerLinkedField("instrument",
                    val -> cev.setInstrument(TypeUtils
                            .getEnum(val, Instrument.class)),
                    () -> TypeUtils.checkEnum(cev.getInstrument()));

            lev.registerLinkedField("note",
                    val -> {
                        LuaTable note = val.checktable();
                        Note newNote = new Note(
                                note.get("octave").checkint(),
                                Note.Tone.valueOf(note.get("tone")
                                        .checkjstring()),
                                note.get("sharped").checkboolean());
                        cev.setNote(newNote);
                    },
                    () -> {
                        LuaImmutableTable note = new LuaImmutableTable();
                        note.set(LuaString.valueOf("octave"),
                                LuaNumber.valueOf(cev.getNote().getOctave()));
                        note.set(LuaString.valueOf("tone"),
                                LuaString.valueOf(cev.getNote().getTone()
                                        .name()));
                        note.set(LuaString.valueOf("sharped"),
                                LuaBoolean.valueOf(cev.getNote().isSharped()));
                        note.lock();
                        return note;
                    });
        }

        if (ev instanceof SignChangeEvent) {
            SignChangeEvent cev = (SignChangeEvent) ev;
            lev.registerField("lines", new LuaTable() {
                @Override
                public void set(LuaValue key, LuaValue value) {
                    cev.setLine(key.checkint(), value.checkjstring());
                }

                @Override
                public LuaValue get(LuaValue key) {
                    return LuaString.valueOf(cev.getLine(key.checkint()));
                }
            });
            lev.registerField("player", new LuaPlayer(cev.getPlayer()));
        }
    }
}
