package net.engin33r.luaspigot.test;

import net.engin33r.luaspigot.lua.TypeUtils;
import org.bukkit.Material;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;

import static org.junit.Assert.assertEquals;

public class TypeUtilsEnumTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void createStringFromEnum() {
        LuaString str = TypeUtils.checkEnum(Material.STONE);
        assertEquals(str.tojstring(), "STONE");
    }

    @Test
    public void createEnumFromString() {
        Material mat = TypeUtils.getEnum(LuaString.valueOf("STONE"),
                Material.class);
        assertEquals(mat, Material.STONE);
    }

    @Test
    public void failCreateEnumFromString() {
        exception.expect(LuaError.class);
        TypeUtils.getEnum(LuaString.valueOf("NOTHING"), Material.class);
    }
}
