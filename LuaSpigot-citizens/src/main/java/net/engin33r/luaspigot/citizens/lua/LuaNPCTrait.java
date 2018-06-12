package net.engin33r.luaspigot.citizens.lua;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import net.engin33r.luaspigot.lua.WrapperType;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LuaNPCTrait extends WrapperType<Trait> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    public static class NPCTrait extends Trait {
        private static final Map<String, Set<LuaFunction>> callbacks =
                new HashMap<>();

        public void setName(String name) {
            try {
                Field f = this.getClass().getSuperclass()
                        .getDeclaredField("value");
                f.setAccessible(true);
                f.set(this, name);
                f.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public NPCTrait(String name) {
            super(name);
        }

        public NPCTrait() {
            super("lua_trait");
        }

        @Override
        public void run() {
            super.run();
            runCallbacks("run");
        }

        @Override
        public void onAttach() {
            super.onAttach();
            this.setName(CitizensAPI.getTraitFactory().getTrait(
                    LuaNPCTrait.NPCTrait.class).getName());
            runCallbacks("onAttach");
        }

        @Override
        public void onCopy() {
            super.onCopy();
            runCallbacks("onCopy");
        }

        @Override
        public void onDespawn() {
            super.onDespawn();
            runCallbacks("onDespawn");
        }

        @Override
        public void onRemove() {
            super.onRemove();
            runCallbacks("onRemove");
        }

        @Override
        public void onSpawn() {
            super.onSpawn();
            runCallbacks("onSpawn");
        }

        // TODO: DataKey processing (I cannot be arsed right now)
        @Override
        public void load(DataKey key) throws NPCLoadException {
            super.load(key);
        }

        @Override
        public void save(DataKey key) {
            super.save(key);
        }

        public void runCallbacks(String type, LuaValue... args) {
            if (callbacks.get(type) != null) {
                for (LuaFunction f : callbacks.get(type)) {
                    f.invoke(args);
                }
            }
        }

        public void addCallback(String type, LuaFunction callback) {
            callbacks.putIfAbsent(type, new HashSet<>());
            callbacks.get(type).add(callback);
        }

        public void deleteCallback(String type, LuaFunction callback) {
            callbacks.putIfAbsent(type, new HashSet<>());
            callbacks.get(type).remove(callback);
        }

        public void clearCallbacks(String type, LuaFunction callback) {
            callbacks.putIfAbsent(type, new HashSet<>());
            callbacks.get(type).clear();
        }
    }

    public LuaNPCTrait(Trait trait) {
        super(trait);
    }

    public LuaNPCTrait(String name) {
        super(new NPCTrait(name));
    }

    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    public String getName() {
        return "npctrait";
    }

    @Override
    public String toLuaString() {
        return "npctrait: "+getHandle().getName();
    }
}
