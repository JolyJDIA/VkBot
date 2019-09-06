import java.util.regex.Pattern;

public class Test {
    static final Pattern NUMBER = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
    public static void main(String[] args) {
        for(int i = 0; i < 5; ++i) {
            long start = System.nanoTime();
            System.out.println(NUMBER.matcher("-1.5").matches());
            long end = System.nanoTime() - start;
            System.out.println(end);//36400//36100
        }
    }
}
