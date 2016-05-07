package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import net.engin33r.luaspigot.lua.type.LuaConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.luaj.vm2.*;

import java.io.File;
import java.io.StringReader;

/**
 * Library to interact with config files.
 */
public class ConfigLibrary extends Library {

    @Override
    public String getName() {
        return "config";
    }

    @LibFunctionDef(name = "load")
    public Varargs load(Varargs args) {
        String path = args.checkjstring(1);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(
                new File(path));

        return new LuaConfig(config);
    }

    @LibFunctionDef(name = "parse")
    public Varargs parse(Varargs args) {
        String path = args.checkjstring(1);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(
                new StringReader(path));

        return new LuaConfig(config);
    }
}
