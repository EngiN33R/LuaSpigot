package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.luaj.vm2.LuaValue;

/**
 * Wrapper type representing a single block within the world.
 */
public class LuaBlock extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private Block block;
    private Material matCache;

    public LuaBlock(Block block) {
        this.block = block;

        registerLinkedField("material", new MaterialField(this));
    }

    public LuaBlock(Location loc) {
        this(loc.getBlock());
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "block";
    }

    private class MaterialField extends LinkedField<LuaBlock> {
        public MaterialField(LuaBlock self) { super(self); }
        @Override public String getName() { return "material"; }

        @Override
        public void update(LuaValue val) {
            matCache = Material.getMaterial(val.checkjstring());
            block.setType(matCache);
        }

        @Override
        public LuaValue query() {
            if (!matCache.equals(block.getType())) {
                matCache = block.getType();
            }
            return LuaValue.valueOf(matCache.name().toLowerCase());
        }
    }
}
