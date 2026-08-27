package dev.verno.bellbird;

public enum BellbirdSpecies {
    THREE_WATTLED("three_wattled", 0xF3EFE7, 0x8A431C),
    WHITE("white", 0xF7F5EF, 0x2A282E),
    BARE_THROATED("bare_throated", 0xF7F5EF, 0x3AC7B8),
    BEARDED("bearded", 0xC9C4BB, 0x5C3B22),
    POTOO("potoo", 0x8A7A66, 0xE8C13A),
    PIHA("piha", 0x9BA0A4, 0x6E7478),
    BLACK_GROUSE("black_grouse", 0x1E1C22, 0xC43B2A),
    SAGE_GROUSE("sage_grouse", 0x9A8F7A, 0xF2EEE4);

    public final String id;
    public final int eggBase;
    public final int eggSpot;

    BellbirdSpecies(String id, int eggBase, int eggSpot) {
        this.id = id;
        this.eggBase = eggBase;
        this.eggSpot = eggSpot;
    }
}
