package morph.avaritia.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEndestPearl extends EntityThrowable {

	public EntityEndestPearl(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityEndestPearl(World world, EntityLivingBase ent) {
		super(world, ent);
	}

	public EntityEndestPearl(World world) {
		super(world);
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		if (pos.entityHit != null) {
			pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
		}

		for (int i = 0; i < 100; ++i) {
			world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, rand.nextGaussian() * 3, rand.nextGaussian() * 3, rand.nextGaussian() * 3);
		}

		if (!world.isRemote) {
			//this.world.createExplosion(this, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 4.0f, true);

			Entity ent = new EntityGapingVoid(world);
			EnumFacing dir = pos.sideHit;
			if (ent != null && dir != null) {
				ent.setLocationAndAngles(posX + dir.getFrontOffsetX() * 0.25, posY + dir.getFrontOffsetY() * 0.25, posZ + dir.getFrontOffsetZ() * 0.25, rotationYaw, 0.0F);
				world.spawnEntity(ent);
			}
			setDead();
		}
	}

}
