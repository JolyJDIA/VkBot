package api.utils;

import jolyjdia.bot.generateText.neuralnetwork.datastructs.Matrix;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class MathUtils {

	@Contract(pure = true)
	private MathUtils() {}
	
	public static int pickIndexFromRandomVector(@NotNull Matrix probs) {
		double mass = 1.0;
		for (int i = 0; i < probs.w.length; i++) {
			double prob = probs.w[i] / mass;
			if (ObedientBot.RANDOM.nextDouble() < prob) {
				return i;
			}
			mass -= probs.w[i];
		}
		return 0;
	}
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
	
	public static double median(List<Double> vals) {
		Collections.sort(vals);
		int mid = vals.size() / 2;
		return vals.size() % 2 == 1 ? vals.get(mid) : (vals.get(mid - 1) + vals.get(mid)) / 2;
	}
}
