package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.Method;
import net.engin33r.luaspigot.lua.WeakType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.luaj.vm2.*;

/**
 * Wrapper type representing a single block within the world.
 */
public class LuaBlock extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private Block block;
    private BlockState state;
    private boolean dynamic = false;
    private Material matCache;

    public LuaBlock(Block block) {
        this.block = block;
        this.dynamic = true;

        registerField("dynamic", LuaBoolean.valueOf(true));

        registerLinkedField("material", new MaterialField(this));
        registerField("x", LuaNumber.valueOf(block.getX()));
        registerField("y", LuaNumber.valueOf(block.getY()));
        registerField("z", LuaNumber.valueOf(block.getZ()));

        registerField("biome", LuaString.valueOf(block.getBiome().toString()));
        registerField("empty", LuaBoolean.valueOf(block.isEmpty()));
        registerField("humidity", LuaNumber.valueOf(block.getHumidity()));
        registerField("liquid", LuaBoolean.valueOf(block.isLiquid()));
        registerField("location", new LuaLocation(block.getLocation()));
        registerField("temperature", LuaNumber.valueOf(block
                .getTemperature()));
        registerField("world", new LuaWorld(block.getWorld()));
    }

    public LuaBlock(BlockState state) {
        this.state = state;

        registerField("block", new LuaBlock(state.getBlock()));
        registerLinkedField("material", new MaterialField(this));
        registerField("x", LuaNumber.valueOf(state.getX()));
        registerField("y", LuaNumber.valueOf(state.getY()));
        registerField("z", LuaNumber.valueOf(state.getZ()));
        registerField("light", LuaNumber.valueOf(state.getLightLevel()));
        registerField("location", new LuaLocation(state.getLocation()));
        registerField("world", new LuaWorld(state.getWorld()));

        registerMethod("update", new Method<LuaBlock>(this) {
            @Override
            public Varargs call(Varargs args) {
                state.update(args.optboolean(1, false),
                        args.optboolean(2, true));
                return NIL;
            }
        });
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

        @Override
        public void update(LuaValue val) {
            matCache = Material.getMaterial(val.checkjstring());
            if (dynamic)
                block.setType(matCache);
            else
                state.setType(matCache);
        }

        @Override
        public LuaValue query() {
            if (dynamic)
                if (!matCache.equals(block.getType())) {
                    matCache = block.getType();
                }
            else
                if (!matCache.equals(state.getType())) {
                    matCache = state.getType();
                }

            return LuaValue.valueOf(matCache.name().toLowerCase());
        }
    }
}
