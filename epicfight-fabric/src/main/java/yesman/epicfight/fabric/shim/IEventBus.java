package yesman.epicfight.fabric.shim;

import java.util.function.Consumer;

/** Pengganti net.neoforged.bus.api.IEventBus. */
public interface IEventBus {
    <E extends Event> void addListener(Consumer<E> listener);
    <E extends Event> void addListener(EventPriority priority, Consumer<E> listener);
    void register(Object target);
    <E extends Event> E post(E event);
}
