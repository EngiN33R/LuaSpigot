package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.*;
import net.engin33r.luaspigot.lua.type.*;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.*;

import java.net.InetAddress;

public class LuaPlayerEventFactory {
    public static void build(PlayerEvent ev, LuaEvent lev) {
        LuaPlayer lpl = new LuaPlayer(ev.getPlayer());
        lev.registerField("player", lpl);

        if (ev instanceof AsyncPlayerChatEvent) {
            AsyncPlayerChatEvent cev = (AsyncPlayerChatEvent) ev;
            lev.registerLinkedField("format",
                    val -> cev.setFormat(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getFormat()));
            lev.registerLinkedField("message",
                    val -> cev.setMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getMessage()));
            lev.registerField("recipients",
                    TableUtils.tableFrom(cev.getRecipients(), LuaPlayer::new));
        }

        if (ev instanceof PlayerAdvancementDoneEvent) {
            PlayerAdvancementDoneEvent cev = (PlayerAdvancementDoneEvent) ev;
            lev.registerField("advancement", LuaString.valueOf(
                    cev.getAdvancement().toString()));
        }

        if (ev instanceof PlayerAnimationEvent) {
            PlayerAnimationEvent cev = (PlayerAnimationEvent) ev;
            lev.registerField("animation", LuaString.valueOf(
                    cev.getAnimationType().toString()));
        }

        if (ev instanceof PlayerBedEnterEvent) {
            PlayerBedEnterEvent cev = (PlayerBedEnterEvent) ev;
            lev.registerField("bed", new LuaBlock(cev.getBed()));
        }

        if (ev instanceof PlayerBedLeaveEvent) {
            PlayerBedLeaveEvent cev = (PlayerBedLeaveEvent) ev;
            lev.registerField("bed", new LuaBlock(cev.getBed()));
        }

        if (ev instanceof PlayerBucketEvent) {
            PlayerBucketEvent cev = (PlayerBucketEvent) ev;
            lev.registerField("block", new LuaBlock(
                    cev.getBlockClicked()));
            lev.registerField("face", LuaString.valueOf(
                    cev.getBlockFace().toString()));
            lev.registerField("bucket", LuaString.valueOf(
                    cev.getBucket().toString()));

            lev.registerLinkedField("item",
                    val -> cev.setItemStack(
                            TypeUtils.handleOf(val, LuaItem.class)),
                    () -> new LuaItem(cev.getItemStack()));
        }

        if (ev instanceof PlayerChangedWorldEvent) {
            PlayerChangedWorldEvent cev = (PlayerChangedWorldEvent) ev;
            lev.registerField("from", new LuaWorld(cev.getFrom()));
        }

        if (ev instanceof PlayerChannelEvent) {
            PlayerChannelEvent cev = (PlayerChannelEvent) ev;
            lev.registerField("channel", LuaString.valueOf(cev.getChannel()));
        }

        if (ev instanceof PlayerChatTabCompleteEvent) {
            PlayerChatTabCompleteEvent cev = (PlayerChatTabCompleteEvent) ev;
            lev.registerField("message", LuaString.valueOf(
                    cev.getChatMessage()));
            lev.registerField("token", LuaString.valueOf(cev.getLastToken()));
            lev.registerField("completions",
                    TableUtils.tableFrom(cev.getTabCompletions(),
                            LuaString::valueOf));
        }

        if (ev instanceof PlayerCommandPreprocessEvent) {
            PlayerCommandPreprocessEvent cev = (PlayerCommandPreprocessEvent) ev;
            lev.registerLinkedField("message",
                    val -> cev.setMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getMessage()));
            lev.registerLinkedField("player",
                    val -> {
                        OfflinePlayer pl = TypeUtils.handleOf(val,
                                LuaPlayer.class);
                        if (pl != null) {
                            cev.setPlayer(pl.getPlayer());
                        }
                    },
                    () -> lpl);
        }

        if (ev instanceof PlayerDropItemEvent) {
            PlayerDropItemEvent cev = (PlayerDropItemEvent) ev;
            lev.registerField("item", new LuaItem(cev.getItemDrop()));
        }

        if (ev instanceof PlayerEditBookEvent) {
            PlayerEditBookEvent cev = (PlayerEditBookEvent) ev;
            lev.registerField("newBook", new LuaBook(cev.getNewBookMeta()));
            lev.registerField("oldBook", new LuaBook(
                    cev.getPreviousBookMeta()));
        }

        if (ev instanceof PlayerEggThrowEvent) {
            PlayerEggThrowEvent cev = (PlayerEggThrowEvent) ev;
            lev.registerLinkedField("type",
                    val -> cev.setHatchingType(
                            EntityType.valueOf(val.checkjstring())),
                    () -> LuaString.valueOf(cev.getHatchingType().name()));
            lev.registerLinkedField("hatching",
                    val -> cev.setHatching(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.isHatching()));
            lev.registerLinkedField("numHatches",
                    val -> cev.setNumHatches((byte) val.checkint()),
                    () -> LuaNumber.valueOf(cev.getNumHatches()));
        }

        if (ev instanceof PlayerExpChangeEvent) {
            PlayerExpChangeEvent cev = (PlayerExpChangeEvent) ev;
            lev.registerLinkedField("amount",
                    val -> cev.setAmount(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getAmount()));
        }

        if (ev instanceof PlayerFishEvent) {
            PlayerFishEvent cev = (PlayerFishEvent) ev;
            Entity caught = cev.getCaught();
            if (caught != null)
                lev.registerField("caught", new LuaEntity(caught));
            lev.registerField("state", LuaString.valueOf(
                    cev.getState().toString()));

            lev.registerLinkedField("exp",
                    val -> cev.setExpToDrop(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getExpToDrop()));
        }

        if (ev instanceof PlayerGameModeChangeEvent) {
            PlayerGameModeChangeEvent cev = (PlayerGameModeChangeEvent) ev;
            lev.registerField("gamemode", LuaString.valueOf(
                    cev.getNewGameMode().toString()));
        }

        if (ev instanceof PlayerInteractEvent) {
            PlayerInteractEvent cev = (PlayerInteractEvent) ev;
            lev.registerField("action", LuaString.valueOf(
                    cev.getAction().toString()));
            Block block = cev.getClickedBlock();
            if (block != null) {
                lev.registerField("block", new LuaBlock(block));
                lev.registerField("face", LuaString.valueOf(
                        cev.getBlockFace().toString()));
            }
            ItemStack item = cev.getItem();
            if (item != null) lev.registerField("item", new LuaItem(item));

            lev.registerField("hasBlock", LuaBoolean.valueOf(
                    cev.hasBlock()));
            lev.registerField("hasItem", LuaBoolean.valueOf(
                    cev.hasItem()));
            lev.registerField("blockPlaced", LuaBoolean.valueOf(
                    cev.isBlockInHand()));
            lev.registerField("hand", LuaString.valueOf(
                    cev.getHand().name()));

            lev.registerLinkedField("blockAction",
                    val -> cev.setUseInteractedBlock(
                            TypeUtils.getEnum(val, Event.Result.class)),
                    () -> TypeUtils.checkEnum(cev.useInteractedBlock()));
            lev.registerLinkedField("itemAction",
                    val -> cev.setUseItemInHand(
                            TypeUtils.getEnum(val, Event.Result.class)),
                    () -> TypeUtils.checkEnum(cev.useItemInHand()));
        }

        if (ev instanceof PlayerInteractAtEntityEvent) {
            PlayerInteractAtEntityEvent cev = (PlayerInteractAtEntityEvent) ev;
            lev.registerField("pos", new LuaVector(cev.getClickedPosition()));
        }

        if (ev instanceof PlayerInteractEntityEvent) {
            PlayerInteractEntityEvent cev = (PlayerInteractEntityEvent) ev;
            lev.registerField("entity", new LuaEntity(cev.getRightClicked()));
        }

        if (ev instanceof PlayerItemBreakEvent) {
            PlayerItemBreakEvent cev = (PlayerItemBreakEvent) ev;
            lev.registerField("item", new LuaItem(cev.getBrokenItem()));
        }

        if (ev instanceof PlayerItemConsumeEvent) {
            PlayerItemConsumeEvent cev = (PlayerItemConsumeEvent) ev;
            lev.registerLinkedField("exp",
                    val -> cev.setItem(TypeUtils.handleOf(val, LuaItem.class)),
                    () -> new LuaItem(cev.getItem()));
        }

        if (ev instanceof PlayerItemHeldEvent) {
            PlayerItemHeldEvent cev = (PlayerItemHeldEvent) ev;
            lev.registerField("previous", LuaNumber.valueOf(
                    cev.getPreviousSlot()));
            lev.registerField("new", LuaNumber.valueOf(
                    cev.getNewSlot()));
        }

        if (ev instanceof PlayerJoinEvent) {
            PlayerJoinEvent cev = (PlayerJoinEvent) ev;
            lev.registerLinkedField("message",
                    val -> cev.setJoinMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getJoinMessage()));
        }

        if (ev instanceof PlayerKickEvent) {
            PlayerKickEvent cev = (PlayerKickEvent) ev;
            lev.registerLinkedField("message",
                    val -> cev.setLeaveMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getLeaveMessage()));
            lev.registerLinkedField("message",
                    val -> cev.setReason(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getReason()));
        }

        if (ev instanceof PlayerLevelChangeEvent) {
            PlayerLevelChangeEvent cev = (PlayerLevelChangeEvent) ev;
            lev.registerField("new", LuaNumber.valueOf(cev.getNewLevel()));
            lev.registerField("old", LuaNumber.valueOf(cev.getOldLevel()));
        }

        if (ev instanceof PlayerLoginEvent) {
            PlayerLoginEvent cev = (PlayerLoginEvent) ev;
            InetAddress addr = cev.getAddress();
            if (addr != null)
                lev.registerField("address", LuaString.valueOf(addr
                        .getCanonicalHostName()));

            lev.registerField("hostname", LuaString.valueOf(
                    cev.getHostname()));

            lev.registerLinkedField("result",
                    val -> cev.setResult(
                            TypeUtils.getEnum(val,
                                    PlayerLoginEvent.Result.class)),
                    () -> TypeUtils.checkEnum(cev.getResult()));
            lev.registerLinkedField("message",
                    val -> cev.setKickMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getKickMessage()));
            lev.registerMethod("allow", cev::allow);
            lev.registerMethod("disallow", (args) -> {
                cev.disallow(
                        TypeUtils.getEnum(args.checkstring(1),
                                PlayerLoginEvent.Result.class),
                        args.optjstring(2, ""));
            });
        }

        if (ev instanceof PlayerMoveEvent) {
            PlayerMoveEvent cev = (PlayerMoveEvent) ev;
            lev.registerLinkedField("from",
                    val -> cev.setFrom(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getFrom()));
            lev.registerLinkedField("to",
                    val -> cev.setTo(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getTo()));
        }

        if (ev instanceof PlayerPortalEvent) {
            PlayerPortalEvent cev = (PlayerPortalEvent) ev;
            lev.registerLinkedField("from",
                    val -> cev.setFrom(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getFrom()));
            lev.registerLinkedField("to",
                    val -> cev.setTo(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getTo()));
        }

        if (ev instanceof PlayerQuitEvent) {
            PlayerQuitEvent cev = (PlayerQuitEvent) ev;
            lev.registerLinkedField("message",
                    val -> cev.setQuitMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getQuitMessage()));
        }

        if (ev instanceof PlayerRespawnEvent) {
            PlayerRespawnEvent cev = (PlayerRespawnEvent) ev;
            lev.registerLinkedField("from",
                    val -> cev.setRespawnLocation(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getRespawnLocation()));
            lev.registerField("bed", LuaBoolean.valueOf(cev.isBedSpawn()));
        }

        if (ev instanceof PlayerShearEntityEvent) {
            PlayerShearEntityEvent cev = (PlayerShearEntityEvent) ev;
            lev.registerField("entity", new LuaEntity(cev.getEntity()));
        }

        if (ev instanceof PlayerStatisticIncrementEvent) {
            PlayerStatisticIncrementEvent cev =
                    (PlayerStatisticIncrementEvent) ev;
            lev.registerField("stat", LuaString.valueOf(
                    cev.getStatistic().toString()));
            if (cev.getMaterial() != null)
                lev.registerField("material", LuaString.valueOf(
                        ((PlayerStatisticIncrementEvent) ev).getMaterial()
                                .toString()));
            lev.registerField("new", LuaNumber.valueOf(cev.getNewValue()));
            lev.registerField("previous", LuaNumber.valueOf(cev.getNewValue()));
        }

        if (ev instanceof PlayerSwapHandItemsEvent) {
            PlayerSwapHandItemsEvent cev = (PlayerSwapHandItemsEvent) ev;
            lev.registerLinkedField("main",
                    val -> cev.setMainHandItem(
                            TypeUtils.handleOf(val, LuaItem.class)),
                    () -> new LuaItem(cev.getMainHandItem()));
            lev.registerLinkedField("offhand",
                    val -> cev.setOffHandItem(
                            TypeUtils.handleOf(val, LuaItem.class)),
                    () -> new LuaItem(cev.getOffHandItem()));
        }

        if (ev instanceof PlayerTeleportEvent) {
            PlayerTeleportEvent cev = (PlayerTeleportEvent) ev;
            lev.registerField("cause", LuaString.valueOf(
                    cev.getCause().toString()));
        }

        if (ev instanceof PlayerToggleFlightEvent) {
            PlayerToggleFlightEvent cev = (PlayerToggleFlightEvent) ev;
            lev.registerField("flying", LuaBoolean.valueOf(cev.isFlying()));
        }

        if (ev instanceof PlayerToggleSneakEvent) {
            PlayerToggleSneakEvent cev = (PlayerToggleSneakEvent) ev;
            lev.registerField("sneaking", LuaBoolean.valueOf(cev.isSneaking()));
        }

        if (ev instanceof PlayerToggleSprintEvent) {
            PlayerToggleSprintEvent cev = (PlayerToggleSprintEvent) ev;
            lev.registerField("sprinting", LuaBoolean.valueOf(
                    cev.isSprinting()));
        }

        if (ev instanceof PlayerVelocityEvent) {
            PlayerVelocityEvent cev = (PlayerVelocityEvent) ev;
            lev.registerLinkedField("main",
                    val -> cev.setVelocity(
                            TypeUtils.handleOf(val, LuaVector.class)),
                    () -> new LuaVector(cev.getVelocity()));
        }
    }
}
