package net.engin33r.luaspigot.lua;

import net.engin33r.luaspigot.lua.annotation.LibFunctionDef;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.luaj.vm2.LuaValue.NIL;
import static org.luaj.vm2.LuaValue.error;

/**
 * Abstract class representing a Lua library for scripts to use.
 */
public abstract class Library implements ILibrary {
    private final LuaValue library;
    private Map<String, Function> funcNameRegistry = new HashMap<>();

    protected Library() {
        library = LuaValue.tableOf();

        final Class<? extends Library> clazz = this.getClass();
        for (java.lang.reflect.Method m : clazz.getDeclaredMethods()) {
            LibFunctionDef funcAnn = m.getAnnotation(LibFunctionDef.class);
            if (funcAnn != null) {
                registerFunction(new Function() {
                    @Override
                    public String getName() {
                        return funcAnn.name();
                    }

                    @Override
                    public Varargs call(Varargs args) {
                        try {
                            return (LuaValue) m.invoke(Library.this, args);
                        } catch (IllegalAccessException |
                                InvocationTargetException e) {
                            error(e.toString() + ": " +
                                    String.valueOf(e.getMessage()));
                            e.printStackTrace();
                        }
                        return NIL;
                    }
                });
            }
        }
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
