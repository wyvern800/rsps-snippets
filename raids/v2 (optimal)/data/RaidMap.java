package com.rs.game.player.content.raids.data;

public class RaidMap {
    private final int[] chunkSize;

    public int[] getChunkSize() {
        return chunkSize;
    }

    public int[] getBoundChuncks() {
        return boundChuncks;
    }

    private final int[] boundChuncks;

    public RaidMap(int[] chunkSize, int[] boundChuncks) {
        this.chunkSize = chunkSize;
        this.boundChuncks = boundChuncks;
    }
}
