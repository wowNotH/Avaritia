package morph.avaritia.integration.jei;

import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import morph.avaritia.recipe.compressor.CompressorRecipe;

/**
 * @author p455w0rd
 *
 */
public class CompressorHandler implements IRecipeHandler<CompressorRecipe> {

	private IJeiHelpers jeiHelpers;

	public CompressorHandler(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public Class<CompressorRecipe> getRecipeClass() {
		return CompressorRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid(CompressorRecipe extremeShapedRecipe) {
		return RecipeCategoryUids.EXTREME_CRAFTING;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CompressorRecipe extremeShapedRecipe) {
		return new CompressorWrapper(jeiHelpers, extremeShapedRecipe);
	}

	@Override
	public boolean isRecipeValid(CompressorRecipe recipe) {
		if (recipe.getOutput().isEmpty()) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, getRecipeWrapper(recipe));
			Log.error("Recipe has no output. {}", recipeInfo);
			return false;
		}
		int inputCount = 0;
		for (Object input : recipe.getInputs()) {
			if (input instanceof List) {
				if (((List) input).isEmpty()) {
					// missing items for an oreDict name. This is normal behavior, but the recipe is invalid.
					return false;
				}
			}
			if (input != null) {
				inputCount++;
			}
		}
		if (inputCount > 81) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, getRecipeWrapper(recipe));
			Log.error("Recipe has too many inputs. {}", recipeInfo);
			return false;
		}
		if (inputCount == 0) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, getRecipeWrapper(recipe));
			Log.error("Recipe has no inputs. {}", recipeInfo);
			return false;
		}
		return true;
	}
}