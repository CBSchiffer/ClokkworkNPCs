package com.clokkwork.clokkworknpc.network.payload;

import com.clokkwork.clokkworknpc.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChooseDialogueOptionPayload(int optionIndex) implements CustomPacketPayload {

	public static final int CANCEL_OPTION_INDEX = -1;

	public static final CustomPacketPayload.Type<ChooseDialogueOptionPayload> TYPE = new CustomPacketPayload.Type<>(
			ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "choose_dialogue_option")
	);

	public static final StreamCodec<ByteBuf, ChooseDialogueOptionPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			ChooseDialogueOptionPayload::optionIndex,
			ChooseDialogueOptionPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
