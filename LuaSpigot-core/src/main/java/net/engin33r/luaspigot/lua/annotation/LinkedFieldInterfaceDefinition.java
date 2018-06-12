package net.engin33r.luaspigot.lua.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation for quick registration of linked field mutators (setters).
 */
@Target(value=TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkedFieldInterfaceDefinition {
    String value();
}
