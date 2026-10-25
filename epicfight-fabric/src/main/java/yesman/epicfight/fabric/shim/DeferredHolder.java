package yesman.epicfight.fabric.shim;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/** Pengganti yesman.epicfight.fabric.shim.DeferredHolder (isi diikat setelah registrasi). */
public class DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T> {
    protected final ResourceKey<R> key;
    private Holder<R> holder;

    protected DeferredHolder(ResourceKey<R> key) {
        this.key = key;
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<R> key) {
        return new DeferredHolder<>(key);
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends Registry<R>> registryKey, ResourceLocation id) {
        return new DeferredHolder<>(ResourceKey.create(registryKey, id));
    }

    public ResourceKey<R> getKey() { return key; }
    public ResourceLocation getId() { return key.location(); }

    /** Dipanggil DeferredRegister setelah entri benar-benar masuk registry. */
    void bindHolder(Holder<R> h) { this.holder = h; }

    @SuppressWarnings("unchecked")
    public void bind(boolean throwOnMissing) {
        if (holder != null) return;
        Registry<R> registry = (Registry<R>) BuiltInRegistries.REGISTRY.get(key.registry());
        if (registry != null) {
            Optional<Holder.Reference<R>> found = registry.getHolder(key);
            if (found.isPresent()) { holder = found.get(); return; }
        }
        if (throwOnMissing) throw new NullPointerException("Belum terdaftar: " + key);
    }

    @Override @SuppressWarnings("unchecked")
    public T get() {
        bind(true);
        return (T) holder.value();
    }

    @Override public R value() { return get(); }
    @Override public boolean isBound() { bind(false); return holder != null && holder.isBound(); }
    @Override public boolean is(ResourceLocation id) { return key.location().equals(id); }
    @Override public boolean is(ResourceKey<R> k) { return key == k || key.equals(k); }
    @Override public boolean is(Predicate<ResourceKey<R>> p) { return p.test(key); }
    @Override public boolean is(TagKey<R> tag) { bind(false); return holder != null && holder.is(tag); }
    @Override public boolean is(Holder<R> other) { return other.is(key); }
    @Override public Stream<TagKey<R>> tags() { bind(false); return holder == null ? Stream.empty() : holder.tags(); }
    @Override public Either<ResourceKey<R>, R> unwrap() { return Either.left(key); }
    @Override public Optional<ResourceKey<R>> unwrapKey() { return Optional.of(key); }
    @Override public Kind kind() { return Kind.REFERENCE; }
    @Override public boolean canSerializeIn(HolderOwner<R> owner) { bind(false); return holder == null || holder.canSerializeIn(owner); }

    @Override public int hashCode() { return key.hashCode(); }
    @Override public boolean equals(Object o) { return o instanceof DeferredHolder<?, ?> d && d.key.equals(key); }
    @Override public String toString() { return "DeferredHolder{" + key + "}"; }
}
