package net.engin33r.luaspigot.lua;

import net.engin33r.luaspigot.lua.annotation.*;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Abstract class representing a weak Lua type (essentially a table) for
 * interfacing various Spigot objects without the need for a wholly new
 * data type. Mostly convenience.
 */
@SuppressWarnings("unused")
public abstract class WeakType extends LuaTable implements IWeakType {
    private final Map<LuaValue, DynamicField<? extends WeakType>>
            dynFields = new HashMap<>();
    private final Map<LuaValue, LinkedField<? extends WeakType>> linkedFields
            = new HashMap<>();

    protected WeakType() {
        this.set("type", this.getName());
        this.setmetatable(getMetatable());

        final Class<? extends WeakType> clazz = this.getClass();

        for (Class<?> c : clazz.getDeclaredClasses()) {
            if (c.isAnnotationPresent(LinkedFieldInterfaceDefinition.class)) {
                try {
                    // For some reason the constructor also takes the
                    // enclosing class as an argument
                    String name = c.getDeclaredAnnotation(
                            LinkedFieldInterfaceDefinition.class).value();
                    linkedFields.put(LuaValue.valueOf(name),
                            (LinkedField<? extends WeakType>) c
                                    .getConstructor(this.getClass(), this.getClass())
                                    .newInstance(this, this));
                } catch (NoSuchMethodException |
                        IllegalAccessException |
                        InstantiationException |
                        InvocationTargetException e) {
                    //error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        for (java.lang.reflect.Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(LinkedFieldMutatorDefinition.class)) {
                LuaValue name = LuaValue.valueOf(m
                        .getAnnotation(LinkedFieldMutatorDefinition.class)
                        .value());
                linkedFields.computeIfAbsent(name,
                        k -> new LinkedField<>(this));
                linkedFields.get(name).setMutator((self, arg) -> {
                    try {
                        Class<?> argClass = m.getParameterTypes()[0];
                        LuaValue checked = arg;
                        if (WeakType.class.isAssignableFrom(argClass)) {
                            @SuppressWarnings("unchecked")
                            Class<? extends WeakType> wClass =
                                    (Class<? extends WeakType>) argClass;
                            checked = TypeUtils.checkOf(arg, wClass);
                        }
                        m.invoke(this, checked);
                    } catch (IllegalAccessException |
                            InvocationTargetException e) {
                        //error(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }

            if (m.isAnnotationPresent(LinkedFieldAccessorDefinition.class)) {
                LuaValue name = LuaValue.valueOf(m
                        .getAnnotation(LinkedFieldAccessorDefinition.class)
                        .value());
                linkedFields.computeIfAbsent(name,
                        k -> new LinkedField<>(this));
                linkedFields.get(name).setAccessor(self -> {
                    try {
                        return (LuaValue) m.invoke(this);
                    } catch (IllegalAccessException |
                            InvocationTargetException e) {
                        //error(e.getMessage());
                        e.printStackTrace();
                    }
                    return NIL;
                });
            }

            if (m.isAnnotationPresent(DynamicFieldDefinition.class)) {
                LuaValue name = LuaValue.valueOf(m
                        .getAnnotation(DynamicFieldDefinition.class)
                        .value());
                dynFields.computeIfAbsent(name,
                        k -> new DynamicField<>(this));
                dynFields.get(name).setAccessor(self -> {
                    try {
                        return (LuaValue) m.invoke(this);
                    } catch (IllegalAccessException |
                            InvocationTargetException e) {
                        //error(e.getMessage());
                        e.printStackTrace();
                    }
                    return NIL;
                });
            }

            if (m.isAnnotationPresent(MethodDefinition.class)) {
                String name = m.getAnnotation(MethodDefinition.class).value();
                name = name.equals("") ? m.getName() : name;
                registerField(name, new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        try {
                            return (Varargs) m.invoke(WeakType.this, args);
                        } catch (IllegalAccessException |
                                InvocationTargetException e) {
                            //error(e.getMessage());
                            e.printStackTrace();
                        }
                        return NIL;
                    }
                });
            }

            if (m.isAnnotationPresent(MetatableMethodDefinition.class)) {
                String name = m.getAnnotation(MetatableMethodDefinition
                        .class).value();
                name = name.equals("") ? m.getName() : name;
                getMetatable().set(name, new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        try {
                            return (Varargs) m.invoke(WeakType.this, args);
                        } catch (IllegalAccessException |
                                InvocationTargetException e) {
                            //error(e.getMessage());
                            e.printStackTrace();
                        }
                        return NIL;
                    }
                });
            }
        }
    }

    @Override
    public void set(LuaValue key, LuaValue value) {
        if (linkedFields.get(key) != null) {
            linkedFields.get(key).mutate(value);
            return;
        }
        super.set(key, value);
    }

    @Override
    public LuaValue get(LuaValue key) {
        LuaValue i = index(key);
        if (i != null) return i;
        if (dynFields.get(key) != null)
            return dynFields.get(key).access();
        if (linkedFields.get(key) != null)
            return linkedFields.get(key).access();
        return super.get(key);
    }

    protected abstract LuaValue getMetatable();

    public String toLuaString() {
        return super.tojstring();
    }

    @Override
    public String tojstring() {
        return this.toLuaString();
    }

    public void registerMethod(String name, Method method) {
        this.set(name, method.getFunction());
    }

    public void registerMethod(String name,
                               java.util.function.Function<Varargs, Varargs>
                                       func) {
        this.set(name, (new Method<WeakType>(this) {
            public Varargs call(Varargs arg) {
                return func.apply(arg);
            }
        }).getFunction());
    }

    public void registerMethod(String name,
                               java.util.function.Consumer<Varargs>
                                       func) {
        this.set(name, (new Method<WeakType>(this) {
            public Varargs call(Varargs arg) {
                func.accept(arg);
                return NIL;
            }
        }).getFunction());
    }

    public void registerMethod(String name,
                               java.util.function.Supplier<Varargs>
                                       func) {
        this.set(name, (new Method<WeakType>(this) {
            public Varargs call(Varargs arg) {
                return func.get();
            }
        }).getFunction());
    }

    public void registerMethod(String name,
                               Runnable func) {
        this.set(name, (new Method<WeakType>(this) {
            public Varargs call(Varargs arg) {
                func.run();
                return NIL;
            }
        }).getFunction());
    }

    public void registerField(String name, LuaValue field) {
        registerField(LuaValue.valueOf(name), field);
    }

    public void registerField(LuaValue key, LuaValue field) {
        this.set(key, field == null ? NIL : field);
    }

    public void registerDynamicField(String name,
                                     DynamicField<? extends WeakType> field) {
        registerDynamicField(LuaValue.valueOf(name), field);
    }

    public void registerDynamicField(String name, Supplier<LuaValue> query) {
        DynamicField<? extends WeakType> field = new DynamicField<>(this);
        field.setAccessor(self -> query.get());
        registerDynamicField(name, field);
    }

    public void registerDynamicField(LuaValue key,
                                     DynamicField<? extends WeakType> field) {
        dynFields.put(key, field);
    }

    public void registerMetaMethod(String name, Function metamethod) {
        getMetatable().set(name, metamethod.getFunction());
    }

    public void registerLinkedField(String name,
                                    LinkedField<? extends WeakType> field) {
        registerLinkedField(LuaValue.valueOf(name), field);
    }

    public void registerLinkedField(String name,
                                    Accessor<WeakType, LuaValue> accessor,
                                    Mutator<WeakType, LuaValue> mutator) {
        LinkedField<WeakType> field = new LinkedField<>(this);
        field.setAccessor(accessor);
        field.setMutator(mutator);
        registerLinkedField(name, field);
    }

    public void registerLinkedField(String name, Consumer<LuaValue> update,
                                    Supplier<LuaValue> query) {
        LinkedField<? extends WeakType> field = new LinkedField<>(this);
        field.setAccessor(self -> query.get());
        field.setMutator((self, arg) -> update.accept(arg));
        registerLinkedField(name, field);
    }

    public void registerLinkedField(LuaValue key,
                                    LinkedField<? extends WeakType> field) {
        linkedFields.put(key, field);
    }
}
