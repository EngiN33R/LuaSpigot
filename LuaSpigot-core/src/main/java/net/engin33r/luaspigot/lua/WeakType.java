package net.engin33r.luaspigot.lua;

import net.engin33r.luaspigot.lua.annotation.DynamicFieldDefinition;
import net.engin33r.luaspigot.lua.annotation.MetatableMethodDefinition;
import net.engin33r.luaspigot.lua.annotation.MethodDefinition;
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
    private final Map<LuaValue, net.engin33r.luaspigot.lua.DynamicField>
            dynFields = new HashMap<>();
    private final Map<LuaValue, LinkedField> linkedFields = new HashMap<>();

    protected WeakType() {
        this.set("type", this.getName());
        this.setmetatable(getMetatable());

        final Class<? extends WeakType> clazz = this.getClass();

        for (java.lang.reflect.Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(DynamicFieldDefinition.class)) {
                String name = m.getAnnotation(DynamicFieldDefinition.class)
                        .value();
                name = name.equals("") ? m.getName() : name;
                dynFields.put(LuaValue.valueOf(name), new
                        DynamicField<WeakType>(this) {
                    @Override
                    public LuaValue query() {
                        try {
                            return (LuaValue) m.invoke(WeakType.this);
                        } catch (IllegalAccessException |
                                InvocationTargetException e) {
                            //error(e.getMessage());
                            e.printStackTrace();
                        }
                        return NIL;
                    }
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
            linkedFields.get(key).update(value);
            return;
        }
        super.set(key, value);
    }

    @Override
    public LuaValue get(LuaValue key) {
        LuaValue i = index(key);
        if (i != null) return i;
        if (dynFields.get(key) != null) return dynFields.get(key).query();
        if (linkedFields.get(key) != null) return linkedFields.get(key).query();
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

    public void registerDynamicField(String name, DynamicField field) {
        registerDynamicField(LuaValue.valueOf(name), field);
    }

    public void registerDynamicField(String name, Supplier<LuaValue> query) {
        registerDynamicField(LuaValue.valueOf(name), new
                DynamicField<WeakType>(this) {
            @Override
            public LuaValue query() {
                return query.get();
            }
        });
    }

    public void registerDynamicField(LuaValue key, DynamicField field) {
        dynFields.put(key, field);
    }

    public void registerMetaMethod(String name, Function metamethod) {
        getMetatable().set(name, metamethod.getFunction());
    }

    public void registerLinkedField(String name, LinkedField field) {
        registerLinkedField(LuaValue.valueOf(name), field);
    }

    public void registerLinkedField(String name, Consumer<LuaValue> update,
                                    Supplier<LuaValue> query) {
        registerLinkedField(LuaValue.valueOf(name), new LinkedField<WeakType>
                () {
            @Override
            public void update(LuaValue val) {
                update.accept(val);
            }

            @Override
            public LuaValue query() {
                return query.get();
            }
        });
    }

    public void registerLinkedField(LuaValue key, LinkedField field) {
        linkedFields.put(key, field);
    }
}
