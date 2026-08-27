package dev.verno.bellbird.client;

import dev.verno.bellbird.CassowaryEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TallBirdRenderer extends MobEntityRenderer<CassowaryEntity, EntityModel<CassowaryEntity>> {
    private final Identifier texture;

    public TallBirdRenderer(EntityRendererFactory.Context ctx, EntityModel<CassowaryEntity> model,
                            float shadow, Identifier texture) {
        super(ctx, model, shadow);
        this.texture = texture;
    }

    @Override
    public Identifier getTexture(CassowaryEntity entity) {
        return this.texture;
    }
}
