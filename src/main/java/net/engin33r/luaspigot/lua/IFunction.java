package net.engin33r.luaspigot.lua;

import org.luaj.vm2.Varargs;

/**
 * Interface defining mandatory methods for Lua functions to override.
 */
interface IFunction {
    String getName();
    Varargs call(Varargs args);
}
