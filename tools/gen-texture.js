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
  },
  black_grouse: {
    body: [30, 30, 38], bodyD: [20, 20, 28], bodyL: [44, 48, 70],
    head: [30, 30, 38], headD: [20, 20, 28], hood: false, noise: 0.3,
    wing: [26, 26, 34], wingD: [16, 16, 22],
    tail: [24, 24, 32], tailD: [244, 240, 232], throat: null, eyes: 'redbrow'
  },
  sage_grouse: {
    body: [150, 138, 115], bodyD: [115, 105, 85], bodyL: [170, 158, 134],
    head: [150, 138, 115], headD: [115, 105, 85], hood: true, noise: 0.45,
    wing: [136, 124, 102], wingD: [104, 94, 76],
    tail: [110, 100, 80], tailD: [80, 72, 56], throat: null, eyes: 'yellowbrow'
  },
  nightjar: {
    body: [110, 95, 78], bodyD: [82, 70, 56], bodyL: [126, 110, 92],
    head: [110, 95, 78], headD: [82, 70, 56], hood: false, noise: 0.5,
    wing: [96, 82, 66], wingD: [70, 60, 48],
    tail: [102, 88, 72], tailD: [74, 64, 52], throat: null, eyes: 'lash'
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
  } else if (P.eyes === 'redbrow' || P.eyes === 'yellowbrow') {
    const brow = P.eyes === 'redbrow' ? [200, 40, 40] : [222, 186, 70];
    s.fill(25, 5, 27, 7, EYE, EYE, 0);
    s.fill(24, 4, 27, 5, brow, brow, 0);
    s.fill(33, 5, 35, 7, EYE, EYE, 0);
    s.fill(33, 4, 36, 5, brow, brow, 0);
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

// ---------- quadrupeds (QuadrupedModel layout) ----------
// regions: body 0..42x0..21, head 0..20x22..32, snout 22..34x22..27,
// legs 36..47x22..33, tail 48..52x22..29, ears 54..58x22..24,
// horn 54..58x26..30, hump 0..22x34..42, neck 24..35x34..42
const QUADS = {
  jaguar:    { hs: 4, base: [212, 160, 74], dark: [190, 138, 58], spots: [60, 44, 26], spotChance: 0.16, snout: [224, 200, 160], horn: null },
  leopard:   { hs: 4, base: [222, 178, 90], dark: [198, 152, 70], spots: [52, 40, 24], spotChance: 0.14, snout: [230, 208, 170], horn: null },
  gray_cat:  { hs: 3, base: [142, 142, 150], dark: [118, 118, 126], spots: [98, 98, 106], spotChance: 0.1, snout: [190, 176, 176], horn: null },
  kalan:     { hs: 3, base: [92, 66, 44], dark: [74, 52, 34], spots: null, snout: [178, 156, 128], headOverride: [178, 156, 128], headOverrideD: [150, 128, 100], horn: null },
  sun_bear:  { hs: 4, base: [28, 25, 22], dark: [18, 16, 14], spots: null, snout: [188, 152, 108], chest: [224, 163, 58], horn: null },
  moon_bear: { hs: 4, base: [30, 27, 26], dark: [20, 18, 17], spots: null, snout: [160, 130, 100], chest: [244, 240, 232], horn: null },
  bison:     { hs: 5, base: [74, 52, 35], dark: [56, 38, 24], spots: null, snout: [50, 34, 22], horn: BONE, humpC: [50, 34, 22] },
  unicorn:   { hs: 4, base: [244, 242, 238], dark: [228, 225, 218], spots: null, snout: [232, 218, 214], horn: [238, 225, 190], neckC: [244, 242, 238], maneC: [232, 150, 190] },
  tarbagan:  { hs: 3, base: [156, 120, 82], dark: [128, 96, 62], spots: null, snout: [196, 164, 120], horn: null }
};

for (const [id, Q] of Object.entries(QUADS)) {
  const s = sheet();
  s.fill(0, 0, 42, 21, Q.base, Q.dark, 0.35);                       // body
  if (Q.spots) for (let i = 0; i < 260; i++) { const x = (rnd() * 42) | 0, y = (rnd() * 21) | 0; if (rnd() < Q.spotChance * 3) s.px(x, y, Q.spots); }
  if (Q.chest) s.fill(8, 14, 16, 20, Q.chest, Q.chest, 0.15);       // chest crescent zone (front-lower body)
  const hB = Q.headOverride || Q.base, hD = Q.headOverrideD || Q.dark;
  s.fill(0, 22, 20, 32, hB, hD, 0.3);                               // head
  // eyes: computed from head size so they land on the actual faces
  // box UV: right face x 0..h, front face x h..2h, left face x 2h..3h, sides start at y 22+h
  const h = Q.hs;
  const eyeY = 22 + h + 1;
  s.px(h + 1, eyeY, EYE); s.px(2 * h - 2, eyeY, EYE);               // front face pair
  if (h <= 3) { s.px(h, eyeY, EYE); s.px(2 * h - 1, eyeY, EYE); }   // widen on small heads
  s.px(h - 1, eyeY, EYE);                                           // right face, front edge
  s.px(2 * h + 1, eyeY, EYE);                                       // left face, front edge
  s.fill(22, 22, 34, 27, Q.snout, hD, 0.2);                         // snout
  s.px(27, 23, [40, 30, 30]);                                       // nose
  s.fill(36, 22, 47, 33, Q.dark, Q.base, 0.35);                     // legs
  s.fill(48, 22, 52, 29, Q.base, Q.dark, 0.3);                      // tail
  s.fill(54, 22, 58, 24, hB, hD, 0.2);                              // ears
  if (Q.horn) s.fill(54, 26, 58, 30, Q.horn, BONE_D, 0.2);          // horn(s)
  if (Q.humpC) s.fill(0, 34, 22, 42, Q.humpC, Q.dark, 0.45);        // bison hump
  if (Q.neckC) { s.fill(24, 34, 35, 42, Q.neckC, Q.dark, 0.15); s.fill(24, 34, 27, 42, Q.maneC, Q.maneC, 0.2); } // unicorn neck + mane stripe
  s.save(id);
  console.log('texture:', id + '.png');
}

// ---------- t-pose cat ----------
{
  const s = sheet();
  const G = [142, 142, 150], GD = [118, 118, 126];
  s.fill(0, 0, 14, 9, G, GD, 0.3);                                  // torso
  s.fill(16, 0, 32, 8, G, GD, 0.3);                                 // head
  s.px(21, 5, EYE); s.px(23, 5, EYE);                               // face front eyes
  s.fill(34, 0, 47, 7, G, GD, 0.3);                                 // arm
  s.fill(48, 0, 56, 6, G, GD, 0.3);                                 // legs
  s.fill(0, 12, 4, 16, [196, 124, 38], [160, 98, 26], 0.2);         // bottle (amber)
  s.px(1, 13, [244, 240, 232]); s.px(2, 13, [244, 240, 232]);       // label
  s.fill(6, 12, 10, 15, [150, 92, 22], [120, 72, 16], 0.2);         // bottleneck
  s.fill(10, 12, 14, 17, G, GD, 0.3);                               // tail
  s.fill(16, 10, 20, 12, G, [232, 150, 170], 0.4);                  // ears w/ pink inner
  s.fill(28, 10, 32, 12, [232, 150, 170], [200, 120, 140], 0.2);    // snout/nose
  s.save('tpose_cat');
  console.log('texture: tpose_cat.png');
}

// ---------- tall birds ----------
{
  const s = sheet();
  const P = [240, 140, 168], PD = [220, 110, 140];
  s.fill(0, 0, 16, 9, P, PD, 0.25);                                 // flamingo body
  s.fill(24, 10, 31, 15, P, PD, 0.25);                              // tail puff
  s.fill(24, 0, 28, 7, P, PD, 0.2);                                 // neck
  s.fill(32, 0, 40, 4, P, PD, 0.2);                                 // head
  s.px(33, 1, EYE); s.px(37, 1, EYE);
  s.fill(44, 0, 50, 3, [230, 200, 170], [200, 168, 136], 0.2);      // beak base
  s.fill(44, 4, 48, 7, [40, 38, 44], [58, 54, 62], 0.2);            // beak tip (black, bent)
  s.fill(0, 14, 4, 24, [216, 120, 130], [190, 96, 108], 0.25);      // legs
  s.save('flamingo');
  console.log('texture: flamingo.png');
}
{
  const s = sheet();
  const B = [124, 132, 140], BD = [100, 108, 116];
  s.fill(0, 0, 20, 11, B, BD, 0.3);                                 // body
  s.fill(24, 10, 33, 15, BD, B, 0.3);                               // tail
  s.fill(24, 0, 30, 5, B, BD, 0.25);                                // neck
  s.fill(32, 0, 43, 6, B, BD, 0.25);                                // head
  s.px(33, 2, [226, 208, 120]); s.px(40, 2, [226, 208, 120]);       // pale glare eyes
  s.fill(44, 0, 55, 6, [180, 158, 120], [150, 128, 92], 0.3);       // the SHOE beak
  s.px(49, 5, [70, 58, 40]);                                        // hook
  s.fill(0, 14, 4, 22, [70, 74, 80], [54, 58, 64], 0.25);           // legs
  s.save('shoebill');
  console.log('texture: shoebill.png');
}

// ---------- lips for the megafauna (repaint over saved sheets) ----------
for (const id of ['elasmotherium', 'arsinoitherium']) {
  const buf = PNG.sync.read(fs.readFileSync(path.join(outDir, id + '.png')));
  const put = (x, y, c) => { const i = (64 * y + x) << 2; buf.data[i] = c[0]; buf.data[i + 1] = c[1]; buf.data[i + 2] = c[2]; buf.data[i + 3] = 255; };
  for (let x = 6; x < 11; x++) put(x, 36, [42, 30, 22]);            // mouth line on head front face
  for (let x = 7; x < 10; x++) put(x, 37, [168, 120, 104]);         // lip
  fs.writeFileSync(path.join(outDir, id + '.png'), PNG.sync.write(buf));
  console.log('lips added:', id);
}
