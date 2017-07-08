package morph.avaritia.init;

import net.minecraftforge.fml.common.Loader;

/**
 * @author p455w0rd
 *
 */
public class ModIntegration {

	public static void init() {
	}

	public static enum Mods {

			JEI("justenoughitems", "Just Enough Items"), CT("crafttweaker", "CraftTweaker"),
			MT3("MineTweaker3", "Craft Tweaker (MT3)"), EXNIHILO_A("exnihiloadscensio", "Ex Nihilo Adscensio"),
			RS("redstonearsenal", "Redstone Arsenal");

		String modid, modName;

		Mods(String modid, String modName) {
			this.modid = modid;
			this.modName = modName;
		}

		public String getID() {
			return modid;
		}

		public String getName() {
			return modName;
		}

		public boolean isLoaded() {
			return Loader.isModLoaded(getID());
		}

	}

}
