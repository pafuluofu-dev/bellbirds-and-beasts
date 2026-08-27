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

const BIRD_IDS = ['three_wattled', 'white', 'bare_throated', 'bearded', 'potoo', 'piha'];
const NAMES = {
  three_wattled: 'Three-wattled', white: 'White', bare_throated: 'Bare-throated',
  bearded: 'Bearded', potoo: 'Great Potoo', piha: 'Piha',
  cassowary: 'Cassowary', elasmotherium: 'Elasmotherium', arsinoitherium: 'Arsinoitherium'
};
let css = '', birds = '', labels = '';
function addTex(id) {
  const b64 = fs.readFileSync(path.join(texDir, id + '.png')).toString('base64');
  css += `.t-${id}{background-image:url(data:image/png;base64,${b64})}\n`;
}
BIRD_IDS.forEach((id, i) => {
  addTex(id);
  const x = (i - 2.5) * 108;
  birds += `<div class="bird g-bird" style="transform:translate3d(${x}px,${-18.5 * S}px,40px)">${partHtml('t-' + id, speciesModel(id))}</div>`;
  labels += `<div class="label g-bird" style="left:calc(50% + ${x - 52}px);bottom:44px">${NAMES[id]}</div>`;
});
const BEASTS = [
  ['cassowary', cassowaryModel(), -280],
  ['elasmotherium', megafaunaModel(false), 0],
  ['arsinoitherium', megafaunaModel(true), 280]
];
for (const [id, model, x] of BEASTS) {
  addTex(id);
  birds += `<div class="bird g-beast" style="transform:translate3d(${x}px,${-13 * S}px,-80px)">${partHtml('t-' + id, model)}</div>`;
  labels += `<div class="label g-beast" style="left:calc(50% + ${x - 60}px);bottom:30px">${NAMES[id]}</div>`;
}

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
    document.querySelectorAll('.g-bird').forEach(el => el.style.display = (g === 'beasts') ? 'none' : '');
    document.querySelectorAll('.g-beast').forEach(el => el.style.display = (g === 'birds') ? 'none' : '');
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
