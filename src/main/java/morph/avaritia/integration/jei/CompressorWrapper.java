package morph.avaritia.integration.jei;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.recipes.BrokenCraftingRecipeException;
import mezz.jei.util.ErrorUtil;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public class CompressorWrapper extends BlankRecipeWrapper {

	private IJeiHelpers jeiHelpers;
	private final CompressorRecipe recipe;

	public CompressorWrapper(IJeiHelpers helpers, CompressorRecipe recipe) {
		jeiHelpers = helpers;
		this.recipe = recipe;
		for (Object input : this.recipe.getInputs()) {
			if (input instanceof ItemStack) {
				ItemStack itemStack = (ItemStack) input;
				if (itemStack.getCount() != 1) {
					itemStack.setCount(1);
				}
			}
		}
	}

	public int getCost() {
		return recipe != null ? recipe.getCost() : 0;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper stackHelper = jeiHelpers.getStackHelper();
		ItemStack recipeOutput = recipe.getOutput();

		try {
			List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(recipe.getInputs());
			ingredients.setInputLists(ItemStack.class, inputs);
			if (!recipeOutput.isEmpty()) {
				ingredients.setOutput(ItemStack.class, recipeOutput);
			}
		}
		catch (RuntimeException e) {
			String info = ErrorUtil.getInfoFromBrokenCraftingRecipe(recipe, Arrays.asList(recipe.getInputs()), recipeOutput);
			throw new BrokenCraftingRecipeException(info, e);
		}
	}

}