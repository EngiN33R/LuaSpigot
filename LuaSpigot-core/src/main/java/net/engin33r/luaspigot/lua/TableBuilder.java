package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Collection;

public class TableBuilder {
    public interface Processor {
        LuaValue process(Object o);
    }

    public static LuaTable tableFrom(Collection l, Processor processor) {
        LuaTable tbl = LuaTable.tableOf();
        for (Object o : l) {
            tbl.set(tbl.length()+1, processor.process(o));
        }
        return tbl;
    }
}
