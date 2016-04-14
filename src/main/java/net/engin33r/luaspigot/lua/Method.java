package net.engin33r.luaspigot.lua;

import lombok.RequiredArgsConstructor;

/**
 * Abstract class describing a weak type method.
 */
@RequiredArgsConstructor
public abstract class Method<T extends WeakType> extends Function {
    public final T self;
}
