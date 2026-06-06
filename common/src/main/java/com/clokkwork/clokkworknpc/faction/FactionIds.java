package com.clokkwork.clokkworknpc.faction;

import com.clokkwork.clokkworknpc.Constants;
import com.clokkwork.clokkworknpc.registry.FactionRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public final class FactionIds {

	private static final int MAX_PATH_LENGTH = 64;

	private FactionIds() {
	}

	public static ResourceLocation worldFactionIdFromTitle(String title, FactionRegistry registry) {
		String basePath = slugify(title);
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, basePath);
		if (!registry.contains(id)) {
			return id;
		}

		int suffix = 2;
		ResourceLocation candidate;
		do {
			candidate = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, truncatePath(basePath + "_" + suffix));
			suffix++;
		} while (registry.contains(candidate));

		return candidate;
	}

	private static String slugify(String title) {
		String slug = title.toLowerCase(Locale.ROOT)
				.replaceAll("[^a-z0-9]+", "_")
				.replaceAll("^_+|_+$", "");
		if (slug.isEmpty()) {
			slug = "faction";
		}
		return truncatePath(slug);
	}

	private static String truncatePath(String path) {
		if (path.length() <= MAX_PATH_LENGTH) {
			return path;
		}
		return path.substring(0, MAX_PATH_LENGTH);
	}
}
