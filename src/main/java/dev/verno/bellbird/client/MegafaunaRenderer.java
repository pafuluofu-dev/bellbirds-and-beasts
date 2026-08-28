package dev.verno.bellbird.client;

import dev.verno.bellbird.MegafaunaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MegafaunaRenderer extends MobEntityRenderer<MegafaunaEntity, MegafaunaModel> {
    private final Identifier texture;
    private final float scale;

    public MegafaunaRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, Identifier texture, float scale) {
        super(ctx, new MegafaunaModel(ctx.getPart(layer)), 0.9f * scale);
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
