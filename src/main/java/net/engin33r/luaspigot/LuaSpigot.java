package net.engin33r.luaspigot;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.State;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

@SuppressWarnings("unused")
public class LuaSpigot {
    private final JavaPlugin plugin;
    private final LibraryRegistry registry;

    /**
     * Utility class for our nifty <pre>import</pre> function to sidestep LuaJ's
     * kind of ugly default <pre>require</pre> behaviour.
     */
    private static class ImportFunction extends VarArgFunction {
        private final Globals env;
        private final LibraryRegistry registry;

        public ImportFunction(Globals env, LibraryRegistry registry) {
            this.env = env;
            this.registry = registry;
        }

        @Override
        public Varargs invoke(Varargs arg) {
            if (arg.narg() == 0) return NIL;

            Library lib = registry.get(arg.checkjstring(1));
            if (arg.narg() == 2)
                env.set(arg.checkjstring(2), lib.getLibrary());
            else
                env.set(lib.getName(), lib.getLibrary());

            return NIL;
        }
    }

    public LuaSpigot(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registry = new LibraryRegistry();
    }

    public LibraryRegistry getLibraryRegistry() {
        return this.registry;
    }

    public State newState() {
        Globals env = JsePlatform.standardGlobals();
        env.set("import", new SpigotGlobals.Import(env, registry));
        env.set("print", new SpigotGlobals.Print(plugin.getLogger()));
        env.set("uuid", new SpigotGlobals.ParseUUID());
        return new State(env);
    }
}
