package morph.avaritia.container;

import javax.annotation.Nullable;

import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;

/**
 * Created by brandon3055 on 18/02/2017.
 */
public class SlotExtremeCrafting extends Slot {

	private final InventoryCrafting craftMatrix;
	private final EntityPlayer thePlayer;
	private int amountCrafted;

	public SlotExtremeCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		thePlayer = player;
		craftMatrix = craftingInventory;
	}

	@Override
	public boolean isItemValid(@Nullable ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		if (getHasStack()) {
			amountCrafted += Math.min(amount, getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {
		amountCrafted += amount;
		this.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		if (amountCrafted > 0) {
			stack.onCrafting(thePlayer.world, thePlayer, amountCrafted);
		}

		amountCrafted = 0;

		if (stack.getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE)) {
			//thePlayer.addStat(AchievementList.BUILD_WORK_BENCH);
		}

		if (stack.getItem() instanceof ItemPickaxe) {
			//thePlayer.addStat(AchievementList.BUILD_PICKAXE);
		}

		if (stack.getItem() == Item.getItemFromBlock(Blocks.FURNACE)) {
			//thePlayer.addStat(AchievementList.BUILD_FURNACE);
		}

		if (stack.getItem() instanceof ItemHoe) {
			//thePlayer.addStat(AchievementList.BUILD_HOE);
		}

		if (stack.getItem() == Items.BREAD) {
			//thePlayer.addStat(AchievementList.MAKE_BREAD);
		}

		if (stack.getItem() == Items.CAKE) {
			//thePlayer.addStat(AchievementList.BAKE_CAKE);
		}

		//if (stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe) stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
		//thePlayer.addStat(AchievementList.BUILD_BETTER_PICKAXE);
		//}

		if (stack.getItem() instanceof ItemSword) {
			//thePlayer.addStat(AchievementList.BUILD_SWORD);
		}

		if (stack.getItem() == Item.getItemFromBlock(Blocks.ENCHANTING_TABLE)) {
			//thePlayer.addStat(AchievementList.ENCHANTMENTS);
		}

		if (stack.getItem() == Item.getItemFromBlock(Blocks.BOOKSHELF)) {
			//thePlayer.addStat(AchievementList.BOOKCASE);
		}
	}

	@Override
	public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
		net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, craftMatrix);
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(playerIn);
		NonNullList<ItemStack> aitemstack = ExtremeCraftingManager.getInstance().getRemainingItems(craftMatrix, playerIn.world);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		for (int i = 0; i < aitemstack.size(); ++i) {
			ItemStack itemstack = craftMatrix.getStackInSlot(i);
			ItemStack itemstack1 = aitemstack.get(i);

			if (!itemstack.isEmpty()) {
				craftMatrix.decrStackSize(i, 1);
				itemstack = craftMatrix.getStackInSlot(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					craftMatrix.setInventorySlotContents(i, itemstack1);
				}
				else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					craftMatrix.setInventorySlotContents(i, itemstack1);
				}
				else if (!thePlayer.inventory.addItemStackToInventory(itemstack1)) {
					thePlayer.dropItem(itemstack1, false);
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
