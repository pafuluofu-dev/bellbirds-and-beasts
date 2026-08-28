# Sound preparation scripts

These shell scripts prepare every audio asset the mod ships. They are build-time
development tools — they are not part of the mod jar and never run on players'
machines.

## What they do

1. For the **Three-wattled Bellbird**: process the two documented xeno-canto
   recordings (see [`../../SOUND_SOURCES.md`](../../SOUND_SOURCES.md)) — trim to
   the loudest ~4 s (`silenceremove`), downmix to mono, resample to 44.1 kHz,
   encode to Ogg Vorbis.
2. For **every other creature**: generate the voice locally with FFmpeg `lavfi`
   sources (`aevalsrc` sine formulas, `anoisesrc` filtered noise). No network
   involved for these.

## Network behavior

The only remote host these scripts ever contact is `xeno-canto.org` (their
public recordings API, which has since been retired — those requests now return
nothing and the scripts fall back to synthesis). They download audio data only.
Nothing downloaded is ever executed. No credentials are read or written. All
output lands in `src/main/resources/assets/bellbird/sounds/`.

## Requirements

`bash`, `ffmpeg`, `curl`, `jq`.
