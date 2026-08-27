package dev.verno.bellbird.client;

import dev.verno.bellbird.Bellbird;
import dev.verno.bellbird.BellbirdSpecies;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BellbirdClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (BellbirdSpecies s : BellbirdSpecies.values()) {
            EntityModelLayerRegistry.registerModelLayer(BellbirdModel.LAYERS.get(s),
                    () -> BellbirdModel.getTexturedModelData(s));
            Identifier tex = new Identifier("bellbird", "textures/entity/" + s.id + ".png");
            EntityRendererRegistry.register(Bellbird.TYPES.get(s),
                    ctx -> new BellbirdRenderer(ctx, BellbirdModel.LAYERS.get(s), tex));
        }

        EntityModelLayerRegistry.registerModelLayer(CassowaryModel.LAYER,
                CassowaryModel::getTexturedModelData);
        EntityRendererRegistry.register(Bellbird.CASSOWARY, CassowaryRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MegafaunaModel.ELASMO_LAYER,
                () -> MegafaunaModel.getTexturedModelData(false));
        EntityModelLayerRegistry.registerModelLayer(MegafaunaModel.ARSINO_LAYER,
                () -> MegafaunaModel.getTexturedModelData(true));
        EntityRendererRegistry.register(Bellbird.ELASMOTHERIUM,
                ctx -> new MegafaunaRenderer(ctx, MegafaunaModel.ELASMO_LAYER,
                        new Identifier("bellbird", "textures/entity/elasmotherium.png")));
        EntityRendererRegistry.register(Bellbird.ARSINOITHERIUM,
                ctx -> new MegafaunaRenderer(ctx, MegafaunaModel.ARSINO_LAYER,
                        new Identifier("bellbird", "textures/entity/arsinoitherium.png")));
    }
}
