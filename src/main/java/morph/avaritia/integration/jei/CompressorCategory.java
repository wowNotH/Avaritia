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
import net.minecraft.client.Minecraft;
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
	private final IGuiHelper guiHelper;
	private final ICraftingGridHelper craftingGridHelper;
	private IRecipeWrapper wrapper;

	public CompressorCategory(IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
		ResourceLocation location = new ResourceLocation("avaritia:textures/gui/compressor.png");
		background = guiHelper.createDrawable(location, 5, 10, 165, 50);
		localizedName = I18n.format("crafting.extreme_compression");
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot, craftOutputSlot);
	}

	public IGuiHelper getGuiHelper() {
		return guiHelper;
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
		wrapper = recipeWrapper;
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(craftOutputSlot, false, 111, 24);
		guiItemStacks.init(craftInputSlot, true, 33, 24);

		if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
			ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
			customWrapper.setRecipe(recipeLayout, ingredients);
			return;
		}

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
		if (inputs.size() > 0 && outputs.get(0) != null) {
			//craftingGridHelper.setInputs(guiItemStacks, inputs);
			guiItemStacks.set(1, inputs.get(0));
			guiItemStacks.set(0, outputs.get(0));
			//craftingGridHelper.setOutput(guiItemStacks, outputs.get(0));
		}
	}

	public CompressorWrapper getWrapper() {
		return wrapper != null && wrapper instanceof CompressorWrapper ? (CompressorWrapper) wrapper : null;
	}

	@Override
	public String getModName() {
		return null;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		if (getWrapper() != null) {
			minecraft.fontRenderer.drawString("Amount: " + getWrapper().getCost(), 45, 50, 0x464646, false);
		}
	}

}