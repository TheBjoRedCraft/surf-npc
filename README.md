# 🏄 surf-npc

Ein leistungsstarkes Minecraft-Plugin zur einfachen Erstellung und Verwaltung von NPCs mit Surf-Funktionalität, ideal für Lobby-Server, Minigames oder RPG-Welten.

## 🌟 Features

- 🧍 Erstelle interaktive NPCs mit einem Befehl
- 🎯 Weise jedem NPC ein Ziel oder Kommando zu
- ⚙️ Vollständig konfigurierbar & permission-basiert
- 🧩 API für Entwickler zur Integration in eigene Plugins
---
## 💬 Commands

| Befehl                                                        | Beschreibung                   | Permission                                                                                                    |
|---------------------------------------------------------------|--------------------------------|---------------------------------------------------------------------------------------------------------------|
| `/npc <subcommand>`                                           | Basis für alle Subcommands     | `surf.npc.command`                                                                                            |
| `/npc create <name> <displayname> <skinOwner> <rotationType>` | Erstellt einen neuen NPC       | `surf.npc.command.create`                                                                                     |
| `/npc delete <npc>`                                           | Entfernt den angegebenen NPC   | `surf.npc.command.delete`                                                                                     |
| `/npc list [<page>]`                                          | Listet alle existierenden NPCs | `surf.npc.command.list`                                                                                       |
| `/npc edit <npc> <key> <value>`                               | Editiert einen NPC deiner Wahl | `surf.npc.command.edit`, `surf.npc.command.rotation`, `surf.npc.command.skin`, `surf.npc.command.displayname` |
| `/npc info <npc>`                                             | Informiert über NPC-Daten      | `surf.npc.command.info`                                                                                       |
| `/npc teleport <npc>`                                         | Teleportiert dich zu einem NPC | `surf.npc.command.teleport`                                                                                   |
| `/npc teleporthere <npc>`                                     | Teleportiert einen NPC zu dir  | `surf.npc.command.teleporthere`                                                                               |

---

## 🔐 Permissions

| Permission                          | Beschreibung                                       |
|-------------------------------------|----------------------------------------------------|
| `surf.npc.command`                  | Basisberechtigung für alle NPC-Kommandos           |
| `surf.npc.command.create`           | Erlaubt das Erstellen von NPCs                     |
| `surf.npc.command.delete`           | Erlaubt das Löschen von NPCs                       |
| `surf.npc.command.list`             | Erlaubt das Anzeigen aller NPCs                    |
| `surf.npc.command.info`             | Zeigt detaillierte Informationen über einen NPC    |
| `surf.npc.command.edit`             | Erlaubt das Bearbeiten von NPCs                    |
| `surf.npc.command.edit.rotation`    | Erlaubt das Anpassen der NPC-Rotation              |
| `surf.npc.command.edit.skin`        | Erlaubt das Ändern des NPC-Skins                   |
| `surf.npc.command.edit.displayname` | Erlaubt das Bearbeiten des Anzeigenamens           |
| `surf.npc.command.teleport`         | Erlaubt das Teleportieren **zum** NPC              |
| `surf.npc.command.teleporthere`     | Erlaubt das Teleportieren **des** NPCs zum Spieler |


---

## 🧪 API

### 🔧 Allgemeine Methoden

#### `createNpc(displayName, internalName, skin, location, global, rotationType, fixedRotation)`
Erstellt einen neuen NPC im Spiel.

| Parameter       | Typ                | Beschreibung                                 |
|-----------------|--------------------|----------------------------------------------|
| `displayName`   | `Component`        | Anzeigename des NPCs                         |
| `internalName`  | `String`           | Interner, unique Name                        |
| `skin`          | `SNpcSkinData`     | Skin-Daten des NPCs (Textur, Signatur usw.)  |
| `location`      | `SNpcLocation`     | Position, an der der NPC gespawnt wird       |
| `global`        | `Boolean`          | Ob der NPC global sichtbar ist               |
| `rotationType`  | `SNpcRotationType` | Art der Rotation (FIXED, PER_PLAYER)         |
| `fixedRotation` | `SNpcRotation?`    | Feste Rotation, falls `rotationType = FIXED` |

#### `deleteNpc(npc)`
Löscht den angegebenen NPC.

| Parameter | Typ     | Beschreibung             |
|-----------|---------|--------------------------|
| `npc`     | `SNpc`  | Der zu löschende NPC     |

#### `getNpc(id)`
Holt einen NPC anhand der internen ID.

| Parameter | Typ   | Beschreibung      |
|-----------|-------|-------------------|
| `id`      | `Int` | Interne NPC-ID    |

#### `getNpc(internalName)`
Holt einen NPC anhand des internen Namens.

| Parameter      | Typ     | Beschreibung                |
|----------------|---------|-----------------------------|
| `internalName` | `String`| Der interne Name des NPCs   |

#### `getNpcs()`
Gibt eine Liste aller NPCs zurück.

#### `despawnAllNpcs()`
Entfernt alle NPCs temporär aus der Welt.

---

### 👁️ Sichtbarkeit

#### `showNpc(npc, uuid)`
Zeigt einen NPC einem bestimmten Spieler.

| Parameter | Typ     | Beschreibung                     |
|-----------|---------|----------------------------------|
| `npc`     | `SNpc`  | Der anzuzeigende NPC             |
| `uuid`    | `UUID`  | UUID des Spielers                |

#### `hideNpc(npc, uuid)`
Versteckt den NPC vor einem Spieler.

| Parameter | Typ     | Beschreibung                    |
|-----------|---------|---------------------------------|
| `npc`     | `SNpc`  | Der zu versteckende NPC         |
| `uuid`    | `UUID`  | UUID des Spielers               |

---

### 🧍 NPC Eigenschaften

#### `setSkin(npc, skin)`
Ändert den Skin eines NPCs.

| Parameter | Typ            | Beschreibung                     |
|-----------|----------------|----------------------------------|
| `npc`     | `SNpc`         | Ziel-NPC                         |
| `skin`    | `SNpcSkinData` | Neue Skin-Daten                  |

#### `setRotationType(npc, rotationType)`
Setzt den Rotationstyp des NPCs.

| Parameter      | Typ                | Beschreibung      |
|----------------|--------------------|-------------------|
| `npc`          | `SNpc`             | Ziel-NPC          |
| `rotationType` | `SNpcRotationType` | PER_PLAYER, FIXED |

#### `setRotation(npc, rotation)`
Setzt eine feste Rotation (nur wenn Typ `FIXED`).

| Parameter  | Typ            | Beschreibung             |
|------------|----------------|--------------------------|
| `npc`      | `SNpc`         | Ziel-NPC                 |
| `rotation` | `SNpcRotation` | Rotation mit Yaw & Pitch |

---

### 🏷️ Properties

#### `getProperties(npc)`
Holt alle Properties eines NPCs.

| Parameter | Typ    | Beschreibung        |
|-----------|--------|---------------------|
| `npc`     | `SNpc` | Ziel-NPC            |

#### `addProperty(npc, property)`
Fügt eine Property zum NPC hinzu.

| Parameter  | Typ            | Beschreibung            |
|------------|----------------|-------------------------|
| `npc`      | `SNpc`         | Ziel-NPC                |
| `property` | `SNpcProperty` | Hinzuzufügende Property |

#### `removeProperty(npc, property)`
Entfernt eine Property vom NPC.

| Parameter  | Typ            | Beschreibung            |
|------------|----------------|-------------------------|
| `npc`      | `SNpc`         | Ziel-NPC                |
| `property` | `SNpcProperty` | Zu entfernende Property |

#### `createProperty(key, value, type)`
Erstellt eine neue Property.

| Parameter | Typ                | Beschreibung                              |
|-----------|--------------------|-------------------------------------------|
| `key`     | `String`           | Name/Schlüssel der Property               |
| `value`   | `String`           | Wert der Property                         |
| `type`    | `SNpcPropertyType` | Typ (STRING, BOOLEAN, BYTE, DOUBLE, etc.) |

---

### 🛠️ Hilfsfunktionen

#### `createRotation(yaw, pitch)`
Erstellt eine neue Rotation.

| Parameter | Typ     | Beschreibung                      |
|-----------|---------|-----------------------------------|
| `yaw`     | `Float` | Horizontale Blickrichtung         |
| `pitch`   | `Float` | Vertikale Blickrichtung           |

#### `createSkinData(owner, value, signature)`
Erstellt Skin-Daten eines NPCs.

| Parameter   | Typ      | Beschreibung                |
|-------------|----------|-----------------------------|
| `owner`     | `String` | Spielername oder Identifier |
| `value`     | `String` | Skin-Textur-Wert            |
| `signature` | `String` | Signatur zum Skin-Wert      |

#### `createLocation(x, y, z, worldName)`
Erstellt eine Position.

| Parameter   | Typ      | Beschreibung                |
|-------------|----------|-----------------------------|
| `x`         | `Double` | X-Koordinate                |
| `y`         | `Double` | Y-Koordinate                |
| `z`         | `Double` | Z-Koordinate                |
| `worldName` | `String` | Name der Welt (z.B.`world`) |

