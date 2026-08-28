package dev.verno.bellbird.client;

import dev.verno.bellbird.BellbirdEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BellbirdRenderer extends MobEntityRenderer<BellbirdEntity, BellbirdModel> {
    private final Identifier texture;
    private final float scale;

    public BellbirdRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, Identifier texture, float scale) {
        super(ctx, new BellbirdModel(ctx.getPart(layer)), 0.25f * scale);
        this.texture = texture;
        this.scale = scale;
    }

    @Override
    protected void scale(BellbirdEntity entity, MatrixStack matrices, float amount) {
        matrices.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTexture(BellbirdEntity entity) {
        return this.texture;
    }
}
