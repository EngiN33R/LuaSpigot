package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.DynamicField;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.type.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.inventory.*;
import org.luaj.vm2.*;

import java.util.Map;

public class LuaInventoryEventFactory {
    public static void build(InventoryEvent ev, LuaEvent lev) {
        lev.registerField("inventory", new LuaInventory(ev.getInventory()));
        lev.registerField("viewers", TableUtils.tableFrom(ev.getViewers(),
                e -> new LuaEntity((Entity) e)));
        // TODO: InventoryView

        if (ev instanceof CraftItemEvent) {
            lev.registerField("recipe", new LuaRecipe(((CraftItemEvent) ev)
                    .getRecipe()));
        }

        if (ev instanceof EnchantItemEvent) {
            lev.registerField("block", new LuaBlock(((EnchantItemEvent) ev)
                    .getEnchantBlock()));
            lev.registerField("player", new LuaPlayer(((EnchantItemEvent) ev)
                    .getEnchanter()));
            lev.registerField("button", LuaNumber.valueOf(
                    ((EnchantItemEvent) ev).whichButton()));

            Map<Enchantment, Integer> enchants = ((EnchantItemEvent) ev)
                    .getEnchantsToAdd();
            LuaTable enchantments = LuaTable.tableOf();
            for (Enchantment e : enchants.keySet()) {
                enchantments.set(LuaString.valueOf(e.getName()),
                        LuaNumber.valueOf(enchants.get(e)));
            }
            lev.registerField("enchantments", enchantments);

            lev.registerLinkedField("cost", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((EnchantItemEvent) ev).setExpLevelCost(val.checkint());
                }

                @Override
                public LuaValue query() {
                    return LuaNumber.valueOf(
                            ((EnchantItemEvent) ev).getExpLevelCost());
                }
            });
        }

        if (ev instanceof InventoryCloseEvent) {
            lev.registerField("player", new LuaPlayer((Player)
                    ((InventoryCloseEvent) ev).getPlayer()));
        }

        if (ev instanceof InventoryInteractEvent) {
            lev.registerLinkedField("result", new LinkedField<LuaEvent>(lev) {
                @Override
                public void update(LuaValue val) {
                    ((InventoryInteractEvent) ev).setResult(Event.Result
                            .valueOf(val.checkjstring()));
                }

                @Override
                public LuaValue query() {
                    return LuaString.valueOf(((InventoryInteractEvent) ev)
                            .getResult().toString());
                }
            });

            lev.registerField("player", new LuaPlayer((Player)
                    ((InventoryInteractEvent) ev).getWhoClicked()));

            if (ev instanceof InventoryClickEvent) {
                lev.registerField("action", LuaString.valueOf(
                        ((InventoryClickEvent) ev).getAction().toString()));
                lev.registerField("click", LuaString.valueOf(
                        ((InventoryClickEvent) ev).getClick().toString()));
                lev.registerField("cursor", new LuaItem(
                        ((InventoryClickEvent) ev).getCursor()));
                lev.registerField("button", LuaNumber.valueOf(
                        ((InventoryClickEvent) ev).getHotbarButton()));
                lev.registerField("slot", LuaNumber.valueOf(
                        ((InventoryClickEvent) ev).getSlot()));
                lev.registerField("rawSlot", LuaNumber.valueOf(
                        ((InventoryClickEvent) ev).getRawSlot()));
                lev.registerField("slotType", LuaString.valueOf(
                        ((InventoryClickEvent) ev).getSlotType().toString()));

                lev.registerField("clickLeft", LuaBoolean.valueOf(
                        ((InventoryClickEvent) ev).isLeftClick()));
                lev.registerField("clickRight", LuaBoolean.valueOf(
                        ((InventoryClickEvent) ev).isRightClick()));
                lev.registerField("clickShift", LuaBoolean.valueOf(
                        ((InventoryClickEvent) ev).isShiftClick()));

                lev.registerLinkedField("currentItem", new LinkedField<LuaEvent>
                        (lev) {
                    @Override
                    public void update(LuaValue val) {
                        ((InventoryClickEvent) ev).setCurrentItem(
                                ((LuaItem) val.checktable()).getItem());
                    }

                    @Override
                    public LuaValue query() {
                        return new LuaItem(((InventoryClickEvent) ev)
                                .getCurrentItem());
                    }
                });
            }

            if (ev instanceof InventoryDragEvent) {
                lev.registerField("oldCursor", new LuaItem(
                        ((InventoryDragEvent) ev).getOldCursor()));
                lev.registerField("slots", TableUtils.tableFrom(
                        ((InventoryDragEvent) ev).getInventorySlots(),
                        i -> LuaNumber.valueOf((Integer) i)));
                lev.registerField("rawSlots", TableUtils.tableFrom(
                        ((InventoryDragEvent) ev).getRawSlots(),
                        i -> LuaNumber.valueOf((Integer) i)));
                lev.registerField("type", LuaString.valueOf(
                        ((InventoryDragEvent) ev).getType().toString()));

                LuaTable newitems = LuaTable.tableOf();
                for (Integer k : ((InventoryDragEvent) ev).getNewItems()
                        .keySet()) {
                    newitems.set(k, new LuaItem(((InventoryDragEvent) ev)
                            .getNewItems().get(k)));
                }
                lev.registerField("newItems", newitems);

                lev.registerLinkedField("cursor", new LinkedField<LuaEvent>(lev)
                {
                    @Override
                    public void update(LuaValue val) {
                        ((InventoryDragEvent) ev).setCursor(
                                ((LuaItem) val.checktable()).getItem());
                    }

                    @Override
                    public LuaValue query() {
                        return new LuaItem(((InventoryDragEvent) ev)
                                .getCursor());
                    }
                });
            }
        }

        if (ev instanceof InventoryOpenEvent) {
            lev.registerField("player", new LuaPlayer((Player)
                    ((InventoryOpenEvent) ev).getPlayer()));
        }

        if (ev instanceof PrepareItemCraftEvent) {
            lev.registerField("recipe", new LuaRecipe(
                    ((PrepareItemCraftEvent) ev).getRecipe()));
            lev.registerField("repair", LuaBoolean.valueOf(
                    ((PrepareItemCraftEvent) ev).isRepair()));
        }

        if (ev instanceof PrepareItemEnchantEvent) {
            lev.registerField("block", new LuaBlock(
                    ((PrepareItemEnchantEvent) ev).getEnchantBlock()));
            lev.registerField("enchanter", new LuaPlayer(
                    ((PrepareItemEnchantEvent) ev).getEnchanter()));
            lev.registerField("repair", LuaNumber.valueOf(
                    ((PrepareItemEnchantEvent) ev).getEnchantmentBonus()));

            lev.registerDynamicField("item", new DynamicField<LuaEvent>(lev) {
                @Override
                public LuaValue query() {
                    return new LuaItem(((PrepareItemEnchantEvent) ev)
                            .getItem());
                }
            });
            lev.registerField("costs", new LuaTable() {
                @Override
                public LuaValue get(LuaValue key) {
                    int k = key.checkint();
                    int[] costs = ((PrepareItemEnchantEvent) ev)
                            .getExpLevelCostsOffered();
                    if (k <= costs.length) {
                        return LuaNumber.valueOf(costs[k]);
                    }
                    return NIL;
                }

                @Override
                public void set(LuaValue key, LuaValue value) {
                    int k = key.checkint();
                    int[] costs = ((PrepareItemEnchantEvent) ev)
                            .getExpLevelCostsOffered();
                    if (k <= costs.length) {
                        costs[k] = value.checkint();
                    }
                }
            });
        }
    }
}
