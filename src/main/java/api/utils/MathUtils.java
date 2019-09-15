package api.utils;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public final class MathUtils {
	public static final Random RANDOM = new Random();

	@Contract(pure = true)
	private MathUtils() {}

	public static int random(String min, String max) {
		int maxI = 100;
		int minI = 1;
		try {
			maxI = Integer.parseInt(max);
			minI = Integer.parseInt(min);
		} catch (NumberFormatException ignored) {}
		return (int) (Math.random() * (maxI - minI) + 1) + minI;
	}
	public static int random(int min, int max) {
		return (int) (Math.random() * (max - min) + 1) + min;
	}
}
