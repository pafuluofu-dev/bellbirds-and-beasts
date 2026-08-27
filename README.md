# Bellbirds & Beasts 🔔🦏🍺

A Fabric mod for **Minecraft 1.19.2** that adds the loudest birds on Earth — the complete
*Procnias* bellbird genus — plus a whole menagerie of remarkable, extinct, and frankly
legendary creatures. **23 mobs**, each with its own model, texture, and voice.

**BONK.**

🌀 **[Interactive 3D preview of every model →](https://pafuluofu.github.io/bellbirds-and-beasts/)**
· 📦 **[Download the latest jar →](https://github.com/pafuluofu/bellbirds-and-beasts/releases)**

## Creatures

### The bellbirds (Procnias — the loudest birds on Earth)
| Creature | Spawns in | Voice |
|---|---|---|
| Three-wattled Bellbird | Jungles, forests | Real field recordings (xeno-canto) |
| White Bellbird | Jungles, forests | Bellbird bonk |
| Bare-throated Bellbird | Jungles, forests | Bellbird bonk |
| Bearded Bellbird | Jungles, forests | Bellbird bonk |

### Other birds
| Creature | Spawns in | Voice |
|---|---|---|
| Great Potoo | Jungles, forests | Falling moan |
| Screaming Piha | Jungles, forests | Whistle-scream |
| Black Grouse | Taiga | Bubbling lek song |
| Sage Grouse | Plains | Chest pops |
| Cassowary | Jungles | Subsonic boom |
| Flamingo | Beaches, rivers | Honk |
| Shoebill | Swamps | Bill clatter |

### Mammals & megafauna
| Creature | Spawns in | Voice |
|---|---|---|
| Jaguar | Jungles | Rasping growl |
| Leopard | Savannas | Growl |
| Gray Cat | Plains | Meow |
| Sea Otter (калан) | Beaches, rivers | Squeak |
| Tarbagan Marmot | Taiga, plains | Alarm whistle |
| Sun Bear | Jungles | Low growl |
| Moon Bear | Taiga | Low growl |
| Bison | Plains | Grunt |
| Elasmotherium | Plains, taiga | Prehistoric bellow |
| Arsinoitherium | Savanna, plains | Prehistoric bellow |

### Legends
| Creature | Spawns in | Notes |
|---|---|---|
| Unicorn | Flower forests, meadows | Rare. Pearlescent horn, pink mane |
| T-Pose Cat | Plains | Very rare. Arms permanently horizontal. Carries a beer. The arms never animate; this is intentional and non-negotiable |

Bellbird bonks are audible from ~96 blocks, as nature intended. Spawn eggs for every
creature are in the Creative **Misc** tab.

## Install

Requires [Fabric Loader](https://fabricmc.net/) + [Fabric API](https://modrinth.com/mod/fabric-api) on **1.19.2**.
Drop the jar into `mods/` on **both server and clients** (it adds entities, so everyone needs it).

## Build

```bash
gradle build
```

Java 17. Sound files (`assets/bellbird/sounds/*.ogg`) are not committed — they are trimmed
from field recordings / synthesized with ffmpeg at package time (see the `fetch-sounds`
scripts). Entity textures are generated programmatically by `tools/gen-texture.js`, and
`tools/build-preview.js` renders the same CSS-3D preview that runs on GitHub Pages —
no Blockbench involved at any point.

## Credits

- Three-wattled bellbird recordings: [xeno-canto](https://xeno-canto.org) XC331004, XC747775 (CC licensed)

## License

MIT
