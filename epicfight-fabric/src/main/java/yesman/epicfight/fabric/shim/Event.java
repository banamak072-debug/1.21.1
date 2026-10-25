package yesman.epicfight.fabric.shim;

/** Pengganti net.neoforged.bus.api.Event. */
public abstract class Event {
    private boolean canceled;
    public boolean isCanceled() { return canceled; }
    public void setCanceled(boolean c) { this.canceled = c; }
}
