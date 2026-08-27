package dev.verno.bellbird.client;

import dev.verno.bellbird.MegafaunaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class QuadrupedModel extends SinglePartEntityModel<MegafaunaEntity> {

    /** Proportion spec for one species; units are model pixels (1/16 block). */
    public record Spec(float bodyW, float bodyH, float bodyL, float legLen, float legTh,
                       float headS, float snoutL, float tailLen, float earH,
                       boolean unicornHorn, boolean bisonBits, boolean flatTail) {}

    public static final Map<String, Spec> SPECS = new HashMap<>();
    public static final Map<String, EntityModelLayer> LAYERS = new HashMap<>();
    static {
        SPECS.put("jaguar",    new Spec(4.5f, 4.5f, 9f, 5f, 1.5f, 3.5f, 1.5f, 6f, 1f, false, false, false));
        SPECS.put("leopard",   new Spec(4.5f, 4.5f, 9f, 5f, 1.5f, 3.5f, 1.5f, 6f, 1f, false, false, false));
        SPECS.put("gray_cat",  new Spec(3.5f, 3.5f, 7f, 4f, 1.2f, 3f, 1.2f, 5f, 1f, false, false, false));
        SPECS.put("kalan",     new Spec(3.5f, 2.8f, 9f, 1.8f, 1.2f, 3f, 1f, 3.5f, 0.5f, false, false, true));
        SPECS.put("sun_bear",  new Spec(5.5f, 5.5f, 9f, 4.5f, 2f, 4f, 1.6f, 1f, 1f, false, false, false));
        SPECS.put("moon_bear", new Spec(6f, 6f, 10f, 5f, 2.2f, 4.2f, 1.6f, 1f, 1f, false, false, false));
        SPECS.put("bison",     new Spec(8f, 8f, 13f, 6f, 2.5f, 5f, 2f, 4f, 1f, false, true, false));
        SPECS.put("unicorn",   new Spec(5f, 5.5f, 10f, 8f, 1.5f, 3.5f, 2.5f, 5f, 1.2f, true, false, false));
        SPECS.put("tarbagan",  new Spec(3.5f, 3f, 6f, 1.5f, 1.3f, 2.8f, 1f, 2f, 0.5f, false, false, false));
        for (String id : SPECS.keySet()) {
            LAYERS.put(id, new EntityModelLayer(new Identifier("bellbird", id), "main"));
        }
    }

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart[] legs = new ModelPart[4];

    public QuadrupedModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.tail = this.body.getChild("tail");
        for (int i = 0; i < 4; i++) this.legs[i] = root.getChild("leg_" + i);
    }

    public static TexturedModelData getTexturedModelData(Spec s) {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        float bodyCenterY = 24f - s.legLen() - s.bodyH() / 2f;
        ModelPartBuilder bodyB = ModelPartBuilder.create()
                .uv(0, 0).cuboid(-s.bodyW() / 2f, -s.bodyH() / 2f, -s.bodyL() / 2f,
                        s.bodyW(), s.bodyH(), s.bodyL());
        if (s.bisonBits()) {
            bodyB.uv(0, 34).cuboid(-s.bodyW() / 2f + 1f, -s.bodyH() / 2f - 2f, -s.bodyL() / 2f + 1f,
                    s.bodyW() - 2f, 2f, 6f); // hump
        }
        ModelPartData body = root.addChild("body", bodyB,
                ModelTransform.pivot(0.0f, bodyCenterY, 0.0f));

        // neck: real box only for the unicorn, empty pivot otherwise
        ModelPartBuilder neckB = ModelPartBuilder.create();
        ModelTransform neckT = ModelTransform.pivot(0.0f, 0.0f, -s.bodyL() / 2f + 0.5f);
        if (s.unicornHorn()) {
            neckB.uv(24, 34).cuboid(-1.25f, -5.0f, -1.5f, 2.5f, 5.5f, 3.0f);
            neckT = ModelTransform.of(0.0f, -s.bodyH() / 2f + 1f, -s.bodyL() / 2f + 1f, 0.5f, 0.0f, 0.0f);
        }
        ModelPartData neck = body.addChild("neck", neckB, neckT);

        float hs = s.headS();
        ModelPartBuilder headB = ModelPartBuilder.create()
                .uv(0, 22).cuboid(-hs / 2f, -hs / 2f, -hs, hs, hs, hs)
                .uv(22, 22).cuboid(-hs / 4f, 0.0f, -hs - s.snoutL(), hs / 2f, hs / 2.8f, s.snoutL());
        if (s.earH() > 0.2f) {
            headB.uv(54, 22).cuboid(-hs / 2f + 0.2f, -hs / 2f - s.earH(), -hs / 2f, 1.0f, s.earH(), 0.6f)
                 .uv(54, 22).cuboid(hs / 2f - 1.2f, -hs / 2f - s.earH(), -hs / 2f, 1.0f, s.earH(), 0.6f);
        }
        if (s.unicornHorn()) {
            headB.uv(54, 26).cuboid(-0.4f, -hs / 2f - 4.0f, -hs / 2f - 0.4f, 0.8f, 4.0f, 0.8f);
        }
        if (s.bisonBits()) {
            headB.uv(54, 26).cuboid(-hs / 2f - 1.0f, -hs / 2f, -hs / 2f, 1.0f, 2.0f, 1.0f)
                 .uv(54, 26).cuboid(hs / 2f, -hs / 2f, -hs / 2f, 1.0f, 2.0f, 1.0f);
        }
        ModelTransform headT = s.unicornHorn()
                ? ModelTransform.of(0.0f, -4.5f, 0.0f, -0.5f, 0.0f, 0.0f)
                : ModelTransform.pivot(0.0f, 0.0f, 0.0f);
        neck.addChild("head", headB, headT);

        ModelPartBuilder tailB = ModelPartBuilder.create();
        if (s.flatTail()) {
            tailB.uv(48, 22).cuboid(-1.5f, -0.5f, 0.0f, 3.0f, 1.0f, s.tailLen());
        } else if (s.tailLen() > 0.5f) {
            tailB.uv(48, 22).cuboid(-0.5f, 0.0f, -0.5f, 1.0f, s.tailLen(), 1.0f);
        }
        body.addChild("tail", tailB,
                ModelTransform.of(0.0f, -s.bodyH() / 2f + 1f, s.bodyL() / 2f, s.flatTail() ? 0.1f : 1.1f, 0.0f, 0.0f));

        float lx = s.bodyW() / 2f - s.legTh() / 2f;
        float lz = s.bodyL() / 2f - s.legTh();
        float[][] legPos = {{lx, -lz}, {-lx, -lz}, {lx, lz}, {-lx, lz}};
        for (int i = 0; i < 4; i++) {
            root.addChild("leg_" + i,
                    ModelPartBuilder.create()
                            .uv(36, 22).cuboid(-s.legTh() / 2f, 0.0f, -s.legTh() / 2f,
                                    s.legTh(), s.legLen(), s.legTh()),
                    ModelTransform.pivot(legPos[i][0], 24f - s.legLen(), legPos[i][1]));
        }

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(MegafaunaEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        this.neck.yaw = headYaw * ((float) Math.PI / 180f) * 0.6f;
        this.head.pitch = headPitch * ((float) Math.PI / 180f) * 0.5f;
        float swing = limbAngle * 0.6662f;
        this.legs[0].pitch = MathHelper.cos(swing) * 1.1f * limbDistance;
        this.legs[1].pitch = MathHelper.cos(swing + (float) Math.PI) * 1.1f * limbDistance;
        this.legs[2].pitch = MathHelper.cos(swing + (float) Math.PI) * 1.1f * limbDistance;
        this.legs[3].pitch = MathHelper.cos(swing) * 1.1f * limbDistance;
        this.tail.roll = MathHelper.cos(animationProgress * 0.15f) * 0.15f;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
