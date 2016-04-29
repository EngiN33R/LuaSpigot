package net.engin33r.luaspigot.lua.type.db;

import lombok.RequiredArgsConstructor;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.MethodDef;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.sql.*;

/**
 * Wrapper type for database queries.
 */
// TODO: proper error handling
@RequiredArgsConstructor
public class LuaDBQuery extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final PreparedStatement stmt;

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "prepquery";
    }

    @Override
    public String toLuaString() {
        return stmt.toString();
    }

    @MethodDef(name = "setParameter")
    public Varargs setParameter(Varargs args) {
        try {
            int param = args.checkint(1);
            String type = args.checkjstring(2);
            switch (type) {
                case "integer":
                    stmt.setInt(param, args.checkint(3));
                    break;
                case "double":
                    stmt.setDouble(param, args.checkdouble(3));
                    break;
                case "string":
                    stmt.setString(param, args.checkjstring(3));
                    break;
                case "boolean":
                    stmt.setBoolean(param, args.checkboolean(3));
                    break;
            }
            return this;
        } catch (SQLException e) {
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }

    @MethodDef(name = "execute")
    public Varargs execute(Varargs args) {
        try {
            LuaTable tbl = LuaTable.tableOf();

            stmt.execute();
            ResultSet result = stmt.getResultSet();
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
            /*if (e.getMessage().equals("no ResultSet available")) {
                return LuaTable.tableOf();
            }*/
            //error(e.getMessage());
            e.printStackTrace();
        }
        return NIL;
    }
}
