package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.DynamicField;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.Method;
import net.engin33r.luaspigot.lua.type.*;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.*;

import java.net.InetAddress;

import static org.luaj.vm2.LuaValue.NIL;

public class LuaPlayerEventFactory {
    public static void build(PlayerEvent ev, LuaEvent lev) {
        LuaPlayer lpl = new LuaPlayer(ev.getPlayer());
        lev.registerField("player", lpl);

        if (ev instanceof AsyncPlayerChatEvent) {
            lev.registerLinkedField("format", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((AsyncPlayerChatEvent) ev).setFormat(val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((AsyncPlayerChatEvent) ev)
                            .getFormat());
                }
            });

            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((AsyncPlayerChatEvent) ev).setMessage(val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((AsyncPlayerChatEvent) ev)
                            .getMessage());
                }
            });

            lev.registerDynamicField("recipients",
                    new DynamicField<LuaEvent>(lev) {
                        @Override
                        public LuaValue query() {
                            LuaTable tbl = LuaTable.tableOf();
                            for (Player p : ((AsyncPlayerChatEvent) ev)
                                    .getRecipients()) {
                                tbl.set(tbl.length()+1, new LuaPlayer(p));
                            }
                            return tbl;
                        }
                    });
        }

        if (ev instanceof PlayerAchievementAwardedEvent) {
            lev.registerField("achievement", LuaString.valueOf(
                    ((PlayerAchievementAwardedEvent) ev).getAchievement()
                            .toString()));
        }

        if (ev instanceof PlayerAnimationEvent) {
            lev.registerField("animation", LuaString.valueOf(
                    ((PlayerAnimationEvent) ev).getAnimationType().toString()));
        }

        if (ev instanceof PlayerBedEnterEvent) {
            lev.registerField("bed", new LuaBlock(((PlayerBedEnterEvent) ev)
                    .getBed()));
        }

        if (ev instanceof PlayerBedLeaveEvent) {
            lev.registerField("bed", new LuaBlock(((PlayerBedLeaveEvent) ev)
                    .getBed()));
        }

        if (ev instanceof PlayerBucketEvent) {
            lev.registerField("block", new LuaBlock(
                    ((PlayerBucketEvent) ev).getBlockClicked()));
            lev.registerField("face", LuaString.valueOf(
                    ((PlayerBucketEvent) ev).getBlockFace().toString()));
            lev.registerField("bucket", LuaString.valueOf(
                    ((PlayerBucketEvent) ev).getBucket().toString()));

            lev.registerLinkedField("item", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerBucketEvent) ev).setItemStack(((LuaItem)
                            val.checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaItem(((PlayerBucketEvent) ev).getItemStack());
                }
            });
        }

        if (ev instanceof PlayerChangedWorldEvent) {
            LuaWorld from = new LuaWorld(((PlayerChangedWorldEvent) ev)
                    .getFrom());
            lev.registerField("from", from);
        }

        if (ev instanceof PlayerChannelEvent) {
            lev.registerField("channel", LuaString.valueOf(
                    ((PlayerChannelEvent) ev).getChannel()));
        }

        if (ev instanceof PlayerChatTabCompleteEvent) {
            lev.registerField("message", LuaString.valueOf(
                    ((PlayerChatTabCompleteEvent) ev).getChatMessage()));
            lev.registerField("token", LuaString.valueOf(
                    ((PlayerChatTabCompleteEvent) ev).getLastToken()));

            LuaTable comps = LuaTable.tableOf();
            for (String comp : ((PlayerChatTabCompleteEvent) ev)
                    .getTabCompletions()) {
                comps.set(comps.length()+1, LuaString.valueOf(comp));
            }
            lev.registerField("completions", comps);
        }

        if (ev instanceof PlayerCommandPreprocessEvent) {
            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerCommandPreprocessEvent) ev).setMessage(
                            val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerCommandPreprocessEvent) ev)
                            .getMessage());
                }
            });
            lev.registerLinkedField("player", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    OfflinePlayer pl = ((LuaPlayer) val.checktable())
                            .getHandle();
                    if (pl.getPlayer() == null) return;

                    ((PlayerCommandPreprocessEvent) ev).setPlayer(pl
                            .getPlayer());
                }

                @Override
                public LuaValue query() {
                    return lpl;
                }
            });
        }

        if (ev instanceof PlayerDropItemEvent) {
            // TODO: Make items work with Item and not just ItemStack
        }

        if (ev instanceof PlayerEditBookEvent) {
            // TODO: Make LuaBook
        }

        if (ev instanceof PlayerEggThrowEvent) {
            // TODO: Process projectiles
        }

        if (ev instanceof PlayerExpChangeEvent) {
            lev.registerLinkedField("amount", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerExpChangeEvent) ev).setAmount(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((PlayerExpChangeEvent) ev)
                            .getAmount());
                }
            });
        }

        if (ev instanceof PlayerFishEvent) {
            Entity caught = ((PlayerFishEvent) ev).getCaught();
            if (caught != null)
                lev.registerField("caught", new LuaEntity(caught));
            lev.registerField("state", LuaString.valueOf(
                    ((PlayerFishEvent) ev).getState().toString()));

            lev.registerLinkedField("exp", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerFishEvent) ev).setExpToDrop(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((PlayerFishEvent) ev)
                            .getExpToDrop());
                }
            });
            lev.registerLinkedField("bitechance", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerFishEvent) ev).getHook().setBiteChance(
                            val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((PlayerFishEvent) ev)
                            .getHook().getBiteChance());
                }
            });
        }

        if (ev instanceof PlayerGameModeChangeEvent) {
            lev.registerField("gamemode", LuaString.valueOf(
                    ((PlayerGameModeChangeEvent) ev).getNewGameMode()
                            .toString()));
        }

        if (ev instanceof PlayerInteractEvent) {
            lev.registerField("action", LuaString.valueOf(
                    ((PlayerInteractEvent) ev).getAction().toString()));
            Block block = ((PlayerInteractEvent) ev).getClickedBlock();
            if (block != null) {
                lev.registerField("block", new LuaBlock(block));
                lev.registerField("face", LuaString.valueOf(
                        ((PlayerInteractEvent) ev).getBlockFace().toString()));
            }
            ItemStack item = ((PlayerInteractEvent) ev).getItem();
            if (item != null) lev.registerField("item", new LuaItem(item));

            lev.registerField("hasBlock", LuaBoolean.valueOf(
                    ((PlayerInteractEvent) ev).hasBlock()));
            lev.registerField("hasItem", LuaBoolean.valueOf(
                    ((PlayerInteractEvent) ev).hasItem()));
            lev.registerField("blockPlaced", LuaBoolean.valueOf(
                    ((PlayerInteractEvent) ev).isBlockInHand()));

            lev.registerLinkedField("blockAction", new LinkedField<LuaEvent>() {
                @Override
                public void update(LuaValue val) {
                    ((PlayerInteractEvent) ev).setUseInteractedBlock(
                            Event.Result.valueOf(val.checkjstring()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerInteractEvent) ev)
                            .useInteractedBlock().toString());
                }
            });
            lev.registerLinkedField("itemAction", new LinkedField<LuaEvent>() {
                @Override
                public void update(LuaValue val) {
                    ((PlayerInteractEvent) ev).setUseItemInHand(
                            Event.Result.valueOf(val.checkjstring()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerInteractEvent) ev)
                            .useItemInHand().toString());
                }
            });
        }

        if (ev instanceof PlayerInteractAtEntityEvent) {
            lev.registerField("pos", new LuaVector(
                    ((PlayerInteractAtEntityEvent) ev).getClickedPosition()));
        }

        if (ev instanceof PlayerInteractEntityEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((PlayerInteractEntityEvent) ev).getRightClicked()));
        }

        if (ev instanceof PlayerItemBreakEvent) {
            lev.registerField("item", new LuaItem(((PlayerItemBreakEvent) ev)
                    .getBrokenItem()));
        }

        if (ev instanceof PlayerItemConsumeEvent) {
            lev.registerLinkedField("item", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerItemConsumeEvent) ev).setItem(
                            ((LuaItem) val.checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaItem(((PlayerItemConsumeEvent) ev).getItem());
                }
            });
        }

        if (ev instanceof PlayerItemHeldEvent) {
            lev.registerField("previous", LuaNumber.valueOf(
                    ((PlayerItemHeldEvent) ev).getPreviousSlot()));
            lev.registerField("new", LuaNumber.valueOf(
                    ((PlayerItemHeldEvent) ev).getNewSlot()));
        }

        if (ev instanceof PlayerJoinEvent) {
            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerJoinEvent) ev).setJoinMessage(val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerJoinEvent) ev)
                            .getJoinMessage());
                }
            });
        }

        if (ev instanceof PlayerKickEvent) {
            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerKickEvent) ev).setLeaveMessage(val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerKickEvent) ev)
                            .getLeaveMessage());
                }
            });
            lev.registerLinkedField("reason", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerKickEvent) ev).setReason(val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerKickEvent) ev)
                            .getReason());
                }
            });
        }

        if (ev instanceof PlayerLevelChangeEvent) {
            lev.registerField("new", LuaNumber.valueOf(
                    ((PlayerLevelChangeEvent) ev).getNewLevel()));
            lev.registerField("old", LuaNumber.valueOf(
                    ((PlayerLevelChangeEvent) ev).getOldLevel()));
        }

        if (ev instanceof PlayerLoginEvent) {
            InetAddress addr = ((PlayerLoginEvent) ev).getAddress();
            if (addr != null)
                lev.registerField("address", LuaString.valueOf(addr
                        .getCanonicalHostName()));

            lev.registerField("hostname", LuaString.valueOf(
                    ((PlayerLoginEvent) ev).getHostname()));

            lev.registerLinkedField("result", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerLoginEvent) ev).setResult(PlayerLoginEvent.Result
                            .valueOf(val.checkjstring().toUpperCase()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerLoginEvent) ev)
                            .getResult().toString());
                }
            });
            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerLoginEvent) ev).setKickMessage(val
                            .checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerLoginEvent) ev)
                            .getKickMessage());
                }
            });
            lev.registerMethod("allow", new Method<LuaEvent>(lev) {
                @Override
                public Varargs call(Varargs args) {
                    ((PlayerLoginEvent) ev).allow();
                    return NIL;
                }
            });
            lev.registerMethod("disallow", new Method<LuaEvent>(lev) {
                @Override
                public Varargs call(Varargs args) {
                    ((PlayerLoginEvent) ev).disallow(
                            PlayerLoginEvent.Result
                                    .valueOf(args.checkjstring(1)),
                            args.optjstring(2, ""));
                    return NIL;
                }
            });
        }

        if (ev instanceof PlayerMoveEvent) {
            lev.registerLinkedField("from", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerMoveEvent) ev).setFrom(((LuaLocation) val
                            .checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaLocation(((PlayerMoveEvent) ev).getFrom());
                }
            });
            lev.registerLinkedField("to", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerMoveEvent) ev).setTo(((LuaLocation) val
                            .checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaLocation(((PlayerMoveEvent) ev).getTo());
                }
            });
        }

        if (ev instanceof PlayerPickupItemEvent) {
            // TODO: Add Item handling
            lev.registerField("remaining", LuaNumber.valueOf(
                    ((PlayerPickupItemEvent) ev).getRemaining()));
        }

        if (ev instanceof PlayerPortalEvent) {
            // TODO: Travel agent?
        }

        if (ev instanceof PlayerQuitEvent) {
            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerQuitEvent) ev).setQuitMessage(val
                            .checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((PlayerQuitEvent) ev)
                            .getQuitMessage());
                }
            });
        }

        if (ev instanceof PlayerRespawnEvent) {
            lev.registerLinkedField("location", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerRespawnEvent) ev).setRespawnLocation(
                            ((LuaLocation) val.checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaLocation(((PlayerRespawnEvent) ev)
                            .getRespawnLocation());
                }
            });
            lev.registerField("bed", LuaBoolean.valueOf(
                    ((PlayerRespawnEvent) ev).isBedSpawn()));
        }

        if (ev instanceof PlayerShearEntityEvent) {
            lev.registerField("entity", new LuaEntity(
                    ((PlayerShearEntityEvent) ev).getEntity()));
        }

        if (ev instanceof PlayerStatisticIncrementEvent) {
            lev.registerField("stat", LuaString.valueOf(
                    ((PlayerStatisticIncrementEvent) ev).getStatistic()
                            .toString()));
            if (((PlayerStatisticIncrementEvent) ev).getMaterial() != null)
                lev.registerField("material", LuaString.valueOf(
                        ((PlayerStatisticIncrementEvent) ev).getMaterial()
                                .toString()));
            lev.registerField("new", LuaNumber.valueOf(
                    ((PlayerStatisticIncrementEvent) ev).getNewValue()));
            lev.registerField("previous", LuaNumber.valueOf(
                    ((PlayerStatisticIncrementEvent) ev).getNewValue()));
        }

        if (ev instanceof PlayerSwapHandItemsEvent) {
            lev.registerLinkedField("main", new LinkedField<LuaEvent>() {
                @Override
                public void update(LuaValue val) {
                    ((PlayerSwapHandItemsEvent) ev).setMainHandItem(
                            ((LuaItem) val.checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaItem(((PlayerSwapHandItemsEvent) ev)
                            .getMainHandItem());
                }
            });
            lev.registerLinkedField("offhand", new LinkedField<LuaEvent>() {
                @Override
                public void update(LuaValue val) {
                    ((PlayerSwapHandItemsEvent) ev).setOffHandItem(
                            ((LuaItem) val.checktable()).getHandle());
                }

                @Override
                public LuaValue query() {
                    return new LuaItem(((PlayerSwapHandItemsEvent) ev)
                            .getOffHandItem());
                }
            });
        }

        if (ev instanceof PlayerTeleportEvent) {
            lev.registerField("cause", LuaString.valueOf(
                    ((PlayerTeleportEvent) ev).getCause().toString()));
        }

        if (ev instanceof PlayerToggleFlightEvent) {
            lev.registerField("flying", LuaBoolean.valueOf(
                    ((PlayerToggleFlightEvent) ev).isFlying()));
        }

        if (ev instanceof PlayerToggleSneakEvent) {
            lev.registerField("sneaking", LuaBoolean.valueOf(
                    ((PlayerToggleSneakEvent) ev).isSneaking()));
        }

        if (ev instanceof PlayerToggleSprintEvent) {
            lev.registerField("sprinting", LuaBoolean.valueOf(
                    ((PlayerToggleSprintEvent) ev).isSprinting()));
        }

        if (ev instanceof PlayerVelocityEvent) {
            lev.registerLinkedField("velocity", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerVelocityEvent) ev).setVelocity(
                            ((LuaVector) val.checktable()).getVector());
                }

                @Override
                public LuaValue query() {
                    return new LuaVector(((PlayerVelocityEvent) ev)
                            .getVelocity());
                }
            });
        }
    }
}
