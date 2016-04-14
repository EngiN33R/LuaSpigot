package net.engin33r.luaspigot;

import org.bukkit.plugin.java.JavaPlugin;

public class LuaSpigotBukkit extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("LuaSpigot successfully loaded! You may now use its" +
                " functionality in your plugins.");
    }
}
