package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.Method;
import net.engin33r.luaspigot.lua.WrapperType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.luaj.vm2.*;

/**
 * Wrapper type representing a single block within the world.
 */
public class LuaBlock extends WrapperType<LuaBlock.BlockHandle> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public static class BlockHandle {
        private boolean dynamic = false;
        private Block block;
        private BlockState state;

        public BlockHandle(Block block) {
            this.dynamic = true;
            this.block = block;
        }

        public BlockHandle(BlockState state) {
            this.state = state;
        }

        public Material getType() {
            return dynamic ? block.getType() : state.getType();
        }

        public void setType(Material material) {
            if (dynamic) {
                block.setType(material);
            } else {
                state.setType(material);
            }
        }

        @SuppressWarnings("deprecation")
        public byte getData() {
            return dynamic ? block.getData() : state.getData().getData();
        }

        @SuppressWarnings("deprecation")
        public void setData(byte data) {
            if (dynamic) {
                block.setData(data);
            } else {
                state.setData(new MaterialData(state.getType(), data));
            }
        }
    }

    public LuaBlock(Block block) {
        super(new BlockHandle(block));

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
        super(new BlockHandle(state));

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
            getHandle().setType(Material.getMaterial(val.checkjstring()));
        }

        @Override
        public LuaValue query() {
            return LuaString.valueOf(getHandle().getType().name());
        }
    }

    @SuppressWarnings("deprecation")
    private class DataField extends LinkedField<LuaBlock> {
        @Override
        public void update(LuaValue val) {
            getHandle().setData((byte) val.checkint());
        }

        @Override
        public LuaValue query() {
            return LuaNumber.valueOf(getHandle().getData());
        }
    }
}
