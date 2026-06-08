package com.clokkwork.clokkworknpc.network.payload;

import com.clokkwork.clokkworknpc.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CloseDialoguePayload() implements CustomPacketPayload {

	public static final CloseDialoguePayload INSTANCE = new CloseDialoguePayload();

	public static final CustomPacketPayload.Type<CloseDialoguePayload> TYPE = new CustomPacketPayload.Type<>(
			ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "close_dialogue")
	);

	public static final StreamCodec<ByteBuf, CloseDialoguePayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
