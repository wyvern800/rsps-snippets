package com.rs.game.player.content.raids.impl;

import com.rs.game.item.Item;
import com.rs.game.player.content.raids.data.RaidBoss;
import com.rs.game.player.content.raids.data.RaidDrops;
import com.rs.game.player.content.raids.data.RaidMap;
import com.rs.game.player.content.raids.data.RaidTiles;
import com.rs.game.player.content.raids.RaidsEncounter;
import com.rs.utils.Utils;

public class KarkathsTemple implements RaidsEncounter {
    @Override
    public int id() {
        return 0;
    }

    @Override
    public String raidName() {
        return "Karkath's Temple";
    }

    @Override
    public String raidDescription() {
        return "This is the lair of an unknown monster, the only way to stop him is through the ritual...";
    }

    @Override
    public RaidBoss raidBoss() {
        return new RaidBoss(13216, -1, 20000, "Leuuni, the Gorilla");
    }

    @Override
    public RaidDrops raidDrops() {
        return new RaidDrops(
                //VERYRARE
                new Item[]{new Item(6833, 1), new Item(28849, 1), new Item(29063, 1), new Item(25189, 1), new Item(29062, 1), new Item(29093, 1), new Item(29095, 1), new Item(29097, 1)},
                //RARE
                new Item[]{new Item(4579, 2000), new Item(17677, 200), new Item(1632, Utils.random(150)), new Item(19337, 1), new Item(3488, 1), new Item(990, 20)},
                //UNCOMMON
                new Item[]{new Item(995, 500000), new Item(2364, 50), new Item(2362, 70), new Item(1514, 200), new Item(27831, 1), new Item(27858, 3),
                        new Item(6829, 1), new Item(445, 100), new Item(448, 60), new Item(220, 15), new Item(25279, 30), new Item(6571, 1), new Item(4151, 1),
                        new Item(11212, 200), new Item(11230, 250), new Item(10034, 100), new Item(19064, 1), new Item(452, 10), new Item(9242, 250),
                        new Item(9243, 250), new Item(384, 100), new Item(11257, 5), new Item(18831, 100), new Item(1632, 100)},
                //NORMAL
                new Item[]{new Item(23352, 100), new Item(23400, 50), new Item(15273, 200), new Item(23610, 20), new Item(995, 25000), new Item(2354, 150), new Item(2352, 200),
                        new Item(441, 100), new Item(454, 80), new Item(208, 50), new Item(210, 70), new Item(560, 550), new Item(565, 700), new Item(3145, 100), new Item(146, 100), new Item(158, 100),
                        new Item(164, 100), new Item(170, 100), new Item(3043, 100), new Item(7937, 10000), new Item(10817, 100), new Item(1120, 50), new Item(10819, 50), new Item(561, 500),
                        new Item(1618, 100), new Item(1516, 100), new Item(6828, 1)});
    }

    @Override
    public RaidMap raidMap() {
        return new RaidMap(new int[]{4, 5}, new int[]{244, 405});
    }

    @Override
    public RaidTiles raidTiles() {
        return new RaidTiles(new int[]{15, 1},
                new int[]{15, 3}, new int[]{15, 13}, new int[]{5, 4}, new int[]{15, 11}, new int[]{12, 17});
    }
}
