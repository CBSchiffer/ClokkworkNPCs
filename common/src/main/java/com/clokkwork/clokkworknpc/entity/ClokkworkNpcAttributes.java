package com.clokkwork.clokkworknpc.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class ClokkworkNpcAttributes {

	private ClokkworkNpcAttributes() {
	}

	public static AttributeSupplier.Builder genericHumanoid() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.FOLLOW_RANGE, 16.0)
				.add(Attributes.ATTACK_DAMAGE, 1.0)
				.add(Attributes.ARMOR, 0.0)
				.add(Attributes.ARMOR_TOUGHNESS, 0.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
	}
}
