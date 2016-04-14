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
    private Map<LuaValue, DynamicField> fields = new HashMap<>();
    private static LuaValue typeMetatable = LuaValue.tableOf();

    protected WeakType() {
        this.set("type", this.getName());
        this.setmetatable(typeMetatable);

        final Class<? extends WeakType> clazz = this.getClass();
        for (java.lang.reflect.Method m : clazz.getDeclaredMethods()) {
            DynFieldDef fieldAnn = m.getAnnotation(DynFieldDef.class);
            if (fieldAnn != null) {
                fields.put(LuaValue.valueOf(fieldAnn.name()),
                        new DynamicField<WeakType>(this) {
                            @Override
                            public String getName() {
                                return fieldAnn.name();
                            }

                            @Override
                            public LuaValue update() {
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
                typeMetatable.set(mtMethodAnn.name(), new VarArgFunction() {
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
    public LuaValue get(LuaValue key) {
        if (fields.get(key) != null)
            return fields.get(key).update();
        return gettable(this, key);
    }

    private LuaValue getMetatable() {
        return typeMetatable;
    }

    public String toLuaString() {
        return this.tojstring();
    }

    @Override
    public String tojstring() {
        return this.toLuaString();
    }

    public void registerMethod(Method method) {
        this.set(method.getName(), method.getFunction());
    }

    public void registerField(String name, LuaValue field) {
        this.set(LuaValue.valueOf(name), field);
    }

    public void registerField(LuaValue key, LuaValue field) {
        this.set(key, field);
    }

    public void registerDynamicField(String name, DynamicField field) {
        fields.put(LuaValue.valueOf(name), field);
    }

    public void registerDynamicField(LuaValue key, DynamicField field) {
        fields.put(key, field);
    }

    public void registerMetaMethod(String name, Function metamethod) {
        typeMetatable.set(name, metamethod.getFunction());
    }
}
