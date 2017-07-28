package morph.avaritia.client.gui;

import morph.avaritia.container.ContainerDireCrafting;
import morph.avaritia.tile.TileDireCraftingTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GUIDireCrafting extends GuiContainer {

	private static final ResourceLocation tex = new ResourceLocation("avaritia:textures/gui/dire_crafting_gui.png");

	public GUIDireCrafting(InventoryPlayer par1InventoryPlayer, World world, BlockPos pos, TileDireCraftingTable table) {
		super(new ContainerDireCrafting(par1InventoryPlayer, world, pos, table));
		ySize = 256;
		xSize = 238;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		//this.fontRendererObj.drawString(StatCollector.translateToLocal("container.extreme_crafting"), 28, 6, 4210752);
		//this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(tex);
		int foo = (width - xSize) / 2;
		int bar = (height - ySize) / 2;
		this.drawTexturedModalRect(foo, bar, 0, 0, ySize, ySize);
	}
}
