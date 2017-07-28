package morph.avaritia.integration.rs;

import java.util.List;

import cofh.redstonearsenal.init.RABlocks;
import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.compressor.CompressorManager;
import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public class RedstoneArsenal {

	public static void addRecipes() {
		CompressorManager.addRecipe(ModItems.fluxed_singularity, 100, new ItemStack(RABlocks.blockStorage));
	}

	public static void addRSSingularityToList(List<ItemStack> list) {
		list.add(ModItems.fluxed_singularity);
	}

}
