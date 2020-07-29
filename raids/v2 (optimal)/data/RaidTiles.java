package com.rs.game.player.content.raids.data;

public class RaidTiles {
    public int[] getEntranceTile() {
        return entranceTile;
    }

    public int[] getBossTile() {
        return bossTile;
    }

    public int[] getGraveyardTile() {
        return graveyardTile;
    }

    public int[] getChestTile() {
        return chestTile;
    }

    private final int[] entranceTile;

    public int[] getResignTile() {
        return resignTile;
    }

    private final int[] resignTile;
    private final int[] bossTile;
    private final int[] graveyardTile;
    private final int[] chestTile;

    public int[] getExitTile() {
        return exitTile;
    }

    private final int[] exitTile;

    public RaidTiles(int[] resignTile, int[] entranceTile, int[] bossTile, int[] graveyardTile, int[] chestTile, int[] exitTile) {
        this.resignTile = resignTile;
        this.entranceTile = entranceTile;
        this.bossTile = bossTile;
        this.graveyardTile = graveyardTile;
        this.chestTile = chestTile;
        this.exitTile = exitTile;
    }
}
