package net.engin33r.luaspigot;

import net.engin33r.luaspigot.lua.Library;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for keeping track of Lua libraries available to scripts.
 */
public class LibraryRegistry {
    private final Map<String, Library> nameRegistry = new HashMap<>();

    public void register(Library lib) {
        nameRegistry.put(lib.getName(), lib);
        System.out.println("Successfully added library " + lib.getName() +
                " to registry");
    }

    public Library get(String name) {
        return nameRegistry.get(name);
    }
}
