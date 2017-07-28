package morph.avaritia.container;

import morph.avaritia.init.ModBlocks;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class ContainerExtremeCrafting extends Container {

	/**
	 * The crafting matrix inventory (9x9).
	 */
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 9, 9);
	public IInventory craftResult = new InventoryCraftResult();
	protected World worldObj;
	protected BlockPos pos;

	public ContainerExtremeCrafting(InventoryPlayer playerInv, World world, BlockPos pos) {
		worldObj = world;
		this.pos = pos;

		addSlotToContainer(new SlotCrafting(playerInv.player, craftMatrix, craftResult, 0, 210, 80));

		for (int y = 0; y < 9; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(craftMatrix, x + y * 9, 12 + x * 18, 8 + y * 18));
			}
		}

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 39 + x * 18, 174 + y * 18));
			}
		}

		for (int x = 0; x < 9; ++x) {
			addSlotToContainer(new Slot(playerInv, x, 39 + x * 18, 232));
		}

		getInventory();
		onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory matrix) {
		craftResult.setInventorySlotContents(0, ExtremeCraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj));
		detectAndSendChanges();
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!worldObj.isRemote) {
			for (int i = 0; i < 9; ++i) {
				ItemStack itemstack = craftMatrix.removeStackFromSlot(i);

				if (!itemstack.isEmpty()) {
					player.dropItem(itemstack, false);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return worldObj.getBlockState(pos) == ModBlocks.dire_craft.getDefaultState() && player.getDistanceSq(pos) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, worldObj, playerIn);

				if (!mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= 10 && index < 37) {
				if (!mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (index >= 37 && index < 46) {
				if (!mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}
		detectAndSendChanges();
		return itemstack;
	}

}
