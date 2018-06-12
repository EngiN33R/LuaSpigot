package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.WrapperType;
import org.bukkit.inventory.meta.BookMeta;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class LuaBook extends WrapperType<BookMeta> {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaBook(BookMeta book) {
        super(book);

        registerLinkedField("pages", new PagesField());
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "book";
    }

    private class PagesField extends LinkedField<LuaBook> {
        PageTable tbl = new PageTable();

        @Override
        public void update(LuaValue val) {
            LuaTable pages = val.checktable();
            getHandle().setPages(TableUtils.listFrom(pages,
                    LuaValue::checkjstring));
        }

        @Override
        public LuaValue query() {
            return tbl;
        }
    }

    private class PageTable extends LuaTable {
        @Override
        public void set(int i, LuaValue luaValue) {
            getHandle().setPage(i, luaValue.checkjstring());
        }

        @Override
        public LuaValue get(int i) {
            return LuaString.valueOf(getHandle().getPage(i));
        }
    }
}
