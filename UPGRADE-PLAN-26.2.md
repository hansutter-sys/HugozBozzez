# HugoBOSS → NeoForge 26.2.1.56 Uppgraderingsplan

> Uppgradering från **NeoForge 21.1.230** (MC 1.21.1) → **NeoForge 26.2.1.56** (MC 26.2)

---

## Översikt

| Nuvarande | Mål |
|---|---|
| Minecraft 1.21.1 | Minecraft 26.2 |
| NeoForge 21.1.230 | NeoForge 26.2.1.56 |
| Java 21 | Java 25 |
| ModDevGradle 2.0.141 | ModDevGradle 2.0.147+ |
| Gradle 9.2.1 | Gradle 9.2.1 |

---

## Skillnader mot 26.3

1. **Config-system**: I 26.2 används fortfarande `ModConfig.Type.COMMON` (`LOCAL` infördes först i 26.3.0.37+).
2. **Stabilitet**: 26.2 har stabilare grundfunktionalitet jämfört med betaversionerna av 26.3.
3. **Branch**: Använder branch `mc-26.2`.

---

## Genomförda ändringar per fas

### Fas 0 — Branch & Java
- [x] Branch döpt till `mc-26.2`
- [x] Java toolchain satt till Java 25 i `build.gradle`

### Fas 1 — Build-system (`gradle.properties` & `build.gradle`)
- [x] `minecraft_version` = `26.2`
- [x] `minecraft_version_range` = `[26.2]`
- [x] `neo_version` = `26.2.1.56`
- [x] `parchment_minecraft_version` = `26.2`
- [x] `parchment_mappings_version` = `2026.08.20`
- [x] `mod_version` = `3.0.0`
- [x] ModDevGradle = `2.0.147`

### Fas 2 — Config-system
- [x] `HugoBOSS.java`: Registrering hålls som `ModConfig.Type.COMMON`

### Fas 3 — Item API
- [x] `ModItems.java`: Ersatt `DeferredSpawnEggItem` med `SpawnEggItem` för alla 6 spawn eggs
- [x] `HugoBOSS.java`: Uppdaterat `FoodProperties` (tagit bort deprecated `.alwaysEdible()`)
- [x] `TntStaffItem.java`: `use()` returnerar `InteractionResult` istället för `InteractionResultHolder<ItemStack>`

### Fas 4 — Entity-system
- [x] `ModEntities.java`: `EntityType.Builder.build()` anropar `ResourceKey.create(Registries.ENTITY_TYPE, ...)` för alla 6 entiteter

### Fas 5 — Registreringssystem
- [x] `DeferredRegister`, `DeferredBlock`, `DeferredItem`, `DeferredHolder` verifierade
- [x] BiomeModifier serializers och JSON-filer verifierade

### Fas 6 — Klientkod
- [x] Renderer- och Layer-registreringar verifierade
- [x] `HugoBOSSClient` och modeller/renderers anpassade och verifierade
