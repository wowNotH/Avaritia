package morph.avaritia.item.tools;

import morph.avaritia.Avaritia;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
public class ItemHoeInfinity extends ItemHoe {

	private static final ToolMaterial opHoe = EnumHelper.addToolMaterial("INFINITY_HOE", 32, 9999, 9999F, 20.0F, 200);

	public ItemHoeInfinity() {
		super(opHoe);
		setUnlocalizedName("avaritia:infinity_hoe");
		setRegistryName("infinity_hoe");
		setCreativeTab(Avaritia.tab);
	}

	/*
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack itemstack = player.getHeldItem(hand);
	
		if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
			return EnumActionResult.FAIL;
		}
		else {
			int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
			if (hook != 0) {
				return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
			}
	
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
	
			if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
					setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
					return EnumActionResult.SUCCESS;
				}
	
				if (block == Blocks.DIRT) {
					switch (iblockstate.getValue(BlockDirt.VARIANT)) {
					case DIRT:
						setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
						return EnumActionResult.SUCCESS;
					case COARSE_DIRT:
						setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
						return EnumActionResult.SUCCESS;
					case PODZOL:
						break;
					default:
						break;
					}
				}
			}
	
			return EnumActionResult.PASS;
		}

	}
	*/

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (!hoeBlock(stack, player, world, pos, facing)) {
			if (world.getBlockState(pos).getBlock() != Blocks.FARMLAND) {
				return EnumActionResult.FAIL;
			}
		}

		if (player.isSneaking()) {
			return EnumActionResult.SUCCESS;
		}

		int AOE = 4;
		boolean fill = true;

		Iterable<BlockPos> blocks = BlockPos.getAllInBox(pos.add(-AOE, 0, -AOE), pos.add(AOE, 0, AOE));

		for (BlockPos aoePos : blocks) {
			if (aoePos.equals(pos)) {
				continue;
			}

			if (!fill && (world.isAirBlock(aoePos) || !world.isAirBlock(aoePos.up()))) {
				continue;
			}

			boolean airOrReplaceable = world.isAirBlock(aoePos) || world.getBlockState(aoePos).getBlock().isReplaceable(world, aoePos);
			boolean lowerBlockOk = world.isSideSolid(aoePos.down(), EnumFacing.UP) || world.getBlockState(aoePos.down()).getBlock() == Blocks.FARMLAND;

			if (fill && airOrReplaceable && lowerBlockOk && (player.capabilities.isCreativeMode || player.inventory.hasItemStack(new ItemStack(Blocks.DIRT)))) {
				BlockEvent.PlaceEvent event = ForgeEventFactory.onPlayerBlockPlace(player, new BlockSnapshot(world, aoePos, Blocks.DIRT.getDefaultState()), EnumFacing.UP, player.getActiveHand());

				if (!event.isCanceled() && (player.capabilities.isCreativeMode || consumeStack(new ItemStack(Blocks.DIRT), player.inventory))) {
					world.setBlockState(aoePos, Blocks.DIRT.getDefaultState());
				}
			}

			boolean canRemoveAbove = world.getBlockState(aoePos.up()).getBlock() == Blocks.DIRT || world.getBlockState(aoePos.up()).getBlock() == Blocks.GRASS || world.getBlockState(aoePos.up()).getBlock() == Blocks.FARMLAND || world.getBlockState(aoePos.up()).getBlock().isReplaceable(world, aoePos.up());
			boolean up2OK = world.isAirBlock(aoePos.up().up()) || world.getBlockState(aoePos.up().up()).getBlock().isReplaceable(world, aoePos.up().up());

			if (fill && !world.isAirBlock(aoePos.up()) && canRemoveAbove && up2OK) {
				if (world.getBlockState(aoePos.up()).getBlock() == Blocks.DIRT) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Blocks.DIRT)));
				}
				world.setBlockToAir(aoePos.up());
			}
			hoeBlock(stack, player, world, aoePos, facing);
		}

		return EnumActionResult.SUCCESS;
	}

	public boolean consumeStack(ItemStack stack, IInventory inventory) {
		if (stack.isEmpty()) {
			return false;
		}

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack s = inventory.getStackInSlot(i);
			if (s.isEmpty()) {
				continue;
			}

			if (ItemStack.areItemsEqual(stack, s) && stack.getItemDamage() == s.getItemDamage() && s.getCount() >= stack.getCount()) {
				s.shrink(stack.getCount());
				inventory.markDirty();
				return true;
			}
		}

		return false;
	}

	private boolean hoeBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing face) {

		if (!player.canPlayerEdit(pos, face, stack)) {
			return false;
		}
		else {
			int hook = ForgeEventFactory.onHoeUse(stack, player, world, pos);
			if (hook != 0) {
				return hook > 0;
			}

			IBlockState iblockstate = world.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (face != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
				if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
					setBlock(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
					return true;
				}

				if (block == Blocks.DIRT) {
					switch (iblockstate.getValue(BlockDirt.VARIANT)) {
					case DIRT:
						setBlock(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
						return true;
					case COARSE_DIRT:
						setBlock(stack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
						return true;
					}
				}
			}

			return false;
		}
	}

	@Override
	protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
		worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

		if (!worldIn.isRemote) {
			worldIn.setBlockState(pos, state, 11);
			//stack.damageItem(1, player);
		}
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, 0);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return ModItems.COSMIC_RARITY;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityImmortalItem(world, location, itemstack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		return false;
	}

}
