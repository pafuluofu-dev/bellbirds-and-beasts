# Bellbirds & Beasts 🔔🦏

A Fabric mod for **Minecraft 1.19.2** that adds the loudest birds on Earth — the complete
*Procnias* bellbird genus — plus a growing menagerie of remarkable (and extinct) creatures.

**BONK.**

## Creatures

| Creature | Where it spawns | Sound |
|---|---|---|
| Three-wattled Bellbird | Jungles, forests | Real field recordings (xeno-canto) |
| White Bellbird | Jungles, forests | Bellbird bonk |
| Bare-throated Bellbird | Jungles, forests | Bellbird bonk |
| Bearded Bellbird | Jungles, forests | Bellbird bonk |
| Great Potoo | Jungles, forests | Synthesized moan |
| Screaming Piha | Jungles, forests | Synthesized whistle-scream |
| Cassowary | Jungles | Synthesized boom |
| Elasmotherium | Plains, taiga | Synthesized bellow |
| Arsinoitherium | Savanna, plains | Synthesized bellow |

All birds fly, perch, and call — bellbird bonks are audible from ~96 blocks, as nature intended.
Spawn eggs for every creature are in the Creative **Misc** tab.

## Install

Requires [Fabric Loader](https://fabricmc.net/) + [Fabric API](https://modrinth.com/mod/fabric-api) on **1.19.2**.
Drop the jar into `mods/` on **both server and clients** (it adds entities, so everyone needs it).

## Build

```bash
gradle build
```

Java 17. Sound files (`assets/bellbird/sounds/*.ogg`) are not committed — they are trimmed
from field recordings / synthesized with ffmpeg at package time (see `tools/` and the
`fetch-sounds` scripts). The entity textures are generated programmatically by
`tools/gen-texture.js`, and `tools/build-preview.js` renders a live CSS-3D preview of every
model in a browser — no Blockbench needed.

## Credits

- Three-wattled bellbird recordings: [xeno-canto](https://xeno-canto.org) XC331004, XC747775 (CC licensed)
- Model geometry, textures, code: built with Claude

## License

MIT
