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

    private static Identifier tex(String id) {
        return new Identifier("bellbird", "textures/entity/" + id + ".png");
    }

    @Override
    public void onInitializeClient() {
        for (BellbirdSpecies s : BellbirdSpecies.values()) {
            EntityModelLayerRegistry.registerModelLayer(BellbirdModel.LAYERS.get(s),
                    () -> BellbirdModel.getTexturedModelData(s));
            Identifier t = tex(s.id);
            float birdScale = Bellbird.scaleOf(s.id);
            EntityRendererRegistry.register(Bellbird.TYPES.get(s),
                    ctx -> new BellbirdRenderer(ctx, BellbirdModel.LAYERS.get(s), t, birdScale));
        }

        EntityModelLayerRegistry.registerModelLayer(CassowaryModel.LAYER,
                CassowaryModel::getTexturedModelData);
        EntityRendererRegistry.register(Bellbird.CASSOWARY, CassowaryRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MegafaunaModel.ELASMO_LAYER,
                () -> MegafaunaModel.getTexturedModelData(false));
        EntityModelLayerRegistry.registerModelLayer(MegafaunaModel.ARSINO_LAYER,
                () -> MegafaunaModel.getTexturedModelData(true));
        EntityRendererRegistry.register(Bellbird.ELASMOTHERIUM,
                ctx -> new MegafaunaRenderer(ctx, MegafaunaModel.ELASMO_LAYER, tex("elasmotherium"),
                        Bellbird.scaleOf("elasmotherium")));
        EntityRendererRegistry.register(Bellbird.ARSINOITHERIUM,
                ctx -> new MegafaunaRenderer(ctx, MegafaunaModel.ARSINO_LAYER, tex("arsinoitherium"),
                        Bellbird.scaleOf("arsinoitherium")));

        // parameterized quadrupeds
        for (var entry : QuadrupedModel.SPECS.entrySet()) {
            String id = entry.getKey();
            QuadrupedModel.Spec spec = entry.getValue();
            EntityModelLayerRegistry.registerModelLayer(QuadrupedModel.LAYERS.get(id),
                    () -> QuadrupedModel.getTexturedModelData(spec));
            Identifier t = tex(id);
            float shadow = spec.bodyW() / 10f;
            float walkerScale = Bellbird.scaleOf(id);
            EntityRendererRegistry.register(Bellbird.WALKER_TYPES.get(id),
                    ctx -> new WalkerRenderer(ctx, new QuadrupedModel(ctx.getPart(QuadrupedModel.LAYERS.get(id))), shadow, t, walkerScale));
        }

        // the sacred t-pose cat
        EntityModelLayerRegistry.registerModelLayer(TposeCatModel.LAYER,
                TposeCatModel::getTexturedModelData);
        EntityRendererRegistry.register(Bellbird.WALKER_TYPES.get("tpose_cat"),
                ctx -> new WalkerRenderer(ctx, new TposeCatModel(ctx.getPart(TposeCatModel.LAYER)), 0.4f, tex("tpose_cat"),
                        Bellbird.scaleOf("tpose_cat")));

        // tall wading birds
        EntityModelLayerRegistry.registerModelLayer(TallBirdModel.FLAMINGO_LAYER,
                () -> TallBirdModel.getTexturedModelData(true));
        EntityModelLayerRegistry.registerModelLayer(TallBirdModel.SHOEBILL_LAYER,
                () -> TallBirdModel.getTexturedModelData(false));
        EntityRendererRegistry.register(Bellbird.TALLBIRD_TYPES.get("flamingo"),
                ctx -> new TallBirdRenderer(ctx, new TallBirdModel(ctx.getPart(TallBirdModel.FLAMINGO_LAYER)), 0.3f, tex("flamingo"),
                        Bellbird.scaleOf("flamingo")));
        EntityRendererRegistry.register(Bellbird.TALLBIRD_TYPES.get("shoebill"),
                ctx -> new TallBirdRenderer(ctx, new TallBirdModel(ctx.getPart(TallBirdModel.SHOEBILL_LAYER)), 0.35f, tex("shoebill"),
                        Bellbird.scaleOf("shoebill")));
    }
}
