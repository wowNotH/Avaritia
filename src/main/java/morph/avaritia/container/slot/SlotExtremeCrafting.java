package morph.avaritia.container.slot;

import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by brandon3055 on 18/02/2017.
 */
public class SlotExtremeCrafting extends Slot {

	private final InventoryCrafting craftMatrix;
	private final EntityPlayer player;
	private int amountCrafted;

	public SlotExtremeCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
		craftMatrix = craftingInventory;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
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
	protected void onSwapCraft(int amount) {
		amountCrafted += amount;
	}

	@Override
	protected void onCrafting(ItemStack stack) {
		if (amountCrafted > 0) {
			stack.onCrafting(player.world, player, amountCrafted);
			net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(player, stack, craftMatrix);
		}

		amountCrafted = 0;
		/*
		if (stack.getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE))
		{
		    this.player.addStat(AchievementList.BUILD_WORK_BENCH);
		}

		if (stack.getItem() instanceof ItemPickaxe)
		{
		    this.player.addStat(AchievementList.BUILD_PICKAXE);
		}

		if (stack.getItem() == Item.getItemFromBlock(Blocks.FURNACE))
		{
		    this.player.addStat(AchievementList.BUILD_FURNACE);
		}

		if (stack.getItem() instanceof ItemHoe)
		{
		    this.player.addStat(AchievementList.BUILD_HOE);
		}

		if (stack.getItem() == Items.BREAD)
		{
		    this.player.addStat(AchievementList.MAKE_BREAD);
		}

		if (stack.getItem() == Items.CAKE)
		{
		    this.player.addStat(AchievementList.BAKE_CAKE);
		}

		if (stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe)stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD)
		{
		    this.player.addStat(AchievementList.BUILD_BETTER_PICKAXE);
		}

		if (stack.getItem() instanceof ItemSword)
		{
		    this.player.addStat(AchievementList.BUILD_SWORD);
		}

		if (stack.getItem() == Item.getItemFromBlock(Blocks.ENCHANTING_TABLE))
		{
		    this.player.addStat(AchievementList.ENCHANTMENTS);
		}

		if (stack.getItem() == Item.getItemFromBlock(Blocks.BOOKSHELF))
		{
		    this.player.addStat(AchievementList.BOOKCASE);
		}
		*/
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
		NonNullList<ItemStack> nonnulllist = ExtremeCraftingManager.getInstance().getRemainingItems(craftMatrix, thePlayer.world);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = craftMatrix.getStackInSlot(i);
			ItemStack itemstack1 = nonnulllist.get(i);

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
				else if (!player.inventory.addItemStackToInventory(itemstack1)) {
					player.dropItem(itemstack1, false);
				}
			}
		}

		return stack;
	}
}
