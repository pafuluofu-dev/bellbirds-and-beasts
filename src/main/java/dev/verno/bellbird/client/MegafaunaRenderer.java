package dev.verno.bellbird.client;

import dev.verno.bellbird.MegafaunaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MegafaunaRenderer extends MobEntityRenderer<MegafaunaEntity, MegafaunaModel> {
    private final Identifier texture;

    public MegafaunaRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, Identifier texture) {
        super(ctx, new MegafaunaModel(ctx.getPart(layer)), 0.9f);
        this.texture = texture;
    }

    @Override
    public Identifier getTexture(MegafaunaEntity entity) {
        return this.texture;
    }
}
