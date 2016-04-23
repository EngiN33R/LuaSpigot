package net.engin33r.luaspigot.lua;

import net.engin33r.luaspigot.lua.annotation.DynFieldDef;
import net.engin33r.luaspigot.lua.annotation.MetaMethodDef;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing a weak Lua type (essentially a table) for
 * interfacing various Spigot objects without the need for a wholly new
 * data type. Mostly convenience.
 */
@SuppressWarnings("unused")
public abstract class WeakType extends LuaTable implements IWeakType {
    private Map<LuaValue, DynamicField> dynFields = new HashMap<>();
    private Map<LuaValue, LinkedField> linkedFields = new HashMap<>();

    protected WeakType() {
        this.set("type", this.getName());
        this.setmetatable(getMetatable());

        final Class<? extends WeakType> clazz = this.getClass();
        for (java.lang.reflect.Method m : clazz.getDeclaredMethods()) {
            DynFieldDef fieldAnn = m.getAnnotation(DynFieldDef.class);
            if (fieldAnn != null) {
                dynFields.put(LuaValue.valueOf(fieldAnn.name()),
                        new DynamicField<WeakType>(this) {
                            @Override
                            public LuaValue query() {
                                try {
                                    return (LuaValue) m.invoke(WeakType.this);
                                } catch (IllegalAccessException |
                                        InvocationTargetException e) {
                                    error(e.getMessage());
                                    e.printStackTrace();
                                }
                                return NIL;
                            }
                        });
            }

            MethodDef methodAnn = m.getAnnotation(MethodDef.class);
            if (methodAnn != null) {
                registerField(methodAnn.name(), new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        try {
                            return (Varargs) m.invoke(WeakType.this, args);
                        } catch (IllegalAccessException |
                                InvocationTargetException e) {
                            error(e.getMessage());
                            e.printStackTrace();
                        }
                        return NIL;
                    }
                });
            }

            MetaMethodDef mtMethodAnn = m.getAnnotation(MetaMethodDef.class);
            if (mtMethodAnn != null) {
                getMetatable().set(mtMethodAnn.name(), new VarArgFunction() {
                    @Override
                    public Varargs invoke(Varargs args) {
                        try {
                            return (Varargs) m.invoke(WeakType.this, args);
                        } catch (IllegalAccessException |
                                InvocationTargetException e) {
                            error(e.getMessage());
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
        if (dynFields.get(key) != null)
            return dynFields.get(key).query();
        if (linkedFields.get(key) != null)
            return linkedFields.get(key).query();
        return super.get(key);
    }

    protected abstract LuaValue getMetatable();

    public String toLuaString() {
        return this.tojstring();
    }

    @Override
    public String tojstring() {
        return this.toLuaString();
    }

    public void registerMethod(String name, Method method) {
        this.set(name, method.getFunction());
    }

    public void registerField(String name, LuaValue field) {
        registerField(LuaValue.valueOf(name), field);
    }

    public void registerField(LuaValue key, LuaValue field) {
        this.set(key, field);
    }

    public void registerDynamicField(String name, DynamicField field) {
        registerDynamicField(LuaValue.valueOf(name), field);
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

    public void registerLinkedField(LuaValue key, LinkedField field) {
        linkedFields.put(key, field);
    }
}
