package com.clokkwork.clokkworknpc.fabric.client;

import com.clokkwork.clokkworknpc.entity.GenericHumanoidNpcEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GenericHumanoidNpcRenderer extends HumanoidMobRenderer<GenericHumanoidNpcEntity, HumanoidModel<GenericHumanoidNpcEntity>> {

	private static final ResourceLocation PLACEHOLDER_TEXTURE =
			ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");

	public GenericHumanoidNpcRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(GenericHumanoidNpcEntity entity) {
		return PLACEHOLDER_TEXTURE;
	}
}
