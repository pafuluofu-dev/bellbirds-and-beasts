package dev.verno.bellbird.client;

import dev.verno.bellbird.CassowaryEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CassowaryRenderer extends MobEntityRenderer<CassowaryEntity, CassowaryModel> {
    private static final Identifier TEXTURE = new Identifier("bellbird", "textures/entity/cassowary.png");

    public CassowaryRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CassowaryModel(ctx.getPart(CassowaryModel.LAYER)), 0.45f);
    }

    @Override
    public Identifier getTexture(CassowaryEntity entity) {
        return TEXTURE;
    }
}
