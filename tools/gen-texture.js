// Generates one 64x64 texture per creature.
// UV layouts must match BellbirdModel / CassowaryModel / MegafaunaModel.
const { PNG } = require('pngjs');
const fs = require('fs');
const path = require('path');

const W = 64, H = 64;
let seed = 42;
function rnd() { seed = (seed * 1103515245 + 12345) & 0x7fffffff; return seed / 0x7fffffff; }

const WHITE = [244, 240, 232], WHITE_D = [228, 222, 210];
const BEAK = [35, 33, 40], BEAK_L = [53, 50, 58];
const GRAY_LEG = [90, 85, 96], GRAY_LEG_D = [70, 66, 76];
const WATTLE = [38, 36, 44], WATTLE_L = [52, 49, 58];
const EYE = [26, 24, 30], LASH = [42, 39, 48];
const BONE = [217, 207, 182], BONE_D = [194, 183, 156];

const outDir = path.join(__dirname, '..', 'src', 'main', 'resources', 'assets', 'bellbird', 'textures', 'entity');
fs.mkdirSync(outDir, { recursive: true });

function sheet() {
  seed = 42;
  const png = new PNG({ width: W, height: H });
  const px = (x, y, c) => {
    if (x < 0 || y < 0 || x >= W || y >= H) return;
    const i = (W * y + x) << 2;
    png.data[i] = c[0]; png.data[i + 1] = c[1]; png.data[i + 2] = c[2]; png.data[i + 3] = 255;
  };
  const fill = (x0, y0, x1, y1, base, dark, noise = 0.25) => {
    for (let y = y0; y < y1; y++) for (let x = x0; x < x1; x++) px(x, y, rnd() < noise ? dark : base);
  };
  const save = id => fs.writeFileSync(path.join(outDir, id + '.png'), PNG.sync.write(png));
  return { px, fill, save };
}

// ---------- bird sheets (BellbirdModel layout) ----------
const BIRDS = {
  three_wattled: {
    body: [150, 73, 30], bodyD: [122, 58, 22], bodyL: [168, 88, 40],
    head: WHITE, headD: WHITE_D, hood: true, noise: 0.25,
    wing: [142, 66, 26], wingD: [112, 52, 20],
    tail: [150, 73, 30], tailD: [122, 58, 22], throat: null, eyes: 'lash'
  },
  white: {
    body: [242, 238, 228], bodyD: [226, 220, 206], bodyL: [248, 245, 238],
    head: [246, 243, 235], headD: [231, 226, 214], hood: false, noise: 0.25,
    wing: [236, 231, 219], wingD: [218, 211, 196],
    tail: [239, 235, 224], tailD: [222, 216, 202], throat: null, eyes: 'lash'
  },
  bare_throated: {
    body: [242, 238, 228], bodyD: [226, 220, 206], bodyL: [248, 245, 238],
    head: [246, 243, 235], headD: [231, 226, 214], hood: false, noise: 0.25,
    wing: [236, 231, 219], wingD: [218, 211, 196],
    tail: [239, 235, 224], tailD: [222, 216, 202],
    throat: [58, 199, 184], throatD: [47, 163, 150], eyes: 'lash'
  },
  bearded: {
    body: [207, 202, 193], bodyD: [187, 181, 170], bodyL: [220, 216, 208],
    head: [107, 70, 48], headD: [87, 56, 31], hood: false, noise: 0.25,
    wing: [38, 36, 42], wingD: [27, 25, 30],
    tail: [74, 70, 63], tailD: [58, 55, 51], throat: null, eyes: 'lash'
  },
  potoo: {
    body: [138, 122, 102], bodyD: [104, 91, 74], bodyL: [155, 140, 120],
    head: [138, 122, 102], headD: [104, 91, 74], hood: false, noise: 0.45,
    wing: [118, 103, 85], wingD: [88, 76, 61],
    tail: [128, 113, 94], tailD: [98, 85, 69], throat: null, eyes: 'huge'
  },
  piha: {
    body: [155, 160, 164], bodyD: [128, 133, 138], bodyL: [168, 173, 177],
    head: [150, 155, 159], headD: [124, 129, 134], hood: false, noise: 0.25,
    wing: [138, 143, 148], wingD: [112, 117, 122],
    tail: [144, 149, 153], tailD: [118, 123, 128], throat: null, eyes: 'lash'
  }
};

for (const [id, P] of Object.entries(BIRDS)) {
  const s = sheet();
  // body box (4x5x7) @ (0,0)
  s.fill(7, 0, 11, 7, P.body, P.bodyD, P.noise);
  s.fill(11, 0, 15, 7, P.bodyL, P.body, P.noise);
  s.fill(0, 7, 7, 12, P.body, P.bodyD, P.noise);
  s.fill(11, 7, 18, 12, P.body, P.bodyD, P.noise);
  s.fill(18, 7, 22, 12, P.bodyD, P.body, P.noise);
  if (P.hood) {
    s.fill(7, 7, 11, 12, WHITE, WHITE_D, 0.15);
    s.fill(0, 7, 2, 12, WHITE, WHITE_D, 0.15);
    s.fill(11, 7, 13, 12, WHITE, WHITE_D, 0.15);
    s.fill(7, 0, 11, 2, WHITE, WHITE_D, 0.15);
    s.fill(7, 11, 11, 12, P.bodyL, P.body);
  } else {
    s.fill(7, 7, 11, 12, P.bodyL, P.body, 0.2);
  }
  // head box (4x4x4) @ (24,0)
  s.fill(28, 0, 32, 4, P.head, P.headD, 0.12);
  s.fill(32, 0, 36, 4, P.head, P.headD, 0.12);
  s.fill(24, 4, 28, 8, P.head, P.headD, 0.12);
  s.fill(28, 4, 32, 8, P.head, P.headD, 0.12);
  s.fill(32, 4, 36, 8, P.head, P.headD, 0.12);
  s.fill(36, 4, 40, 8, P.head, P.headD, 0.12);
  if (P.throat) {
    s.fill(32, 0, 36, 4, P.throat, P.throatD, 0.2);
    s.fill(28, 6, 32, 8, P.throat, P.throatD, 0.2);
  }
  if (P.eyes === 'huge') {
    const Y = [232, 193, 58], YD = [198, 160, 40];
    s.fill(25, 4, 28, 7, Y, YD, 0.2); s.fill(26, 5, 28, 7, EYE, EYE, 0);   // right face
    s.fill(32, 4, 35, 7, Y, YD, 0.2); s.fill(32, 5, 34, 7, EYE, EYE, 0);   // left face
    s.fill(28, 4, 30, 6, Y, YD, 0.2); s.px(29, 5, EYE);                    // front left eye
    s.fill(30, 4, 32, 6, Y, YD, 0.2); s.px(30, 5, EYE);                    // front right eye
  } else {
    s.fill(25, 5, 27, 7, EYE, EYE, 0);
    s.fill(24, 4, 27, 5, LASH, EYE, 0.4);
    s.fill(33, 5, 35, 7, EYE, EYE, 0);
    s.fill(33, 4, 36, 5, LASH, EYE, 0.4);
  }
  // beak @ (44,0)
  s.fill(44, 0, 52, 4, BEAK, BEAK_L, 0.12);
  // wing (1x4x6) @ (0,16)
  s.fill(0, 16, 14, 27, P.wing, P.wingD, 0.3);
  for (let y = 23; y < 27; y += 2) for (let x = 1; x < 13; x++) s.px(x, y, P.wingD);
  // tail (3x1x5) @ (20,16)
  s.fill(20, 16, 36, 22, P.tail, P.tailD, 0.3);
  s.fill(28, 16, 31, 21, P.tailD, P.tailD, 0);
  // legs @ (40,16)
  s.fill(40, 16, 44, 20, GRAY_LEG, GRAY_LEG_D, 0.3);
  // wattle strips @ (48..59,16)
  for (const u of [48, 52, 56]) s.fill(u, 16, u + 3, 23, WATTLE, WATTLE_L, 0.2);
  s.save(id);
  console.log('texture:', id + '.png');
}

// ---------- cassowary (CassowaryModel layout) ----------
{
  const s = sheet();
  const FEATHER = [30, 28, 34], FEATHER_D = [20, 19, 24];
  const BLUE = [36, 86, 200], BLUE_D = [26, 63, 150];
  const RED = [196, 59, 42];
  s.fill(0, 0, 26, 13, FEATHER, FEATHER_D, 0.35);        // body
  s.fill(28, 10, 38, 15, FEATHER, FEATHER_D, 0.35);      // tail box
  s.fill(28, 0, 36, 7, BLUE, BLUE_D, 0.25);              // neck
  s.fill(30, 4, 32, 7, RED, [160, 44, 30], 0.25);        // neck front = red wattle
  s.fill(40, 0, 52, 5, BLUE, BLUE_D, 0.25);              // head
  s.px(41, 3, EYE); s.px(47, 3, EYE);                    // eyes
  s.fill(40, 8, 48, 13, [122, 102, 82], [94, 76, 59], 0.3); // casque
  s.fill(52, 0, 58, 3, BEAK, BEAK_L, 0.12);              // beak
  s.fill(0, 16, 8, 26, [110, 106, 82], [88, 84, 62], 0.3); // legs
  s.save('cassowary');
  console.log('texture: cassowary.png');
}

// ---------- megafauna (MegafaunaModel layout) ----------
function megafauna(id, base, dark, belly) {
  const s = sheet();
  s.fill(0, 0, 48, 24, base, dark, 0.45);                // body (shaggy)
  s.fill(12, 0, 28, 8, belly, base, 0.35);               // top back lighter streak
  s.fill(0, 26, 22, 38, base, dark, 0.4);                // head
  s.px(2, 33, EYE); s.px(14, 33, EYE);                   // eyes
  s.fill(24, 26, 36, 39, dark, base, 0.35);              // legs
  s.fill(44, 26, 52, 33, BONE, BONE_D, 0.25);            // horn(s)
  s.fill(54, 26, 58, 31, BONE, BONE_D, 0.25);            // horn tip
  s.fill(56, 32, 60, 38, dark, base, 0.3);               // tail
  s.save(id);
  console.log('texture:', id + '.png');
}
megafauna('elasmotherium', [90, 70, 50], [72, 56, 31], [108, 86, 62]);
megafauna('arsinoitherium', [112, 100, 90], [90, 80, 72], [128, 116, 105]);
