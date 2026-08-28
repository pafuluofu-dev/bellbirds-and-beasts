#!/bin/bash
cd /root/bellbird/audio
ffmpeg -y -loglevel error -f lavfi -i "aevalsrc=sin(2*PI*750*t)*(0.55+0.45*sin(2*PI*t*26)):d=2.5:s=44100" \
  -af "lowpass=f=1600,afade=t=in:d=0.2,afade=t=out:st=1.9:d=0.6,volume=2dB" \
  -ac 1 -ar 44100 -c:a libvorbis nightjar.ogg
echo "nightjar: $(ffprobe -v error -show_entries format=duration -of csv=p=0 nightjar.ogg)s (churring trill)"
