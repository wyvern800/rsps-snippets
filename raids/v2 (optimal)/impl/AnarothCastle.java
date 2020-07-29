package com.rs.game.player.content.raids.impl;

import com.rs.game.item.Item;
import com.rs.game.player.content.raids.data.RaidBoss;
import com.rs.game.player.content.raids.data.RaidDrops;
import com.rs.game.player.content.raids.data.RaidMap;
import com.rs.game.player.content.raids.data.RaidTiles;
import com.rs.game.player.content.raids.RaidsEncounter;
import com.rs.utils.Utils;

public class AnarothCastle implements RaidsEncounter {
    @Override
    public int id() {
        return 1;
    }

    @Override
    public String raidName() {
        return "Anaroth Castle";
    }

    @Override
    public String raidDescription() {
        return "Here lies one of the most powerful mages of Atrox, Winterglaze, only your power can stop him from destructing the world!";
    }

    @Override
    public RaidBoss raidBoss() {
        return new RaidBoss(8335, -1, 4000, "Foul Winterglaze");
    }

    @Override
    public RaidDrops raidDrops() {
        return new RaidDrops(
                //VERYRARE
                new Item[]{new Item(6833, 1), new Item(28848, 1), new Item(29064, 1), new Item(29065, 1), new Item(29066, 1), new Item(29099, 1), new Item(29101, 1), new Item(29111, 1), new Item(29105, 1), new Item(29107, 1), new Item(29109, 1)},
                //RARE
                new Item[]{new Item(29085, 100), new Item(29085, Utils.random(20)), new Item(29085, Utils.random(200)), new Item(6571, 1), new Item(990, 15)},
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
        return new RaidMap(new int[]{3, 3}, new int[]{451, 416});
    }

    @Override
    public RaidTiles raidTiles() {
        return new RaidTiles(new int[]{0, 14},
                new int[]{3, 12}, new int[]{11, 14}, new int[]{3, 19}, new int[]{11, 14}, new int[]{11, 21});
    }
}
