import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    private static final Pattern COMPILE = Pattern.compile(":[A-Za-z_0-9]+:");

    public static void main(String[] args) {
        String text = ":ban:";
        Matcher mat = COMPILE.matcher(text);
        mat.results().map(e -> {
            String s = e.group();
            s = s.substring(1);
            s = s.substring(0, s.length()-1);
            return s;
        }).forEach(e -> {
            System.out.println(e);
        });
    }
}

