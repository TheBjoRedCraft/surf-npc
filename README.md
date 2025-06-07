
# 🏄 surf-npc

A powerful Minecraft plugin for easily creating and managing NPCs with surf functionality – perfect for lobby servers, minigames, or RPG worlds.

## 🌟 Features

- 🧍 Create interactive NPCs with a single command
- 🎯 Assign each NPC a target or command
- ⚙️ Fully configurable & permission-based
- 🧩 API for developers to integrate into custom plugins

---

## 💬 Commands

| Command                                                    | Description                      | Permission                                                                                                    |
|------------------------------------------------------------|----------------------------------|---------------------------------------------------------------------------------------------------------------|
| `/npc <subcommand>`                                        | Base command for all subcommands | `surf.npc.command`                                                                                            |
| `/npc create <name> <displayname> <skinOwner> <rotationType>` | Creates a new NPC             | `surf.npc.command.create`                                                                                     |
| `/npc delete <npc>`                                        | Deletes the specified NPC        | `surf.npc.command.delete`                                                                                     |
| `/npc list [<page>]`                                       | Lists all existing NPCs          | `surf.npc.command.list`                                                                                       |
| `/npc edit <npc> <key> <value>`                            | Edits the specified NPC          | `surf.npc.command.edit`, `surf.npc.command.rotation`, `surf.npc.command.skin`, `surf.npc.command.displayname` |
| `/npc info <npc>`                                          | Shows information about the NPC  | `surf.npc.command.info`                                                                                       |
| `/npc teleport <npc>`                                      | Teleports you to an NPC          | `surf.npc.command.teleport`                                                                                   |
| `/npc teleporthere <npc>`                                  | Teleports an NPC to you          | `surf.npc.command.teleporthere`                                                                               |

---

## 🔐 Permissions

| Permission                          | Description                                       |
|-------------------------------------|----------------------------------------------------|
| `surf.npc.command`                  | Base permission for all NPC commands               |
| `surf.npc.command.create`           | Allows creation of NPCs                            |
| `surf.npc.command.delete`           | Allows deletion of NPCs                            |
| `surf.npc.command.list`             | Allows listing all NPCs                            |
| `surf.npc.command.info`             | Shows detailed NPC information                     |
| `surf.npc.command.edit`             | Allows editing NPCs                                |
| `surf.npc.command.edit.rotation`    | Allows changing NPC rotation                       |
| `surf.npc.command.edit.skin`        | Allows changing the NPC skin                       |
| `surf.npc.command.edit.displayname` | Allows editing the display name                    |
| `surf.npc.command.teleport`         | Allows teleportation **to** an NPC                 |
| `surf.npc.command.teleporthere`     | Allows teleportation **of** an NPC to the player   |

---

## 🧪 API

### 🔧 General Methods

#### `createNpc(displayName, internalName, skin, location, global, rotationType, fixedRotation)`
Creates a new NPC in-game.

| Parameter       | Type               | Description                                      |
|----------------|--------------------|--------------------------------------------------|
| `displayName`   | `Component`        | NPC's display name                               |
| `internalName`  | `String`           | Internal, unique name                            |
| `skin`          | `SNpcSkinData`     | Skin data (texture, signature, etc.)             |
| `location`      | `SNpcLocation`     | Spawn position                                   |
| `global`        | `Boolean`          | Whether the NPC is globally visible              |
| `rotationType`  | `SNpcRotationType` | Type of rotation (FIXED, PER_PLAYER)             |
| `fixedRotation` | `SNpcRotation?`    | Fixed rotation if `rotationType = FIXED`         |

#### `deleteNpc(npc)`
Deletes the specified NPC.

| Parameter | Type    | Description       |
|----------|---------|-------------------|
| `npc`    | `SNpc`  | The NPC to delete |

#### `getNpc(id)`
Gets an NPC by internal ID.

| Parameter | Type   | Description    |
|-----------|--------|----------------|
| `id`      | `Int`  | Internal NPC ID|

#### `getNpc(internalName)`
Gets an NPC by internal name.

| Parameter      | Type    | Description          |
|----------------|---------|----------------------|
| `internalName` | `String`| Internal name of NPC |

#### `getNpcs()`
Returns a list of all NPCs.

#### `despawnAllNpcs()`
Temporarily removes all NPCs from the world.

---

### 👁️ Visibility

#### `showNpc(npc, uuid)`
Shows an NPC to a specific player.

| Parameter | Type    | Description                |
|-----------|---------|----------------------------|
| `npc`     | `SNpc`  | The NPC to show            |
| `uuid`    | `UUID`  | UUID of the player         |

#### `hideNpc(npc, uuid)`
Hides an NPC from a player.

| Parameter | Type    | Description                |
|-----------|---------|----------------------------|
| `npc`     | `SNpc`  | The NPC to hide            |
| `uuid`    | `UUID`  | UUID of the player         |

---

### 🧍 NPC Attributes

#### `setSkin(npc, skin)`
Changes an NPC's skin.

| Parameter | Type            | Description       |
|-----------|------------------|------------------|
| `npc`     | `SNpc`          | Target NPC       |
| `skin`    | `SNpcSkinData`  | New skin data    |

#### `setRotationType(npc, rotationType)`
Sets the rotation type for an NPC.

| Parameter      | Type               | Description                  |
|----------------|--------------------|------------------------------|
| `npc`          | `SNpc`             | Target NPC                   |
| `rotationType` | `SNpcRotationType` | PER_PLAYER or FIXED rotation |

#### `setRotation(npc, rotation)`
Sets a fixed rotation (only when type is `FIXED`).

| Parameter  | Type            | Description             |
|------------|------------------|-------------------------|
| `npc`      | `SNpc`          | Target NPC              |
| `rotation` | `SNpcRotation`  | Rotation with Yaw/Pitch |

---

### 🏷️ Properties

#### `getProperties(npc)`
Retrieves all properties of an NPC.

| Parameter | Type    | Description |
|-----------|---------|-------------|
| `npc`     | `SNpc`  | Target NPC  |

#### `addProperty(npc, property)`
Adds a property to an NPC.

| Parameter  | Type            | Description           |
|------------|------------------|------------------------|
| `npc`      | `SNpc`          | Target NPC            |
| `property` | `SNpcProperty`  | Property to add       |

#### `removeProperty(npc, property)`
Removes a property from an NPC.

| Parameter  | Type            | Description              |
|------------|------------------|---------------------------|
| `npc`      | `SNpc`          | Target NPC               |
| `property` | `SNpcProperty`  | Property to remove       |

#### `createProperty(key, value, type)`
Creates a new property.

| Parameter | Type                | Description                              |
|-----------|---------------------|------------------------------------------|
| `key`     | `String`            | Property name/key                        |
| `value`   | `String`            | Property value                           |
| `type`    | `SNpcPropertyType`  | Type (STRING, BOOLEAN, BYTE, DOUBLE, etc)|

---

### 🛠️ Helper Functions

#### `createRotation(yaw, pitch)`
Creates a new rotation.

| Parameter | Type     | Description          |
|-----------|----------|----------------------|
| `yaw`     | `Float`  | Horizontal direction |
| `pitch`   | `Float`  | Vertical direction   |

#### `createSkinData(owner, value, signature)`
Creates skin data for an NPC.

| Parameter   | Type     | Description                |
|-------------|----------|----------------------------|
| `owner`     | `String` | Player name or identifier  |
| `value`     | `String` | Skin texture value         |
| `signature` | `String` | Signature of skin value    |

#### `createLocation(x, y, z, worldName)`
Creates a location.

| Parameter   | Type     | Description         |
|-------------|----------|---------------------|
| `x`         | `Double` | X coordinate        |
| `y`         | `Double` | Y coordinate        |
| `z`         | `Double` | Z coordinate        |
| `worldName` | `String` | Name of the world   |
