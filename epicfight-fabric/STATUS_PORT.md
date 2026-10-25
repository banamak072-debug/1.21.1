# Status port Epic Fight 1.21.1 -> Fabric

Sumber: github.com/Epic-Fight/epicfight (branch 1.21.1), kode GPL-3.0. Aset: All Rights Reserved (lihat LICENSE-ASSETS).

## Sudah
- 877 file Java asli disalin utuh (938 asli, dikurangi 52 file kompat mod pihak ketiga + 9 file platform NeoForge).
- 782 file murni vanilla (animasi, model, skill logic, collider, math) tidak butuh perubahan.
- Lapisan shim `yesman.epicfight.fabric.shim`: Dist, OnlyIn, FMLEnvironment, DeferredRegister, DeferredHolder.
  Import di 77 file sudah dialihkan ke shim. File NeoForge turun dari 156 menjadi 79 (daftar: SISA_FILE_NEOFORGE.txt).

## Belum (urutan pengerjaan, combat dulu)
1. `main/EpicFightMod.java` -> entrypoint Fabric (ModInitializer + ClientModInitializer), pakai `platform/ModPlatform`.
2. `registry/EpicFightRegistries` -> `FabricRegistryBuilder` (RegistryBuilder + callback Add/Bake/Clear).
3. Capability -> Attachment: `EpicFightCapabilities`, `GlobalCapability`, `AttachmentEntityPatchProvider`,
   `CommonItemCapabilityProvider`, `EpicFightAttachmentTypes`.
4. Event combat: `VanillaEntityEventHooks`, `platform/neoforge/event/*` (dulu dihapus; perlu versi Fabric:
   serang/terima damage, jump, tick, equipment change, join level) lewat callback + Mixin.
5. Jaringan: `EpicFightNetworkManager` + 2 payload handler -> PayloadTypeRegistry / ServerPlayNetworking / ClientPlayNetworking.
6. `EntityAttributeModificationEvent` (15 file patch mob) -> Mixin ke DefaultAttributes.
7. Input client: `EpicFightKeyMappings`, `CombatKeyMapping`, `InputUtils` -> KeyBindingHelper + mixin.
8. Config (ModConfigSpec), loot modifier, datagen, GUI, reload listener.
9. Mixin: `epicfight.mixins.json` asli (38 mixin) perlu disalin dan dipecah common/client; buang yang khusus NeoForge.

## Catatan jujur
- Belum pernah dikompilasi (sandbox tidak punya JDK/Gradle/jaringan). Build pertama di GitHub Actions hampir pasti
  menghasilkan banyak error yang harus dibereskan bertahap.
