package com.clokkwork.clokkworknpc.data;

/**
 * Path segments scanned by {@link com.clokkwork.clokkworknpc.data.load.JsonDefinitionReloadListener}
 * under {@code data/<namespace>/}. Example: {@code factions} matches
 * {@code data/clokkworknpc/factions/*.json}.
 */
public final class DefinitionPaths {

	public static final String NPCS = "npcs";
	public static final String DIALOGUE = "dialogue";
	public static final String FACTIONS = "factions";

	private DefinitionPaths() {
	}
}
