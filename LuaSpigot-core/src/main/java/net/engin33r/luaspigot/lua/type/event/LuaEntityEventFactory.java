package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.*;
import net.engin33r.luaspigot.lua.type.*;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.*;

import static org.luaj.vm2.LuaValue.NIL;

public class LuaEntityEventFactory {
    public static void build(EntityEvent ev, LuaEvent lev) {
        lev.registerField("entity", new LuaEntity(ev.getEntity()));
        lev.registerField("type", LuaString.valueOf(ev.getEntityType()
                .toString()));

        if (ev instanceof CreatureSpawnEvent) {
            lev.registerField("location", new LuaLocation(
                    ((CreatureSpawnEvent) ev).getLocation()));
            lev.registerField("reason", LuaString.valueOf(
                    ((CreatureSpawnEvent) ev).getSpawnReason().toString()));
        }

        if (ev instanceof CreeperPowerEvent) {
            lev.registerField("cause", LuaString.valueOf(
                    ((CreeperPowerEvent) ev).getCause().toString()));
            lev.registerField("strike", new LuaEntity(((CreeperPowerEvent) ev)
                    .getLightning()));
        }

        if (ev instanceof EntityChangeBlockEvent) {
            lev.registerField("block", new LuaBlock(
                    ((EntityChangeBlockEvent) ev).getBlock()));
            lev.registerField("block", LuaString.valueOf(
                    ((EntityChangeBlockEvent) ev).getTo().toString()));
        }

        if (ev instanceof EntityCombustByBlockEvent) {
            Block block = ((EntityCombustByBlockEvent) ev).getCombuster();
            if (block != null) lev.registerField("block", new LuaBlock(block));
        }

        if (ev instanceof EntityCombustByEntityEvent) {
            lev.registerField("combuster", new LuaEntity(
                    ((EntityCombustByEntityEvent) ev).getCombuster()));
        }

        if (ev instanceof EntityCombustEvent) {
            lev.registerLinkedField("duration", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityCombustEvent) ev).setDuration(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(
                            ((EntityCombustEvent) ev).getDuration());
                }
            });
        }

        if (ev instanceof EntityCreatePortalEvent) {
            LuaTable blocks = LuaTable.tableOf();
            for (BlockState bs : ((EntityCreatePortalEvent) ev).getBlocks()) {
                blocks.set(blocks.length()+1, new LuaBlock(bs));
            }
            lev.registerField("blocks", blocks);

            lev.registerField("type", LuaString.valueOf(
                    ((EntityCreatePortalEvent) ev).getPortalType().toString()));
        }

        if (ev instanceof EntityDamageByBlockEvent) {
            if (((EntityDamageByBlockEvent) ev).getDamager() != null)
                lev.registerField("damager", new LuaBlock(
                        ((EntityDamageByBlockEvent) ev).getDamager()));
        }

        if (ev instanceof EntityDamageByEntityEvent) {
            lev.registerField("damager", new LuaEntity(
                    ((EntityDamageByEntityEvent) ev).getDamager()));
        }

        if (ev instanceof EntityDamageEvent) {
            lev.registerField("cause", LuaString.valueOf(
                    ((EntityDamageEvent) ev).getCause().toString()));
            lev.registerDynamicField("final", new DynamicField<LuaEvent>(
                    lev) {
                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((EntityDamageEvent) ev)
                            .getFinalDamage());
                }
            });

            lev.registerLinkedField("raw", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityDamageEvent) ev).setDamage(val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((EntityDamageEvent) ev)
                            .getDamage());
                }
            });

            lev.registerField("modified", new LuaTable() {
                @Override
                public void set(LuaValue key, LuaValue value) {
                    ((EntityDamageEvent) ev).setDamage(EntityDamageEvent
                            .DamageModifier.valueOf(key.checkjstring()),
                            value.checkdouble());
                }

                @Override
                public LuaValue get(LuaValue key) {
                    return LuaNumber.valueOf(((EntityDamageEvent) ev).getDamage(
                            EntityDamageEvent.DamageModifier
                                    .valueOf(key.checkjstring())));
                }
            });
        }

        if (ev instanceof EntityDeathEvent) {
            lev.registerLinkedField("exp", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityDeathEvent) ev).setDroppedExp(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((EntityDeathEvent) ev)
                            .getDroppedExp());
                }
            });

            lev.registerField("items", TableUtils.tableFrom(
                    ((EntityDeathEvent) ev).getDrops(),
                    i -> new LuaItem((ItemStack) i)));
        }

        if (ev instanceof EntityExplodeEvent) {
            lev.registerField("blocks", TableUtils.tableFrom(
                    ((EntityExplodeEvent) ev).blockList(),
                    b -> new LuaBlock((Block) b)));

            lev.registerField("location", new LuaLocation(
                    ((EntityExplodeEvent) ev).getLocation()));

            lev.registerLinkedField("yield", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityExplodeEvent) ev).setYield((float)
                            val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((EntityExplodeEvent) ev)
                            .getYield());
                }
            });
        }

        if (ev instanceof EntityInteractEvent) {
            lev.registerField("block", new LuaBlock(
                    ((EntityInteractEvent) ev).getBlock()));
        }

        if (ev instanceof EntityPortalEnterEvent) {
            lev.registerField("location", new LuaLocation(
                    ((EntityPortalEnterEvent) ev).getLocation()));
        }

        if (ev instanceof EntityPortalEvent) {
            // TODO: Travel agent
        }

        if (ev instanceof EntityPortalExitEvent) {
            lev.registerField("before", new LuaVector(
                    ((EntityPortalExitEvent) ev).getBefore()));
            lev.registerLinkedField("after", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityPortalExitEvent) ev).setAfter(
                            ((LuaVector) val.checktable()).getVector());
                }

                @Override
                public LuaValue query() {
                    return new LuaVector(((EntityPortalExitEvent) ev)
                            .getAfter());
                }
            });
        }

        if (ev instanceof EntityRegainHealthEvent) {
            lev.registerLinkedField("amount", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityRegainHealthEvent) ev).setAmount(
                            val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((EntityRegainHealthEvent) ev)
                            .getAmount());
                }
            });
            lev.registerField("reason", LuaString.valueOf(
                    ((EntityRegainHealthEvent) ev).getRegainReason()
                            .toString()));
        }

        if (ev instanceof EntityShootBowEvent) {
            lev.registerField("bow", new LuaItem(((EntityShootBowEvent) ev)
                    .getBow()));
            lev.registerField("force", LuaNumber.valueOf(
                    ((EntityShootBowEvent) ev).getForce()));
            lev.registerLinkedField("projectile", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityShootBowEvent) ev).setProjectile(
                            ((LuaEntity) val.checktable()).getEntity());
                }

                @Override
                public LuaValue query() {
                    return new LuaEntity(
                            ((EntityShootBowEvent) ev).getProjectile());
                }
            });
        }

        if (ev instanceof EntityTameEvent) {
            LuaTable tamer = LuaTable.tableOf();
            AnimalTamer owner = ((EntityTameEvent) ev).getOwner();
            tamer.set("uuid", new LuaUUID(owner.getUniqueId()));
            tamer.set("name", LuaString.valueOf(owner.getName()));
            lev.registerField("tamer", tamer);
        }

        if (ev instanceof EntityTargetEvent) {
            lev.registerLinkedField("target", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityTargetEvent) ev).setTarget(
                            ((LuaEntity) val.checktable()).getEntity());
                }

                @Override
                public LuaValue query() {
                    return new LuaEntity(((EntityTargetEvent) ev).getTarget());
                }
            });
            lev.registerField("reason", LuaString.valueOf(
                    ((EntityTargetEvent) ev).getReason().toString()));
        }

        if (ev instanceof EntityTeleportEvent) {
            lev.registerLinkedField("from", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityTeleportEvent) ev).setFrom(
                            ((LuaLocation) val.checktable()).getLocation());
                }

                @Override
                public LuaValue query() {
                    return new LuaLocation(
                            ((EntityTeleportEvent) ev).getFrom());
                }
            });
            lev.registerLinkedField("to", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EntityTeleportEvent) ev).setTo(
                            ((LuaLocation) val.checktable()).getLocation());
                }

                @Override
                public LuaValue query() {
                    return new LuaLocation(
                            ((EntityTeleportEvent) ev).getTo());
                }
            });
        }

        if (ev instanceof EntityUnleashEvent) {
            lev.registerField("reason", LuaString.valueOf(
                    ((EntityUnleashEvent) ev).getReason().toString()));
        }

        if (ev instanceof ExpBottleEvent) {
            lev.registerLinkedField("effect", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((ExpBottleEvent) ev).setShowEffect(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(
                            ((ExpBottleEvent) ev).getShowEffect());
                }
            });
            lev.registerLinkedField("exp", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((ExpBottleEvent) ev).setExperience(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(
                            ((ExpBottleEvent) ev).getExperience());
                }
            });
        }

        if (ev instanceof ExplosionPrimeEvent) {
            lev.registerLinkedField("fire", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((ExplosionPrimeEvent) ev).setFire(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(
                            ((ExplosionPrimeEvent) ev).getFire());
                }
            });
            lev.registerLinkedField("radius", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((ExplosionPrimeEvent) ev).setRadius(
                            (float) val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(
                            ((ExplosionPrimeEvent) ev).getRadius());
                }
            });
        }

        if (ev instanceof FoodLevelChangeEvent) {
            lev.registerLinkedField("food", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((FoodLevelChangeEvent) ev).setFoodLevel(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(
                            ((FoodLevelChangeEvent) ev).getFoodLevel());
                }
            });
        }

        if (ev instanceof HorseJumpEvent) {
            lev.registerLinkedField("power", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((HorseJumpEvent) ev).setPower((float) val.checkdouble());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(
                            ((HorseJumpEvent) ev).getPower());
                }
            });
        }

        if (ev instanceof ItemDespawnEvent) {
            lev.registerField("location", new LuaLocation(
                    ((ItemDespawnEvent) ev).getLocation()));
        }

        if (ev instanceof ItemSpawnEvent) {
            lev.registerField("location", new LuaLocation(
                    ((ItemSpawnEvent) ev).getLocation()));
        }

        if (ev instanceof PigZapEvent) {
            lev.registerField("lightning", new LuaEntity(((PigZapEvent) ev)
                    .getLightning()));
            lev.registerField("pigman", new LuaEntity(((PigZapEvent) ev)
                    .getPigZombie()));
        }

        if (ev instanceof PlayerDeathEvent) {
            lev.registerLinkedField("message", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerDeathEvent) ev).setDeathMessage(val.checkjstring());
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(
                            ((PlayerDeathEvent) ev).getDeathMessage());
                }
            });
            lev.registerLinkedField("keeplevel", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerDeathEvent) ev).setKeepLevel(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((PlayerDeathEvent) ev)
                            .getKeepLevel());
                }
            });
            lev.registerLinkedField("keepinv", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerDeathEvent) ev).setKeepInventory(val
                            .checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((PlayerDeathEvent) ev)
                            .getKeepInventory());
                }
            });
            lev.registerLinkedField("newexp", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {

                }

                @Override
                public LuaValue query() {
                    return null;
                }
            });
            lev.registerLinkedField("newtotalexp", new LinkedField<LuaEvent>(
                    lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerDeathEvent) ev).setNewTotalExp(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((PlayerDeathEvent) ev)
                            .getNewTotalExp());
                }
            });
            lev.registerLinkedField("newexp", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerDeathEvent) ev).setNewExp(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((PlayerDeathEvent) ev)
                            .getNewExp());
                }
            });
            lev.registerLinkedField("newlevel", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((PlayerDeathEvent) ev).setNewLevel(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((PlayerDeathEvent) ev)
                            .getNewLevel());
                }
            });
        }

        if (ev instanceof PotionSplashEvent) {
            lev.registerField("affected", TableUtils.tableFrom(
                    ((PotionSplashEvent) ev).getAffectedEntities(),
                    e -> new LuaEntity((Entity) e)
            ));
            lev.registerField("potion", new LuaEntity(((PotionSplashEvent) ev)
                    .getPotion()));

            lev.registerMethod("setIntensity", new Method<LuaEvent>(lev) {
                @Override
                public Varargs call(Varargs args) {
                    ((PotionSplashEvent) ev).setIntensity(
                            (LivingEntity) ((LuaEntity) args.checktable(1))
                                    .getEntity(),
                            args.checkdouble(2));
                    return NIL;
                }
            });
            lev.registerMethod("getIntensity", new Method<LuaEvent>(lev) {
                @Override
                public Varargs call(Varargs args) {
                    return LuaNumber.valueOf(((PotionSplashEvent) ev)
                            .getIntensity((LivingEntity) ((LuaEntity) args
                                    .checktable(1)).getEntity()));
                }
            });
            lev.registerField("intensity", new LuaTable() {
                @Override
                public LuaValue get(LuaValue key) {
                    return LuaNumber.valueOf(
                            ((PotionSplashEvent) ev).getIntensity((LivingEntity)
                                    ((LuaEntity) key).getEntity()));
                }

                @Override
                public void set(LuaValue key, LuaValue value) {
                    ((PotionSplashEvent) ev).setIntensity((LivingEntity)
                            ((LuaEntity) key).getEntity(), value.checkdouble());
                }
            });
        }

        if (ev instanceof ProjectileLaunchEvent ||
                ev instanceof ProjectileHitEvent) {
            LuaEntity e = (LuaEntity) lev.get("entity");
            Projectile proj = (Projectile) e.getEntity();
            e.registerLinkedField("shooter", new LinkedField<LuaEvent>() {
                @Override
                public void update(LuaValue val) {
                    TypeValidator.validate(val.checktable(), "player");
                    Player p = ((LuaPlayer) val.checktable()).getPlayer()
                            .getPlayer();
                    if (p == null) return;
                    proj.setShooter(p);
                }

                @Override
                public LuaValue query() {
                    if (proj.getShooter() instanceof Player)
                        return new LuaPlayer((Player) proj.getShooter());
                    return NIL;
                }
            });
            e.registerLinkedField("bounce", new LinkedField<LuaEvent>() {
                @Override
                public void update(LuaValue val) {
                    ((Projectile) e.getEntity()).setBounce(val.checkboolean());
                }

                @Override
                public LuaValue query() {
                    return LuaBoolean.valueOf(((Projectile) e.getEntity())
                            .doesBounce());
                }
            });
        }

        if (ev instanceof SheepDyeWoolEvent) {
            LinkedField<LuaEvent> colour = new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((SheepDyeWoolEvent) ev).setColor(DyeColor.valueOf(
                            val.checkjstring()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((SheepDyeWoolEvent) ev)
                            .getColor().toString());
                }
            };

            lev.registerLinkedField("colour", colour);
            lev.registerLinkedField("color", colour); // All right then
        }

        if (ev instanceof SlimeSplitEvent) {
            lev.registerLinkedField("count", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((SlimeSplitEvent) ev).setCount(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(((SlimeSplitEvent) ev).getCount());
                }
            });
        }
    }
}
