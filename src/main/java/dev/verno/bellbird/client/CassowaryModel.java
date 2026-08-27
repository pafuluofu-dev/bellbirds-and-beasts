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
public class CassowaryModel extends SinglePartEntityModel<CassowaryEntity> {
    public static final EntityModelLayer LAYER =
            new EntityModelLayer(new Identifier("bellbird", "cassowary"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public CassowaryModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild("body",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-2.5f, -2.5f, -4.0f, 5.0f, 5.0f, 8.0f)
                        .uv(28, 10).cuboid(-1.5f, -2.0f, 3.8f, 3.0f, 3.0f, 2.0f),
                ModelTransform.pivot(0.0f, 13.5f, 0.0f));

        ModelPartData neck = body.addChild("neck",
                ModelPartBuilder.create()
                        .uv(28, 0).cuboid(-1.0f, -5.0f, -1.0f, 2.0f, 5.0f, 2.0f),
                ModelTransform.of(0.0f, -2.0f, -3.0f, 0.15f, 0.0f, 0.0f));

        neck.addChild("head",
                ModelPartBuilder.create()
                        .uv(40, 0).cuboid(-1.5f, -2.0f, -2.5f, 3.0f, 2.0f, 3.0f)
                        .uv(40, 8).cuboid(-0.75f, -4.2f, -1.8f, 1.5f, 2.2f, 2.0f)
                        .uv(52, 0).cuboid(-0.5f, -1.4f, -4.2f, 1.0f, 1.0f, 1.8f),
                ModelTransform.pivot(0.0f, -5.0f, 0.0f));

        root.addChild("left_leg",
                ModelPartBuilder.create()
                        .uv(0, 16).cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f),
                ModelTransform.pivot(1.5f, 16.0f, 1.0f));
        root.addChild("right_leg",
                ModelPartBuilder.create()
                        .uv(0, 16).mirrored().cuboid(-1.0f, 0.0f, -1.0f, 2.0f, 8.0f, 2.0f),
                ModelTransform.pivot(-1.5f, 16.0f, 1.0f));

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(CassowaryEntity entity, float limbAngle, float limbDistance,
                          float animationProgress, float headYaw, float headPitch) {
        this.neck.yaw = headYaw * ((float) Math.PI / 180f) * 0.6f;
        this.head.pitch = headPitch * ((float) Math.PI / 180f) * 0.6f;
        this.neck.pitch = 0.15f + MathHelper.cos(animationProgress * 0.08f) * 0.03f;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.2f * limbDistance;
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.2f * limbDistance;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
