package com.clokkwork.clokkworknpc.network.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DialogueOptionView(int index, String label) {

	public static final StreamCodec<ByteBuf, DialogueOptionView> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			DialogueOptionView::index,
			ByteBufCodecs.STRING_UTF8,
			DialogueOptionView::label,
			DialogueOptionView::new
	);
}
