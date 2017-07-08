package morph.avaritia.integration.jei;

import static morph.avaritia.integration.jei.RecipeCategoryUids.COMPRESSOR;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author p455w0rd
 *
 */
public class CompressorCategory extends BlankRecipeCategory<IRecipeWrapper> {

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot = 1;

	private final IDrawable background;
	private final String localizedName;
	private final ICraftingGridHelper craftingGridHelper;

	public CompressorCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("avaritia:textures/gui/compressor.png");
		background = guiHelper.createDrawable(location, 0, 0, 189, 163);
		localizedName = I18n.format("crafting.extreme");
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot, craftOutputSlot);
	}

	@Override
	public String getUid() {
		return COMPRESSOR;
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(craftOutputSlot, false, 117, 35);
		guiItemStacks.init(craftInputSlot, true, 38, 34);

		if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
			ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
			customWrapper.setRecipe(recipeLayout, ingredients);
			return;
		}

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		craftingGridHelper.setInputs(guiItemStacks, inputs);
		craftingGridHelper.setOutput(guiItemStacks, outputs.get(0));
	}

	@Override
	public String getModName() {
		return null;
	}
}