package yesman.epicfight.fabric.shim;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Pengganti yesman.epicfight.fabric.shim.DeferredRegister.
 * Di Fabric registrasi dilakukan langsung saat {@link #register(IEventBus)} dipanggil
 * (urutan pemanggilan = urutan registrasi, jadi urutan daftar di EpicFightRegistries penting).
 */
public class DeferredRegister<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String namespace;
    private final Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
    private boolean applied = false;

    protected DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return new DeferredRegister<>(key, namespace);
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
        return new DeferredRegister<>(registry.key(), namespace);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> sup) {
        return register(name, (Function<ResourceLocation, ? extends I>) id -> sup.get());
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
        if (applied) throw new IllegalStateException("Registrasi sudah diterapkan: " + registryKey.location());
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        DeferredHolder<T, I> holder = createHolder(ResourceKey.create(registryKey, id));
        if (entries.putIfAbsent(holder, () -> func.apply(id)) != null) {
            throw new IllegalArgumentException("Nama ganda: " + name);
        }
        return holder;
    }

    protected <I extends T> DeferredHolder<T, I> createHolder(ResourceKey<T> key) {
        return DeferredHolder.create(key);
    }

    /** Panggil setelah registry tujuan ada. Argumen bus diabaikan (hanya demi kompatibilitas sumber). */
    public void register(IEventBus ignoredBus) { applyRegistration(); }

    @SuppressWarnings("unchecked")
    public void applyRegistration() {
        if (applied) return;
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null) throw new IllegalStateException("Registry belum dibuat: " + registryKey.location());
        applied = true;
        for (Map.Entry<DeferredHolder<T, ? extends T>, Supplier<? extends T>> e : entries.entrySet()) {
            Holder.Reference<T> ref = Registry.registerForHolder(registry, e.getKey().getKey(), e.getValue().get());
            e.getKey().bindHolder(ref);
        }
    }

    public Collection<DeferredHolder<T, ? extends T>> getEntries() { return Collections.unmodifiableCollection(entries.keySet()); }
    public ResourceKey<? extends Registry<T>> getRegistryKey() { return registryKey; }
    public String getNamespace() { return namespace; }
}
