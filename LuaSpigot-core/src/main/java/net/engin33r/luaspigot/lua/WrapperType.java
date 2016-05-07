package net.engin33r.luaspigot.lua;

/**
 * Convenience class implementing boilerplate code for Spigot classes.
 */
public abstract class WrapperType<T> extends WeakType {
    private final T handle;

    protected WrapperType(T handle) {
        this.handle = handle;
    }

    public T getHandle() {
        return this.handle;
    }
}
