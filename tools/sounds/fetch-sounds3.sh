#!/bin/bash
cd /root/bellbird/audio
syn() { # id, lavfi source, extra filters
  id=$1; src=$2; flt=$3
  ffmpeg -y -loglevel error -f lavfi -i "$src" -af "$flt" -ac 1 -ar 44100 -c:a libvorbis "$id.ogg"
  echo "$id: $(ffprobe -v error -show_entries format=duration -of csv=p=0 "$id.ogg")s"
}
# growls
syn jaguar   "anoisesrc=colour=brown:r=44100:d=1.8" "lowpass=f=300,tremolo=f=25:d=0.85,afade=t=in:d=0.1,afade=t=out:st=1.2:d=0.6,volume=13dB"
syn leopard  "anoisesrc=colour=brown:r=44100:d=1.5" "lowpass=f=420,tremolo=f=30:d=0.85,afade=t=in:d=0.1,afade=t=out:st=1.0:d=0.5,volume=12dB"
syn sun_bear "anoisesrc=colour=brown:r=44100:d=1.6" "lowpass=f=200,tremolo=f=15:d=0.8,afade=t=in:d=0.15,afade=t=out:st=1.1:d=0.5,volume=13dB"
syn moon_bear "anoisesrc=colour=brown:r=44100:d=1.8" "lowpass=f=160,tremolo=f=12:d=0.8,afade=t=in:d=0.15,afade=t=out:st=1.2:d=0.6,volume=13dB"
syn bison    "anoisesrc=colour=brown:r=44100:d=1.2" "lowpass=f=100,tremolo=f=20:d=0.7,afade=t=in:d=0.05,afade=t=out:st=0.8:d=0.4,volume=15dB"
# meows
syn gray_cat  "aevalsrc=sin(2*PI*t*(650-180*t+30*sin(2*PI*t*7)))*exp(-t*1.5):d=0.9:s=44100" "volume=4dB"
syn tpose_cat "aevalsrc=sin(2*PI*t*(480-120*t+24*sin(2*PI*t*5)))*exp(-t*1.1):d=1.3:s=44100" "lowpass=f=1200,volume=5dB"
# squeaks and whistles
syn kalan    "aevalsrc=sin(2*PI*t*(1500-600*t))*exp(-t*4):d=0.6:s=44100" "volume=4dB"
syn tarbagan "aevalsrc=sin(2*PI*2500*t)*if(lt(mod(t\,0.3)\,0.12)\,1\,0):d=0.7:s=44100" "afade=t=out:st=0.5:d=0.2,volume=3dB"
# horse-ish neigh
syn unicorn  "aevalsrc=sin(2*PI*t*(950-350*t+90*sin(2*PI*t*13)))*exp(-t*0.9):d=1.4:s=44100" "lowpass=f=2200,volume=5dB"
# birds
syn flamingo "aevalsrc=tanh(8*sin(2*PI*285*t))*if(lt(mod(t\,0.5)\,0.27)\,1\,0):d=1.0:s=44100" "lowpass=f=1500,volume=6dB"
syn shoebill "anoisesrc=colour=white:r=44100:d=1.6" "lowpass=f=3200,highpass=f=400,tremolo=f=14:d=0.95,afade=t=out:st=1.1:d=0.5,volume=6dB"
syn black_grouse "aevalsrc=sin(2*PI*t*(265+15*sin(2*PI*t*2)))*(0.7+0.3*sin(2*PI*t*9)):d=1.8:s=44100" "lowpass=f=800,afade=t=out:st=1.3:d=0.5,volume=5dB"
syn sage_grouse "aevalsrc=sin(2*PI*95*t)*exp(-mod(t\,0.38)*16):d=1.2:s=44100" "lowpass=f=500,volume=9dB"
ls /root/bellbird/audio/*.ogg | wc -l
