package net.engin33r.luaspigot.lua;

@FunctionalInterface
public interface Mutator<S, A> {

    /**
     * Performs this operation on the given argument, supplying the object being
     * mutated.
     *
     * @param self the mutated object
     * @param a the input argument
     */
    void mutate(S self, A a);
}
