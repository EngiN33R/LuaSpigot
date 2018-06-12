package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Function;
import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.type.LuaEntity;
import net.engin33r.luaspigot.lua.type.LuaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Library for registering and unregistering Spigot commands from Lua.
 */
@SuppressWarnings("unused")
public class CommandLibrary extends Library {
    private CommandMap commandMap;
    private final Map<String, LuaCommand> commands = new HashMap<>();

    public CommandLibrary() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass()
                    .getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            this.commandMap = (CommandMap) bukkitCommandMap.get(Bukkit
                    .getServer());

            registerFunction("register", new RegisterFunction());
            registerFunction("unregister", new UnregisterFunction());
        } catch (Exception e) {
            Bukkit.getPluginManager().getPlugin("LuaSpigot").getLogger()
                    .log(Level.WARNING, "Could not access" +
                            " CraftServer command map", e);
        }
    }

    public class LuaCommand extends Command {
        private final LuaFunction func;

        public LuaCommand(String name, LuaTable properties) {
            super(name);

            String description = properties.get("description").optjstring("");
            String usage = properties.get("usage").optjstring("");
            String permission = properties.get("permission").optjstring(null);
            String forbidden = properties.get("forbidden").optjstring(null);
            List<String> aliases = TableUtils.listFrom(properties
                    .get("aliases").opttable(LuaTable.tableOf()),
                    LuaValue::checkjstring);

            this.setDescription(description)
                    .setUsage(usage)
                    .setAliases(aliases)
                    .setPermissionMessage(forbidden)
                    .setPermission(permission);

            this.func = properties.get("execute").checkfunction();
        }

        @Override
        public boolean execute(CommandSender sender, String label,
                               String[] args) {
            LuaTable lsender = LuaTable.tableOf();
            if (sender instanceof Player) {
                lsender.set("player", new LuaPlayer((Player) sender));
            } else if (sender instanceof Entity) {
                lsender.set("entity", new LuaEntity((Entity) sender));
            }
            lsender.set("name", sender.getName());
            lsender.set("message", new LuaFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    sender.sendMessage(args.checkjstring(1));
                    return NIL;
                }
            });

            return this.func.call(lsender, LuaString.valueOf(label),
                    TableUtils.tableFrom(args, LuaString::valueOf)).toboolean();
        }
    }

    @Override
    public String getName() {
        return "commands";
    }

    public void cleanup() {
        commands.keySet().forEach(this::unregister);
        commands.clear();
    }

    private void unregister(String name) {
        try {
            Field fknown = commandMap.getClass()
                    .getDeclaredField("knownCommands");
            fknown.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Command> known = (HashMap<String, Command>)
                    fknown.get(commandMap);
            Set<String> todel = new HashSet<>();
            known.keySet().stream()
                    .filter(k -> known.get(k).getName().equals(name))
                    .forEach(todel::add);
            todel.forEach(known::remove);
            if (commands.get(name) != null) commands.get(name)
                    .unregister(commandMap);
        } catch (Exception e) {
            Bukkit.getPluginManager().getPlugin("LuaSpigot").getLogger()
                    .log(Level.WARNING, "Could not access" +
                            " CommandMap known commands", e);
        }
    }

    private class RegisterFunction extends Function {
        @Override
        public Varargs call(Varargs args) {
            LuaTable properties = args.checktable(1);
            String name = properties.get("name").checkjstring();

            LuaCommand cmd = new LuaCommand(name, properties);
            commandMap.register(name, "luaspigot", cmd);
            cmd.register(commandMap);
            commands.put(name, cmd);

            Bukkit.getPluginManager().getPlugin("LuaSpigot").getLogger()
                    .log(Level.INFO, "Registered command '" + name + "'");

            return NIL;
        }
    }

    private class UnregisterFunction extends Function {
        @Override
        public Varargs call(Varargs args) {
            String name = args.checkjstring(1);

            unregister(name);
            commands.remove(name);

            Bukkit.getPluginManager().getPlugin("LuaSpigot").getLogger()
                    .log(Level.INFO, "Unregistered command '" + name + "'");
            return NIL;
        }
    }
}
