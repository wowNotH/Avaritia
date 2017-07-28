package morph.avaritia.integration.jei;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import morph.avaritia.client.gui.GUIDireCrafting;
import morph.avaritia.client.gui.GUINeutroniumCompressor;
import morph.avaritia.container.ContainerDireCrafting;
import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.compressor.CompressorManager;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by brandon3055 on 17/02/2017.
 */
@JEIPlugin
public class AvaritiaJEIPlugin implements IModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry iSubtypeRegistry) {

	}

	@Override
	public void registerIngredients(IModIngredientRegistration iModIngredientRegistration) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		IIngredientBlacklist blacklist = helpers.getIngredientBlacklist();

		//Dire Crafting
		registry.addRecipeHandlers(new ExtremeShapedRecipeHandler(), new ExtremeShapedOreRecipeHandler(helpers), new ExtremeShelessRecipeHandler(), new ExtremeShapelessOreRecipeHandler(helpers), new CompressorHandler(helpers));
		registry.addRecipeClickArea(GUIDireCrafting.class, 175, 79, 28, 26, RecipeCategoryUids.EXTREME_CRAFTING); //175
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.dire_craft), RecipeCategoryUids.EXTREME_CRAFTING);
		registry.addRecipes(ExtremeCraftingManager.getInstance().getRecipeList(), RecipeCategoryUids.EXTREME_CRAFTING);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerDireCrafting.class, RecipeCategoryUids.EXTREME_CRAFTING, 1, 81, 82, 36);

		//Compressor
		//registry.handleRecipes(CompressorRecipe.class, recipe -> new CompressorWrapper(helpers, recipe), RecipeCategoryUids.COMPRESSOR);
		registry.addRecipeClickArea(GUINeutroniumCompressor.class, 5, 79, 28, 26, RecipeCategoryUids.COMPRESSOR); //175
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.neutronium_compressor), RecipeCategoryUids.COMPRESSOR);
		registry.addRecipes(CompressorManager.getRecipes(), RecipeCategoryUids.COMPRESSOR);
		//registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerNeutroniumCompressor.class, RecipeCategoryUids.COMPRESSOR, 0, 1, 2, 38);

		if (!ConfigHandler.copper || OreDictionary.getOres("blockCopper").isEmpty()) {
			blacklist.addIngredientToBlacklist(ModItems.copperSingularity);
		}
		if (!ConfigHandler.tin || OreDictionary.getOres("blockTin").isEmpty()) {
			blacklist.addIngredientToBlacklist(ModItems.tinSingularity);
		}
		if (!ConfigHandler.lead || OreDictionary.getOres("blockLead").isEmpty()) {
			blacklist.addIngredientToBlacklist(ModItems.leadSingularity);
		}
		if (!ConfigHandler.silver || OreDictionary.getOres("blockSilver").isEmpty()) {
			blacklist.addIngredientToBlacklist(ModItems.silverSingularity);
		}
		if (!ConfigHandler.nickel || OreDictionary.getOres("blockNickel").isEmpty()) {
			blacklist.addIngredientToBlacklist(ModItems.nickelSingularity);
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime iJeiRuntime) {

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = helpers.getGuiHelper();
		registry.addRecipeCategories(new ExtremeCraftingCategory(guiHelper));
		registry.addRecipeCategories(new CompressorCategory(guiHelper));
	}

	public static void setRecipeItems(@Nonnull IRecipeLayout recipeLayout, @Nonnull IIngredients ingredients, @Nullable int[] itemInputSlots, @Nullable int[] itemOutputSlots, @Nullable int[] fluidInputSlots, @Nullable int[] fluidOutputSlots) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		if (itemInputSlots != null) {
			List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
			for (int i = 0; i < inputs.size() && i < itemInputSlots.length; i++) {
				int inputSlot = itemInputSlots[i];
				guiItemStacks.set(inputSlot, inputs.get(i));
			}
		}

		if (itemOutputSlots != null) {
			List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
			for (int i = 0; i < outputs.get(0).size() && i < itemOutputSlots.length; i++) {
				int outputSlot = itemOutputSlots[i];
				guiItemStacks.set(outputSlot, outputs.get(0).get(i));
			}
		}

		if (fluidInputSlots != null) {
			List<List<FluidStack>> fluidInputs = ingredients.getInputs(FluidStack.class);
			for (int i = 0; i < fluidInputs.size() && i < fluidInputSlots.length; i++) {
				int inputTank = fluidInputSlots[i];
				guiFluidStacks.set(inputTank, fluidInputs.get(i));
			}
		}

		if (fluidOutputSlots != null) {
			List<List<FluidStack>> fluidOutputs = ingredients.getOutputs(FluidStack.class);
			for (int i = 0; i < fluidOutputs.get(0).size() && i < fluidOutputSlots.length; i++) {
				int outputTank = fluidOutputSlots[i];
				guiFluidStacks.set(outputTank, fluidOutputs.get(0).get(i));
			}
		}
	}

}
