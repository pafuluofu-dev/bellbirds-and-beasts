package dev.verno.bellbird;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class Bellbird implements ModInitializer {
    public static final String MOD_ID = "bellbird";

    public static final Map<BellbirdSpecies, EntityType<BellbirdEntity>> TYPES = new EnumMap<>(BellbirdSpecies.class);
    public static final Map<BellbirdSpecies, SoundEvent> SOUNDS = new EnumMap<>(BellbirdSpecies.class);
    public static final Map<EntityType<?>, SoundEvent> SOUND_BY_TYPE = new HashMap<>();

    public static final EntityType<CassowaryEntity> CASSOWARY = FabricEntityTypeBuilder
            .createMob().entityFactory(CassowaryEntity::new)
            .spawnGroup(SpawnGroup.CREATURE)
            .dimensions(EntityDimensions.fixed(0.9f, 1.7f))
            .trackRangeChunks(8).build();
    public static final EntityType<MegafaunaEntity> ELASMOTHERIUM = FabricEntityTypeBuilder
            .createMob().entityFactory(MegafaunaEntity::new)
            .spawnGroup(SpawnGroup.CREATURE)
            .dimensions(EntityDimensions.fixed(1.9f, 2.3f))
            .trackRangeChunks(8).build();
    public static final EntityType<MegafaunaEntity> ARSINOITHERIUM = FabricEntityTypeBuilder
            .createMob().entityFactory(MegafaunaEntity::new)
            .spawnGroup(SpawnGroup.CREATURE)
            .dimensions(EntityDimensions.fixed(1.9f, 2.3f))
            .trackRangeChunks(8).build();

    public static final SoundEvent CALL_CASSOWARY = new SoundEvent(new Identifier(MOD_ID, "call_cassowary"));
    public static final SoundEvent CALL_ELASMOTHERIUM = new SoundEvent(new Identifier(MOD_ID, "call_elasmotherium"));
    public static final SoundEvent CALL_ARSINOITHERIUM = new SoundEvent(new Identifier(MOD_ID, "call_arsinoitherium"));

    static {
        for (BellbirdSpecies s : BellbirdSpecies.values()) {
            SOUNDS.put(s, new SoundEvent(new Identifier(MOD_ID, "bonk_" + s.id)));
            TYPES.put(s, FabricEntityTypeBuilder
                    .createMob()
                    .entityFactory(BellbirdEntity::new)
                    .spawnGroup(SpawnGroup.CREATURE)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.7f))
                    .trackRangeChunks(8)
                    .build());
        }
    }

    @Override
    public void onInitialize() {
        for (BellbirdSpecies s : BellbirdSpecies.values()) {
            EntityType<BellbirdEntity> type = TYPES.get(s);
            Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "bonk_" + s.id), SOUNDS.get(s));
            Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, s.id), type);
            FabricDefaultAttributeRegistry.register(type, BellbirdEntity.createBellbirdAttributes());
            SpawnRestriction.register(type, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BellbirdEntity::canSpawn);
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, s.id + "_spawn_egg"),
                    new SpawnEggItem(type, s.eggBase, s.eggSpot, new Item.Settings().group(ItemGroup.MISC)));
            SOUND_BY_TYPE.put(type, SOUNDS.get(s));

            int jungleWeight = s == BellbirdSpecies.THREE_WATTLED ? 14 : 7;
            int forestWeight = s == BellbirdSpecies.THREE_WATTLED ? 5 : 2;
            BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                    SpawnGroup.CREATURE, type, jungleWeight, 1, 2);
            BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST),
                    SpawnGroup.CREATURE, type, forestWeight, 1, 2);
        }

        Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "call_cassowary"), CALL_CASSOWARY);
        Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "call_elasmotherium"), CALL_ELASMOTHERIUM);
        Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "call_arsinoitherium"), CALL_ARSINOITHERIUM);

        Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, "cassowary"), CASSOWARY);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, "elasmotherium"), ELASMOTHERIUM);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, "arsinoitherium"), ARSINOITHERIUM);

        FabricDefaultAttributeRegistry.register(CASSOWARY, CassowaryEntity.createCassowaryAttributes());
        FabricDefaultAttributeRegistry.register(ELASMOTHERIUM, MegafaunaEntity.createMegafaunaAttributes());
        FabricDefaultAttributeRegistry.register(ARSINOITHERIUM, MegafaunaEntity.createMegafaunaAttributes());

        SpawnRestriction.register(CASSOWARY, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
        SpawnRestriction.register(ELASMOTHERIUM, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
        SpawnRestriction.register(ARSINOITHERIUM, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);

        SOUND_BY_TYPE.put(CASSOWARY, CALL_CASSOWARY);
        SOUND_BY_TYPE.put(ELASMOTHERIUM, CALL_ELASMOTHERIUM);
        SOUND_BY_TYPE.put(ARSINOITHERIUM, CALL_ARSINOITHERIUM);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cassowary_spawn_egg"),
                new SpawnEggItem(CASSOWARY, 0x1E1C22, 0x2456C8, new Item.Settings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "elasmotherium_spawn_egg"),
                new SpawnEggItem(ELASMOTHERIUM, 0x5A4632, 0xD9CFB6, new Item.Settings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "arsinoitherium_spawn_egg"),
                new SpawnEggItem(ARSINOITHERIUM, 0x70645A, 0xD9CFB6, new Item.Settings().group(ItemGroup.MISC)));

        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                SpawnGroup.CREATURE, CASSOWARY, 6, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA),
                SpawnGroup.CREATURE, ELASMOTHERIUM, 4, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.PLAINS),
                SpawnGroup.CREATURE, ARSINOITHERIUM, 4, 1, 2);
    }
}
