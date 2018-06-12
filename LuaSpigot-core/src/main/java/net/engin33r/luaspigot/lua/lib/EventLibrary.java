package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.EventListener;
import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.VarargBuilder;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.LuaEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.*;

import java.util.*;
import java.util.logging.Level;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Library for listening to and interacting with gameplay events.
 */
@SuppressWarnings("unused")
public class EventLibrary extends Library {
    private final Map<String, Set<LuaFunction>> handlers = new HashMap<>();
    private static final Set<Class<? extends EventListener>> listeners =
            new HashSet<>();

    /* HERE WE GO BOYS */
    private class InternalListener extends EventListener {
        public InternalListener(EventLibrary lib) {
            super(lib);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onAsyncPlayerChat(AsyncPlayerChatEvent ev) {
            lib.callEvent("AsyncPlayerChat", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerPreLogin(AsyncPlayerPreLoginEvent ev) {
            lib.callEvent("AsyncPlayerPreLogin", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onBlockBreak(BlockBreakEvent ev) {
            lib.callEvent("BlockBreak", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerPreLogin(BlockBurnEvent ev) {
            lib.callEvent("BlockBurn", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onBlockCanBuild(BlockCanBuildEvent ev) {
            lib.callEvent("BlockCanBuild", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockDamage(BlockDamageEvent ev) {
            lib.callEvent("BlockDamage", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockDispense(BlockDispenseEvent ev) {
            lib.callEvent("BlockDispense", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onBlockExp(BlockExpEvent ev) {
            lib.callEvent("BlockExp", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockFade(BlockFadeEvent ev) {
            lib.callEvent("BlockFade", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onBlockForm(BlockFormEvent ev) {
            lib.callEvent("BlockForm", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockFromTo(BlockFromToEvent ev) {
            lib.callEvent("BlockFromTo", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockGrow(BlockGrowEvent ev) {
            lib.callEvent("BlockGrow", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockIgnite(BlockIgniteEvent ev) {
            lib.callEvent("BlockIgnite", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockMultiPlace(BlockMultiPlaceEvent ev) {
            lib.callEvent("BlockMultiPlace", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockPhysics(BlockPhysicsEvent ev) {
            lib.callEvent("BlockPhysics", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockPistonExtend(BlockPistonExtendEvent ev) {
            lib.callEvent("BlockPistonExtend", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockPistonRetract(BlockPistonRetractEvent ev) {
            lib.callEvent("BlockPistonRetract", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBlockPlace(BlockPlaceEvent ev) {
            lib.callEvent("BlockPlace", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onBlockRedstone(BlockRedstoneEvent ev) {
            lib.callEvent("BlockRedstone", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onBrew(BrewEvent ev) {
            lib.callEvent("Brew", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onChunkLoad(ChunkLoadEvent ev) {
            lib.callEvent("ChunkLoad", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onChunkPopulate(ChunkPopulateEvent ev) {
            lib.callEvent("ChunkPopulate", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onChunkUnload(ChunkUnloadEvent ev) {
            lib.callEvent("ChunkUnload", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onCreatureSpawn(CreatureSpawnEvent ev) {
            lib.callEvent("CreatureSpawn", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onCreeperPower(CreeperPowerEvent ev) {
            lib.callEvent("CreeperPower", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEnchantItem(EnchantItemEvent ev) {
            lib.callEvent("EnchantItem", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityBreakDoor(EntityBreakDoorEvent ev) {
            lib.callEvent("EntityBreakDoor", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityChangeBlock(EntityChangeBlockEvent ev) {
            lib.callEvent("EntityChangeBlock", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityCombust(EntityCombustEvent ev) {
            lib.callEvent("EntityCombust", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityCombustByBlock(EntityCombustByBlockEvent ev) {
            lib.callEvent("EntityCombustByBlock", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityCombustByEntity(EntityCombustByEntityEvent ev) {
            lib.callEvent("EntityCombustByEntity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityCreatePortal(EntityCreatePortalEvent ev) {
            lib.callEvent("EntityCreatePortal", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityDamage(EntityDamageEvent ev) {
            lib.callEvent("EntityDamage", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityDamageByBlock(EntityDamageByBlockEvent ev) {
            lib.callEvent("EntityDamageByBlock", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityDamageByEntity(EntityDamageByEntityEvent ev) {
            lib.callEvent("EntityDamageByEntity", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onEntityDeath(EntityDeathEvent ev) {
            lib.callEvent("EntityDeath", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityExplode(EntityExplodeEvent ev) {
            lib.callEvent("EntityExplode", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityInteract(EntityInteractEvent ev) {
            lib.callEvent("EntityInteract", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityPortal(EntityPortalEvent ev) {
            lib.callEvent("EntityTeleport", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onEntityPortalEnter(EntityPortalEnterEvent ev) {
            lib.callEvent("EntityPortalEnter", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityPortalExit(EntityPortalExitEvent ev) {
            lib.callEvent("EntityPortalExit", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityRegainHealth(EntityRegainHealthEvent ev) {
            lib.callEvent("EntityRegainHealth", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityShootBow(EntityShootBowEvent ev) {
            lib.callEvent("EntityShootBow", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityTame(EntityTameEvent ev) {
            lib.callEvent("EntityTame", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityTarget(EntityTargetEvent ev) {
            lib.callEvent("EntityTarget", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent
                                                               ev) {
            lib.callEvent("EntityTargetLivingEntity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onEntityTeleport(EntityTeleportEvent ev) {
            lib.callEvent("EntityTeleport", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onEntityUnleash(EntityUnleashEvent ev) {
            lib.callEvent("EntityUnleash", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onExplosionPrime(ExplosionPrimeEvent ev) {
            lib.callEvent("ExplosionPrime", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onFoodLevelChange(FoodLevelChangeEvent ev) {
            lib.callEvent("FoodLevelChange", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onFurnaceBurn(FurnaceBurnEvent ev) {
            lib.callEvent("FurnaceBurn", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onFurnaceExtract(FurnaceExtractEvent ev) {
            lib.callEvent("FurnaceExtract", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onFurnaceSmelt(FurnaceSmeltEvent ev) {
            lib.callEvent("FurnaceSmelt", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onHangingBreakByEntity(HangingBreakByEntityEvent ev) {
            lib.callEvent("HangingBreakByEntity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onHangingBreak(HangingBreakEvent ev) {
            lib.callEvent("HangingBreak", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onHangingPlace(HangingPlaceEvent ev) {
            lib.callEvent("HangingPlace", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onHorseJump(HorseJumpEvent ev) {
            lib.callEvent("HorseJump", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClick(InventoryClickEvent ev) {
            lib.callEvent("InventoryClick", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onInventoryClose(InventoryCloseEvent ev) {
            lib.callEvent("InventoryClose", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryDrag(InventoryDragEvent ev) {
            lib.callEvent("InventoryDrag", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryInteract(InventoryInteractEvent ev) {
            lib.callEvent("InventoryInteract", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryMoveItem(InventoryMoveItemEvent ev) {
            lib.callEvent("InventoryMoveItem", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryOpen(InventoryOpenEvent ev) {
            lib.callEvent("InventoryOpen", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryPickupItem(InventoryPickupItemEvent ev) {
            lib.callEvent("InventoryPickupItem", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onItemDespawn(ItemDespawnEvent ev) {
            lib.callEvent("ItemDespawn", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onItemSpawn(ItemSpawnEvent ev) {
            lib.callEvent("ItemSpawn", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onLeavesDecay(LeavesDecayEvent ev) {
            lib.callEvent("LeavesDecay", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onLightningStrike(LightningStrikeEvent ev) {
            lib.callEvent("LightningStrike", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onMapInitialize(MapInitializeEvent ev) {
            lib.callEvent("MapInitialize", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onNotePlay(NotePlayEvent ev) {
            lib.callEvent("NotePlay", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPigZap(PigZapEvent ev) {
            lib.callEvent("PigZap", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent ev) {
            lib.callEvent("PlayerAdvancementDone", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerAnimation(PlayerAnimationEvent ev) {
            lib.callEvent("PlayerAnimation", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerBedEnter(PlayerBedEnterEvent ev) {
            lib.callEvent("PlayerBedEnter", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerBedLeave(PlayerBedLeaveEvent ev) {
            lib.callEvent("PlayerBedLeave", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerBucket(PlayerBucketEmptyEvent ev) {
            lib.callEvent("PlayerBucketEmpty", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerBucket(PlayerBucketFillEvent ev) {
            lib.callEvent("PlayerBucketFill", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerChangedWorld(PlayerChangedWorldEvent ev) {
            lib.callEvent("PlayerChangedWorld", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerChannel(PlayerChannelEvent ev) {
            lib.callEvent("PlayerChannel", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerChatTabComplete(PlayerChatTabCompleteEvent ev) {
            lib.callEvent("PlayerChatTabComplete", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent ev) {
            lib.callEvent("PlayerCommandPreprocess", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerDeathEvent(PlayerDeathEvent ev) {
            lib.callEvent("PlayerDeath", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerDropItem(PlayerDropItemEvent ev) {
            lib.callEvent("PlayerDropItem", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerEditBook(PlayerEditBookEvent ev) {
            lib.callEvent("PlayerEditBook", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerEggThrow(PlayerEggThrowEvent ev) {
            lib.callEvent("PlayerEggThrow", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerExpChange(PlayerExpChangeEvent ev) {
            lib.callEvent("PlayerExpChange", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerFish(PlayerFishEvent ev) {
            lib.callEvent("PlayerFish", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerGameModeChange(PlayerGameModeChangeEvent ev) {
            lib.callEvent("PlayerGameModeChange", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent ev) {
            lib.callEvent("PlayerInteractAtEntity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerInteractEntity(PlayerInteractEntityEvent ev) {
            lib.callEvent("PlayerInteractEntity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerInteract(PlayerInteractEvent ev) {
            lib.callEvent("PlayerInteract", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerItemBreak(PlayerItemBreakEvent ev) {
            lib.callEvent("PlayerItemBreak", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerItemConsume(PlayerItemConsumeEvent ev) {
            lib.callEvent("PlayerItemConsume", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerItemHeld(PlayerItemHeldEvent ev) {
            lib.callEvent("PlayerItemHeld", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerJoin(PlayerJoinEvent ev) {
            lib.callEvent("PlayerJoin", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerKick(PlayerKickEvent ev) {
            lib.callEvent("PlayerKick", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerLeashEntity(PlayerLeashEntityEvent ev) {
            lib.callEvent("PlayerLeashEntity", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerLevelChange(PlayerLevelChangeEvent ev) {
            lib.callEvent("PlayerLevelChange", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerLogin(PlayerLoginEvent ev) {
            lib.callEvent("PlayerLogin", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerMove(PlayerMoveEvent ev) {
            lib.callEvent("PlayerMove", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerPortal(PlayerPortalEvent ev) {
            lib.callEvent("PlayerPortal", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerQuit(PlayerQuitEvent ev) {
            lib.callEvent("PlayerQuit", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerRegisterChannelEvent(PlayerRegisterChannelEvent
                                                                 ev) {
            lib.callEvent("PlayerRegisterChannelEvent", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerRespawn(PlayerRespawnEvent ev) {
            lib.callEvent("PlayerRespawn", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerShearEntity(PlayerShearEntityEvent ev) {
            lib.callEvent("PlayerShearEntity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent
                                                               ev) {
            lib.callEvent("PlayerStatisticIncrement", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent ev) {
            lib.callEvent("PlayerSwapHandItems", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerTeleport(PlayerTeleportEvent ev) {
            lib.callEvent("PlayerTeleport", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerToggleFlight(PlayerToggleFlightEvent ev) {
            lib.callEvent("PlayerToggleFlight", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerToggleSneak(PlayerToggleSneakEvent ev) {
            lib.callEvent("PlayerToggleSneak", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerToggleSprint(PlayerToggleSprintEvent ev) {
            lib.callEvent("PlayerToggleSprint", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerUnleashEntity(PlayerUnleashEntityEvent ev) {
            lib.callEvent("PlayerUnleashEntity", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerUnregisterChannel(PlayerUnregisterChannelEvent ev) {
            lib.callEvent("PlayerUnregisterChannel", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerVelocity(PlayerVelocityEvent ev) {
            lib.callEvent("PlayerVelocity", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPortalCreate(PortalCreateEvent ev) {
            lib.callEvent("PortalCreate", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPrepareItemCraft(PrepareItemCraftEvent ev) {
            lib.callEvent("PrepareItemCraft", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPrepareItemEnchant(PrepareItemEnchantEvent ev) {
            lib.callEvent("PrepareItemEnchant", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onProjectileHit(ProjectileHitEvent ev) {
            lib.callEvent("ProjectileHit", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onProjectileLaunch(ProjectileLaunchEvent ev) {
            lib.callEvent("ProjectileLaunch", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onRemoteServerCommand(RemoteServerCommandEvent ev) {
            lib.callEvent("RemoteServerCommand", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onServerCommand(ServerCommandEvent ev) {
            lib.callEvent("ServerCommand", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onServerListPing(ServerListPingEvent ev) {
            lib.callEvent("ServerListPing", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onServiceRegister(ServiceRegisterEvent ev) {
            lib.callEvent("ServiceRegister", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onServiceUnregister(ServiceUnregisterEvent ev) {
            lib.callEvent("ServiceUnregister", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onSheepDyeWool(SheepDyeWoolEvent ev) {
            lib.callEvent("SheepDyeWool", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onSheepRegrowWool(SheepRegrowWoolEvent ev) {
            lib.callEvent("SheepRegrowWool", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onSignChange(SignChangeEvent ev) {
            lib.callEvent("SignChange", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onSlimeSplit(SlimeSplitEvent ev) {
            lib.callEvent("SlimeSplit", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onSpawnChange(SpawnChangeEvent ev) {
            lib.callEvent("SpawnChange", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onStructureGrow(StructureGrowEvent ev) {
            lib.callEvent("StructureGrow", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onThunderChange(ThunderChangeEvent ev) {
            lib.callEvent("ThunderChange", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onVehicleBlockCollision(VehicleBlockCollisionEvent ev) {
            lib.callEvent("VehicleBlockCollision", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onVehicleEntityCollision(VehicleEntityCollisionEvent ev) {
            lib.callEvent("VehicleEntityCollision", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onVehicleCreate(VehicleCreateEvent ev) {
            lib.callEvent("VehicleCreate", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onVehicleDamage(VehicleDamageEvent ev) {
            lib.callEvent("VehicleDamage", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onVehicleDestroy(VehicleDestroyEvent ev) {
            lib.callEvent("VehicleDestroy", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onVehicleEnter(VehicleEnterEvent ev) {
            lib.callEvent("VehicleEnter", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onVehicleExit(VehicleExitEvent ev) {
            lib.callEvent("VehicleExit", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onVehicleMove(VehicleMoveEvent ev) {
            lib.callEvent("VehicleMove", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onVehicleUpdate(VehicleUpdateEvent ev) {
            lib.callEvent("VehicleUpdate", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onWeatherChange(WeatherChangeEvent ev) {
            lib.callEvent("WeatherChange", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onWorldInit(WorldInitEvent ev) {
            lib.callEvent("WorldInit", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onWorldLoad(WorldLoadEvent ev) {
            lib.callEvent("WorldLoad", ev);
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onWorldSave(WorldSaveEvent ev) {
            lib.callEvent("WorldSave", ev);
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onWorldUnload(WorldUnloadEvent ev) {
            lib.callEvent("WorldUnload", ev);
        }
    }

    public EventLibrary(JavaPlugin plugin) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new InternalListener(this), plugin);
        for (Class<? extends EventListener> l : listeners) {
            try {
                manager.registerEvents(l.getConstructor(EventLibrary.class)
                        .newInstance(this), plugin);
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Could not register " +
                        l.getSimpleName(), e);
            }
        }
    }

    public void reset() {
        handlers.clear();
    }

    public static void registerListener(Class<? extends EventListener>
                                                listener) {
        Bukkit.getPluginManager().getPlugin("LuaSpigot").getLogger()
                .info("Added listener " + listener.getSimpleName());
        listeners.add(listener);
    }

    public static void deregisterListener(Class<? extends EventListener>
                                                  listener) {
        Bukkit.getPluginManager().getPlugin("LuaSpigot").getLogger()
                .info("Removed listener " + listener.getSimpleName());
        listeners.remove(listener);
    }

    public void callEvent(String name, Event ev) {
        callEvent(name, new LuaEvent(ev));
    }

    public void callEvent(String name, LuaEvent ev) {
        Varargs result = call(VarargBuilder.build(LuaValue.valueOf(name), ev));
        if (result.checkboolean(1)) {
            if (ev instanceof Cancellable) {
                ((Cancellable) ev).setCancelled(true);
            }
        }
    }

    @Override
    public String getName() {
        return "event";
    }

    @LibFunctionDef(name = "addHandler")
    public Varargs addHandler(Varargs args) {
        String eventName = args.checkjstring(1);
        LuaFunction func = args.checkfunction(2);
        System.out.println("attempting to add handler");

        Set<LuaFunction> set = handlers.computeIfAbsent(eventName,
                k -> new HashSet<>());

        set.add(func);

        return NIL;
    }

    @LibFunctionDef(name = "removeHandler")
    public Varargs removeHandler(Varargs args) {
        String eventName = args.checkjstring(1);
        LuaFunction func = args.checkfunction(2);

        Set<LuaFunction> set = handlers.get(eventName);
        if (set != null) {
            set.remove(func);
        }

        return NIL;
    }

    @LibFunctionDef(name = "clearHandlers")
    public Varargs clearHandlers(Varargs args) {
        String eventName = args.checkjstring(1);
        handlers.remove(eventName);
        return NIL;
    }

    @LibFunctionDef(name = "call")
    public Varargs call(Varargs args) {
        String eventName = args.checkjstring(1);
        LuaTable tbl = args.checktable(2);

        boolean cancel = false;

        Set<LuaFunction> set = handlers.get(eventName);
        if (set != null) {
            for (LuaFunction func : set) {
                LuaValue ret = func.call(tbl);
                LuaBoolean res = ret.equals(NIL) ? LuaBoolean.FALSE :
                        (LuaBoolean) ret;
                if (res.equals(LuaBoolean.TRUE)) {
                    cancel = true;
                }
            }
        }

        return cancel ? LuaBoolean.TRUE : LuaBoolean.FALSE;
    }
}
