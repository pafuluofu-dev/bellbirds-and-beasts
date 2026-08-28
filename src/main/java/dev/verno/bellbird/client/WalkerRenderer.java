package dev.verno.bellbird.client;

import dev.verno.bellbird.MegafaunaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WalkerRenderer extends MobEntityRenderer<MegafaunaEntity, EntityModel<MegafaunaEntity>> {
    private final Identifier texture;
    private final float scale;

    public WalkerRenderer(EntityRendererFactory.Context ctx, EntityModel<MegafaunaEntity> model,
                          float shadow, Identifier texture, float scale) {
        super(ctx, model, shadow * scale);
        this.texture = texture;
        this.scale = scale;
    }

    @Override
    protected void scale(MegafaunaEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTexture(MegafaunaEntity entity) {
        return this.texture;
    }
}
