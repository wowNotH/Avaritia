package morph.avaritia.client.gui;

import codechicken.lib.texture.TextureUtils;
import morph.avaritia.container.ContainerExtremeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author p455w0rd
 *
 */
public class GUIExtremeCrafting extends GuiContainer {

	private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/dire_crafting_gui.png");

	public GUIExtremeCrafting(InventoryPlayer playerInv, ContainerExtremeCrafting container) {
		super(container);
		ySize = 256;
		xSize = 238;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		//this.fontRenderer.drawString(StatCollector.translateToLocal("container.extreme_crafting"), 28, 6, 4210752);
		//this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		TextureUtils.changeTexture(GUI_TEX);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);//TODO, this was, (x, y, 0, 0, ySize, ySize), Why was it ySize and not xSize..
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (int i1 = 0; i1 < inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = inventorySlots.inventorySlots.get(i1);

			if (slot.getStack() == null) {
				slot.putStack(ItemStack.EMPTY);
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
