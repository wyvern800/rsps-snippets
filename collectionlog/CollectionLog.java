package com.rs.game.player.content.interfaces.collectionlog;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;

public class CollectionLog {
    static final int INTER = 3031;
    static final int BOSSES = 0, CLUES = 1, MINIGAMES = 2, OTHERS = 3, RAIDS = 4;
    static final String SELECTED = "<col=ffb70f>";
    static final String UNSELECTED = "<col=BF751D>";
    static final int CONTAINER_ID = 90;

    public static final int FULL_COMPS[] = {51,54,57,60,63,66,69,72,75,78,81,84,87,90,93,96,99,102,105,108,111,114,117,120,123,126,129,132,135,138,141};

    //int namings for killcount
    static final int /*Bosses*/BLINK = 0, CHAOS_ELE = 1, SARADOMIN = 2, CORP = 3, DKS = 4, MOLLY = 5, BANDOS = 6, ARMADYL = 7, ZAMORAK = 8, KQ = 9, KBD = 10, NEX = 11, QBD = 12, SUNFREET = 13, TORMENTED = 14,
                     /*Minigames*/BARROWS = 0, DT = 1, PC = 2, FCAVES = 3, FKILN = 4,
                     /*Clues*/EASY_CLUES = 0, MED_CLUES = 1, HARD_CLUES = 2, ELITE_CLUES = 3,
                     /*Others*/SLAYER_DROPS = 0, SUMMONING_CHARMS = 1, BOSS_PETS = 2;

    //Boss Name (if null, wont display), Collection type 1, Collection type 2 (if gwd bosses, set it to GWD, if not, just left it as null), tabid, custom text (egg. Kills, Completed), display order, componentId(leave as it is)
    static final Object BOSS_LIST[][] = {
            {"Blink", CollectionItems.CollectionType.BLINK, null, BOSSES, "Kills", 0, FULL_COMPS[0]},
            {"Chaos Elemental", CollectionItems.CollectionType.CHAOS_ELEMENTAL, null, BOSSES, "Kills", 1, FULL_COMPS[1]},
            {"Commander Zyliana", CollectionItems.CollectionType.SARADOMIN, CollectionItems.CollectionType.GWD, BOSSES, "Kills", 2, FULL_COMPS[2]},
            {"Corporeal Beast", CollectionItems.CollectionType.CORPOREAL, null, BOSSES, "Kills", 3, FULL_COMPS[3]},
            {"Dagannoth Kings", CollectionItems.CollectionType.DKS, null, BOSSES, "Kills", 4, FULL_COMPS[4]},
            {"Giant Mole", CollectionItems.CollectionType.GIANT_MOLE, null, BOSSES, "Kills", 5, FULL_COMPS[5]},
            {"General Graardor", CollectionItems.CollectionType.BANDOS, CollectionItems.CollectionType.GWD, BOSSES, "Kills", 6, FULL_COMPS[6]},
            {"Kree'arra", CollectionItems.CollectionType.ARMADYL, CollectionItems.CollectionType.GWD, BOSSES, "Kills", 7, FULL_COMPS[7]},
            {"K'ril Tsusaroth", CollectionItems.CollectionType.ZAMORAK, CollectionItems.CollectionType.GWD, BOSSES, "Kills", 8, FULL_COMPS[8]},
            {"Kalphite Queen", CollectionItems.CollectionType.KQ, null, BOSSES, "Kills", 9, FULL_COMPS[9]},
            {"King Black Dragon", CollectionItems.CollectionType.KBD, null, BOSSES, "Kills", 10, FULL_COMPS[10]},
            {"Nex", CollectionItems.CollectionType.ZAROS, null, BOSSES, "Kills", 11, FULL_COMPS[11]},
            {"Queen Black Dragon", CollectionItems.CollectionType.QBD, null, BOSSES, "Kills", 12, FULL_COMPS[12]},
            {"Sunfreet", CollectionItems.CollectionType.SUNFREET, null, BOSSES, "Kills", 13, FULL_COMPS[13]},
            {"Tormented Demons", CollectionItems.CollectionType.TORMENTED, null, BOSSES, "Kills", 14, FULL_COMPS[14]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 15, FULL_COMPS[15]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 16, FULL_COMPS[16]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 17, FULL_COMPS[17]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 18, FULL_COMPS[18]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 19, FULL_COMPS[19]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 20, FULL_COMPS[20]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 21, FULL_COMPS[21]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 22, FULL_COMPS[22]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 23, FULL_COMPS[23]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 24, FULL_COMPS[24]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 25, FULL_COMPS[25]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 26, FULL_COMPS[26]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 27, FULL_COMPS[27]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 28, FULL_COMPS[28]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 29, FULL_COMPS[29]},
            {null, CollectionItems.CollectionType.NONE, null, BOSSES, "Kills", 30, FULL_COMPS[30]}};

    static final Object CLUES_LIST[][] = {
            {"Easy Treasure Trails", CollectionItems.CollectionType.CLUE_EASY, null, CLUES, "Completed", 0, FULL_COMPS[0]},
            {"Medium Treasure Trails", CollectionItems.CollectionType.CLUE_MEDIUM, null, CLUES, "Completed", 1, FULL_COMPS[1]},
            {"Hard Treasure Trails", CollectionItems.CollectionType.CLUE_HARD, null, CLUES, "Completed", 2, FULL_COMPS[2]},
            {"Elite Treasure Trails", CollectionItems.CollectionType.CLUE_ELITE, null, CLUES, "Completed", 3, FULL_COMPS[3]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 4, FULL_COMPS[4]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 5, FULL_COMPS[5]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 6, FULL_COMPS[6]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 7, FULL_COMPS[7]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 8, FULL_COMPS[8]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 9, FULL_COMPS[9]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 10, FULL_COMPS[10]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 11, FULL_COMPS[11]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 12, FULL_COMPS[12]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 13, FULL_COMPS[13]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 14, FULL_COMPS[14]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 15, FULL_COMPS[15]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 16, FULL_COMPS[16]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 17, FULL_COMPS[17]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 18, FULL_COMPS[18]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 19, FULL_COMPS[19]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 20, FULL_COMPS[20]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 21, FULL_COMPS[21]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 22, FULL_COMPS[22]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 23, FULL_COMPS[23]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 24, FULL_COMPS[24]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 25, FULL_COMPS[25]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 26, FULL_COMPS[26]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 27, FULL_COMPS[27]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 28, FULL_COMPS[28]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 29, FULL_COMPS[29]},
            {null, CollectionItems.CollectionType.NONE, null, CLUES, "Kills", 30, FULL_COMPS[30]}};

    static final Object MINIGAMES_LIST[][] = {
            {"Barrows", CollectionItems.CollectionType.BARROWS, null, MINIGAMES, "Completed", 0, FULL_COMPS[0]},
            {"Dominion Tower", CollectionItems.CollectionType.DOMINION_TOWER, null, MINIGAMES, "Kills", 1, FULL_COMPS[1]},
            {"Pest Control", CollectionItems.CollectionType.PEST_CONTROL, null, MINIGAMES, "Completed", 2, FULL_COMPS[2]},
            {"Fight Caves", CollectionItems.CollectionType.FIGHT_CAVES, null, MINIGAMES, "Completed", 3, FULL_COMPS[3]},
            {"Fight Kiln", CollectionItems.CollectionType.FIGHT_KILN, null, MINIGAMES, "Completed", 4, FULL_COMPS[4]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 5, FULL_COMPS[5]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 6, FULL_COMPS[6]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 7, FULL_COMPS[7]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 8, FULL_COMPS[8]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 9, FULL_COMPS[9]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 10, FULL_COMPS[10]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 11, FULL_COMPS[11]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 12, FULL_COMPS[12]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 13, FULL_COMPS[13]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 14, FULL_COMPS[14]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 15, FULL_COMPS[15]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 16, FULL_COMPS[16]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 17, FULL_COMPS[17]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 18, FULL_COMPS[18]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 19, FULL_COMPS[19]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 20, FULL_COMPS[20]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 21, FULL_COMPS[21]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 22, FULL_COMPS[22]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 23, FULL_COMPS[23]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 24, FULL_COMPS[24]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 25, FULL_COMPS[25]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 26, FULL_COMPS[26]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 27, FULL_COMPS[27]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 28, FULL_COMPS[28]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 29, FULL_COMPS[29]},
            {null, CollectionItems.CollectionType.NONE, null, MINIGAMES, "Kills", 30, FULL_COMPS[30]}};

    static final Object OTHERS_LIST[][] = {/*BOSSES*/
            {"Slayer Drops", CollectionItems.CollectionType.SLAYER_DROPS, null, OTHERS, null, 0, FULL_COMPS[0]},
            {"Summoning Charms", CollectionItems.CollectionType.SUMMONING_CHARMS, null, OTHERS, null, 1, FULL_COMPS[1]},
            {"Boss Pets", CollectionItems.CollectionType.BOSS_PETS, null, OTHERS, null, 2, FULL_COMPS[2]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 3, FULL_COMPS[3]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 4, FULL_COMPS[4]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 5, FULL_COMPS[5]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 6, FULL_COMPS[6]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 7, FULL_COMPS[7]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 8, FULL_COMPS[8]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 9, FULL_COMPS[9]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 10, FULL_COMPS[10]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 11, FULL_COMPS[11]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 12, FULL_COMPS[12]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 13, FULL_COMPS[13]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 14, FULL_COMPS[14]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 15, FULL_COMPS[15]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 16, FULL_COMPS[16]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 17, FULL_COMPS[17]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 18, FULL_COMPS[18]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 19, FULL_COMPS[19]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 20, FULL_COMPS[20]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 21, FULL_COMPS[21]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 22, FULL_COMPS[22]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 23, FULL_COMPS[23]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 24, FULL_COMPS[24]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 25, FULL_COMPS[25]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 26, FULL_COMPS[26]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 27, FULL_COMPS[27]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 28, FULL_COMPS[28]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 29, FULL_COMPS[29]},
            {null, CollectionItems.CollectionType.NONE, null, OTHERS, "Kills", 30, FULL_COMPS[30]}};

    static final Object RAIDS_LIST[][] = {/*BOSSES*/
            {"Chambers of Xeric", CollectionItems.CollectionType.XERIC, null, RAIDS, "Completed", 0, FULL_COMPS[0]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 1, FULL_COMPS[1]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 2, FULL_COMPS[2]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 3, FULL_COMPS[3]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 4, FULL_COMPS[4]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 5, FULL_COMPS[5]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 6, FULL_COMPS[6]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 7, FULL_COMPS[7]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 8, FULL_COMPS[8]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 9, FULL_COMPS[9]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 10, FULL_COMPS[10]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 11, FULL_COMPS[11]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 12, FULL_COMPS[12]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 13, FULL_COMPS[13]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 14, FULL_COMPS[14]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 15, FULL_COMPS[15]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 16, FULL_COMPS[16]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 17, FULL_COMPS[17]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 18, FULL_COMPS[18]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 19, FULL_COMPS[19]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 20, FULL_COMPS[20]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 21, FULL_COMPS[21]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 22, FULL_COMPS[22]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 23, FULL_COMPS[23]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 24, FULL_COMPS[24]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 25, FULL_COMPS[25]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 26, FULL_COMPS[26]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 27, FULL_COMPS[27]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 28, FULL_COMPS[28]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 29, FULL_COMPS[29]},
            {null, CollectionItems.CollectionType.NONE, null, RAIDS, "Kills", 30, FULL_COMPS[30]}};

    public static Object[][] TABS_COMPONENTS = {{18, "Bosses"}, {21, "Clues"}, {24, "Minigames"}, {27, "Others"}, {30,"Raids"}};

    public static ItemsContainer<Item> Rewards = new ItemsContainer<Item>(200, true);

    public static void sendInterface(Player player) {
        int tab = player.getTemporaryAttributes().get("cat") != null ? (int) player.getTemporaryAttributes().get("cat") : 0;
        player.getTemporaryAttributes().put("cat", tab);
        player.getInterfaceManager().sendInterface(INTER);
        getBossName(player);
        highlighTab(player, (int)TABS_COMPONENTS[tab][0], (String) TABS_COMPONENTS[tab][1]);
        sendSelectedEntityItems(player, tab, 0);
        sendOptions(player);
    }



    private static void sendSelectedEntityItems(Player player, int tab, int index) {
        for (int i = 0; i < FULL_COMPS.length; i++) {
            player.getPackets().sendIComponentText(INTER, FULL_COMPS[i]+2, (String) (tab == BOSSES ? UNSELECTED+BOSS_LIST[i][0] : tab == CLUES ? UNSELECTED+CLUES_LIST[i][0] : tab == MINIGAMES ? UNSELECTED+MINIGAMES_LIST[i][0] : tab == OTHERS ? UNSELECTED+OTHERS_LIST[i][0] : UNSELECTED+RAIDS_LIST[i][0]));
        }
        player.getPackets().sendIComponentText(INTER, FULL_COMPS[index]+2, (String) (tab == BOSSES ? SELECTED+BOSS_LIST[index][0] : tab == CLUES ? SELECTED+CLUES_LIST[index][0] : tab == MINIGAMES ? SELECTED+MINIGAMES_LIST[index][0] : tab == OTHERS ? SELECTED+OTHERS_LIST[index][0] : SELECTED+RAIDS_LIST[index][0]));

        sendItems(player,(String) (tab == BOSSES ? BOSS_LIST[index][0] :  tab == CLUES ? CLUES_LIST[index][0] : tab == MINIGAMES ? MINIGAMES_LIST[index][0] : tab == OTHERS ? OTHERS_LIST[index][0] : RAIDS_LIST[index][0]),
                (CollectionItems.CollectionType) (tab == BOSSES ? BOSS_LIST[index][1] : tab == CLUES ? CLUES_LIST[index][1] : tab == MINIGAMES ? MINIGAMES_LIST[index][1] : tab == OTHERS ? OTHERS_LIST[index][1] : RAIDS_LIST[index][1]),
                (CollectionItems.CollectionType) (tab == BOSSES ? BOSS_LIST[index][2] : tab == CLUES ? CLUES_LIST[index][2] : tab == MINIGAMES ? MINIGAMES_LIST[index][2] : tab == OTHERS ? OTHERS_LIST[index][2] : RAIDS_LIST[index][2]),
                getCompletedCount(player, (Integer) (tab == BOSSES ? BOSS_LIST[index][3] : tab == CLUES ? CLUES_LIST[index][3] : tab == MINIGAMES ? MINIGAMES_LIST[index][3] : tab == OTHERS ? OTHERS_LIST[index][3] : RAIDS_LIST[index][3]), index),
                ((String) (tab == BOSSES ? BOSS_LIST[index][4] : tab == CLUES ? CLUES_LIST[index][4] : tab == MINIGAMES ? MINIGAMES_LIST[index][4] : tab == OTHERS ? OTHERS_LIST[index][4] : RAIDS_LIST[index][4])),
                ((tab == BOSSES ? (BOSS_LIST[index][4] != null ? true : false) : tab == CLUES ? (CLUES_LIST[index][4] != null ? true : false) : tab == MINIGAMES ? (MINIGAMES_LIST[index][4] != null ? true : false) : tab == OTHERS ? (OTHERS_LIST[index][4] != null ? true : false) : (RAIDS_LIST[index][4] != null ? true : false))));
    }

    /**
     * @param player - the player
     * @param tab - tab category
     * @param index - index
     * @return
     */
    private static int getCompletedCount(Player player, int tab, int index) {
        switch (tab) {//Cattegory
            case BOSSES:
                switch (index) {//Boss index
                    case BLINK:
                        return player.getBlinkKills();
                    case CHAOS_ELE:
                        return player.getChaoselekills();
                    case SARADOMIN:
                        return player.getSarakills();
                    case CORP:
                        return player.getCorpkills();
                    case DKS:
                        return player.getDksKills();
                    case MOLLY:
                        return player.getgiantmollykills();
                    case BANDOS:
                        return player.getBandoskills();
                    case ARMADYL:
                        return player.getArmakills();
                    case ZAMORAK:
                        return player.getZamykills();
                    case KQ:
                        return player.getKqkills();
                    case KBD:
                        return player.getKbdkills();
                    case NEX:
                        return player.getNexkills();
                    case QBD:
                        return player.getQbdkills();
                    case SUNFREET:
                        return player.getSunfreetkills();
                    case TORMENTED:
                        return player.getTormKills();
                    default:
                        return 0;
                }
            case CLUES:
                switch (index) {//clues index
                    case EASY_CLUES://clues completed
                    case MED_CLUES:
                    case HARD_CLUES:
                    case ELITE_CLUES:
                        return player.getCluescompleted();
                    default:
                        return 0;
                }
            case MINIGAMES:
                switch (index) {//others index
                    case BARROWS://barrows
                        return player.getOpenedbarrowschest();
                    case DT:
                        return player.getDominionTower().getKilledBossesCount();
                    case PC:
                        return player.getPcgames();
                    case FCAVES:
                        return player.getJadkills();
                    case FKILN:
                        return player.getHarAkenKills();
                    default:
                        return 0;
                }
            case OTHERS:
                switch (index) {//others index
                    case SLAYER_DROPS://slayer drops
                    case SUMMONING_CHARMS:
                    case BOSS_PETS:
                        return 0;
                    default:
                        return 0;
                }
            case RAIDS:
                switch (index) {//others index
                    case 0:
                        return 0;
                    default:
                        return 0;
                }
            default:
                return 0;
        }
    }

    public static void handleButtons(Player player, int componentId, int slotId2) {
        int cat = player.getTemporaryAttributes().get("cat") != null ? (int) player.getTemporaryAttributes().get("cat") : 0;

        // When player clicks a boss on the list, this should send the selection
        for (int i = 0; i < FULL_COMPS.length; i++) {
             if (componentId == FULL_COMPS[i]+2) {
                sendSelectedEntityItems(player, cat, i);
                }
        }
        // The other components within the interface
            switch (componentId) {
                case 45: //examine
                    String itemName = new Item(slotId2).getName();
                    if (player.getUniqueItemQuantity(slotId2) == 0) {
                        player.sm("I did not get any " + itemName + " yet.");
                    } else {
                        player.sm("I have obtained <col=f00000>" + player.getUniqueItemQuantity(slotId2) + "</col>x " + itemName + ".");
                    }
                    break;

                // Tabs Switching components
                case 18: //bosses
                    switchTab(player, BOSSES);
                    highlighTab(player, componentId, (String) TABS_COMPONENTS[0][1]);
                    break;
                case 21: //clues
                    switchTab(player, CLUES);
                    highlighTab(player, componentId, (String) TABS_COMPONENTS[1][1]);
                    break;
                case 24: //minigames
                    switchTab(player, MINIGAMES);
                    highlighTab(player, componentId, (String) TABS_COMPONENTS[2][1]);
                    break;
                case 27: //others
                    switchTab(player, OTHERS);
                    highlighTab(player, componentId, (String) TABS_COMPONENTS[3][1]);
                    break;
                case 30: //raids
                    switchTab(player, RAIDS);
                    highlighTab(player, componentId, (String) TABS_COMPONENTS[4][1]);
                    break;
        }
    }

    private static void switchTab(Player player, int category) {
        player.getTemporaryAttributes().put("cat", category);
        sendSelectedEntityItems(player, category, 0);
        sendInterface(player);
        if (player.isDebugMode())
        player.sm("Tab selected: "+ (category == BOSSES ? "Bosses" : category == CLUES ? "Clues" : category == MINIGAMES ? "Minigames" : category == OTHERS ? "Others" : "Raids"));
    }

    private static void highlighTab(Player player, int component, String tabName) {
        for (int i = 0; i < TABS_COMPONENTS.length; i++) {
           player.getPackets().sendIComponentSprite(INTER,  (int) TABS_COMPONENTS[i][0] + 1, 953);
           player.getPackets().sendIComponentText(INTER, (int) TABS_COMPONENTS[i][0] + 2,  (String) UNSELECTED+TABS_COMPONENTS[i][1]);
        }
        player.getPackets().sendIComponentSprite(INTER, component+1, 952);
        player.getPackets().sendIComponentText(INTER, component+2, SELECTED+tabName);
    }

    private static void sendItems(Player player, String bossName, CollectionItems.CollectionType collectionType1, CollectionItems.CollectionType collectionType2, int bossKills, String killOrCompleted, boolean show) {
        Rewards.clear();
        Rewards.reset();
        player.getPackets().sendIComponentText(INTER, 40, "<col=ff9c24>"+bossName+"</col>");
        player.getPackets().sendIComponentText(INTER, 41, "Obtained: <col=ffd900>" + getObtainedItems(player, collectionType1) + "/"+getMax(collectionType1)+"</col>");
        if (show) {
            player.getPackets().sendHideIComponent(INTER, 42, false);
            player.getPackets().sendIComponentText(INTER, 42, killOrCompleted + ": <col=ffffff>" + bossKills);
        } else {
            player.getPackets().sendHideIComponent(INTER, 42, true);
        }
        getItemsTable(player, collectionType1);
        if (collectionType2 != null) {
            getItemsTable(player, collectionType2);
        }
        player.getPackets().sendItems(CONTAINER_ID, false, Rewards);
    }

    private static void getBossName(Player player) {
        int cat = player.getTemporaryAttributes().get("cat") != null ? (int) player.getTemporaryAttributes().get("cat") : 0;
        for (int i = 0; i < FULL_COMPS.length; i++) {
            if ((cat == BOSSES ? BOSS_LIST[i][0] != null : cat == CLUES ? CLUES_LIST[i][0] != null : cat == MINIGAMES ? MINIGAMES_LIST[i][0] != null : cat == OTHERS ? OTHERS_LIST[i][0] != null : RAIDS_LIST[i][0] != null)) {
                if ((int) (cat == BOSSES ? BOSS_LIST[i][6] : cat == CLUES ? CLUES_LIST[i][6] : cat == MINIGAMES ? MINIGAMES_LIST[i][6] : cat == OTHERS ? OTHERS_LIST[i][6] : RAIDS_LIST[i][6]) != FULL_COMPS[i])
                    continue;
                if ((int) (cat == BOSSES ? BOSS_LIST[i][5] : cat == CLUES ? CLUES_LIST[i][5] : cat == MINIGAMES ? MINIGAMES_LIST[i][5] : cat == OTHERS ? OTHERS_LIST[i][5] : RAIDS_LIST[i][5]) != i)
                    continue;
                if ((int) (cat == BOSSES ? BOSS_LIST[i][3] : cat == CLUES ? CLUES_LIST[i][3] : cat == MINIGAMES ? MINIGAMES_LIST[i][3] : cat == OTHERS ? OTHERS_LIST[i][3] : RAIDS_LIST[i][3]) != cat)
                    continue;
                player.getPackets().sendHideIComponent(INTER, FULL_COMPS[i], false);
                player.getPackets().sendIComponentText(INTER, FULL_COMPS[i]+2,
                     (cat == BOSSES ? (String) BOSS_LIST[i][0] : cat == CLUES ? (String) CLUES_LIST[i][0] : cat == MINIGAMES ? (String) MINIGAMES_LIST[i][0] : cat == OTHERS ? (String) OTHERS_LIST[i][0] : (String) RAIDS_LIST[i][0]));
            }
        }
    }

    public static int getMax(CollectionItems.CollectionType collectionType) {
        int quant = 0;
        for (CollectionItems.Collection item : CollectionItems.Collection.values()) {
            if (item.getType() == collectionType) {
                quant++;
            }
        }
        return quant;
    }

    public static void sendOptions(Player player) {
        player.getPackets().sendInterSetItemsOptionsScript(INTER, 45, CONTAINER_ID, 7, 18, "Check"); //12 5
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, 45, 0, 160, 0);
    }

    public static void getItemsTable(Player player, CollectionItems.CollectionType collectionType) {
        for (CollectionItems.Collection item : CollectionItems.Collection.values()) {
            if (item.getType() == collectionType) {
                Rewards.add(new Item(item.getId(), player.getUniqueItemQuantity(item.getId())));
            }
        }
    }


    public static int getObtainedItems(Player player, CollectionItems.CollectionType collectionType) {
        int quant = 0;
        for (CollectionItems.Collection item : CollectionItems.Collection.values()) {
            if (item.getType() == collectionType) {
                if (player.containsUniqueItem(item.getId())) {
                    if (player.isDebugMode()) {
                        player.sm("Entry on hashmap (id=" + item.getId() + ")");
                    }
                    quant++;
                }
            }
        }
        if (player.isDebugMode()) {
                    player.sm("Count: " + quant);
                }
        return quant;
    }
}