package com.rs.game.minigames.raids.data;

public final class RaidBoss {

    private final int npcId;
    private final int npcLevel;
    private final String npcName;
    private final int baseHitpoints;


    public RaidBoss(final int npcId, final int npcLevel, final int baseHitpoints, final String npcName) {
        this.npcId = npcId;
        this.npcLevel = npcLevel;
        this.baseHitpoints = baseHitpoints;
        this.npcName = npcName;
    }

    public final int getNPCId() {
        return npcId;
    }

    public int getBaseHitpoints() {
        return baseHitpoints;
    }

    public int getNpcLevel() {
        return npcLevel;
    }

    public String getNpcName() {
        return npcName;
    }

}