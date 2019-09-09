package api.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public final class LookupUtil {
    public static MethodHandles.Lookup ALL_LOOKUP;
    private static MethodHandle LOOKUP_CONSTRUCTOR;
    private static MethodHandle METHODS_GETTER;

    static {
        try {
       /*     Field allPermsLookup = Arrays.stream(MethodHandles.Lookup.class.getDeclaredFields())
                    .filter(e -> e.getType().equals(MethodHandles.Lookup.class)
                            && e.getName().toLowerCase(Locale.US).contains("lookup")
                            && e.getName().toLowerCase(Locale.US).contains("impl")
                            && !e.getName().toLowerCase(Locale.US).contains("public"))
                    .findFirst().get();
            allPermsLookup.setAccessible(true);*/
     //       ALL_LOOKUP = (MethodHandles.Lookup) allPermsLookup.get(null);
            ALL_LOOKUP = MethodHandles.lookup();
            LOOKUP_CONSTRUCTOR = ALL_LOOKUP
                    .findVirtual(MethodHandles.Lookup.class, "in", MethodType.methodType(MethodHandles.Lookup.class, Class.class))
                    .bindTo(ALL_LOOKUP);
            METHODS_GETTER = ALL_LOOKUP.findSpecial(LookupUtil.class, "getDeclaredMethods",
                    MethodType.methodType(Method[].class), LookupUtil.class);
        } catch (IllegalAccessException | SecurityException | NoSuchMethodException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    private LookupUtil() {}

    public static Method[] getDeclaredMethods(Class<?> clazz) {
        try {
            return (Method[]) METHODS_GETTER.invoke(clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public static MethodHandles.Lookup in(Class<?> clazz) {
        try {
            return (MethodHandles.Lookup) LOOKUP_CONSTRUCTOR.invokeExact(clazz);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }
}
