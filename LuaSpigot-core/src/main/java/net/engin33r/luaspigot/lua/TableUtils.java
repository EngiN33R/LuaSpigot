package net.engin33r.luaspigot.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class TableUtils {
    /**
     * Processor interface used in array-to-table and collection-to-table
     * transformations.
     */
    public interface Processor<T> {
        LuaValue process(T o);
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
    public static <T> LuaTable tableFrom(T[] arr, Processor<T> processor) {
        LuaTable tbl = LuaTable.tableOf();
        for (T o : arr) {
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
                                         Processor<T> processor) {
        LuaTable tbl = LuaTable.tableOf();
        for (T o : coll) {
            tbl.set(tbl.length()+1, processor.process(o));
        }
        return tbl;
    }

    /**
     * Create a table from an object iterator. How each object is converted
     * is dependent on the implementation of the processor passed to the method.
     * @param iter Collection of objects
     * @param processor Processor
     * @return Table of transformed objects in the exact sequence they were
     * passed to the method.
     */
    public static <T> LuaTable tableFrom(Iterable<T> iter,
                                         Processor<T> processor) {
        LuaTable tbl = LuaTable.tableOf();
        for (T o : iter) {
            tbl.set(tbl.length()+1, processor.process(o));
        }
        return tbl;
    }

    /**
     * Create a table from a map. How each object is converted
     * is dependent on the processor functions passed to the method.
     * @param map Object map
     * @param keyProcessor Key processor
     * @param valueProcessor Value processor
     * @return Table of transformed objects with the same key-value mapping,
     * after transformations
     */
    public static <K,V> LuaTable tableFrom(Map<K,V> map,
                                           Function<K,LuaValue> keyProcessor,
                                           Function<V,LuaValue> valueProcessor
    ) {
        LuaTable tbl = LuaTable.tableOf();
        for (K key : map.keySet()) {
            tbl.set(keyProcessor.apply(key),
                    valueProcessor.apply(map.get(key)));
        }
        return tbl;
    }

    public static <T> T[] arrayFrom(LuaTable tbl,
                                    Function<LuaValue, T> processor,
                                    T[] arr) {
        return listFrom(tbl, processor).toArray(arr);
    }

    /**
     * Create a list ({@link ArrayList}) from the table passed to the method.
     * How each value in the table is processed is dependent on the
     * implementation of the processor passed to the method.
     * <p>
     * This method only processes sequential numbered indices in the table.
     * For non-sequential or non-integer indices, use {@link #mapFrom(LuaTable,
     * Function, Function)}
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
                list.add(i-1, obj);
            }
        }
        return list;
    }

    /**
     * Create a list ({@link ArrayList}) from the table passed to the method.
     * How each value in the table is processed is dependent on the
     * processor function passed to the method.
     * <p>
     * This method only processes sequential numbered indices in the table.
     * For non-sequential or non-integer indices, use {@link #mapFrom(LuaTable,
     * Function, Function)}
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
                                       Function<LuaValue, T> processor) {
        List<T> list = new ArrayList<>();
        for (int i = 1; i <= tbl.length(); i++) {
            T obj = processor.apply(tbl.get(i));
            if (!nonull || obj != null) {
                list.add(i-1, obj);
            }
        }
        return list;
    }

    /**
     * Create a list ({@link ArrayList}) from the table passed to the method.
     * How each value in the table is processed is dependent on the
     * processor function passed to the method. Equivalent to calling
     * <code>listFrom(tbl, false, processor)</code>.
     * @param tbl Table to process
     * @param processor Processor
     * @param <T> Type of values stored in the list
     * @return List representation of the table
     * @see #listFrom(LuaTable, boolean, SingleProcessor)
     */
    public static <T> List<T> listFrom(LuaTable tbl,
                                       Function<LuaValue, T> processor) {
        return listFrom(tbl, false, processor);
    }

    /**
     * Create a map ({@link HashMap}) from the table passed to the method.
     * How each key and value in the table is processed is dependent on the
     * processor functions passed to the method.
     * @param tbl Table to process
     * @param keyProcessor Processor function for table keys
     * @param valueProcessor Processor function for table values
     * @param <K> Type of keys used by the map
     * @param <V> Type of values stored by the map
     * @return Map representation of the table
     */
    public static <K,V> Map<K,V> mapFrom(LuaTable tbl,
                                         Function<LuaValue,K> keyProcessor,
                                         Function<LuaValue,V> valueProcessor) {
        Map<K,V> map = new HashMap<>();
        for (LuaValue k : tbl.keys()) {
            map.put(keyProcessor.apply(k), valueProcessor.apply(tbl.get(k)));
        }
        return map;
    }
}
