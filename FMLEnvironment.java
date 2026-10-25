package yesman.epicfight.fabric.shim;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

/** Pengganti yesman.epicfight.fabric.shim.FMLEnvironment. */
public final class FMLEnvironment {
    private FMLEnvironment() {}
    public static final Dist dist = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Dist.CLIENT : Dist.DEDICATED_SERVER;
    public static final boolean production = !FabricLoader.getInstance().isDevelopmentEnvironment();
}
