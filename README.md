## Preordain

Immersive documentation system for Minecraft 1.12.2.

Makes use of moderately modified Ponder source code, taken from Siepert's [Create Legacy](https://github.com/Siepert123/create-legacy).

### Documentation

Preordain uses JSON files which can be placed under `minecraft/preordain`. The JSON format used for documents is currently undocumented, reading the source code is recommended to understand what's going on. Mods can also supply their own JSON files.

Documents in `minecraft/preordain` can be automatically reloaded using `/preordainreload`. (This usually does not work for mods since the mod's resources get frozen on compilation.)

This mod is only needed on the client side.
