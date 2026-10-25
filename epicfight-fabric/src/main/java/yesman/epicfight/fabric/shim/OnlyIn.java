package yesman.epicfight.fabric.shim;

import java.lang.annotation.*;

/** Penanda saja (sama seperti NeoForge modern, tidak memotong class). */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface OnlyIn {
    Dist value();
    Class<?> _interface() default Void.class;
}
