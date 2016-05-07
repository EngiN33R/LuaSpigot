package net.engin33r.luaspigot.lua.type.util;

import lombok.RequiredArgsConstructor;
import net.engin33r.luaspigot.lua.TypeValidator;
import net.engin33r.luaspigot.lua.WeakType;
import net.engin33r.luaspigot.lua.annotation.MetaMethodDef;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.UUID;

/**
 * Weak type to definitely represent UUIDs.
 */
@RequiredArgsConstructor
public class LuaUUID extends WeakType {
    private final UUID uuid;
    private static LuaValue typeMetatable = LuaValue.tableOf();

    public LuaUUID(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    @Override
    public String getName() {
        return "uuid";
    }

    @Override
    public String toLuaString() {
        return uuid.toString();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    @MetaMethodDef("__eq")
    public Varargs equals(Varargs arg) {
        TypeValidator.validate(arg.checktable(1), "uuid");
        TypeValidator.validate(arg.checktable(2), "uuid");

        LuaUUID uuid1 = (LuaUUID) arg.checktable(1);
        LuaUUID uuid2 = (LuaUUID) arg.checktable(2);
        return LuaBoolean.valueOf(uuid1.getUUID().equals(uuid2.getUUID()));
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }
}
