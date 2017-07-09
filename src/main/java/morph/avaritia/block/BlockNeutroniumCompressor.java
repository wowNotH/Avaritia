package morph.avaritia.block;

import java.util.Random;

import codechicken.lib.util.ItemUtils;
import codechicken.lib.util.RotationUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.init.AvaritiaProps;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNeutroniumCompressor extends BlockContainer implements IModelRegister {

	public BlockNeutroniumCompressor() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(20);
		setUnlocalizedName("avaritia:neutronium_compressor");
		setRegistryName("neutronium_compressor");
		setHarvestLevel("pickaxe", 3);
		setCreativeTab(Avaritia.tab);
		setDefaultState(getDefaultState().withProperty(AvaritiaProps.HORIZONTAL_FACING, EnumFacing.NORTH).withProperty(AvaritiaProps.ACTIVE, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AvaritiaProps.HORIZONTAL_FACING, AvaritiaProps.ACTIVE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AvaritiaProps.ACTIVE) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(AvaritiaProps.ACTIVE, meta == 0 ? Boolean.valueOf(false) : Boolean.valueOf(true));
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(AvaritiaProps.ACTIVE) ? 9 : 0;
	}

	private TileNeutroniumCompressor getTE(World worldIn, BlockPos pos) {
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileNeutroniumCompressor) {
			return (TileNeutroniumCompressor) worldIn.getTileEntity(pos);
		}
		return null;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighborBlock) {
		if (world instanceof World) {
			updatePowered((World) world, pos, world.getBlockState(pos));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		updatePowered(world, pos, state);
	}

	private void updatePowered(World world, BlockPos pos, IBlockState state) {
		if (getTE(world, pos) != null && (getTE(world, pos) instanceof TileNeutroniumCompressor)) {
			TileNeutroniumCompressor te = getTE(world, pos);
			boolean running = te.getCompressionProgress() > 0;
			if (running != world.getBlockState(pos).getValue(AvaritiaProps.ACTIVE)) {
				world.setBlockState(pos, state.withProperty(AvaritiaProps.ACTIVE, Boolean.valueOf(running)), 2);
			}
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess blockAccessor, BlockPos pos) {
		TileEntity te;
		if (blockAccessor instanceof ChunkCache) {
			te = ((ChunkCache) blockAccessor).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
		}
		else {
			te = blockAccessor.getTileEntity(pos);
		}
		if (!(te instanceof TileNeutroniumCompressor)) {
			return getDefaultState();
		}
		TileNeutroniumCompressor compressor = (TileNeutroniumCompressor) te;
		state = state.withProperty(AvaritiaProps.HORIZONTAL_FACING, compressor.getFacing());
		boolean isActive = Boolean.valueOf(compressor.getInputItems().size() > 0);
		return state.withProperty(AvaritiaProps.ACTIVE, isActive);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		else {
			player.openGui(Avaritia.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileNeutroniumCompressor();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileNeutroniumCompressor) {
			TileNeutroniumCompressor machine = (TileNeutroniumCompressor) tile;
			machine.setFacing(RotationUtils.getPlacedRotationHorizontal(player));
		}

	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileNeutroniumCompressor compressor = (TileNeutroniumCompressor) world.getTileEntity(pos);

		if (compressor != null) {
			ItemUtils.dropInventory(world, pos, compressor);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ResourceLocation location = new ResourceLocation("avaritia:machine");
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				String modelLoc = "type=neutronium_compressor";
				modelLoc += ",facing=" + state.getValue(AvaritiaProps.HORIZONTAL_FACING).getName();
				modelLoc += ",active=" + state.getValue(AvaritiaProps.ACTIVE).toString().toLowerCase();
				return new ModelResourceLocation(location, modelLoc);
			}
		});
		ModelResourceLocation invLoc = new ModelResourceLocation(location, "type=neutronium_compressor,facing=north,active=true");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, invLoc);
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), stack -> invLoc);
	}
}
