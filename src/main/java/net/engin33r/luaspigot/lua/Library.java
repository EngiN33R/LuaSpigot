package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing a Lua library for scripts to use.
 */
public abstract class Library implements ILibrary {
    private final LuaValue library;
    private Map<String, Function> funcNameRegistry = new HashMap<>();

    protected Library() {
        library = LuaValue.tableOf();
    }

    @SuppressWarnings("unused")
    public void onImport(LuaValue environment) {
        environment.set(this.getName(), library);
    }

    public LuaValue getLibrary() {
        return this.library;
    }

    protected void registerFunction(Function f) {
        String name = f.getName();
        library.set(name, f.getFunction());
        funcNameRegistry.put(name, f);
        System.out.println("Registered function " + name + " (" +
                f.toString() + ")");
    }

    public Function get(String name) {
        return funcNameRegistry.get(name);
    }
}
