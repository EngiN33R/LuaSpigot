package net.engin33r.luaspigot;

import lombok.RequiredArgsConstructor;
import net.engin33r.luaspigot.lua.lib.EventLibrary;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public abstract class EventListener implements Listener {
    protected final EventLibrary lib;
}