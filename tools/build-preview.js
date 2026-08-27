// Generates preview.html — all four bellbird species side by side, CSS-3D,
// mirroring BellbirdModel.getTexturedModelData() geometry per species.
const fs = require('fs');
const path = require('path');

const S = 10;
const texDir = path.join(__dirname, '..', 'src', 'main', 'resources', 'assets', 'bellbird', 'textures', 'entity');
const deg = r => (r * 180 / Math.PI).toFixed(2);

const BEARD_X = [-0.8, -0.4, 0, 0.4, 0.8];
const BEARD_Z = [-2.4, -2.7, -2.9, -2.7, -2.4];
const BEARD_LEN = [1.6, 2.3, 2.9, 2.3, 1.6];
const BEARD_ROLL = [-0.25, -0.12, 0, 0.12, 0.25];

function speciesModel(id) {
  const wattles = [];
  if (id === 'three_wattled') {
    wattles.push({ name: 'wc', pivot: [0, -2.2, -3.3], rot: [0.18, 0, 0], boxes: [[-0.3, 0, -0.3, 0.6, 5, 0.4, 48, 16]] });
    wattles.push({ name: 'wl', pivot: [0.8, -1.9, -3.1], rot: [0.12, 0, 0.25], boxes: [[-0.3, 0, -0.3, 0.6, 4, 0.4, 52, 16]] });
    wattles.push({ name: 'wr', pivot: [-0.8, -1.9, -3.1], rot: [0.12, 0, -0.25], boxes: [[-0.3, 0, -0.3, 0.6, 4, 0.4, 56, 16]] });
  } else if (id === 'white') {
    wattles.push({ name: 'wc', pivot: [0, -3.4, -3.4], rot: [0.25, 0, 0], boxes: [[-0.35, 0, -0.35, 0.7, 6.5, 0.4, 48, 16]] });
  } else if (id === 'bearded') {
    for (let i = 0; i < 5; i++) {
      wattles.push({ name: 'b' + i, pivot: [BEARD_X[i], -0.4, BEARD_Z[i]], rot: [0.1, 0, BEARD_ROLL[i]], boxes: [[-0.2, 0, -0.2, 0.4, BEARD_LEN[i], 0.4, 48, 16]] });
    }
  }
  return {
    name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
      {
        name: 'body', pivot: [0, 18.5, 0], rot: [0, 0, 0],
        boxes: [[-2, -2.5, -3.5, 4, 5, 7, 0, 0]],
        children: [
          {
            name: 'head', pivot: [0, -1.5, -2.5], rot: [0, 0, 0],
            boxes: [[-2, -4, -2, 4, 4, 4, 24, 0], [-1, -2.5, -3.5, 2, 1.5, 1.5, 44, 0]],
            children: wattles.map(w => ({ ...w, children: [] }))
          },
          { name: 'lw', pivot: [2, -1.5, 0], rot: [0, 0, -0.05], boxes: [[0, -1, -3, 1, 4, 6, 0, 16]], children: [] },
          { name: 'rw', pivot: [-2, -1.5, 0], rot: [0, 0, 0.05], boxes: [[-1, -1, -3, 1, 4, 6, 0, 16]], children: [] },
          { name: 'tail', pivot: [0, 1.5, 3.2], rot: [0.25, 0, 0], boxes: [[-1.5, -0.5, 0, 3, 1, 5, 20, 16]], children: [] }
        ]
      },
      { name: 'll', pivot: [1, 21, -1], rot: [0, 0, 0], boxes: [[-0.5, 0, -0.5, 1, 3, 1, 40, 16]], children: [] },
      { name: 'rl', pivot: [-1, 21, -1], rot: [0, 0, 0], boxes: [[-0.5, 0, -0.5, 1, 3, 1, 40, 16]], children: [] }
    ]
  };
}

function face(cls, fw, fh, u, v, rot, tz) {
  return `<div class="face ${cls}" style="width:${fw * S}px;height:${fh * S}px;`
    + `margin-left:${-fw * S / 2}px;margin-top:${-fh * S / 2}px;`
    + `background-position:${-u * S}px ${-v * S}px;`
    + `transform:${rot} translateZ(${tz * S}px)"></div>`;
}

function boxHtml(cls, [bx, by, bz, w, h, d, u, v]) {
  const cx = (bx + w / 2) * S, cy = (by + h / 2) * S, cz = -(bz + d / 2) * S;
  const f = [];
  f.push(face(cls, w, h, u + d, v + d, 'rotateY(0deg)', d / 2));
  f.push(face(cls, w, h, u + 2 * d + w, v + d, 'rotateY(180deg)', d / 2));
  f.push(face(cls, d, h, u, v + d, 'rotateY(-90deg)', w / 2));
  f.push(face(cls, d, h, u + d + w, v + d, 'rotateY(90deg)', w / 2));
  f.push(face(cls, w, d, u + d, v, 'rotateX(90deg)', h / 2));
  f.push(face(cls, w, d, u + d + w, v, 'rotateX(-90deg)', h / 2));
  return `<div class="boxc" style="transform:translate3d(${cx}px,${cy}px,${cz}px)">${f.join('')}</div>`;
}

function partHtml(cls, p) {
  const [px_, py, pz] = p.pivot;
  const [pitch, yaw, roll] = p.rot;
  const t = `translate3d(${px_ * S}px,${py * S}px,${-pz * S}px) rotateX(${deg(pitch)}deg) rotateY(${deg(yaw)}deg) rotateZ(${deg(-roll)}deg)`;
  return `<div class="part" style="transform:${t}">`
    + p.boxes.map(b => boxHtml(cls, b)).join('')
    + p.children.map(c => partHtml(cls, c)).join('')
    + `</div>`;
}

function cassowaryModel() {
  return {
    name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
      {
        name: 'body', pivot: [0, 13.5, 0], rot: [0, 0, 0],
        boxes: [[-2.5, -2.5, -4, 5, 5, 8, 0, 0], [-1.5, -2, 3.8, 3, 3, 2, 28, 10]],
        children: [
          {
            name: 'neck', pivot: [0, -2, -3], rot: [0.15, 0, 0],
            boxes: [[-1, -5, -1, 2, 5, 2, 28, 0]],
            children: [
              {
                name: 'head', pivot: [0, -5, 0], rot: [0, 0, 0],
                boxes: [[-1.5, -2, -2.5, 3, 2, 3, 40, 0], [-0.75, -4.2, -1.8, 1.5, 2.2, 2, 40, 8], [-0.5, -1.4, -4.2, 1, 1, 1.8, 52, 0]],
                children: []
              }
            ]
          }
        ]
      },
      { name: 'll', pivot: [1.5, 16, 1], rot: [0, 0, 0], boxes: [[-1, 0, -1, 2, 8, 2, 0, 16]], children: [] },
      { name: 'rl', pivot: [-1.5, 16, 1], rot: [0, 0, 0], boxes: [[-1, 0, -1, 2, 8, 2, 0, 16]], children: [] }
    ]
  };
}

function megafaunaModel(twoHorns) {
  const headBoxes = [[-2.5, -4, -5.5, 5, 6, 6, 0, 26]];
  if (twoHorns) {
    headBoxes.push([-2.3, -8, -5, 1.8, 4.5, 1.8, 44, 26], [0.5, -8, -5, 1.8, 4.5, 1.8, 44, 26]);
  } else {
    headBoxes.push([-1, -9, -4.5, 2, 5, 2, 44, 26], [-0.5, -12, -4, 1, 3.5, 1, 54, 26]);
  }
  const legs = [[2.5, -5], [-2.5, -5], [2.5, 5], [-2.5, 5]].map(([lx, lz], i) => (
    { name: 'leg' + i, pivot: [lx, 14, lz], rot: [0, 0, 0], boxes: [[-1.5, 0, -1.5, 3, 10, 3, 24, 26]], children: [] }
  ));
  return {
    name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
      {
        name: 'body', pivot: [0, 13, 0], rot: [0, 0, 0],
        boxes: [[-4, -5, -8, 8, 8, 16, 0, 0]],
        children: [
          { name: 'head', pivot: [0, -1, -8], rot: [0.15, 0, 0], boxes: headBoxes, children: [] },
          { name: 'tail', pivot: [0, -3, 7.8], rot: [0.4, 0, 0], boxes: [[-0.5, 0, 0, 1, 5, 1, 56, 32]], children: [] }
        ]
      },
      ...legs
    ]
  };
}

const QUAD_SPECS = {
  jaguar:    { bw: 4.5, bh: 4.5, bl: 9, leg: 5, lt: 1.5, hs: 3.5, sn: 1.5, tl: 6, ear: 1, uni: false, bison: false, flat: false },
  leopard:   { bw: 4.5, bh: 4.5, bl: 9, leg: 5, lt: 1.5, hs: 3.5, sn: 1.5, tl: 6, ear: 1, uni: false, bison: false, flat: false },
  gray_cat:  { bw: 3.5, bh: 3.5, bl: 7, leg: 4, lt: 1.2, hs: 3, sn: 1.2, tl: 5, ear: 1, uni: false, bison: false, flat: false },
  kalan:     { bw: 3.5, bh: 2.8, bl: 9, leg: 1.8, lt: 1.2, hs: 3, sn: 1, tl: 3.5, ear: 0.5, uni: false, bison: false, flat: true },
  sun_bear:  { bw: 5.5, bh: 5.5, bl: 9, leg: 4.5, lt: 2, hs: 4, sn: 1.6, tl: 1, ear: 1, uni: false, bison: false, flat: false },
  moon_bear: { bw: 6, bh: 6, bl: 10, leg: 5, lt: 2.2, hs: 4.2, sn: 1.6, tl: 1, ear: 1, uni: false, bison: false, flat: false },
  bison:     { bw: 8, bh: 8, bl: 13, leg: 6, lt: 2.5, hs: 5, sn: 2, tl: 4, ear: 1, uni: false, bison: true, flat: false },
  unicorn:   { bw: 5, bh: 5.5, bl: 10, leg: 8, lt: 1.5, hs: 3.5, sn: 2.5, tl: 5, ear: 1.2, uni: true, bison: false, flat: false },
  tarbagan:  { bw: 3.5, bh: 3, bl: 6, leg: 1.5, lt: 1.3, hs: 2.8, sn: 1, tl: 2, ear: 0.5, uni: false, bison: false, flat: false }
};

function quadModel(id) {
  const s = QUAD_SPECS[id];
  const bodyY = 24 - s.leg - s.bh / 2;
  const bodyBoxes = [[-s.bw / 2, -s.bh / 2, -s.bl / 2, s.bw, s.bh, s.bl, 0, 0]];
  if (s.bison) bodyBoxes.push([-s.bw / 2 + 1, -s.bh / 2 - 2, -s.bl / 2 + 1, s.bw - 2, 2, 6, 0, 34]);
  const headBoxes = [
    [-s.hs / 2, -s.hs / 2, -s.hs, s.hs, s.hs, s.hs, 0, 22],
    [-s.hs / 4, 0, -s.hs - s.sn, s.hs / 2, s.hs / 2.8, s.sn, 22, 22]
  ];
  if (s.ear > 0.2) {
    headBoxes.push([-s.hs / 2 + 0.2, -s.hs / 2 - s.ear, -s.hs / 2, 1, s.ear, 0.6, 54, 22]);
    headBoxes.push([s.hs / 2 - 1.2, -s.hs / 2 - s.ear, -s.hs / 2, 1, s.ear, 0.6, 54, 22]);
  }
  if (s.uni) headBoxes.push([-0.4, -s.hs / 2 - 4, -s.hs / 2 - 0.4, 0.8, 4, 0.8, 54, 26]);
  if (s.bison) {
    headBoxes.push([-s.hs / 2 - 1, -s.hs / 2, -s.hs / 2, 1, 2, 1, 54, 26]);
    headBoxes.push([s.hs / 2, -s.hs / 2, -s.hs / 2, 1, 2, 1, 54, 26]);
  }
  const neckBoxes = s.uni ? [[-1.25, -5, -1.5, 2.5, 5.5, 3, 24, 34]] : [];
  const neckPivot = s.uni ? [0, -s.bh / 2 + 1, -s.bl / 2 + 1] : [0, 0, -s.bl / 2 + 0.5];
  const neckRot = s.uni ? [0.5, 0, 0] : [0, 0, 0];
  const headPivot = s.uni ? [0, -4.5, 0] : [0, 0, 0];
  const headRot = s.uni ? [-0.5, 0, 0] : [0, 0, 0];
  const tailBoxes = s.flat
    ? [[-1.5, -0.5, 0, 3, 1, s.tl, 48, 22]]
    : (s.tl > 0.5 ? [[-0.5, 0, -0.5, 1, s.tl, 1, 48, 22]] : []);
  const lx = s.bw / 2 - s.lt / 2, lz = s.bl / 2 - s.lt;
  const legs = [[lx, -lz], [-lx, -lz], [lx, lz], [-lx, lz]].map(([x, z], i) => (
    { name: 'leg' + i, pivot: [x, 24 - s.leg, z], rot: [0, 0, 0], boxes: [[-s.lt / 2, 0, -s.lt / 2, s.lt, s.leg, s.lt, 36, 22]], children: [] }
  ));
  return {
    name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
      {
        name: 'body', pivot: [0, bodyY, 0], rot: [0, 0, 0], boxes: bodyBoxes,
        children: [
          {
            name: 'neck', pivot: neckPivot, rot: neckRot, boxes: neckBoxes,
            children: [{ name: 'head', pivot: headPivot, rot: headRot, boxes: headBoxes, children: [] }]
          },
          { name: 'tail', pivot: [0, -s.bh / 2 + 1, s.bl / 2], rot: [s.flat ? 0.1 : 1.1, 0, 0], boxes: tailBoxes, children: [] }
        ]
      },
      ...legs
    ]
  };
}

function tposeModel() {
  return {
    name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
      {
        name: 'torso', pivot: [0, 20, 0], rot: [0, 0, 0],
        boxes: [
          [-2, -6, -1.5, 4, 6, 3, 0, 0],
          [2, -5.5, -0.75, 5, 1.5, 1.5, 34, 0],
          [6.2, -4, -0.5, 1, 2.5, 1, 0, 12],
          [6.45, -4.8, -0.25, 0.5, 0.8, 0.5, 6, 12],
          [-7, -5.5, -0.75, 5, 1.5, 1.5, 34, 0],
          [-0.5, -1, 1.5, 1, 4.5, 1, 10, 12]
        ],
        children: [
          {
            name: 'head', pivot: [0, -6, 0], rot: [0, 0, 0],
            boxes: [
              [-2, -3.5, -2, 4, 3.5, 4, 16, 0],
              [-1.6, -4.5, -0.5, 1, 1, 0.6, 16, 10],
              [0.6, -4.5, -0.5, 1, 1, 0.6, 16, 10],
              [-0.75, -1.2, -2.8, 1.5, 1, 0.8, 28, 10]
            ], children: []
          }
        ]
      },
      { name: 'll', pivot: [1, 20, 0], rot: [0, 0, 0], boxes: [[-1, 0, -1, 2, 4, 2, 48, 0]], children: [] },
      { name: 'rl', pivot: [-1, 20, 0], rot: [0, 0, 0], boxes: [[-1, 0, -1, 2, 4, 2, 48, 0]], children: [] }
    ]
  };
}

function tallBirdModel(flamingo) {
  if (flamingo) {
    return {
      name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
        {
          name: 'body', pivot: [0, 13, 0], rot: [0, 0, 0],
          boxes: [[-1.5, -1.75, -2.5, 3, 3.5, 5, 0, 0], [-1, -1.2, 2.3, 2, 2, 1.5, 24, 10]],
          children: [
            {
              name: 'neck', pivot: [0, -1, -2], rot: [0.35, 0, 0],
              boxes: [[-0.5, -6, -0.5, 1, 6, 1, 24, 0]],
              children: [{
                name: 'head', pivot: [0, -6, 0], rot: [-0.5, 0, 0],
                boxes: [[-0.75, -1.5, -1.6, 1.5, 1.5, 1.8, 32, 0], [-0.4, -1.2, -3, 0.8, 0.8, 1.5, 44, 0], [-0.4, -0.5, -3, 0.8, 1.4, 0.8, 44, 4]],
                children: []
              }]
            }
          ]
        },
        { name: 'll', pivot: [0.8, 15, 0.5], rot: [0, 0, 0], boxes: [[-0.4, 0, -0.4, 0.8, 9, 0.8, 0, 14]], children: [] },
        { name: 'rl', pivot: [-0.8, 15, 0.5], rot: [0, 0, 0], boxes: [[-0.4, 0, -0.4, 0.8, 9, 0.8, 0, 14]], children: [] }
      ]
    };
  }
  return {
    name: 'root', pivot: [0, 0, 0], rot: [0, 0, 0], boxes: [], children: [
      {
        name: 'body', pivot: [0, 14.5, 0], rot: [0, 0, 0],
        boxes: [[-2, -2.5, -3, 4, 5, 6, 0, 0], [-1.5, -2, 2.8, 3, 3, 1.5, 24, 10]],
        children: [
          {
            name: 'neck', pivot: [0, -2, -2.2], rot: [0.25, 0, 0],
            boxes: [[-0.75, -3, -0.75, 1.5, 3, 1.5, 24, 0]],
            children: [{
              name: 'head', pivot: [0, -3, 0], rot: [-0.25, 0, 0],
              boxes: [[-1.25, -2.5, -1.5, 2.5, 2.5, 3, 32, 0], [-1, -1.8, -4.8, 2, 2, 3.5, 44, 0]],
              children: []
            }]
          }
        ]
      },
      { name: 'll', pivot: [1, 17, 0.5], rot: [0, 0, 0], boxes: [[-0.5, 0, -0.5, 1, 7, 1, 0, 14]], children: [] },
      { name: 'rl', pivot: [-1, 17, 0.5], rot: [0, 0, 0], boxes: [[-0.5, 0, -0.5, 1, 7, 1, 0, 14]], children: [] }
    ]
  };
}

const NAMES = {
  three_wattled: 'Three-wattled', white: 'White', bare_throated: 'Bare-throated',
  bearded: 'Bearded', potoo: 'Great Potoo', piha: 'Piha',
  black_grouse: 'Black Grouse', sage_grouse: 'Sage Grouse',
  cassowary: 'Cassowary', elasmotherium: 'Elasmotherium', arsinoitherium: 'Arsinoitherium',
  jaguar: 'Jaguar', leopard: 'Leopard', gray_cat: 'Gray Cat', kalan: 'Sea Otter',
  tarbagan: 'Tarbagan', sun_bear: 'Sun Bear', moon_bear: 'Moon Bear', bison: 'Bison',
  unicorn: 'Unicorn', tpose_cat: 'T-Pose Cat', flamingo: 'Flamingo', shoebill: 'Shoebill'
};
let css = '', birds = '', labels = '';
function addTex(id) {
  const b64 = fs.readFileSync(path.join(texDir, id + '.png')).toString('base64');
  css += `.t-${id}{background-image:url(data:image/png;base64,${b64})}\n`;
}
function place(group, id, model, x, y, z, labelY) {
  addTex(id);
  birds += `<div class="bird ${group}" data-group="${group}" style="transform:translate3d(${x}px,${y}px,${z}px)">${partHtml('t-' + id, model)}</div>`;
  labels += `<div class="label ${group}" data-group="${group}" style="left:calc(50% + ${x - 52}px);bottom:${labelY}px">${NAMES[id]}</div>`;
}

['three_wattled', 'white', 'bare_throated', 'bearded', 'potoo', 'piha', 'nightjar'].forEach((id, i) =>
  place('g-birds', id, speciesModel(id), (i - 3) * 100, -18.5 * S, 40, 44));
['black_grouse', 'sage_grouse'].forEach((id, i) =>
  place('g-misc', id, speciesModel(id), (i - 2) * 130, -18.5 * S, 60, 44));
place('g-misc', 'tpose_cat', tposeModel(), 60, -18.5 * S, 60, 44);
place('g-misc', 'flamingo', tallBirdModel(true), 190, -16 * S, 40, 44);
place('g-misc', 'shoebill', tallBirdModel(false), 320, -16 * S, 40, 44);
place('g-beasts', 'cassowary', cassowaryModel(), -280, -13 * S, -80, 30);
place('g-beasts', 'elasmotherium', megafaunaModel(false), 0, -13 * S, -80, 30);
place('g-beasts', 'arsinoitherium', megafaunaModel(true), 280, -13 * S, -80, 30);
['jaguar', 'leopard', 'gray_cat', 'kalan', 'tarbagan'].forEach((id, i) =>
  place('g-cats', id, quadModel(id), (i - 2) * 150, -16 * S, 0, 44));
['sun_bear', 'moon_bear', 'bison', 'unicorn'].forEach((id, i) =>
  place('g-big', id, quadModel(id), (i - 1.5) * 180, -14 * S, -40, 44));

const html = `<!doctype html>
<meta charset="utf-8">
<title>Bellbird flock preview</title>
<style>
  html,body{margin:0;height:100%;background:#1c2820;overflow:hidden;font-family:system-ui}
  .face{position:absolute;background-size:${64 * S}px ${64 * S}px;image-rendering:pixelated;
    backface-visibility:hidden;transform-origin:50% 50%}
  ${css}
  .part,.boxc,.bird{position:absolute;transform-style:preserve-3d}
  #scene{position:absolute;inset:0;perspective:1600px;display:flex;align-items:center;justify-content:center}
  #stage{position:relative;transform-style:preserve-3d;width:0;height:0}
  .label{position:fixed;bottom:64px;width:120px;text-align:center;color:#cfe0d4;font-size:13px}
  #hint{position:fixed;left:12px;bottom:10px;color:#9fb8a5;font-size:13px}
  #title{position:fixed;left:12px;top:10px;color:#e6efe8;font-size:15px}
</style>
<div id="scene"><div id="stage">${birds}</div></div>
<div id="title">Bellbirds &amp; Beasts — all nine creatures, in-game geometry</div>
${labels}
<div id="hint">drag to rotate · auto-spins when idle</div>
<script>
  const stage = document.getElementById('stage');
  let yaw = -25, pitchV = -10, auto = true;
  function apply() {
    stage.style.transform = 'rotateX(' + (-pitchV) + 'deg) rotateY(' + yaw + 'deg)';
  }
  window.setView = (y, p) => { auto = false; yaw = y; pitchV = p; apply(); };
  window.setAuto = v => auto = v;
  window.showGroup = g => {
    document.querySelectorAll('[data-group]').forEach(el =>
      el.style.display = (g === 'all' || el.dataset.group === 'g-' + g) ? '' : 'none');
  };
  let dragging = false, lx = 0, ly = 0;
  addEventListener('pointerdown', e => { dragging = true; auto = false; lx = e.clientX; ly = e.clientY; });
  addEventListener('pointerup', () => dragging = false);
  addEventListener('pointermove', e => {
    if (!dragging) return;
    yaw += (e.clientX - lx) * 0.5; pitchV += (e.clientY - ly) * 0.3;
    lx = e.clientX; ly = e.clientY; apply();
  });
  setInterval(() => { if (auto) { yaw += 0.5; apply(); } }, 30);
  apply();
</script>`;

fs.writeFileSync(path.join(__dirname, 'preview.html'), html);
console.log('preview.html written,', (html.length / 1024).toFixed(0), 'KB');
