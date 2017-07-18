package morph.avaritia.item.tools;

import java.util.List;

import morph.avaritia.Avaritia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemSwordSkulls extends ItemSword {

	public ItemSwordSkulls() {
		super(ToolMaterial.DIAMOND);
		setUnlocalizedName("avaritia:skullfire_sword");
		setRegistryName("skullfire_sword");
		setCreativeTab(Avaritia.tab);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip.skullfire_sword.desc"));
	}
}
