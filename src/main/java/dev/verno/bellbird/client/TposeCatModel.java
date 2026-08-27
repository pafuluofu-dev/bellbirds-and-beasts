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
public class TposeCatModel extends SinglePartEntityModel<MegafaunaEntity> {
    public static final EntityModelLayer LAYER =
            new EntityModelLayer(new Identifier("bellbird", "tpose_cat"), "main");

    private final ModelPart root;
    private final ModelPart torso;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public TposeCatModel(ModelPart root) {
        this.root = root;
        this.torso = root.getChild("torso");
        this.head = this.torso.getChild("head");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // upright torso; arms are part of the torso so the T never wavers
        ModelPartData torso = root.addChild("torso",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.0f, -6.0f, -1.5f, 4.0f, 6.0f, 3.0f)
                        // left arm straight out, with the sacred beer bottle at its end
                        .uv(34, 0).cuboid(2.0f, -5.5f, -0.75f, 5.0f, 1.5f, 1.5f)
                        .uv(0, 12).cuboid(6.2f, -4.0f, -0.5f, 1.0f, 2.5f, 1.0f)
                        .uv(6, 12).cuboid(6.45f, -4.8f, -0.25f, 0.5f, 0.8f, 0.5f)
                        // right arm straight out
                        .uv(34, 0).mirrored().cuboid(-7.0f, -5.5f, -0.75f, 5.0f, 1.5f, 1.5f)
                        // tail
                        .uv(10, 12).cuboid(-0.5f, -1.0f, 1.5f, 1.0f, 4.5f, 1.0f),
                ModelTransform.pivot(0.0f, 20.0f, 0.0f));

        torso.addChild("head",
                ModelPartBuilder.create()
                        .uv(16, 0).cuboid(-2.0f, -3.5f, -2.0f, 4.0f, 3.5f, 4.0f)
                        .uv(16, 10).cuboid(-1.6f, -4.5f, -0.5f, 1.0f, 1.0f, 0.6f)
                        .uv(16, 10).cuboid(0.6f, -4.5f, -0.5f, 1.0f, 1.0f, 0.6f)
                        .uv(28, 10).cuboid(-0.75f, -1.2f, -2.8f, 1.5f, 1.0f, 0.8f),
                ModelTransform.pivot(0.0f, -6.0f, 0.0f));

        root.addChild("left_leg",
                ModelPartBuilder.create()
                        .uv(48, 0).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.pivot(1.0f, 20.0f, 0.0f));
        root.addChild("right_leg",
                ModelPartBuilder.create()
                        .uv(48, 0).mirrored().cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                ModelTransform.pivot(-1.0f, 20.0f, 0.0f));

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(MegafaunaEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        // the arms do not animate. that is the entire point.
        this.head.yaw = headYaw * ((float) Math.PI / 180f);
        this.head.pitch = headPitch * ((float) Math.PI / 180f) * 0.5f;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.2f * limbDistance;
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.2f * limbDistance;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
