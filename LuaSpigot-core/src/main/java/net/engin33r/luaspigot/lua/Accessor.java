package net.engin33r.luaspigot.lua;

@FunctionalInterface
public interface Accessor<S, R> {

    /**
     * Returns a result for the supplied object.
     *
     * @param self the mutated object
     * @return the function result
     */
    R access(S self);
}
