package com.rs.game.player.content.raids.data;

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
