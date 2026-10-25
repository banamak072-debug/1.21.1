package yesman.epicfight.fabric.shim;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

/**
 * Event bus sederhana berbasis refleksi. Fabric tidak punya bus seperti ini, jadi event NeoForge
 * harus di-post manual dari callback Fabric / Mixin (lihat STATUS_PORT.md).
 */
public class EventBus implements IEventBus {
    public static final EventBus MOD = new EventBus();   // setara mod event bus
    public static final EventBus GAME = new EventBus();  // setara NeoForge.EVENT_BUS

    private record Listener(EventPriority priority, Class<?> type, Consumer<Object> fn) {}
    private final List<Listener> listeners = new ArrayList<>();

    @Override @SuppressWarnings("unchecked")
    public <E extends Event> void addListener(Consumer<E> l) { addListener(EventPriority.NORMAL, l); }

    @Override @SuppressWarnings("unchecked")
    public <E extends Event> void addListener(EventPriority p, Consumer<E> l) {
        listeners.add(new Listener(p, Event.class, (Consumer<Object>) (Consumer<?>) l));
        sort();
    }

    /** Terima instance (method non-static) atau Class (method static). */
    @Override
    public void register(Object target) {
        Class<?> cls = target instanceof Class<?> c ? c : target.getClass();
        boolean statics = target instanceof Class<?>;
        for (Method m : cls.getDeclaredMethods()) {
            SubscribeEvent a = m.getAnnotation(SubscribeEvent.class);
            if (a == null || m.getParameterCount() != 1) continue;
            if (Modifier.isStatic(m.getModifiers()) != statics) continue;
            m.setAccessible(true);
            Class<?> type = m.getParameterTypes()[0];
            listeners.add(new Listener(a.priority(), type, ev -> {
                try { m.invoke(statics ? null : target, ev); }
                catch (Exception e) { throw new RuntimeException("Gagal memanggil listener " + m, e); }
            }));
        }
        sort();
    }

    @Override
    public <E extends Event> E post(E event) {
        for (Listener l : new ArrayList<>(listeners)) {
            if (l.type == Event.class || l.type.isInstance(event)) l.fn.accept(event);
        }
        return event;
    }

    private void sort() { listeners.sort(Comparator.comparingInt(l -> l.priority.ordinal())); }
}
