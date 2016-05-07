package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.*;

public class TableUtils {
    /**
     * Processor interface used in array-to-table and collection-to-table
     * transformations.
     */
    public interface Processor {
        LuaValue process(Object o);
    }

    /**
     * Processor interface used in table-to-list and table-to-array
     * transformations.
     * @param <T> Type of values stored in the list
     */
    public interface SingleProcessor<T> {
        T process(LuaValue v);
    }

    /**
     * Processor interface used in table-to-map transformations.
     * @param <K> Type of keys used by the map
     * @param <V> Type of values stored by the map
     */
    public interface MapProcessor<K,V> {
        K processKey(LuaValue k);
        V processValue(LuaValue v);
    }

    /**
     * Create a table from an array of objects. How each object is converted
     * is dependent on the implementation of the processor passed to the method.
     * @param arr Array of objects
     * @param processor Processor
     * @return Table of transformed objects in the exact sequence they were
     * passed to the method.
     */
    public static LuaTable tableFrom(Object[] arr, Processor processor) {
        LuaTable tbl = LuaTable.tableOf();
        for (Object o : arr) {
            tbl.set(tbl.length()+1, processor.process(o));
        }
        return tbl;
    }

    /**
     * Create a table from a collection of objects. How each object is converted
     * is dependent on the implementation of the processor passed to the method.
     * @param coll Collection of objects
     * @param processor Processor
     * @return Table of transformed objects in the exact sequence they were
     * passed to the method.
     */
    public static <T> LuaTable tableFrom(Collection<T> coll,
                                         Processor processor) {
        LuaTable tbl = LuaTable.tableOf();
        for (T o : coll) {
            tbl.set(tbl.length()+1, processor.process(o));
        }
        return tbl;
    }

    /**
     * Create a list ({@link ArrayList}) from the table passed to the method.
     * How each value in the table is processed is dependent on the
     * implementation of the processor passed to the method.
     * <p>
     * This method only processes sequential numbered indices in the table.
     * For non-sequential or non-integer indices, use {@link #mapFrom(LuaTable,
     * MapProcessor)}
     * <p>
     * If <code>nonull</code> is <code>true</code>, the method will ignore any
     * object which the processor has returned <code>null</code> for. This makes
     * it possible to create a list that is not a representation of the entire
     * table.
     * @param tbl Table to process
     * @param nonull Ignore objects processed as null
     * @param processor Processor
     * @param <T> Type of values stored in the list
     * @return List representation of the table
     */
    public static <T> List<T> listFrom(LuaTable tbl, boolean nonull,
                                       SingleProcessor<T> processor) {
        List<T> list = new ArrayList<>();
        for (int i = 1; i <= tbl.length(); i++) {
            T obj = processor.process(tbl.get(i));
            if (!nonull || obj != null) {
                list.add(i, obj);
            }
        }
        return list;
    }


    /**
     * Create a list ({@link ArrayList}) from the table passed to the method.
     * How each value in the table is processed is dependent on the
     * implementation of the processor passed to the method. Equivalent to
     * calling <code>listFrom(tbl, false, processor)</code>.
     * @param tbl Table to process
     * @param processor Processor
     * @param <T> Type of values stored in the list
     * @return List representation of the table
     * @see #listFrom(LuaTable, boolean, SingleProcessor)
     */
    public static <T> List<T> listFrom(LuaTable tbl,
                                       SingleProcessor<T> processor) {
        return listFrom(tbl, false, processor);
    }

    /**
     * Create a map ({@link HashMap}) from the table passed to the method.
     * How each key and value in the table is processed is dependent on the
     * implementation of the processor passed to the method.
     * @param tbl Table to process
     * @param processor Processor
     * @param <K> Type of keys used by the map
     * @param <V> Type of values stored by the map
     * @return Map representation of the table
     */
    public static <K,V> Map<K,V> mapFrom(LuaTable tbl,
                                         MapProcessor<K,V> processor) {
        Map<K,V> map = new HashMap<>();
        for (LuaValue k : tbl.keys()) {
            map.put(processor.processKey(k), processor.processValue(
                    tbl.get(k)));
        }
        return map;
    }
}
