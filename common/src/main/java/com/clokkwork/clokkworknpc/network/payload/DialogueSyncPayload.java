package com.clokkwork.clokkworknpc.network.payload;

import com.clokkwork.clokkworknpc.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record DialogueSyncPayload(
		int npcEntityId,
		String npcDisplayName,
		ResourceLocation dialogueId,
		String nodeId,
		String nodeText,
		List<DialogueOptionView> options
) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<DialogueSyncPayload> TYPE = new CustomPacketPayload.Type<>(
			ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "dialogue_sync")
	);

	public static final StreamCodec<ByteBuf, DialogueSyncPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			DialogueSyncPayload::npcEntityId,
			ByteBufCodecs.STRING_UTF8,
			DialogueSyncPayload::npcDisplayName,
			ResourceLocation.STREAM_CODEC,
			DialogueSyncPayload::dialogueId,
			ByteBufCodecs.STRING_UTF8,
			DialogueSyncPayload::nodeId,
			ByteBufCodecs.STRING_UTF8,
			DialogueSyncPayload::nodeText,
			DialogueOptionView.STREAM_CODEC.apply(ByteBufCodecs.list()),
			DialogueSyncPayload::options,
			DialogueSyncPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
