package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.init.ModIntegration.Mods;
import morph.avaritia.item.ItemArmorInfinity;
import morph.avaritia.item.ItemEndestPearl;
import morph.avaritia.item.ItemFracturedOre;
import morph.avaritia.item.ItemMatterCluster;
import morph.avaritia.item.ItemResource;
import morph.avaritia.item.ItemSingularity;
import morph.avaritia.item.tools.ItemAxeInfinity;
import morph.avaritia.item.tools.ItemBowInfinity;
import morph.avaritia.item.tools.ItemHoeInfinity;
import morph.avaritia.item.tools.ItemPickaxeInfinity;
import morph.avaritia.item.tools.ItemShovelInfinity;
import morph.avaritia.item.tools.ItemSwordInfinity;
import morph.avaritia.item.tools.ItemSwordSkulls;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

/**
 * Created by covers1624 on 11/04/2017.
 */
public class ModItems {

	public static EnumRarity COSMIC_RARITY = EnumHelper.addRarity("COSMIC", TextFormatting.RED, "Cosmic");

	public static ItemResource resource;

	/**
	 * 0 = Iron,
	 * 1 = Gold,
	 * 2 = Lapis,
	 * 3 = Redstone,
	 * 4 = Nether Quartz,
	 * 5 = Copper,
	 * 6 = Tin,
	 * 7 = Lead,
	 * 8 = Silver,
	 * 9 = Nickel,
	 * 10 = Diamond,
	 * 11 = Emerald
	 */
	public static ItemSingularity singularity;

	public static ItemSwordInfinity infinity_sword;
	public static ItemBowInfinity infinity_bow;
	public static ItemPickaxeInfinity infinity_pickaxe;
	public static ItemShovelInfinity infinity_shovel;
	public static ItemAxeInfinity infinity_axe;
	public static ItemHoeInfinity infinity_hoe;

	public static ItemArmorInfinity infinity_helmet;
	public static ItemArmorInfinity infinity_chestplate;
	public static ItemArmorInfinity infinity_pants;
	public static ItemArmorInfinity infinity_boots;

	public static ItemSwordSkulls skull_sword;

	public static ItemEndestPearl endest_pearl;
	public static ItemFood ultimate_stew;
	public static ItemFood cosmic_meatballs;

	public static ItemFracturedOre fractured_ore;

	public static ItemMatterCluster matter_cluster;

	public static ItemStack ironSingularity;
	public static ItemStack goldSingularity;
	public static ItemStack lapisSingularity;
	public static ItemStack redstoneSingularity;
	public static ItemStack quartzSingularity;
	public static ItemStack copperSingularity;
	public static ItemStack tinSingularity;
	public static ItemStack leadSingularity;
	public static ItemStack silverSingularity;
	public static ItemStack nickelSingularity;
	public static ItemStack diamondSingularity;
	public static ItemStack emeraldSingularity;
	public static ItemStack FLUXED_SINGULARITY;

	public static ItemStack diamond_lattice;
	public static ItemStack crystal_matrix_ingot;
	public static ItemStack neutron_pile;
	public static ItemStack neutron_nugget;
	public static ItemStack neutronium_ingot;
	public static ItemStack infinity_catalyst;
	public static ItemStack infinity_ingot;
	public static ItemStack record_fragment;

	public static void init() {

		resource = register(new ItemResource(Avaritia.tab, "resource"));
		if (FMLCommonHandler.instance().getSide().isClient()) {
			resource.registerModels();
		}

		//0
		diamond_lattice = resource.registerItem("diamond_lattice");
		//1
		crystal_matrix_ingot = resource.registerItem("crystal_matrix_ingot");
		//2
		neutron_pile = resource.registerItem("neutron_pile");
		//3
		neutron_nugget = resource.registerItem("neutron_nugget");
		//4
		neutronium_ingot = resource.registerItem("neutronium_ingot");
		//5
		infinity_catalyst = resource.registerItem("infinity_catalyst");
		//6
		infinity_ingot = resource.registerItem("infinity_ingot");
		//7
		record_fragment = resource.registerItem("record_fragment");

		if (ConfigHandler.craftingOnly) {
			return;
		}

		singularity = register(new ItemSingularity(Avaritia.tab, "singularity"));
		if (FMLCommonHandler.instance().getSide().isClient()) {
			singularity.registerModels();
		}
		ironSingularity = singularity.registerItem("iron");
		goldSingularity = singularity.registerItem("gold");
		lapisSingularity = singularity.registerItem("lapis");
		redstoneSingularity = singularity.registerItem("redstone");
		quartzSingularity = singularity.registerItem("quartz");
		copperSingularity = singularity.registerItem("copper");
		tinSingularity = singularity.registerItem("tin");
		leadSingularity = singularity.registerItem("lead");
		silverSingularity = singularity.registerItem("silver");
		nickelSingularity = singularity.registerItem("nickel");
		diamondSingularity = singularity.registerItem("diamond");
		emeraldSingularity = singularity.registerItem("emerald");
		if (Mods.RS.isLoaded()) {
			FLUXED_SINGULARITY = singularity.registerItem("flux_singularity");
		}

		infinity_sword = register(new ItemSwordInfinity());

		infinity_bow = register(new ItemBowInfinity());

		infinity_pickaxe = register(new ItemPickaxeInfinity());

		infinity_shovel = register(new ItemShovelInfinity());

		infinity_axe = register(new ItemAxeInfinity());

		infinity_hoe = register(new ItemHoeInfinity());

		infinity_helmet = new ItemArmorInfinity(EntityEquipmentSlot.HEAD);
		infinity_helmet.setUnlocalizedName("avaritia:infinity_helmet");
		register(infinity_helmet.setRegistryName("infinity_helmet"));

		infinity_chestplate = new ItemArmorInfinity(EntityEquipmentSlot.CHEST);
		infinity_chestplate.setUnlocalizedName("avaritia:infinity_chestplate");
		register(infinity_chestplate.setRegistryName("infinity_chestplate"));

		infinity_pants = new ItemArmorInfinity(EntityEquipmentSlot.LEGS);
		infinity_pants.setUnlocalizedName("avaritia:infinity_pants");
		register(infinity_pants.setRegistryName("infinity_pants"));

		infinity_boots = new ItemArmorInfinity(EntityEquipmentSlot.FEET);
		infinity_boots.setUnlocalizedName("avaritia:infinity_boots");
		register(infinity_boots.setRegistryName("infinity_boots"));

		skull_sword = register(new ItemSwordSkulls());

		endest_pearl = register(new ItemEndestPearl());

		ultimate_stew = new ItemFood(20, 20, false);
		register(ultimate_stew.setRegistryName("ultimate_stew"));
		ultimate_stew.setPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 1), 1.0F).setUnlocalizedName("avaritia:ultimate_stew").setCreativeTab(Avaritia.tab);

		cosmic_meatballs = new ItemFood(20, 20, false);
		register(cosmic_meatballs.setRegistryName("cosmic_meatballs"));
		cosmic_meatballs.setPotionEffect(new PotionEffect(MobEffects.STRENGTH, 300, 1), 1.0F).setUnlocalizedName("avaritia:cosmic_meatballs").setCreativeTab(Avaritia.tab);

		if (ConfigHandler.fractured) {
			fractured_ore = register(new ItemFracturedOre());
			ItemFracturedOre.parseOreDictionary();
		}

		matter_cluster = register(new ItemMatterCluster());
	}

	public static <V extends IForgeRegistryEntry<?>> V register(V registerObject) {
		GameRegistry.register(registerObject);

		if (registerObject instanceof IModelRegister) {
			Avaritia.proxy.addModelRegister((IModelRegister) registerObject);
		}

		return registerObject;
	}

}
