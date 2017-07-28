package morph.avaritia.recipe.compressor;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import morph.avaritia.util.CompressorBalanceCalculator;
import net.minecraft.item.ItemStack;

public class CompressorRecipe {

	private ItemStack product = ItemStack.EMPTY;
	private int cost;
	private ItemStack input = ItemStack.EMPTY;
	private boolean specific;

	public CompressorRecipe(@Nonnull ItemStack output, int amount, @Nonnull ItemStack ingredient, boolean exact) {
		product = output;
		cost = amount;
		input = ingredient;
		specific = exact;
	}

	public CompressorRecipe(@Nonnull ItemStack output, int amount, @Nonnull ItemStack ingredient) {
		this(output, amount, ingredient, false);
	}

	public ItemStack getOutput() {
		return product.copy();
	}

	public List<ItemStack> getInputs() {
		return Collections.singletonList(input);
	}

	public int getCost() {
		if (specific) {
			return cost;
		}
		else {
			return CompressorBalanceCalculator.balanceCost(cost);
		}
	}

	public boolean isValidInput(ItemStack ingredient) {
		return ingredient.isItemEqual(input);
	}

	public String getIngredientName() {
		return input.getDisplayName();
	}

	public Object getIngredient() {
		return input;
	}

}
