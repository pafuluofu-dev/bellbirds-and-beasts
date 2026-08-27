package dev.verno.bellbird;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
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

    /** Ground creatures: quadrupeds, the T-pose cat, cassowary, tall wading birds, megafauna. */
    public record WalkerDef(String id, boolean tallBird, float w, float h,
                            double hp, double speed, int eggBase, int eggSpot,
                            String biome, int weight) {}

    public static final List<WalkerDef> WALKERS = List.of(
            new WalkerDef("jaguar",    false, 0.9f, 1.1f, 20, 0.30, 0xC98A3B, 0x2A2118, "jungle", 5),
            new WalkerDef("leopard",   false, 0.9f, 1.1f, 20, 0.30, 0xD9A94C, 0x2A2118, "savanna", 4),
            new WalkerDef("gray_cat",  false, 0.5f, 0.6f, 10, 0.30, 0x8E8E96, 0x5C5C64, "plains", 4),
            new WalkerDef("tpose_cat", false, 0.6f, 1.2f, 14, 0.28, 0x8E8E96, 0xC98A3B, "plains", 2),
            new WalkerDef("kalan",     false, 0.7f, 0.5f, 10, 0.28, 0x6B4F38, 0xD8C7A8, "beach", 6),
            new WalkerDef("sun_bear",  false, 1.1f, 1.2f, 24, 0.25, 0x1E1B18, 0xE0A33A, "jungle", 4),
            new WalkerDef("moon_bear", false, 1.2f, 1.3f, 26, 0.25, 0x1E1B18, 0xF2EEE4, "taiga", 4),
            new WalkerDef("bison",     false, 1.6f, 1.8f, 30, 0.20, 0x4A3423, 0x2A1E14, "plains", 6),
            new WalkerDef("unicorn",   false, 1.3f, 1.8f, 26, 0.32, 0xF7F5EF, 0xE87BC1, "flower", 2),
            new WalkerDef("tarbagan",  false, 0.6f, 0.6f, 8, 0.25, 0x8A6B4A, 0xC7A87E, "taiga", 6),
            new WalkerDef("flamingo",  true,  0.6f, 1.4f, 12, 0.30, 0xF08CA8, 0xE85C86, "beach", 6),
            new WalkerDef("shoebill",  true,  0.7f, 1.5f, 14, 0.25, 0x7E8489, 0x3A4046, "swamp", 5)
    );

    public static final Map<String, EntityType<MegafaunaEntity>> WALKER_TYPES = new HashMap<>();
    public static final Map<String, EntityType<CassowaryEntity>> TALLBIRD_TYPES = new HashMap<>();
    public static final Map<String, SoundEvent> WALKER_SOUNDS = new HashMap<>();

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
        for (WalkerDef d : WALKERS) {
            WALKER_SOUNDS.put(d.id(), new SoundEvent(new Identifier(MOD_ID, "call_" + d.id())));
            if (d.tallBird()) {
                TALLBIRD_TYPES.put(d.id(), FabricEntityTypeBuilder
                        .createMob().entityFactory(CassowaryEntity::new)
                        .spawnGroup(SpawnGroup.CREATURE)
                        .dimensions(EntityDimensions.fixed(d.w(), d.h()))
                        .trackRangeChunks(8).build());
            } else {
                WALKER_TYPES.put(d.id(), FabricEntityTypeBuilder
                        .createMob().entityFactory(MegafaunaEntity::new)
                        .spawnGroup(SpawnGroup.CREATURE)
                        .dimensions(EntityDimensions.fixed(d.w(), d.h()))
                        .trackRangeChunks(8).build());
            }
        }
    }

    private static java.util.function.Predicate<net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext> selector(String biome) {
        return switch (biome) {
            case "jungle" -> BiomeSelectors.tag(BiomeTags.IS_JUNGLE);
            case "forest" -> BiomeSelectors.tag(BiomeTags.IS_FOREST);
            case "savanna" -> BiomeSelectors.includeByKey(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA);
            case "plains" -> BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.MEADOW);
            case "taiga" -> BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA, BiomeKeys.SNOWY_PLAINS, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA);
            case "beach" -> BiomeSelectors.includeByKey(BiomeKeys.BEACH, BiomeKeys.RIVER, BiomeKeys.STONY_SHORE);
            case "swamp" -> BiomeSelectors.includeByKey(BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP);
            case "flower" -> BiomeSelectors.includeByKey(BiomeKeys.FLOWER_FOREST, BiomeKeys.MEADOW);
            default -> BiomeSelectors.tag(BiomeTags.IS_FOREST);
        };
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

            boolean grouse = s == BellbirdSpecies.BLACK_GROUSE || s == BellbirdSpecies.SAGE_GROUSE;
            if (grouse) {
                BiomeModifications.addSpawn(selector(s == BellbirdSpecies.BLACK_GROUSE ? "taiga" : "plains"),
                        SpawnGroup.CREATURE, type, 8, 1, 3);
            } else {
                int jungleWeight = s == BellbirdSpecies.THREE_WATTLED ? 14 : 7;
                int forestWeight = s == BellbirdSpecies.THREE_WATTLED ? 5 : 2;
                BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_JUNGLE),
                        SpawnGroup.CREATURE, type, jungleWeight, 1, 2);
                BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST),
                        SpawnGroup.CREATURE, type, forestWeight, 1, 2);
            }
        }

        for (WalkerDef d : WALKERS) {
            SoundEvent snd = WALKER_SOUNDS.get(d.id());
            Registry.register(Registry.SOUND_EVENT, new Identifier(MOD_ID, "call_" + d.id()), snd);
            EntityType<?> type = d.tallBird() ? TALLBIRD_TYPES.get(d.id()) : WALKER_TYPES.get(d.id());
            Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, d.id()), type);
            FabricDefaultAttributeRegistry.register((EntityType<? extends net.minecraft.entity.LivingEntity>) type,
                    MegafaunaEntity.createWalkerAttributes(d.hp(), d.speed()));
            SpawnRestriction.register((EntityType<? extends AnimalEntity>) type, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, d.id() + "_spawn_egg"),
                    new SpawnEggItem((EntityType<? extends net.minecraft.entity.mob.MobEntity>) type,
                            d.eggBase(), d.eggSpot(), new Item.Settings().group(ItemGroup.MISC)));
            SOUND_BY_TYPE.put(type, snd);
            BiomeModifications.addSpawn(selector(d.biome()), SpawnGroup.CREATURE,
                    (EntityType<? extends net.minecraft.entity.mob.MobEntity>) type, d.weight(), 1, 2);
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
        BiomeModifications.addSpawn(selector("taiga"), SpawnGroup.CREATURE, ELASMOTHERIUM, 4, 1, 2);
        BiomeModifications.addSpawn(selector("savanna"), SpawnGroup.CREATURE, ARSINOITHERIUM, 4, 1, 2);
    }
}
