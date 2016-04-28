package net.engin33r.luaspigot.lua.type;

import net.engin33r.luaspigot.lua.LinkedField;
import net.engin33r.luaspigot.lua.WeakType;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper type describing a crafting recipe.
 */
public class LuaRecipe extends WeakType {
    private static LuaValue typeMetatable = LuaValue.tableOf();

    private final Recipe r;
    private final String type;

    private Map<Character, ItemStack> map = new HashMap<>();
    private String[] strshape = {"", "", ""};

    public LuaRecipe(Recipe r) {
        this.r = r;
        if (r instanceof ShapedRecipe) {
            this.type = "shaped";
            registerLinkedField("shape", new ShapeField(this));
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

    private class ShapeField extends LinkedField<LuaRecipe> {
        public ShapeField(LuaRecipe self) { super(self); }

        @Override
        public void update(LuaValue val) {
            ShapedRecipe recipe = (ShapedRecipe) r;
            LuaTable shape = val.checktable();
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

        @Override
        public LuaValue query() {
            ShapedRecipe recipe = (ShapedRecipe) r;
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
}
