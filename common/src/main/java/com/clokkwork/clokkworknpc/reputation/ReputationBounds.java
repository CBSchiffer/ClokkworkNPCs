package com.clokkwork.clokkworknpc.reputation;

public final class ReputationBounds {

	public static final int MIN = -100;
	public static final int MAX = 100;
	public static final int DEFAULT = 0;

	private ReputationBounds() {
	}

	public static int clamp(int value) {
		return Math.clamp(value, MIN, MAX);
	}
}
