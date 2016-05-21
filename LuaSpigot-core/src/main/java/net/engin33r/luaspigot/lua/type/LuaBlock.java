package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.Method;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
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
        this.matCache = block.getType();

        registerField("dynamic", LuaBoolean.valueOf(true));

        registerLinkedField("material", new MaterialField());
        registerLinkedField("data", new DataField());
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
        registerField("light", LuaNumber.valueOf(block.getLightLevel()));
        registerField("world", new LuaWorld(block.getWorld()));
    }

    public LuaBlock(BlockState state) {
        this.state = state;
        this.matCache = state.getType();

        registerField("block", new LuaBlock(state.getBlock()));

        registerLinkedField("material", new MaterialField());
        registerLinkedField("data", new DataField());
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

    public Object getHandle() {
        return dynamic ? block : state;
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
            if (dynamic) {
                if (!matCache.equals(block.getType())) {
                    matCache = block.getType();
                }
            } else {
                if (!matCache.equals(state.getType())) {
                    matCache = state.getType();
                }
            }

            return LuaString.valueOf(matCache.toString());
        }
    }

    @SuppressWarnings("deprecation")
    private class DataField extends LinkedField<LuaBlock> {
        @Override
        public void update(LuaValue val) {
            if (dynamic)
                block.setData((byte) val.checkint());
            else
                state.setData(new MaterialData(block.getType(),
                        (byte) val.checkint()));
        }

        @Override
        public LuaValue query() {
            if (dynamic) {
                return LuaNumber.valueOf(block.getData());
            } else {
                return LuaNumber.valueOf(state.getData().getData());
            }
        }
    }
}
