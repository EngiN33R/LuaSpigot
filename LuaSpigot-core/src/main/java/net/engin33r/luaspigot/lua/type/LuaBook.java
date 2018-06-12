package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.TableUtils;
import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldAccessorDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldMutatorDefinition;
import org.bukkit.inventory.meta.BookMeta;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class LuaBook extends WrapperType<BookMeta> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();
    private final PageTable pages = new PageTable();

    public LuaBook(BookMeta book) {
        super(book);
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "book";
    }

    @LinkedFieldMutatorDefinition("pages")
    public void setPages(LuaTable pages) {
        getHandle().setPages(TableUtils.listFrom(pages,
                LuaValue::checkjstring));
    }

    @LinkedFieldAccessorDefinition("pages")
    public PageTable getPages() {
        return pages;
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
