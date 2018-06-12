package net.engin33r.luaspigot;

import net.engin33r.luaspigot.lua.Library;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Registry for keeping track of Lua libraries available to scripts.
 */
@SuppressWarnings("unused")
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

    public Set<Library> get(Class<? extends Library> clazz) {
        return nameRegistry.values().stream()
                .filter(lib -> clazz.isAssignableFrom(lib.getClass()))
                .collect(Collectors.toSet());
    }
}
