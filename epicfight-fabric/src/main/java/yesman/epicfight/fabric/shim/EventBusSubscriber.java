package yesman.epicfight.fabric.shim;

import java.lang.annotation.*;

/** Didaftarkan manual lewat EventBus#register(Class) di EpicFightFabric (tidak ada auto-scan di Fabric). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber {
    String modid() default "";
    Dist[] value() default {Dist.CLIENT, Dist.DEDICATED_SERVER};
}
