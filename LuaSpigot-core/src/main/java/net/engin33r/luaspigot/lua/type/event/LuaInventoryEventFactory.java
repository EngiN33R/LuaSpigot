package net.engin33r.luaspigot.lua.type.event;

import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.type.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.inventory.*;
import org.luaj.vm2.*;

public class LuaInventoryEventFactory {
    public static void build(InventoryEvent ev, LuaEvent lev) {
        lev.registerField("inventory", new LuaInventory(ev.getInventory()));
        lev.registerField("viewers", TableUtils.tableFrom(ev.getViewers(),
                LuaEntity::new));

        if (ev instanceof CraftItemEvent) {
            CraftItemEvent cev = (CraftItemEvent) ev;
            lev.registerField("recipe", new LuaRecipe(cev.getRecipe()));
        }

        if (ev instanceof EnchantItemEvent) {
            EnchantItemEvent cev = (EnchantItemEvent) ev;
            lev.registerField("block", new LuaBlock(cev.getEnchantBlock()));
            lev.registerField("player", new LuaPlayer(cev.getEnchanter()));
            lev.registerField("button", LuaNumber.valueOf(cev.whichButton()));
            lev.registerField("enchantments", TableUtils.tableFrom(
                    cev.getEnchantsToAdd(),
                    k -> LuaString.valueOf(k.getName()),
                    LuaNumber::valueOf));
            lev.registerLinkedField("cost",
                    val -> cev.setExpLevelCost(val.checkint()),
                    () -> LuaNumber.valueOf(cev.getExpLevelCost()));
        }

        if (ev instanceof InventoryCloseEvent) {
            InventoryCloseEvent cev = (InventoryCloseEvent) ev;
            lev.registerField("player", new LuaPlayer((Player)
                    cev.getPlayer()));
        }

        if (ev instanceof InventoryInteractEvent) {
            InventoryInteractEvent cev = (InventoryInteractEvent) ev;
            lev.registerLinkedField("result",
                    val -> cev.setResult(
                            TypeUtils.getEnum(val, Event.Result.class)),
                    () -> TypeUtils.checkEnum(cev.getResult()));
            lev.registerField("player", new LuaPlayer((Player)
                    cev.getWhoClicked()));

            if (ev instanceof InventoryClickEvent) {
                InventoryClickEvent ccev = (InventoryClickEvent) ev;
                lev.registerField("action", LuaString.valueOf(
                        ccev.getAction().toString()));
                lev.registerField("click", LuaString.valueOf(
                        ccev.getClick().toString()));
                lev.registerField("cursor", new LuaItem(ccev.getCursor()));
                lev.registerField("button", LuaNumber.valueOf(
                        ccev.getHotbarButton()));
                lev.registerField("slot", LuaNumber.valueOf(ccev.getSlot()));
                lev.registerField("rawSlot", LuaNumber.valueOf(
                        ccev.getRawSlot()));
                lev.registerField("slotType", LuaString.valueOf(
                        ccev.getSlotType().toString()));

                lev.registerField("clickLeft", LuaBoolean.valueOf(
                        ccev.isLeftClick()));
                lev.registerField("clickRight", LuaBoolean.valueOf(
                        ccev.isRightClick()));
                lev.registerField("clickShift", LuaBoolean.valueOf(
                        ccev.isShiftClick()));

                lev.registerLinkedField("currentItem",
                        val -> ccev.setCurrentItem(
                                TypeUtils.handleOf(val, LuaItem.class)),
                        () -> new LuaItem(ccev.getCurrentItem()));
            }

            if (ev instanceof InventoryDragEvent) {
                InventoryDragEvent ccev = (InventoryDragEvent) ev;
                lev.registerField("oldCursor", new LuaItem(
                        ccev.getOldCursor()));
                lev.registerField("slots", TableUtils.tableFrom(
                        ccev.getInventorySlots(),
                        LuaNumber::valueOf));
                lev.registerField("rawSlots", TableUtils.tableFrom(
                        ccev.getRawSlots(),
                        LuaNumber::valueOf));
                lev.registerField("type", LuaString.valueOf(
                        ccev.getType().toString()));
                lev.registerField("newItems", TableUtils.tableFrom(
                        ccev.getNewItems(), LuaNumber::valueOf, LuaItem::new));

                lev.registerLinkedField("cursor",
                        val -> ccev.setCursor(
                                TypeUtils.handleOf(val, LuaItem.class)),
                        () -> new LuaItem(ccev.getCursor()));
            }
        }

        if (ev instanceof InventoryOpenEvent) {
            InventoryOpenEvent cev = (InventoryOpenEvent) ev;
            lev.registerField("player", new LuaPlayer(
                    (Player) cev.getPlayer()));
        }

        if (ev instanceof PrepareItemCraftEvent) {
            PrepareItemCraftEvent cev = (PrepareItemCraftEvent) ev;
            lev.registerField("recipe", new LuaRecipe(cev.getRecipe()));
            lev.registerField("repair", LuaBoolean.valueOf(cev.isRepair()));
        }

        if (ev instanceof PrepareItemEnchantEvent) {
            PrepareItemEnchantEvent cev = (PrepareItemEnchantEvent) ev;
            lev.registerField("block", new LuaBlock(cev.getEnchantBlock()));
            lev.registerField("enchanter", new LuaPlayer(cev.getEnchanter()));
            lev.registerField("repair", LuaNumber.valueOf(
                    cev.getEnchantmentBonus()));
            lev.registerField("item", new LuaItem(cev.getItem()));
            // TODO: Process enchantment offers
        }
    }
}
