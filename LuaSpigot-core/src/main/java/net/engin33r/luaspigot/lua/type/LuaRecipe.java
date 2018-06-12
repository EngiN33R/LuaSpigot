package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.WrapperType;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldAccessorDefinition;
import net.engin33r.luaspigot.lua.annotation.LinkedFieldMutatorDefinition;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper type describing a crafting recipe.
 */
public class LuaRecipe extends WrapperType<Recipe> {
    private static final LuaValue typeMetatable = LuaValue.tableOf();

    private final String type;

    private final String[] strshape = {"", "", ""};

    public LuaRecipe(Recipe r) {
        super(r);

        if (r instanceof ShapedRecipe) {
            this.type = "shaped";
        } else if (r instanceof ShapelessRecipe) {
            this.type = "shapeless";
        } else if (r instanceof FurnaceRecipe) {
            this.type = "furnace";
        } else {
            this.type = "";
        }
        registerField("kind", LuaString.valueOf(this.type));
    }

    @Override
    protected LuaValue getMetatable() {
        return typeMetatable;
    }

    @Override
    public String getName() {
        return "recipe";
    }

    @LinkedFieldMutatorDefinition("shape")
    public void setShape(LuaTable shape) {
        if (!type.equals("shaped")) {
            throw new LuaError("cannot set shape of non-shaped recipe");
        }
        ShapedRecipe recipe = (ShapedRecipe) getHandle();
        if (shape.length() == 0) error("recipe shape must have at least" +
                " 1 item");

        for (int i = 1; i <= 9 && i <= shape.length(); i++) {
            int row = (int) Math.floor(i/3);
            String str = shape.get(i).tojstring();
            if (str.equals("") || str.equals(" ")) {
                strshape[row] += " ";
            } else {
                recipe.setIngredient(String.valueOf(i)
                        .charAt(0), Material.valueOf(str));
                strshape[row] += String.valueOf(i).charAt(0);
            }
        }

        recipe.shape(strshape);
    }

    @LinkedFieldAccessorDefinition("shape")
    public LuaValue getShape() {
        if (!type.equals("shaped")) {
            return NIL;
        }
        ShapedRecipe recipe = (ShapedRecipe) getHandle();
        LuaTable shape = LuaTable.tableOf();
        Map<Character, ItemStack> map = recipe.getIngredientMap();
        for (String row : recipe.getShape()) {
            for (int i = 0; i < 3; i++) {
                if (map.get(row.charAt(i)) != null) {
                    shape.set(shape.length() + 1, LuaString.valueOf(
                            map.get(row.charAt(i)).getType().toString()));
                }
            }
        }
        return shape;
    }
}
