package morph.avaritia.tile;

import codechicken.lib.util.BlockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileDireCraftingTable extends TileBase implements ISidedInventory {

	private ItemStack result = ItemStack.EMPTY;
	private NonNullList<ItemStack> matrix = NonNullList.<ItemStack>withSize(81, ItemStack.EMPTY);

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		result = new ItemStack(tag.getCompoundTag("Result"));
		for (int x = 0; x < matrix.size(); x++) {
			matrix.set(x, new ItemStack(tag.getCompoundTag("Craft" + x)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		if (!result.isEmpty()) {
			NBTTagCompound produce = new NBTTagCompound();
			result.writeToNBT(produce);
			tag.setTag("Result", produce);
		}
		else {
			tag.removeTag("Result");
		}

		for (int x = 0; x < matrix.size(); x++) {
			if (!matrix.get(x).isEmpty()) {
				NBTTagCompound craft = new NBTTagCompound();
				matrix.get(x).writeToNBT(craft);
				tag.setTag("Craft" + x, craft);
			}
			else {
				tag.removeTag("Craft" + x);
			}
		}
		return super.writeToNBT(tag);
	}

	@Override
	public int getSizeInventory() {
		return 82;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 0) {
			return result;
		}
		else if (slot <= matrix.size()) {
			return matrix.get(slot - 1);
		}
		else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int decrement) {

		if (slot == 0) {
			if (!result.isEmpty()) {
				for (int x = 1; x <= matrix.size(); x++) {
					decrStackSize(x, 1);
				}
				if (result.getCount() <= decrement) {
					ItemStack craft = result;
					result = ItemStack.EMPTY;
					return craft;
				}
				ItemStack split = result.splitStack(decrement);
				if (result.getCount() <= 0) {
					result = ItemStack.EMPTY;
				}
				return split;
			}
			else {
				return ItemStack.EMPTY;
			}
		}
		else if (slot <= matrix.size()) {
			if (!matrix.get(slot - 1).isEmpty()) {
				if (matrix.get(slot - 1).getCount() <= decrement) {
					ItemStack ingredient = matrix.get(slot - 1);
					matrix.set(slot - 1, ItemStack.EMPTY);
					return ingredient;
				}
				ItemStack split = matrix.get(slot - 1).splitStack(decrement);
				if (matrix.get(slot - 1).getCount() <= 0) {
					matrix.set(slot - 1, ItemStack.EMPTY);
				}
				return split;
			}
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (slot == 0) {
			if (!result.isEmpty()) {
				for (int x = 1; x <= matrix.size(); x++) {
					decrStackSize(x, 1);
				}

				ItemStack craft = result;
				result = ItemStack.EMPTY;
				return craft;

			}
			else {
				return ItemStack.EMPTY;
			}
		}
		else if (slot <= matrix.size()) {
			if (!matrix.get(slot - 1).isEmpty()) {
				ItemStack ingredient = matrix.get(slot - 1);
				matrix.set(slot - 1, ItemStack.EMPTY);
				return ingredient;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && BlockUtils.isEntityInRange(pos, player, 64);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {

		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (slot == 0) {
			result = stack;
		}
		else if (slot <= matrix.size()) {
			matrix.set(slot - 1, stack);
		}
	}

	@Override
	public String getName() {
		return "container.dire";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		if (hasCustomName()) {
			return new TextComponentString(getName());
		}
		return new TextComponentTranslation(getName());
	}

	@Override
	public int[] getSlotsForFace(EnumFacing face) {
		return new int[] {
				0
		};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, EnumFacing face) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, EnumFacing face) {
		return slot == 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void clear() {
		result = null;
		for (int x = 0; x < matrix.size(); x++) {
			matrix.set(x, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack element : matrix) {
			if (!element.isEmpty()) {
				return false;
			}
		}
		return true;
	}

}
