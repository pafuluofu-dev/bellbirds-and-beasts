# Sound sources & provenance

Every sound asset in Bellbirds & Beasts falls into exactly one of two categories.

## 1. Real field recordings (one species only)

The **Three-wattled Bellbird** uses two real wildlife recordings from
[xeno-canto](https://xeno-canto.org), obtained manually from their recording pages:

| Recording | Species | Source page | License |
|---|---|---|---|
| XC331004 | *Procnias tricarunculatus* (Three-wattled Bellbird) | https://xeno-canto.org/331004 | As stated on the source page (xeno-canto recordings carry a Creative Commons license chosen by the recordist; see the page for the exact terms and recordist credit) |
| XC747775 | *Procnias tricarunculatus* (Three-wattled Bellbird) | https://xeno-canto.org/747775 | As stated on the source page (same as above) |

**Modifications:** trimmed to the loudest ~4 seconds with FFmpeg's `silenceremove`
filter, downmixed to mono, resampled to 44.1 kHz, and encoded as Ogg Vorbis.

**Runtime destination:** `assets/bellbird/sounds/bonk1.ogg` and `bonk2.ogg` inside
the built jar (played by the `bellbird:bonk_three_wattled` sound event).

These recordings are **not committed to this repository**. They enter the build
only through the documented preparation step (`tools/sounds/`), and the exact
license of each recording should be verified on its source page before any
further redistribution of the audio.

## 2. Locally synthesized sounds (all 23 other creatures)

Every other creature voice — the other three bellbirds' bonk variants included in
early releases aside, see note below — is **generated locally from mathematical
signal definitions** using FFmpeg's `lavfi` sources (`aevalsrc`, `anoisesrc`,
sine sweeps, filtered noise). No third-party audio is involved. The exact
formulas are in `tools/sounds/fetch-sounds*.sh`.

Examples: the jaguar growl is band-limited brown noise with tremolo; the
flamingo honk is a clipped 285 Hz sine burst; the nightjar churr is a
26 Hz-modulated 750 Hz tone.

**Note on the White, Bare-throated and Bearded Bellbirds:** in releases up to
v1.2.x these three reuse the processed XC331004 recording as their bonk
(documented fallback, same provenance as above).

## Machine-readable manifest

See [`sound_sources.json`](sound_sources.json).

## Network behavior of the sound tooling

The scripts in `tools/sounds/` contact **only** `xeno-canto.org` (an API that has
since been retired — those calls now return nothing and the scripts fall back to
synthesis). They download audio data only, never executables; nothing that is
downloaded is executed; no credentials are read; all output is written into the
build's resource folder.
