# 👑 HugoBOSS Mod – Komplett Guide (Minecraft 1.21.1 / NeoForge)

**HugoBOSS** är en kraftfull och fartfylld Minecraft-mod för **NeoForge (Minecraft 1.21.1)** som lägger till unika bossar, gigantiska varelser, ridbara flygdjur och magiska vapen i din värld.

---

## 🐲 Mobs, Bossar & Vad de gör

### 🦅 1. Giant Eagle (Jätteörn)
- **Hälsa (HP)**: `50` (25 hjärtan) | **Skada**: `7`
- **Storlek**: `2.8x` (Tillräckligt stor för att rida på)
- **Beteende**:
  - Flyger högt i luften i berg och spejar efter mat.
  - Dyker ner mot marken/vattnet för att anfalla och äta **kaniner** 🐇, **hönor** 🐔 och **fiskar** 🐟. Landar på marken när den äter eller vilar.
- **Tämjning**:
  - Smyg fram när örnen står på marken och högerklicka med **Rå kanin**, **Rå kyckling**, **Lax** eller **Torsk** (33% chans per matbit).
- **Ridning & Flyg**:
  - Högerklicka med en **Sadel (`Saddle`)** på den tämjda örnen.
  - Sitt upp på ryggen och tryck på **Spacebar** för att stiga till skyn! Styr fritt i luften med **WASD** och musen.
- **Spawn-plats**: Naturligt i berg-biomer (`#c:is_mountain`).

---

### 💥 2. Mega Creeper
- **Hälsa (HP)**: `200` (100 hjärtan)
- **Storlek**: `3.5x` (Gigantisk Creeper)
- **Beteende**:
  - Kastar antända TNT-block mot spelaren på avstånd och orsakar en enorm explosion om den kommer nära.
- **Special-drop**: Droppar **Creeper Heart** när du besegrar den.
- **Spawn-plats**: **Endast i Öken-biomer** (`#c:is_desert`) med låg chanse (weight 2).

---

### 🌊 3. The Great Sea Serpent (Sjöorm-boss)
- **Hälsa (HP)**: `150` (75 hjärtan) | **Skada**: `8`
- **Beteende**:
  - Stor havsboss med lila **BossBar**.
  - **Swallow Attack**: Sväljer spelaren hel! Du drabbas av mörker (`Blindness` & `Darkness`) och mister luft inuti ormens mage.
- **💡 Hur du rymmer (Viktigt!)**:
  - Om du är ensam måste du hugga dig ut inifrån genom att göra 40 skadepoäng.
  - **REKOMMENDERAS**: Ha hjälp från en **medspelare på utsidan**! Om någon skadar ormen utifrån tvingas den att omedelbart spotta ut alla svalda spelare.
- **Spawn-plats**: Naturligt i djupt hav (`is_deep_ocean`).

---

### 🐍 4. Minor Serpent
- **Hälsa (HP)**: `30` (15 hjärtan) | **Skada**: `6`
- **Beteende**: Snabb och aggressiv vattenorm som anfaller spelare som simmar i havet.
- **Spawn-plats**: Naturligt i alla hav (`is_ocean`).

---

### 🦍 5. King Gorilla
- **Hälsa (HP)**: `100` (50 hjärtan) | **Skada**: `6`
- **Storlek**: `4.0x` (Gigantisk King Kong)
- **Beteende**:
  - Farlig djungelboss med lila **BossBar**.
  - Klättrar vertikalt på träd, gör kraftfulla **träd-hopp (Tree Leap)** ner mot spelaren och frammanar mindre apor (`Monkeys`) under strid.
- **Spawn-plats**: Naturligt i djungel-biomer (`is_jungle`).

---

### 🐒 6. Agile Monkey (Smidig Apa)
- **Hälsa (HP)**: `15` (7.5 hjärtan) | **Skada**: `2`
- **Beteende**: Snabb och rörlig apa i djungeln. Kan även frammanas av King Gorilla.
- **Spawn-plats**: Naturligt i djungel-biomer (`is_jungle`).

---

## 🥚 Hur du skapar / skaffar Spawn-ägg

### 1. Mega Creeper Spawn Egg (Crafting-recept)
Kan tillverkas i ett **Crafting Table**:
- **Ingredienser**:
  - 1x **Creeper Head** (`minecraft:creeper_head`)
  - 2x **TNT** (`minecraft:tnt`)
  - 2x **Sköldpaddsägg** (`minecraft:turtle_egg`)
- **Mönster**:
  ```text
  [   ] [ TNT ] [   ]
  [ÄGG] [HUVUD] [ÄGG]
  [   ] [ TNT ] [   ]
  ```

### 2. Övriga Spawn-ägg
Spawn-äggen för *Giant Eagle*, *Gorilla*, *Sea Serpent*, *Minor Serpent* och *Monkey* finns i **Creative Mode** under mod-fliken, eller kan ges via kommandon:
- `/give @s hugoboss:giant_eagle_spawn_egg`
- `/give @s hugoboss:gorilla_spawn_egg`
- `/give @s hugoboss:sea_serpent_spawn_egg`
- `/give @s hugoboss:minor_serpent_spawn_egg`
- `/give @s hugoboss:monkey_spawn_egg`

---

## 🧨 Specialvapen (Items)

- 🧨 **TNT Staff (`tnt_staff`)**: Tillverkas i Crafting Table med **Blaze Rod** och **TNT**. Högerklicka för att skuta iväg brinnande TNT-block i den riktning du tittar!
- 💚 **Creeper Heart (`creeper_heart`)**: Ett sällsynt hjärta som droppas från Mega Creeper.

---

## 📜 Releases & Downloads
Alla färdiga JAR-filer finns publicerade på GitHub under [GitHub Releases](https://github.com/hansutter-sys/HugozBozzez/releases).
