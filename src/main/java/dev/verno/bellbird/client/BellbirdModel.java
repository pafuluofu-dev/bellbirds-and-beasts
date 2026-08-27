package dev.verno.bellbird.client;

import dev.verno.bellbird.BellbirdEntity;
import dev.verno.bellbird.BellbirdSpecies;
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

import java.util.EnumMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BellbirdModel extends SinglePartEntityModel<BellbirdEntity> {

    public static final Map<BellbirdSpecies, EntityModelLayer> LAYERS = new EnumMap<>(BellbirdSpecies.class);
    static {
        for (BellbirdSpecies s : BellbirdSpecies.values()) {
            LAYERS.put(s, new EntityModelLayer(new Identifier("bellbird", s.id), "main"));
        }
    }

    private static final float[] BEARD_X = {-0.8f, -0.4f, 0.0f, 0.4f, 0.8f};
    private static final float[] BEARD_Z = {-2.4f, -2.7f, -2.9f, -2.7f, -2.4f};
    private static final float[] BEARD_LEN = {1.6f, 2.3f, 2.9f, 2.3f, 1.6f};
    private static final float[] BEARD_ROLL = {-0.25f, -0.12f, 0.0f, 0.12f, 0.25f};

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart tail;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart wattleCenter;
    private final ModelPart wattleLeft;
    private final ModelPart wattleRight;
    private final ModelPart[] beard = new ModelPart[5];

    public BellbirdModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.leftWing = this.body.getChild("left_wing");
        this.rightWing = this.body.getChild("right_wing");
        this.tail = this.body.getChild("tail");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        this.wattleCenter = this.head.getChild("wattle_center");
        this.wattleLeft = this.head.getChild("wattle_left");
        this.wattleRight = this.head.getChild("wattle_right");
        for (int i = 0; i < 5; i++) {
            this.beard[i] = this.head.getChild("beard_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData(BellbirdSpecies s) {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.0f, -2.5f, -3.5f, 4.0f, 5.0f, 7.0f),
                ModelTransform.pivot(0.0f, 18.5f, 0.0f));

        ModelPartData head = body.addChild("head",
                ModelPartBuilder.create()
                        .uv(24, 0).cuboid(-2.0f, -4.0f, -2.0f, 4.0f, 4.0f, 4.0f)
                        .uv(44, 0).cuboid(-1.0f, -2.5f, -3.5f, 2.0f, 1.5f, 1.5f),
                ModelTransform.pivot(0.0f, -1.5f, -2.5f));

        // wattles: presence and shape depend on species; parts always exist
        ModelPartBuilder wc = ModelPartBuilder.create();
        ModelTransform wct = ModelTransform.pivot(0.0f, -2.2f, -3.3f);
        if (s == BellbirdSpecies.THREE_WATTLED) {
            wc.uv(48, 16).cuboid(-0.3f, 0.0f, -0.3f, 0.6f, 5.0f, 0.4f);
        } else if (s == BellbirdSpecies.WHITE) {
            // rooted at the top edge of the beak, draping forward over it
            wc.uv(48, 16).cuboid(-0.35f, 0.0f, -0.35f, 0.7f, 6.5f, 0.4f);
            wct = ModelTransform.of(0.0f, -2.5f, -3.5f, 0.3f, 0.0f, 0.0f);
        }
        head.addChild("wattle_center", wc, wct);

        ModelPartBuilder wl = ModelPartBuilder.create();
        ModelPartBuilder wr = ModelPartBuilder.create();
        if (s == BellbirdSpecies.THREE_WATTLED) {
            wl.uv(52, 16).cuboid(-0.3f, 0.0f, -0.3f, 0.6f, 4.0f, 0.4f);
            wr.uv(56, 16).cuboid(-0.3f, 0.0f, -0.3f, 0.6f, 4.0f, 0.4f);
        }
        head.addChild("wattle_left", wl, ModelTransform.of(0.8f, -1.9f, -3.1f, 0.0f, 0.0f, 0.25f));
        head.addChild("wattle_right", wr, ModelTransform.of(-0.8f, -1.9f, -3.1f, 0.0f, 0.0f, -0.25f));

        for (int i = 0; i < 5; i++) {
            ModelPartBuilder b = ModelPartBuilder.create();
            if (s == BellbirdSpecies.BEARDED) {
                b.uv(48, 16).cuboid(-0.2f, 0.0f, -0.2f, 0.4f, BEARD_LEN[i], 0.4f);
            }
            head.addChild("beard_" + i, b,
                    ModelTransform.of(BEARD_X[i], -0.4f, BEARD_Z[i], 0.0f, 0.0f, BEARD_ROLL[i]));
        }

        body.addChild("left_wing",
                ModelPartBuilder.create()
                        .uv(0, 16).cuboid(0.0f, -1.0f, -3.0f, 1.0f, 4.0f, 6.0f),
                ModelTransform.pivot(2.0f, -1.5f, 0.0f));
        body.addChild("right_wing",
                ModelPartBuilder.create()
                        .uv(0, 16).mirrored().cuboid(-1.0f, -1.0f, -3.0f, 1.0f, 4.0f, 6.0f),
                ModelTransform.pivot(-2.0f, -1.5f, 0.0f));

        body.addChild("tail",
                ModelPartBuilder.create()
                        .uv(20, 16).cuboid(-1.5f, -0.5f, 0.0f, 3.0f, 1.0f, 5.0f),
                ModelTransform.of(0.0f, 1.5f, 3.2f, 0.25f, 0.0f, 0.0f));

        // legs: 3 tall, pivot raised into the body so they connect (body bottom is y=21)
        root.addChild("left_leg",
                ModelPartBuilder.create()
                        .uv(40, 16).cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 3.0f, 1.0f),
                ModelTransform.pivot(1.0f, 21.0f, -1.0f));
        root.addChild("right_leg",
                ModelPartBuilder.create()
                        .uv(40, 16).mirrored().cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 3.0f, 1.0f),
                ModelTransform.pivot(-1.0f, 21.0f, -1.0f));

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(BellbirdEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        this.head.yaw = headYaw * ((float) Math.PI / 180f);
        this.head.pitch = headPitch * ((float) Math.PI / 180f);

        boolean flying = !entity.isOnGround();
        if (flying) {
            float flap = MathHelper.cos(animationProgress * 1.6f) * 0.8f;
            this.leftWing.roll = -0.5f - flap;
            this.rightWing.roll = 0.5f + flap;
            this.leftLeg.pitch = 0.6f;
            this.rightLeg.pitch = 0.6f;
            this.body.pitch = 0.35f;
        } else {
            this.leftWing.roll = -0.05f;
            this.rightWing.roll = 0.05f;
            this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.0f * limbDistance;
            this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.0f * limbDistance;
            this.body.pitch = 0.0f;
        }

        float sway = MathHelper.cos(animationProgress * 0.12f) * 0.08f + (flying ? 0.35f : 0.0f);
        this.wattleCenter.pitch = 0.18f + sway;
        this.wattleLeft.pitch = 0.12f + sway * 0.8f;
        this.wattleRight.pitch = 0.12f + sway * 0.8f;
        this.wattleLeft.roll = 0.25f + MathHelper.cos(animationProgress * 0.17f) * 0.05f;
        this.wattleRight.roll = -0.25f - MathHelper.cos(animationProgress * 0.17f) * 0.05f;
        for (int i = 0; i < 5; i++) {
            this.beard[i].pitch = 0.1f + sway * 0.7f;
            this.beard[i].roll = BEARD_ROLL[i] + MathHelper.cos(animationProgress * 0.15f + i) * 0.04f;
        }

        this.tail.pitch = 0.25f + MathHelper.cos(animationProgress * 0.1f) * 0.05f;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
