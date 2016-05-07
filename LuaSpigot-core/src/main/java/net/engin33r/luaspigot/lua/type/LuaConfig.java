package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.TypeValidator;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import net.engin33r.luaspigot.lua.type.util.LuaColor;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.luaj.vm2.*;

import java.io.IOException;
import java.util.List;

public class LuaConfig extends WrapperType<ConfigurationSection> {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaConfig(ConfigurationSection handle) {
        super(handle);
    }

    @Override
    public LuaValue index(LuaValue key) {
        String k = key.checkjstring();
        return processYaml(getHandle(), k);
    }

    private LuaValue processObject(Object o) {
        if (o instanceof Boolean) {
            return LuaBoolean.valueOf((boolean) o);
        } else if (o instanceof Integer) {
            return LuaNumber.valueOf((int) o);
        } else if (o instanceof Double) {
            return LuaNumber.valueOf((double) o);
        } else if (o instanceof Long) {
            return LuaNumber.valueOf((long) o);
        } else if (o instanceof String) {
            return LuaString.valueOf((String) o);
        } else if (o instanceof ItemStack) {
            return new LuaItem((ItemStack) o);
        } else if (o instanceof OfflinePlayer) {
            return new LuaPlayer((OfflinePlayer) o);
        } else if (o instanceof Vector) {
            return new LuaVector((Vector) o);
        } else if (o instanceof Color) {
            return new LuaColor((Color) o);
        } else if (o instanceof ConfigurationSection) {
            return new LuaConfig((ConfigurationSection) o);
        } else if (o instanceof List) {
            return processList((List) o);
        }
        return LuaValue.NIL;
    }

    private LuaValue processList(List<?> list) {
        return TableUtils.tableFrom(list, this::processObject);
    }

    private LuaValue processYaml(ConfigurationSection yaml, String k) {
        if (yaml.get(k) == null) {
            return LuaValue.NIL;
        }

        if (yaml.isBoolean(k)) {
            return LuaBoolean.valueOf(yaml.getBoolean(k));
        } else if (yaml.isDouble(k) || yaml.isInt(k)) {
            return LuaNumber.valueOf((double) yaml.get(k));
        } else if (yaml.isLong(k)) {
            return LuaNumber.valueOf(yaml.getLong(k));
        } else if (yaml.isString(k)) {
            return LuaString.valueOf(yaml.getString(k));
        } else if (yaml.isOfflinePlayer(k)) {
            return new LuaPlayer(yaml.getOfflinePlayer(k));
        } else if (yaml.isItemStack(k)) {
            return new LuaItem(yaml.getItemStack(k));
        } else if (yaml.isVector(k)) {
            return new LuaVector(yaml.getVector(k));
        } else if (yaml.isColor(k)) {
            return new LuaColor(yaml.getColor(k));
        } else if (yaml.isConfigurationSection(k)) {
            return new LuaConfig(yaml.getConfigurationSection(k));
        } else if (yaml.isList(k)) {
            return processList(yaml.getList(k));
        }
        return LuaValue.NIL;
    }

    private List<?> formList(LuaTable tbl, String type) {
        if (TypeValidator.strongCheckOf(tbl, TBOOLEAN)) {
            return TableUtils.listFrom(tbl, LuaValue::toboolean);
        } else if (TypeValidator.strongCheckOf(tbl, TSTRING)) {
            return TableUtils.listFrom(tbl, LuaValue::tojstring);
        } else if (TypeValidator.strongCheckOf(tbl, TNUMBER)) {
            switch (type) {
                case "int":
                    return TableUtils.listFrom(tbl, LuaValue::toint);
                case "long":
                    return TableUtils.listFrom(tbl, LuaValue::tolong);
                default:
                    return TableUtils.listFrom(tbl, LuaValue::todouble);
            }
        } else if (TypeValidator.strongCheckOf(tbl, TTABLE)) {
            if (TypeValidator.checkOf(tbl, "player")) {
                return TableUtils.listFrom(tbl,
                        o -> ((LuaPlayer) o).getHandle());
            } else if (TypeValidator.checkOf(tbl, "item")) {
                return TableUtils.listFrom(tbl,
                        o -> ((LuaItem) o).getHandle());
            } else if (TypeValidator.checkOf(tbl, "vector")) {
                return TableUtils.listFrom(tbl,
                        o -> ((LuaVector) o).getVector());
            } else if (TypeValidator.checkOf(tbl, "color")) {
                return TableUtils.listFrom(tbl,
                        o -> ((LuaColor) o).getHandle());
            }
        }
        return null;
    }

    @MethodDef("set")
    public Varargs set(Varargs args) {
        String key = args.checkjstring(1);
        LuaValue value = args.checknotnil(2);
        String type = args.optjstring(3, null);

        if (value.type() == LuaValue.TNIL) {
            getHandle().set(key, null);
        } else if (value.type() == LuaValue.TBOOLEAN) {
            getHandle().set(key, value.toboolean());
        } else if (value.type() == LuaValue.TNUMBER) {
            switch (type) {
                case "int":
                    getHandle().set(key, value.toint());
                    break;
                case "long":
                    getHandle().set(key, value.tolong());
                    break;
                default:
                    getHandle().set(key, value.todouble());
                    break;
            }
        } else if (value.type() == LuaValue.TSTRING) {
            getHandle().set(key, value.tojstring());
        } else if (value.type() == LuaValue.TTABLE) {
            LuaTable tbl = value.checktable();
            if (!tbl.get("type").isnil()) {
                String ttype = tbl.get("type").tojstring();
                // Only allow attempting to store Spigot objects and not
                // ConfigurationSections
                if (tbl instanceof WrapperType && !ttype.equals("config")) {
                    getHandle().set(key, ((WrapperType) tbl).getHandle());
                } else {
                    error("cannot set YAML value to " + ttype);
                }
            } else {
                getHandle().set(key, formList(tbl, type));
            }
        } else {
            error("cannot set YAML value to " + value.typename());
        }

        return NIL;
    }

    @MethodDef("save")
    public Varargs save(Varargs args) {
        if (getHandle() instanceof FileConfiguration) {
            try {
                ((FileConfiguration) getHandle()).save(args.checkjstring(1));
            } catch (IOException e) {
                error("saving configuration failed: " + e.getMessage());
            }
        }
        return NIL;
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "config";
    }
}
