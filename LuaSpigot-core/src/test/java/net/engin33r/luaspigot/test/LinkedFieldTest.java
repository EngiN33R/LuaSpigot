package net.engin33r.luaspigot.test;

import lombok.Getter;
import lombok.Setter;
import net.engin33r.luaspigot.lua.Accessor;
import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.Mutator;
import net.engin33r.luaspigot.lua.type.LuaPlayer;
import org.junit.Test;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class LinkedFieldTest {
    @Getter
    @Setter
    private class VelocityField {
        private LuaValue value;

        public VelocityField(LuaString str) {
            value = str;
        }
    }

    @Test
    public void reflectionTest() {
        Class<VelocityField> cls = VelocityField.class;
        try {
            Constructor c = cls.getConstructor(LinkedFieldTest.class, LuaString.class);
            Class[] params = c.getParameterTypes();
            System.out.println(c.getName() + "(" + String.join(", ",
                    Arrays.stream(params).map(Class::getName)
                            .toArray(String[]::new)) + ")");
            VelocityField field = (VelocityField) cls
                    .getDeclaredConstructors()[0]
                    .newInstance(this, LuaString.valueOf("hello"));
            System.out.println(field.getValue());
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
