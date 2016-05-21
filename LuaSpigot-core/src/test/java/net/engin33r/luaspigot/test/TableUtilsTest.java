package net.engin33r.luaspigot.test;

import net.engin33r.luaspigot.lua.TableUtils;
import org.junit.Test;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class TableUtilsTest {
    @Test
    public void createTableFromArrayTest() {
        Integer[] arr = new Integer[]{1, 2, 3};
        LuaTable tbl = TableUtils.tableFrom(arr, LuaInteger::valueOf);
        for (LuaValue key : tbl.keys()) {
            System.out.println("[" + key + "] = " + tbl.get(key));
        }
    }

    @Test
    public void createTableFromCollectionTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        LuaTable tbl = TableUtils.tableFrom(list, LuaInteger::valueOf);
        for (LuaValue key : tbl.keys()) {
            System.out.println("[" + key + "] = " + tbl.get(key));
        }
    }
}
