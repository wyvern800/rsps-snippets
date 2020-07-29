package com.rs.game.player.content.raids;

import com.rs.game.player.content.raids.data.RaidBoss;
import com.rs.game.player.content.raids.data.RaidDrops;
import com.rs.game.player.content.raids.data.RaidMap;
import com.rs.game.player.content.raids.data.RaidTiles;

import java.io.Serializable;

public interface RaidsEncounter extends Serializable {
    int id();
    String raidName();
    String raidDescription();
    RaidBoss raidBoss();
    RaidDrops raidDrops();
    RaidMap raidMap();
    RaidTiles raidTiles();
}
