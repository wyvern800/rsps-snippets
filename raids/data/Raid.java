package com.rs.game.minigames.raids.data;

public class Raid {

    public RaidBoss getRaidBoss() {
        return raidBoss;
    }

    public int getNpcId() {
        return raidBoss.getNPCId();
    }

    private RaidBoss raidBoss;

    public Raid(RaidBoss raidBoss) {
        this.raidBoss = raidBoss;
    }
}
