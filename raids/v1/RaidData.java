package com.rs.game.minigames.raids;

import com.rs.game.item.Item;
import com.rs.game.minigames.raids.data.*;
import com.rs.game.player.content.Colors;
import com.rs.utils.Utils;

public class RaidData {
    public static final String RAID_DESCRIPTION = "A minigame where a team of one to five members come together to slay a difficult BOSS, its health/level may change taking in consideration the group size, which means more HP if more members are in. Remember: this is in " + Colors.Data.NON_STARTED.getColor() + "ETA</col> and " + Colors.Data.NON_STARTED.getColor() + "WILL HAVE MANY BUGS</col>, please remember to " + Colors.Data.NON_STARTED.getColor() + "REPORT</col> them to discord and be rewarded later on! :)";

    public enum Store {
        //id, Raid(Object), RaidTiles(int[] resignTile, int[] entranceTile, int[] bossTile, int[] graveYardTile, int[]chestTile, int[]exitTile),
        // RaidMap(size x,y)(chunkx,chunky), RaidDrops(rare, uncommon, common)
        TEMPLE_OF_KARKATH(0, new Raid(new RaidBoss(13216, -1, 20000, "Leuuni, the Crippling")), new RaidTiles(new int[]{15, 1},
                new int[]{15, 3}, new int[]{15, 13}, new int[]{5, 4}, new int[]{15, 11}, new int[]{12, 17}), new RaidMap(new int[]{4, 5}, new int[]{244, 405}),
                new RaidDrops(
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
                                new Item(1618, 100), new Item(1516, 100), new Item(6828, 1)}),
                "This is the lair of an unkwnown monster, the only way to stop him is through the ritual..."), // demon id 15581


        ANAROCH_CASTLE(1, new Raid(new RaidBoss(8335, -1, 4000, "Foul Winterglaze")), new RaidTiles(new int[]{0, 14},
                new int[]{3, 12}, new int[]{11, 14}, new int[]{3, 19}, new int[]{11, 14}, new int[]{11, 21}), new RaidMap(new int[]{3, 3}, new int[]{451, 416}),
                new RaidDrops(
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
                                new Item(1618, 100), new Item(1516, 100), new Item(6828, 1)}),
                "Here lies one of the most powerful mages of Atrox, Winterglaze, only your power can stop him from destructing the world!"), // demon id 15581
        ;

        Store(final int id, final Raid raid, final RaidTiles raidTiles, final RaidMap raidMap, final RaidDrops raidDrops, final String raidDescription) {
            this.id = id;
            this.raid = raid;
            this.raidTiles = raidTiles;
            this.raidMap = raidMap;
            this.raidDrops = raidDrops;
            this.raidDescription = raidDescription;
        }

        public String getRaidDescription() {
            return raidDescription;
        }

        private String raidDescription;

        public RaidDrops getRaidDrops() {
            return raidDrops;
        }

        private RaidDrops raidDrops;

        public int getId() {
            return id;
        }

        private final int id;

        public RaidTiles getRaidTiles() {
            return raidTiles;
        }

        private final RaidTiles raidTiles;

        public Raid getRaid() {
            return raid;
        }

        private final Raid raid;

        public RaidMap getRaidMap() {
            return raidMap;
        }

        private final RaidMap raidMap;

    }
}