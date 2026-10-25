package yesman.epicfight.fabric.shim;

/** Pengganti yesman.epicfight.fabric.shim.Dist. */
public enum Dist {
    CLIENT, DEDICATED_SERVER;

    public boolean isClient() { return this == CLIENT; }
    public boolean isDedicatedServer() { return this == DEDICATED_SERVER; }
}
