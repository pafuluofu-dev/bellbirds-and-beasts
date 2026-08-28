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

## 2. All other creatures: silent by design (since v1.2.3)

Releases up to v1.2.2 shipped locally generated placeholder voices for the other
creatures (FFmpeg `lavfi` synthesis — pure mathematical signal definitions, no
third-party audio; formulas remain in `tools/sounds/fetch-sounds*.sh` for
reference). **From v1.2.3 those placeholders are removed**: creatures without a
documented real recording make no ambient sound at all. Real, properly licensed
recordings may be added per species in future versions and will be documented
here first.

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
