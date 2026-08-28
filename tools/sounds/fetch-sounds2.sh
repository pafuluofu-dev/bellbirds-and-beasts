#!/bin/bash
cd /root/bellbird/audio

fetch() {
  id=$1; q=$2
  json=$(curl -sL "https://xeno-canto.org/api/2/recordings?query=${q}%20q:A")
  url=$(echo "$json" | jq -r '.recordings[0].file // empty' 2>/dev/null)
  if [ -z "$url" ]; then
    json=$(curl -sL "https://xeno-canto.org/api/2/recordings?query=${q}")
    url=$(echo "$json" | jq -r '.recordings[0].file // empty' 2>/dev/null)
  fi
  case "$url" in //*) url="https:$url";; esac
  rm -f "$id.ogg"
  if [ -n "$url" ]; then
    curl -sL "$url" -o "/tmp/$id.mp3" && \
    ffmpeg -y -loglevel error -i "/tmp/$id.mp3" \
      -af "silenceremove=start_periods=1:start_threshold=-30dB:stop_periods=-1:stop_threshold=-30dB:stop_duration=0.4" \
      -t 4 -ac 1 -ar 44100 -c:a libvorbis -q:a 4 "$id.ogg" 2>/dev/null
  fi
  if [ -s "$id.ogg" ]; then echo "$id: REAL from $url"; else echo "$id: no recording, will synthesize"; fi
}

fetch potoo "Nyctibius%20grandis"
fetch piha "Lipaugus%20vociferans"
fetch cassowary "Casuarius"

# synthesized fallbacks / extinct species
if [ ! -s potoo.ogg ]; then
  ffmpeg -y -loglevel error -f lavfi -i "aevalsrc=sin(2*PI*t*(520-160*t))*exp(-t*1.2):d=1.6:s=44100" \
    -af "lowpass=f=900,volume=6dB" -ac 1 -c:a libvorbis potoo.ogg
  echo "potoo: synthesized moan"
fi
if [ ! -s piha.ogg ]; then
  ffmpeg -y -loglevel error -f lavfi -i "aevalsrc=sin(2*PI*t*(800+2200*t))*if(lt(t\,0.5)\,1\,exp(-(t-0.5)*3)):d=1.1:s=44100" \
    -af "volume=8dB" -ac 1 -c:a libvorbis piha.ogg
  echo "piha: synthesized whistle"
fi
if [ ! -s cassowary.ogg ]; then
  ffmpeg -y -loglevel error -f lavfi -i "anoisesrc=colour=brown:r=44100:d=2.0" \
    -af "lowpass=f=90,tremolo=f=12:d=0.8,afade=t=in:d=0.2,afade=t=out:st=1.4:d=0.6,volume=16dB" \
    -ac 1 -c:a libvorbis cassowary.ogg
  echo "cassowary: synthesized boom"
fi
ffmpeg -y -loglevel error -f lavfi -i "anoisesrc=colour=brown:r=44100:d=2.4" \
  -af "lowpass=f=120,tremolo=f=8:d=0.75,afade=t=in:d=0.2,afade=t=out:st=1.7:d=0.7,volume=15dB" \
  -ac 1 -c:a libvorbis elasmotherium.ogg
ffmpeg -y -loglevel error -f lavfi -i "anoisesrc=colour=brown:r=44100:d=2.0" \
  -af "lowpass=f=170,tremolo=f=5:d=0.7,afade=t=in:d=0.15,afade=t=out:st=1.4:d=0.6,volume=14dB" \
  -ac 1 -c:a libvorbis arsinoitherium.ogg
echo "megafauna: synthesized bellows"
ls -la /root/bellbird/audio/
