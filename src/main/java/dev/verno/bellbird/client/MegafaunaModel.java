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

@Environment(EnvType.CLIENT)
public class MegafaunaModel extends SinglePartEntityModel<MegafaunaEntity> {
    public static final EntityModelLayer ELASMO_LAYER =
            new EntityModelLayer(new Identifier("bellbird", "elasmotherium"), "main");
    public static final EntityModelLayer ARSINO_LAYER =
            new EntityModelLayer(new Identifier("bellbird", "arsinoitherium"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart[] legs = new ModelPart[4];

    public MegafaunaModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.tail = this.body.getChild("tail");
        for (int i = 0; i < 4; i++) {
            this.legs[i] = root.getChild("leg_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData(boolean twoHorns) {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0f, -5.0f, -8.0f, 8.0f, 8.0f, 16.0f),
                ModelTransform.pivot(0.0f, 13.0f, 0.0f));

        ModelPartBuilder headB = ModelPartBuilder.create()
                .uv(0, 26).cuboid(-2.5f, -4.0f, -5.5f, 5.0f, 6.0f, 6.0f);
        if (twoHorns) {
            headB.uv(44, 26).cuboid(-2.3f, -8.0f, -5.0f, 1.8f, 4.5f, 1.8f)
                 .uv(44, 26).cuboid(0.5f, -8.0f, -5.0f, 1.8f, 4.5f, 1.8f);
        } else {
            headB.uv(44, 26).cuboid(-1.0f, -9.0f, -4.5f, 2.0f, 5.0f, 2.0f)
                 .uv(54, 26).cuboid(-0.5f, -12.0f, -4.0f, 1.0f, 3.5f, 1.0f);
        }
        body.addChild("head", headB, ModelTransform.of(0.0f, -1.0f, -8.0f, 0.15f, 0.0f, 0.0f));

        body.addChild("tail",
                ModelPartBuilder.create()
                        .uv(56, 32).cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(0.0f, -3.0f, 7.8f, 0.4f, 0.0f, 0.0f));

        float[][] legPos = {{2.5f, -5.0f}, {-2.5f, -5.0f}, {2.5f, 5.0f}, {-2.5f, 5.0f}};
        for (int i = 0; i < 4; i++) {
            root.addChild("leg_" + i,
                    ModelPartBuilder.create()
                            .uv(24, 26).cuboid(-1.5f, 0.0f, -1.5f, 3.0f, 10.0f, 3.0f),
                    ModelTransform.pivot(legPos[i][0], 14.0f, legPos[i][1]));
        }

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(MegafaunaEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        this.head.yaw = headYaw * ((float) Math.PI / 180f) * 0.5f;
        this.head.pitch = 0.15f + headPitch * ((float) Math.PI / 180f) * 0.4f;
        float swing = limbAngle * 0.5f;
        this.legs[0].pitch = MathHelper.cos(swing) * 0.9f * limbDistance;
        this.legs[1].pitch = MathHelper.cos(swing + (float) Math.PI) * 0.9f * limbDistance;
        this.legs[2].pitch = MathHelper.cos(swing + (float) Math.PI) * 0.9f * limbDistance;
        this.legs[3].pitch = MathHelper.cos(swing) * 0.9f * limbDistance;
        this.tail.pitch = 0.4f + MathHelper.cos(animationProgress * 0.07f) * 0.08f;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
