package net.engin33r.luaspigot.lua.lib;

import net.engin33r.luaspigot.lua.Library;
import net.engin33r.luaspigot.lua.annotation.LibraryFunctionDefinition;
import net.engin33r.luaspigot.lua.type.db.LuaDB;
import org.luaj.vm2.Varargs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.luaj.vm2.LuaValue.NIL;

/**
 * Library for interacting with SQLite databases. These methods should ideally
 * be used in async tasks (see {@link TaskLibrary}).
 */
@SuppressWarnings("unused")
public class DatabaseLibrary extends Library {
    private final Set<Connection> conns = new HashSet<>();

    @Override
    public String getName() {
        return "db";
    }

    @LibraryFunctionDefinition("connect")
    public Varargs connect(Varargs args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:"
                    + args.checkjstring(1));
            conns.add(conn);
            return new LuaDB(conn);
        } catch (SQLException e) {
            //LuaValue.error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    public void close() {
        try {
            for (Connection conn : conns) {
                conn.close();
            }
            conns.clear();
        } catch (SQLException e) {
            //LuaValue.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
