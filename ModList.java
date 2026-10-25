package yesman.epicfight.fabric.shim;

import net.fabricmc.loader.api.FabricLoader;

/** Pengganti sebagian net.neoforged.fml.ModList. */
public final class ModList {
    private static final ModList INSTANCE = new ModList();
    private ModList() {}
    public static ModList get() { return INSTANCE; }
    public boolean isLoaded(String id) { return FabricLoader.getInstance().isModLoaded(id); }
}
