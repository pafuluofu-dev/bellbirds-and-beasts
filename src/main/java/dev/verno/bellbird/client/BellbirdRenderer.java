package dev.verno.bellbird.client;

import dev.verno.bellbird.BellbirdEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BellbirdRenderer extends MobEntityRenderer<BellbirdEntity, BellbirdModel> {
    private final Identifier texture;

    public BellbirdRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, Identifier texture) {
        super(ctx, new BellbirdModel(ctx.getPart(layer)), 0.25f);
        this.texture = texture;
    }

    @Override
    public Identifier getTexture(BellbirdEntity entity) {
        return this.texture;
    }
}
