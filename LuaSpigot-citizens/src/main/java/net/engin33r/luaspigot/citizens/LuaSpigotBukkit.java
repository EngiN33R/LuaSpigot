package net.engin33r.luaspigot.citizens;

import net.engin33r.luaspigot.lua.lib.EventLibrary;
import org.bukkit.plugin.java.JavaPlugin;

public class LuaSpigotBukkit extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("LuaSpigot for Citizens successfully loaded!");
        EventLibrary.registerListener(NPCEventListener.class);
    }
}
