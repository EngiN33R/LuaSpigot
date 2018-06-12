package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.*;
import net.engin33r.luaspigot.lua.type.*;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.luaj.vm2.*;

public class LuaEntityEventFactory {
    public static void build(EntityEvent ev, LuaEvent lev) {
        lev.registerField("entity", new LuaEntity(ev.getEntity()));
        lev.registerField("type", LuaString.valueOf(ev.getEntityType()
                .toString()));

        if (ev instanceof CreatureSpawnEvent) {
            CreatureSpawnEvent cev = (CreatureSpawnEvent) ev;
            lev.registerField("location", new LuaLocation(cev.getLocation()));
            lev.registerField("reason", LuaString.valueOf(
                    cev.getSpawnReason().toString()));
        }

        if (ev instanceof CreeperPowerEvent) {
            CreeperPowerEvent cev = (CreeperPowerEvent) ev;
            lev.registerField("cause", LuaString.valueOf(
                    cev.getCause().toString()));
            lev.registerField("strike", new LuaEntity(cev.getLightning()));
        }

        if (ev instanceof EntityChangeBlockEvent) {
            EntityChangeBlockEvent cev = (EntityChangeBlockEvent) ev;
            lev.registerField("block", new LuaBlock(cev.getBlock()));
            lev.registerField("block", LuaString.valueOf(
                    cev.getTo().toString()));
        }

        if (ev instanceof EntityCombustByBlockEvent) {
            EntityCombustByBlockEvent cev = (EntityCombustByBlockEvent) ev;
            Block block = cev.getCombuster();
            if (block != null) lev.registerField("block", new LuaBlock(block));
        }

        if (ev instanceof EntityCombustByEntityEvent) {
            EntityCombustByEntityEvent cev = (EntityCombustByEntityEvent) ev;
            lev.registerField("combuster", new LuaEntity(
                    cev.getCombuster()));
        }

        if (ev instanceof EntityCombustEvent) {
            EntityCombustEvent cev = (EntityCombustEvent) ev;
            lev.registerLinkedField("duration",
                    val -> cev.setDuration(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getDuration()));
        }

        if (ev instanceof EntityCreatePortalEvent) {
            EntityCreatePortalEvent cev = (EntityCreatePortalEvent) ev;
            lev.registerField("blocks", TableUtils.tableFrom(cev.getBlocks(),
                    LuaBlock::new));
            lev.registerField("type", LuaString.valueOf(
                    cev.getPortalType().toString()));
        }

        if (ev instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent cev = (EntityDamageByBlockEvent) ev;
            if (cev.getDamager() != null)
                lev.registerField("damager", new LuaBlock(cev.getDamager()));
        }

        if (ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent cev = (EntityDamageByEntityEvent) ev;
            lev.registerField("damager", new LuaEntity(cev.getDamager()));
        }

        if (ev instanceof EntityDamageEvent) {
            EntityDamageEvent cev = (EntityDamageEvent) ev;
            lev.registerField("cause", LuaString.valueOf(
                    cev.getCause().toString()));
            lev.registerDynamicField("final",
                    () -> LuaNumber.valueOf(cev.getFinalDamage()));

            lev.registerLinkedField("raw",
                    val -> cev.setDamage(val.checkdouble()),
                    () -> LuaNumber.valueOf(cev.getDamage()));
        }

        if (ev instanceof EntityDeathEvent) {
            EntityDeathEvent cev = (EntityDeathEvent) ev;
            lev.registerLinkedField("exp",
                    val -> cev.setDroppedExp(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getDroppedExp()));
            lev.registerField("items", TableUtils.tableFrom(
                    cev.getDrops(), LuaItem::new));
        }

        if (ev instanceof EntityExplodeEvent) {
            EntityExplodeEvent cev = (EntityExplodeEvent) ev;
            lev.registerField("blocks", TableUtils.tableFrom(
                    cev.blockList(), LuaBlock::new));
            lev.registerField("location", new LuaLocation(
                    cev.getLocation()));
            lev.registerLinkedField("yield",
                    val -> cev.setYield((float) val.checkdouble()),
                    () -> LuaNumber.valueOf(cev.getYield()));
        }

        if (ev instanceof EntityInteractEvent) {
            EntityInteractEvent cev = (EntityInteractEvent) ev;
            lev.registerField("block", new LuaBlock(cev.getBlock()));
        }

        if (ev instanceof EntityPickupItemEvent) {
            EntityPickupItemEvent cev = (EntityPickupItemEvent) ev;
            lev.registerField("item", new LuaItem(cev.getItem()));
            lev.registerField("remaining", LuaNumber.valueOf(
                    cev.getRemaining()));
        }

        if (ev instanceof EntityPortalEnterEvent) {
            EntityPortalEnterEvent cev = (EntityPortalEnterEvent) ev;
            lev.registerField("location", new LuaLocation(cev.getLocation()));
        }

        if (ev instanceof EntityPortalEvent) {
            EntityPortalEvent cev = (EntityPortalEvent) ev;
            lev.registerField("from", new LuaLocation(cev.getFrom()));
            lev.registerField("to", new LuaLocation(cev.getTo()));
        }

        if (ev instanceof EntityPortalExitEvent) {
            EntityPortalExitEvent cev = (EntityPortalExitEvent) ev;
            lev.registerField("before", new LuaVector(cev.getBefore()));
            lev.registerLinkedField("after",
                    val -> cev.setAfter(
                            TypeUtils.handleOf(val, LuaVector.class)),
                    () -> new LuaVector(cev.getAfter()));
        }

        if (ev instanceof EntityRegainHealthEvent) {
            EntityRegainHealthEvent cev = (EntityRegainHealthEvent) ev;
            lev.registerLinkedField("amount",
                    val -> cev.setAmount(val.checkdouble()),
                    () -> LuaNumber.valueOf(cev.getAmount()));
            lev.registerField("reason", LuaString.valueOf(
                    cev.getRegainReason().toString()));
        }

        if (ev instanceof EntityShootBowEvent) {
            EntityShootBowEvent cev = (EntityShootBowEvent) ev;
            lev.registerField("bow", new LuaItem(cev.getBow()));
            lev.registerField("force", LuaNumber.valueOf(cev.getForce()));
            lev.registerLinkedField("projectile",
                    val -> cev.setProjectile(
                            TypeUtils.handleOf(val, LuaEntity.class)),
                    () -> new LuaEntity(cev.getProjectile()));
        }

        if (ev instanceof EntityTameEvent) {
            EntityTameEvent cev = (EntityTameEvent) ev;
            LuaTable tamer = LuaTable.tableOf();
            AnimalTamer owner = cev.getOwner();
            tamer.set("uuid", new LuaUUID(owner.getUniqueId()));
            tamer.set("value", LuaString.valueOf(owner.getName()));
            lev.registerField("tamer", tamer);
        }

        if (ev instanceof EntityTargetEvent) {
            EntityTargetEvent cev = (EntityTargetEvent) ev;
            lev.registerLinkedField("target",
                    val -> cev.setTarget(
                            TypeUtils.handleOf(val, LuaEntity.class)),
                    () -> new LuaEntity(cev.getTarget()));
            lev.registerField("reason", LuaString.valueOf(
                    cev.getReason().toString()));
        }

        if (ev instanceof EntityTeleportEvent) {
            EntityTeleportEvent cev = (EntityTeleportEvent) ev;
            lev.registerLinkedField("from",
                    val -> cev.setFrom(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getFrom()));
            lev.registerLinkedField("to",
                    val -> cev.setTo(
                            TypeUtils.handleOf(val, LuaLocation.class)),
                    () -> new LuaLocation(cev.getTo()));
        }

        if (ev instanceof EntityUnleashEvent) {
            EntityUnleashEvent cev = (EntityUnleashEvent) ev;
            lev.registerField("reason", LuaString.valueOf(
                    cev.getReason().toString()));
        }

        if (ev instanceof ExpBottleEvent) {
            ExpBottleEvent cev = (ExpBottleEvent) ev;
            lev.registerLinkedField("effect",
                    val -> cev.setShowEffect(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.getShowEffect()));
            lev.registerLinkedField("from",
                    val -> cev.setExperience(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getExperience()));
        }

        if (ev instanceof ExplosionPrimeEvent) {
            ExplosionPrimeEvent cev = (ExplosionPrimeEvent) ev;
            lev.registerLinkedField("fire",
                    val -> cev.setFire(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.getFire()));
            lev.registerLinkedField("radius",
                    val -> cev.setRadius((float) val.checkdouble()),
                    () -> LuaValue.valueOf(cev.getRadius()));
        }

        if (ev instanceof FoodLevelChangeEvent) {
            FoodLevelChangeEvent cev = (FoodLevelChangeEvent) ev;
            lev.registerLinkedField("food",
                    val -> cev.setFoodLevel(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getFoodLevel()));
        }

        if (ev instanceof HorseJumpEvent) {
            HorseJumpEvent cev = (HorseJumpEvent) ev;
            lev.registerField("power", LuaNumber.valueOf(cev.getPower()));
        }

        if (ev instanceof ItemDespawnEvent) {
            ItemDespawnEvent cev = (ItemDespawnEvent) ev;
            lev.registerField("location", new LuaLocation(cev.getLocation()));
        }

        if (ev instanceof ItemSpawnEvent) {
            ItemSpawnEvent cev = (ItemSpawnEvent) ev;
            lev.registerField("location", new LuaLocation(cev.getLocation()));
        }

        if (ev instanceof PigZapEvent) {
            PigZapEvent cev = (PigZapEvent) ev;
            lev.registerField("lightning", new LuaEntity(cev.getLightning()));
            lev.registerField("pigman", new LuaEntity(cev.getPigZombie()));
        }

        if (ev instanceof PlayerDeathEvent) {
            PlayerDeathEvent cev = (PlayerDeathEvent) ev;
            lev.registerLinkedField("message",
                    val -> cev.setDeathMessage(val.checkjstring()),
                    () -> LuaString.valueOf(cev.getDeathMessage()));
            lev.registerLinkedField("keepLevel",
                    val -> cev.setKeepLevel(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.getKeepLevel()));
            lev.registerLinkedField("keepInventory",
                    val -> cev.setKeepInventory(val.checkboolean()),
                    () -> LuaBoolean.valueOf(cev.getKeepInventory()));
            lev.registerLinkedField("newExp",
                    val -> cev.setNewExp(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getNewExp()));
            lev.registerLinkedField("newTotalExp",
                    val -> cev.setNewTotalExp(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getNewTotalExp()));
            lev.registerLinkedField("newLevel",
                    val -> cev.setNewLevel(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getNewLevel()));
        }

        if (ev instanceof PotionSplashEvent) {
            PotionSplashEvent cev = (PotionSplashEvent) ev;
            lev.registerField("affected", TableUtils.tableFrom(
                    cev.getAffectedEntities(), LuaEntity::new));
            lev.registerField("potion", new LuaEntity(cev.getPotion()));

            lev.registerField("intensity", new LuaTable() {
                @Override
                public LuaValue get(LuaValue key) {
                    return LuaNumber.valueOf(
                            cev.getIntensity((LivingEntity)
                                    TypeUtils.handleOf(key, LuaEntity.class)));
                }

                @Override
                public void set(LuaValue key, LuaValue value) {
                    cev.setIntensity((LivingEntity)
                            TypeUtils.handleOf(key, LuaEntity.class),
                            value.checkdouble());
                }
            });
        }

        if (ev instanceof ProjectileLaunchEvent ||
                ev instanceof ProjectileHitEvent) {
            LuaEntity e = (LuaEntity) lev.get("entity");
            Projectile proj = (Projectile) e.getHandle();

            e.registerLinkedField("shooter",
                    val -> {
                        Player p = TypeUtils.handleOf(val, LuaPlayer.class)
                                .getPlayer();
                        if (p != null) proj.setShooter(p);
                    },
                    () -> new LuaEntity((Entity) proj.getShooter()));
            e.registerLinkedField("bounce",
                    val -> proj.setBounce(val.checkboolean()),
                    () -> LuaBoolean.valueOf(proj.doesBounce()));
        }

        if (ev instanceof SheepDyeWoolEvent) {
            SheepDyeWoolEvent cev = (SheepDyeWoolEvent) ev;
            lev.registerLinkedField("color",
                    val -> cev.setColor(TypeUtils.getEnum(val, DyeColor.class)),
                    () -> TypeUtils.checkEnum(cev.getColor()));
            lev.set("colour", lev.get("color"));
        }

        if (ev instanceof SlimeSplitEvent) {
            SlimeSplitEvent cev = (SlimeSplitEvent) ev;
            lev.registerLinkedField("count",
                    val -> cev.setCount(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getCount()));
        }
    }
}
