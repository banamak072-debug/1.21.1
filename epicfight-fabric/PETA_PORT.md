# Peta port Epic Fight: NeoForge -> Fabric 1.21.1

Sumber acuan: github.com/Epic-Fight/epicfight (branch 1.21.1), GPL-3.0-or-later.
Mapping: Mojang official (sama dengan Epic Fight), jadi nama class vanilla tidak berubah.

## Padanan API
| NeoForge (dipakai Epic Fight) | Fabric |
|---|---|
| `@Mod` + konstruktor | `ModInitializer` / `ClientModInitializer` |
| `DeferredRegister` | `Registry.register(...)` langsung di init |
| Capability (`PlayerPatch`, `LivingEntityPatch`, `ItemCapability`) | Data Attachment API (`fabric-data-attachment-api-v1`), atau komponen yang ditempel ke entity/item |
| `@SubscribeEvent` + event bus | Callback Fabric API (`ServerLivingEntityEvents`, `AttackEntityCallback`, `ServerTickEvents`, dst.) |
| Event custom NeoForge (`api/neoevent`) | Event buatan sendiri via `EventFactory` |
| Payload / `RegisterPayloadHandlersEvent` | `PayloadTypeRegistry` + `ServerPlayNetworking` / `ClientPlayNetworking` |
| `ModConfigSpec` | Config sendiri (JSON/Gson) atau lib seperti Cloth Config |
| `AddReloadListenerEvent` | `ResourceManagerHelper` + `SimpleSynchronousResourceReloadListener` |
| `RegisterClientReloadListenersEvent` | `ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)` |
| `IClientItemExtensions` | Mixin atau `BuiltinItemRendererRegistry` |
| Access Transformer (`accesstransformer.cfg`) | Access Widener (`.accesswidener`) |
| `enum_extensions.json` | Mixin/ASM atau ganti enum jadi registry biasa |
| `epicfight-platform.neoforge.mixins.json` | Dibuang, diganti mixin khusus Fabric |
| `@WrapOperation` (MixinExtras) | Sama, MixinExtras sudah ikut Fabric Loader |

## Tahap
1. [x] Kerangka proyek + build via GitHub Actions
2. [ ] Registry (item, entity, sound, particle) + attachment pengganti capability
3. [ ] Jaringan (33 paket server, 11 klien)
4. [ ] Armature, mesh, loader animasi JSON
5. [ ] Renderer pemain + layer
6. [ ] Mode Epic Fight, hitbox, combo, dodge, guard, stun
7. [ ] Senjata/gaya bertarung (data JSON)
8. [ ] Skill + mob patch + AI
9. [ ] GUI + config
