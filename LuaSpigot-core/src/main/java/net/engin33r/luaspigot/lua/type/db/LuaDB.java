package net.engin33r.luaspigot.lua.type.db;

import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.sql.*;

/**
 * Wrapper type for direct interaction with SQL databases.
 */
// TODO: proper error handling
public class LuaDB extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Connection conn;

    public LuaDB(Connection conn) {
        this.conn = conn;
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "database";
    }

    @MethodDef("prepare")
    public Varargs prepare(Varargs args) {
        try {
            PreparedStatement stmt = conn.prepareStatement(args
                    .checkjstring(1));
            return new LuaDBQuery(stmt);
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef("begin")
    public Varargs begin(Varargs args) {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef("execute")
    public Varargs execute(Varargs args) {
        try {
            LuaTable tbl = LuaTable.tableOf();

            Statement stmt = conn.createStatement();
            stmt.execute(args.checkjstring(1));
            ResultSet result = stmt.getResultSet();
            if (result == null) return tbl;

            ResultSetMetaData mdata = result.getMetaData();

            while (result.next()) {
                LuaTable row = LuaTable.tableOf();
                for (int i = 1; i <= mdata.getColumnCount(); i++) {
                    int type = mdata.getColumnType(i);
                    switch (type) {
                        case Types.BOOLEAN:
                            row.set(i, LuaValue.valueOf(result.getBoolean(i)));
                            break;
                        case Types.INTEGER:
                        case Types.SMALLINT:
                        case Types.BIGINT:
                        case Types.FLOAT:
                        case Types.DECIMAL:
                        case Types.DOUBLE:
                            row.set(i, LuaValue.valueOf(result.getDouble(i)));
                            break;
                        case Types.VARCHAR:
                            row.set(i, LuaValue.valueOf(result.getString(i)));
                            break;
                    }
                }
                tbl.set(result.getRow(), row);
            }
            result.close();
            stmt.close();
            return tbl;
        } catch (SQLException e) {
            if (e.getMessage().equals("no ResultSet available")) {
                return LuaTable.tableOf();
            }
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef("commit")
    public Varargs commit(Varargs args) {
        try {
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef("rollback")
    public Varargs rollback(Varargs args) {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef("errors")
    public Varargs errors(Varargs args) {
        try {
            LuaTable errs = LuaTable.tableOf();
            SQLWarning warn = conn.getWarnings();
            errs.set(1, warn.getMessage());
            while (warn.getNextWarning() != null) {
                errs.set(errs.length()+1, warn.getNextWarning().getMessage());
                warn = warn.getNextWarning();
            }
            return errs;
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef("close")
    public Varargs close(Varargs args) {
        try {
            conn.close();
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }
}
