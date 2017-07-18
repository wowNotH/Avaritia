package morph.avaritia.recipe.compressor;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CompressOreRecipe extends CompressorRecipe {

	private int oreID;

	public CompressOreRecipe(ItemStack output, int amount, String oreId, boolean exact) {
		super(output, amount, ItemStack.EMPTY, exact);
		oreID = OreDictionary.getOreID(oreId);
	}

	public CompressOreRecipe(ItemStack output, int amount, String oreId) {
		this(output, amount, oreId, false);
	}

	@Override
	public boolean isValidInput(ItemStack ingredient) {

		int[] ids = OreDictionary.getOreIDs(ingredient);
		for (int id : ids) {
			if (oreID == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ItemStack> getInputs() {
		return OreDictionary.getOres(OreDictionary.getOreName(oreID));
	}

	@Override
	public String getIngredientName() {
		List<ItemStack> ores = OreDictionary.getOres(OreDictionary.getOreName(oreID));
		if (!ores.isEmpty()) {
			return ores.get(0).getDisplayName();
		}
		return OreDictionary.getOreName(oreID);
	}

	@Override
	public Object getIngredient() {
		return OreDictionary.getOres(OreDictionary.getOreName(oreID));
	}

}
