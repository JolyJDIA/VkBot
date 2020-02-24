package jolyjdia.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandLabel {
    String name();
    String[] alias();
    String usage() default "";
    String desc() default "";
    int min() default 0;
    int max() default 0;
    String permission() default "";
    String noPermissionMsg() default "У Вас нет прав!";
}
