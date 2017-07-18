package morph.avaritia.container.slot;

import java.util.function.Supplier;

import morph.avaritia.Avaritia;
import morph.avaritia.util.TimeTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * Created by covers1624 on 22/05/2017.
 */
public class ScrollingFakeSlot extends FakeSlot {

	private final Supplier<NonNullList<ItemStack>> stacksSupplier;

	private TimeTracker tracker = new TimeTracker();
	private int index = 0;

	public ScrollingFakeSlot(int xPosition, int yPosition, Supplier<NonNullList<ItemStack>> stacksSupplier) {
		super(xPosition, yPosition);
		this.stacksSupplier = stacksSupplier;
	}

	@Override
	public ItemStack getStack() {
		if (Avaritia.proxy.isClient() && !stacksSupplier.get().isEmpty() && stacksSupplier.get().size() > 0) {
			World world = Avaritia.proxy.getClientWorld();
			NonNullList<ItemStack> stacks = stacksSupplier.get();
			if (tracker.hasDelayPassed(world, 25)) {
				index++;
				if (index >= stacks.size() - 1) {
					index = 0;
				}
				tracker.markTime(world);
			}
			return stacks.get(index);
		}
		return ItemStack.EMPTY;
	}
}
