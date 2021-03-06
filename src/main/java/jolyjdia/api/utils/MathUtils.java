package jolyjdia.api.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public final class MathUtils {
	public static final Random RANDOM = new Random();

	private MathUtils() {}

	public static int random(int min, int max) {
		return min == max ? min : min + RANDOM.nextInt((max - min)+1);
	}
	public static int random(int max) {
		return random(1, max);
	}

	public static double swap(int a, int b) {
		a ^= b;
		b = a ^ b;
		return a ^ b;
	}
	public static double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}

	@NotNull
	@Contract(pure = true)
	public static  double[] calculateDelta(@NotNull double[] doubleList) {
		if (doubleList.length <= 1) {
			throw new IllegalArgumentException("The list must contain 2 or more items to calculate the Delta");
		}
		int i1 = 0;
		double[] d = new double[doubleList.length-1];
		for (int i = 1; i < doubleList.length; ++i) {
			d[i1] = doubleList[i] - doubleList[i - 1];
			++i1;
		}
		return d;
	}

	public static double mean(@NotNull double[] angles) {
		double sum = 0;
		for (double angle : angles) {
			sum += angle;
		}
		return sum / angles.length;
	}
	public static double median(List<Double> vals) {
		Collections.sort(vals);
		int mid = vals.size()/2;
		return vals.size() % 2 == 1 ? vals.get(mid) : (vals.get(mid - 1) + vals.get(mid)) / 2;
	}

	public static @NotNull List<Double> toList(double[] doubleArray) {
		return Arrays.asList(ArrayUtils.toObject(doubleArray));
	}

	public static double[] toArray(@NotNull List<Double> doubleList) {
		return doubleList.stream().mapToDouble(e -> e).toArray();
	}
	public static @NotNull double[] randomArray(int length) {
		double[] randomArray = new double[length];
		applyFunc(randomArray, e -> ThreadLocalRandom.current().nextDouble());
		return randomArray;
	}

	public static void applyFunc(@NotNull double[] doubleArray, Function<? super Double, Double> func) {
		for (int i = 0; i < doubleArray.length; ++i) {
			doubleArray[i] = func.apply(doubleArray[i]);
		}
	}
	public static double[] multiply(@NotNull double[] vector, double factor) {
		double[] output = vector.clone();
		applyFunc(output, e -> e * factor);
		return output;
	}
}
