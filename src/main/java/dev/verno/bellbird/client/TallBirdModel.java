package dev.verno.bellbird.client;

import dev.verno.bellbird.CassowaryEntity;
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
public class TallBirdModel extends SinglePartEntityModel<CassowaryEntity> {
    public static final EntityModelLayer FLAMINGO_LAYER =
            new EntityModelLayer(new Identifier("bellbird", "flamingo"), "main");
    public static final EntityModelLayer SHOEBILL_LAYER =
            new EntityModelLayer(new Identifier("bellbird", "shoebill"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public TallBirdModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData(boolean flamingo) {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        if (flamingo) {
            // tiny round body way up on stilts, S-curve neck, kinked beak
            ModelPartData body = root.addChild("body",
                    ModelPartBuilder.create()
                            .uv(0, 0).cuboid(-1.5f, -1.75f, -2.5f, 3.0f, 3.5f, 5.0f)
                            .uv(24, 10).cuboid(-1.0f, -1.2f, 2.3f, 2.0f, 2.0f, 1.5f),
                    ModelTransform.pivot(0.0f, 13.0f, 0.0f));
            ModelPartData neck = body.addChild("neck",
                    ModelPartBuilder.create()
                            .uv(24, 0).cuboid(-0.5f, -6.0f, -0.5f, 1.0f, 6.0f, 1.0f),
                    ModelTransform.of(0.0f, -1.0f, -2.0f, 0.35f, 0.0f, 0.0f));
            neck.addChild("head",
                    ModelPartBuilder.create()
                            .uv(32, 0).cuboid(-0.75f, -1.5f, -1.6f, 1.5f, 1.5f, 1.8f)
                            .uv(44, 0).cuboid(-0.4f, -1.2f, -3.0f, 0.8f, 0.8f, 1.5f)
                            .uv(44, 4).cuboid(-0.4f, -0.5f, -3.0f, 0.8f, 1.4f, 0.8f),
                    ModelTransform.of(0.0f, -6.0f, 0.0f, -0.5f, 0.0f, 0.0f));
            root.addChild("left_leg",
                    ModelPartBuilder.create()
                            .uv(0, 14).cuboid(-0.4f, 0.0f, -0.4f, 0.8f, 9.0f, 0.8f),
                    ModelTransform.pivot(0.8f, 15.0f, 0.5f));
            root.addChild("right_leg",
                    ModelPartBuilder.create()
                            .uv(0, 14).mirrored().cuboid(-0.4f, 0.0f, -0.4f, 0.8f, 9.0f, 0.8f),
                    ModelTransform.pivot(-0.8f, 15.0f, 0.5f));
        } else {
            // shoebill: hunched body, thick neck, enormous shoe of a beak
            ModelPartData body = root.addChild("body",
                    ModelPartBuilder.create()
                            .uv(0, 0).cuboid(-2.0f, -2.5f, -3.0f, 4.0f, 5.0f, 6.0f)
                            .uv(24, 10).cuboid(-1.5f, -2.0f, 2.8f, 3.0f, 3.0f, 1.5f),
                    ModelTransform.pivot(0.0f, 14.5f, 0.0f));
            ModelPartData neck = body.addChild("neck",
                    ModelPartBuilder.create()
                            .uv(24, 0).cuboid(-0.75f, -3.0f, -0.75f, 1.5f, 3.0f, 1.5f),
                    ModelTransform.of(0.0f, -2.0f, -2.2f, 0.25f, 0.0f, 0.0f));
            neck.addChild("head",
                    ModelPartBuilder.create()
                            .uv(32, 0).cuboid(-1.25f, -2.5f, -1.5f, 2.5f, 2.5f, 3.0f)
                            .uv(44, 0).cuboid(-1.0f, -1.8f, -4.8f, 2.0f, 2.0f, 3.5f),
                    ModelTransform.of(0.0f, -3.0f, 0.0f, -0.25f, 0.0f, 0.0f));
            root.addChild("left_leg",
                    ModelPartBuilder.create()
                            .uv(0, 14).cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 7.0f, 1.0f),
                    ModelTransform.pivot(1.0f, 17.0f, 0.5f));
            root.addChild("right_leg",
                    ModelPartBuilder.create()
                            .uv(0, 14).mirrored().cuboid(-0.5f, 0.0f, -0.5f, 1.0f, 7.0f, 1.0f),
                    ModelTransform.pivot(-1.0f, 17.0f, 0.5f));
        }

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(CassowaryEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        this.neck.yaw = headYaw * ((float) Math.PI / 180f) * 0.7f;
        this.head.pitch = -0.4f + headPitch * ((float) Math.PI / 180f) * 0.5f
                + MathHelper.cos(animationProgress * 0.06f) * 0.05f;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.2f * limbDistance;
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.2f * limbDistance;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
