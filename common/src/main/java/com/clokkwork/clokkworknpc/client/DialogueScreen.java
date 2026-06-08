package com.clokkwork.clokkworknpc.client;

import com.clokkwork.clokkworknpc.network.payload.DialogueOptionView;
import com.clokkwork.clokkworknpc.network.payload.DialogueSyncPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class DialogueScreen extends Screen {

	private final DialogueSyncPayload payload;
	private boolean closingFromServer;

	protected DialogueScreen(DialogueSyncPayload payload) {
		super(Component.literal(payload.npcDisplayName()));
		this.payload = payload;
	}

	@Override
	protected void init() {
		super.init();
		List<DialogueOptionView> options = payload.options();
		int buttonWidth = Math.min(220, this.width - 40);
		int buttonX = (this.width - buttonWidth) / 2;
		int buttonY = this.height - 28 - (options.size() * 24);

		for (DialogueOptionView option : options) {
			int index = option.index();
			this.addRenderableWidget(Button.builder(Component.literal(option.label()), button -> {
				DialogueClientHandlers.sendChooseOption(index);
			}).bounds(buttonX, buttonY, buttonWidth, 20).build());
			buttonY += 24;
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(graphics, mouseX, mouseY, partialTick);
		graphics.fill(0, 0, this.width, this.height, 0x66000000);
		int panelWidth = Math.min(320, this.width - 40);
		int panelHeight = Math.min(180, this.height - 80);
		int panelX = (this.width - panelWidth) / 2;
		int panelY = 40;
		graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xAA000000);
		graphics.drawCenteredString(this.font, this.payload.npcDisplayName(), this.width / 2, panelY + 8, 0xFFFFFF);
		graphics.drawWordWrap(this.font, Component.literal(this.payload.nodeText()), panelX + 12, panelY + 28, panelWidth - 24, 0xE0E0E0);
		super.render(graphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void onClose() {
		if (!closingFromServer) {
			DialogueClientHandlers.sendCancelDialogue();
		}
		super.onClose();
	}

	public void closeFromServer() {
		closingFromServer = true;
		if (this.minecraft != null) {
			this.minecraft.setScreen(null);
		}
	}

	public void updateFromServer(DialogueSyncPayload updatedPayload) {
		if (this.minecraft != null) {
			this.minecraft.setScreen(new DialogueScreen(updatedPayload));
		}
	}

	@Override
	protected void renderBlurredBackground(float partialTick) {
		// Do nothing.
		// Vanilla calls this to apply the menu/world blur behind screens.
		// Leaving this empty prevents the blur from being applied.
	}
}
