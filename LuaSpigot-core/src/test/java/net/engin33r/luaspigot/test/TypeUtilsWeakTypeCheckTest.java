package net.engin33r.luaspigot.test;

import net.engin33r.luaspigot.lua.TypeUtils;
import net.engin33r.luaspigot.lua.type.util.LuaUUID;
import net.engin33r.luaspigot.lua.type.util.LuaVector;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.luaj.vm2.LuaError;

import java.util.UUID;

public class TypeUtilsWeakTypeCheckTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void checkExpectedClassValue() {
        LuaUUID val = new LuaUUID(UUID.randomUUID());
        LuaUUID checked = TypeUtils.checkOf(val, LuaUUID.class);
        Assert.assertEquals(val, checked);
    }

    @Test
    public void checkInvalidClassValue() {
        LuaVector val = new LuaVector(new Vector(0, 0, 0));
        exception.expect(LuaError.class);
        TypeUtils.checkOf(val, LuaUUID.class);
    }
}
