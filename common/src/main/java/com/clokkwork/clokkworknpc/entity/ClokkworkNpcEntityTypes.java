package com.clokkwork.clokkworknpc.entity;

import com.clokkwork.clokkworknpc.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

/**
 * Holder for registered entity types. Loader modules assign these during registration.
 */
public final class ClokkworkNpcEntityTypes {

	public static final ResourceLocation GENERIC_HUMANOID_ID =
			ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "generic_humanoid");

	public static EntityType<GenericHumanoidNpcEntity> GENERIC_HUMANOID;

	private ClokkworkNpcEntityTypes() {
	}
}
