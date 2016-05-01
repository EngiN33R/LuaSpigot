package net.engin33r.luaspigot;

import lombok.RequiredArgsConstructor;
import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.type.LuaUUID;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.logging.Logger;

/**
 * Container for new global functions and overrides introduced for Spigot.
 */
public class SpigotGlobals {
    @RequiredArgsConstructor
    public static class Print extends VarArgFunction {
        private final Logger logger;

        public Varargs invoke(Varargs arg) {
            if (arg.narg() > 0) {
                String str = "[Lua] ";
                for (int i = 1; i <= arg.narg(); i++) {
                    if (i > 1) str += "\t";
                    str += arg.tojstring(i);
                }
                logger.info(str);
            }
            return NIL;
        }
    }

    public static class Import extends VarArgFunction {
        private final Globals env;
        private final LibraryRegistry registry;

        public Import(Globals env, LibraryRegistry registry) {
            this.env = env;
            this.registry = registry;
        }

        @Override
        public Varargs invoke(Varargs arg) {
            if (arg.narg() == 0) return NIL;

            String name = arg.checkjstring(1);
            Library lib = registry.get(name);
            if (lib == null) {
                error("library not found: "+name);
                return NIL;
            }
            if (arg.narg() == 2) {
                if (env.get(arg.checkjstring(2)) == lib.getLibrary())
                    return NIL;
                env.set(arg.checkjstring(2), lib.getLibrary());
            } else {
                if (env.get(lib.getName()) == lib.getLibrary())
                    return NIL;
                env.set(lib.getName(), lib.getLibrary());
            }

            return NIL;
        }
    }

    public static class ParseUUID extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            return new LuaUUID(arg.checkjstring());
        }
    }
}
