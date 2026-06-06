package com.clokkwork.clokkworknpc.data.npc;

import com.clokkwork.clokkworknpc.Constants;
import net.minecraft.resources.ResourceLocation;

public final class NpcDefinitionDefaults {

	public static final String DISPLAY_NAME = "NPC";
	public static final ResourceLocation HUMANOID_MODEL_TYPE =
			ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "humanoid");
	public static final ResourceLocation DEFAULT_STEVE_SKIN =
			ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
	public static final boolean NO_AI = false;
	public static final boolean UNIQUE = false;

	private NpcDefinitionDefaults() {
	}
}
