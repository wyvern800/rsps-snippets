package com.rs.game.player.content.interfaces;

import com.rs.Settings;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.GameModes;
import com.rs.game.player.Player;

/* @author - Matheus G. Ferreira (Sagacity) - 05/01/2019 - https://www.rune-server.ee/members/sagacity/
 */

public class StarterInterface {
    static final int INTER = 3005;

    static final int TXT_PERSISTANT = 27, TXT_NATURAL = 28, TXT_ROYAL = 29, TXT_STANDARD_IRON = 30, TXT_HARDCORE_IRON = 31, TXT_ULTIMATE_IRON = 32, TXT_DESC = 43;

    static final int PERSISTANT_MODE = 1, ROYAL_MODE = 3, NATURAL_MODE = 2, STANDARD_IRONMAN_MODE = 4, HARDCORE_IRONMAN_MODE = 5, ULTIMATE_IRONMAN_MODE = 6;

    static final int COMP_PERSISTANT = 37,COMP_NATURAL = 38,COMP_ROYAL = 39,COMP_STANDARD_IRON = 40,COMP_HARDCORE_IRON = 41,COMP_ULTIMATE_IRON = 42,COMP_CONFIRM = 44;

    static final int[][] PERSISTANT_ITEMS = {{995, 2000000}, {1323, 1}, {1731, 1}, {9740, 10},
            {334, 50}, {841, 1}, {1169, 1}, {1129, 1}, {1095, 1}, {884, 100},
            {1059, 1}, {1061, 1}, {10498, 1}, {556, 300}, {554, 300}, {555, 300},
            {557, 300}, {558, 150}, {579, 1}, {577, 1}, {1011, 1}, {1381, 1}, {1540, 1}, {29985, 1}};
    static final int[][] NATURAL_ITEMS = {{995, 2000000}, {1323, 1}, {1731, 1}, {9740, 10}, {334, 50}, {841, 1},
            {1169, 1}, {1129, 1}, {1095, 1}, {884, 100}, {1059, 1}, {1061, 1}, {10498, 1},
            {556, 300}, {554, 300}, {555, 300}, {557, 300}, {558, 150}, {579, 1}, {577, 1},
            {1011, 1}, {1381, 1}, {1540, 1}, {29985, 1}};
    static final int[][] ROYAL_ITEMS = {{995, 2000000}, {1323, 1}, {1731, 1}, {9740, 10}, {334, 50}, {841, 1}, {1169, 1}, {1129, 1},
            {1095, 1}, {884, 100}, {1059, 1}, {1061, 1}, {10498, 1}, {556, 300}, {554, 300}, {555, 300}, {557, 300},
            {558, 150}, {579, 1}, {577, 1}, {1011, 1},  {1381, 1}, {1540, 1}, {29985, 1}};
    static final int[][] STD_IRONMAN_ITEMS = {{995, 500000}, {26805, 1}, {26803,1}, {26804,1}, {1323, 1}, {1731, 1}, {9740, 10},
            {334, 50}, {841, 1},{1169, 1},{1129, 1},{1095, 1},{884, 100},{1059, 1},
            {1061, 1},{10498, 1},{556, 300}, {554, 300}, {555, 300}, {557, 300}, {558, 150},{579, 1},{577, 1},
            {1011, 1},{1381, 1},{1540, 1}, {29985, 1}};
    static final int[][] HDC_IRONMAN_ITEMS = {{995, 500000}, {26800,1},{26802,1},{26801,1}, {1323, 1}, {1731, 1}, {9740, 10},  {334, 50}, {841, 1},
            {1169, 1},{1129, 1}, {1095, 1}, {884, 100},{1059, 1},{1061, 1},{10498, 1},{556, 300},
            {554, 300},{555, 300}, {557, 300},{558, 150}, {579, 1},{577, 1}, {1011, 1},
            {1381, 1}, {1540, 1},{29985, 1},};
    static final int[][] ULT_IRONMAN_ITEMS = {{995, 500000}, {26806, 1}, {26807, 1}, {26808, 1}, {1323, 1},{1731, 1},{9740, 10},
            {334, 50}, {841, 1}, {1169, 1}, {1129, 1}, {1095, 1}, {884, 100}, {1059, 1}, {1061, 1}, {10498, 1}, {556, 300},
            {554, 300}, {555, 300}, {557, 300},{558, 150},{579, 1},{577, 1}, {1011, 1},{1381, 1},{1540, 1}, {29985, 1}};

    public static ItemsContainer<Item> starterItems = new ItemsContainer<Item>(500, true);

    static int gameMode;

    public static void sendInterface(Player player) {
        player.getPackets().sendIComponentText(INTER, 50, "<col=ff2929>Welcome to Infernal");
        player.getPackets().sendIComponentText(INTER, 25, "<col=ff9c24>~ Normal game modes ~</col>");
        player.getPackets().sendIComponentText(INTER, TXT_PERSISTANT, "<col=c7c7c7>Persistant</col>");
        player.getPackets().sendIComponentText(INTER, TXT_NATURAL, "<col=c7c7c7>Natural</col>");
        player.getPackets().sendIComponentText(INTER, TXT_ROYAL, "<col=c7c7c7>Royal</col>");
        player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>~ Ironman game modes ~</col>");
        player.getPackets().sendIComponentText(INTER, TXT_STANDARD_IRON, "<col=c7c7c7><img=13> Standard</col>");
        player.getPackets().sendIComponentText(INTER, TXT_HARDCORE_IRON, "<col=c7c7c7><img=14> Hardcore</col>");
        player.getPackets().sendIComponentText(INTER, TXT_ULTIMATE_IRON, "<col=c7c7c7><img=15> Ultimate</col>");
        player.getPackets().sendIComponentText(INTER, 34, "<col=ff9c24>~ Description ~</col>");
        player.getPackets().sendIComponentText(INTER, 48, "<col=ff9c24>~ Starter Kit ~</col>");
        player.getPackets().sendIComponentText(INTER, TXT_DESC, "Select a game mode on the buttons to read its descriptions!");
        player.getPackets().sendHideIComponent(INTER, 44, true); //hide confirm button
        player.getInterfaceManager().sendInterface(INTER);
    }

    public static void markCattegory(Player player, int comp) {
        for (int i = 37; i <= 42; i++) {
            player.getPackets().sendHideIComponent(INTER, i, false);
        }
        player.getPackets().sendHideIComponent(INTER, comp, true);
    }

    public static void sendItems(Player player, int type) {
        starterItems.clear();
        starterItems.reset();
        sendOptions(player);
        switch (type) {
            case PERSISTANT_MODE:
                for (int i = 0; i < PERSISTANT_ITEMS.length; i++) {
                    starterItems.add(new Item(PERSISTANT_ITEMS[i][0], PERSISTANT_ITEMS[i][1]));
                }
                break;
            case NATURAL_MODE:
                for (int i2 = 0; i2 < NATURAL_ITEMS.length; i2++) {
                    starterItems.add(new Item(NATURAL_ITEMS[i2][0], NATURAL_ITEMS[i2][1]));
                }
                break;
            case ROYAL_MODE:
                for (int i3 = 0; i3 < ROYAL_ITEMS.length; i3++) {
                    starterItems.add(new Item(ROYAL_ITEMS[i3][0], ROYAL_ITEMS[i3][1]));
                }
                break;
            case STANDARD_IRONMAN_MODE:
                for (int i4 = 0; i4 < STD_IRONMAN_ITEMS.length; i4++) {
                    starterItems.add(new Item(STD_IRONMAN_ITEMS[i4][0], STD_IRONMAN_ITEMS[i4][1]));
                }
                break;
            case HARDCORE_IRONMAN_MODE:
                for (int i5 = 0; i5 < HDC_IRONMAN_ITEMS.length; i5++) {
                    starterItems.add(new Item(HDC_IRONMAN_ITEMS[i5][0], HDC_IRONMAN_ITEMS[i5][1]));
                }
                break;
            case ULTIMATE_IRONMAN_MODE:
                for (int i6 = 0; i6 < ULT_IRONMAN_ITEMS.length; i6++) {
                    starterItems.add(new Item(ULT_IRONMAN_ITEMS[i6][0], ULT_IRONMAN_ITEMS[i6][1]));
                }
                break;
        }
        player.getPackets().sendItems(90, false, starterItems);
    }

    public static void sendOptions(Player player) {
        player.getPackets().sendInterSetItemsOptionsScript(INTER, 46, 90, 6, 6, "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, 46, 0, 160, 0);
    }

    /*
    @param Player - means the player to receive reward
    @param gMode - means the gamemode
    @param description - means the description of the chosen gamemode
    @param compId - means the componentid to be marked as selected
     */
    public static void sendDescription(Player player, int gMode, String description, int compId) {
        starterItems.clear();
        starterItems.reset();
        gameMode = gMode;
        player.getPackets().sendHideIComponent(INTER, TXT_DESC, false); //Hide description
        player.getPackets().sendIComponentText(INTER, TXT_DESC, description);
        markCattegory(player, compId);
        player.getPackets().sendHideIComponent(INTER, COMP_CONFIRM, false); //Show confirm button
        sendOptions(player);
        sendItems(player, gMode);
    }

    public static void handleButtons(Player player, int componentId) {
        if (componentId == COMP_PERSISTANT) { //persistant mode
            sendDescription(player, PERSISTANT_MODE, "<col=ff9c24>"+GameModes.HARD_NAME+"</col> is a mode designed " +
                    "to players that enjoys lower experience rates, we tie them as a truesome hero. And for being disciplinated, the gods blessed them " +
                    "with <col=ff9c24> higher drop rates</col>, compensating their choose!<br><br>" +
                    "- <col=ff9c24>Combat Exp Rate:</col> "+ Settings.COMBAT_XP_RATE+13+"x<br>" +
                    "- <col=ff9c24>Skilling Exp Rate:</col> "+Settings.SKILLING_XP_RATE+8+"x<br>" +
                    "- <col=ff9c24>Base Drop Bonus:</col> 2,80%",COMP_PERSISTANT);
        }

        if (componentId == COMP_NATURAL) {//natural
            sendDescription(player, NATURAL_MODE, "<col=ff9c24>"+GameModes.MID_NAME+"</col> is a mode brought " +
                    "to players that doesn't like higher nor lower experience rates, we tie them as a piece of nature. Although, as they're natural, they has born " +
                    "with inner powers of<col=ff9c24> average drop rates</col>, making their choose bad and good at the same time!<br><br>" +
                    "- <col=ff9c24>Combat Exp Rate:</col> "+ Settings.COMBAT_XP_RATE+30.88+"x<br>" +
                    "- <col=ff9c24>Skilling Exp Rate:</col> "+ Settings.SKILLING_XP_RATE+25.62+"x<br>" +
                    "- <col=ff9c24>Base Drop Bonus:</col> 1,00%", COMP_NATURAL);
        }

        if (componentId == COMP_ROYAL) {//royal
            sendDescription(player, ROYAL_MODE,"<col=ff9c24>"+GameModes.EASY_NAME+"</col> is a mode that " +
                    " players who doesn't like much effort on training, we tie them as a contemporaneous player. So, considering they're lazy, they has " +
                    "the <col=ff9c24> lowest drop rates</col>, making their choose <col=ff9c24>bad for PvMers</col> and <col=ff9c24>good for Skillers</col>!<br><br>" +
                    "- <col=ff9c24>Combat Exp Rate:</col> "+ Settings.COMBAT_XP_RATE+45.25+"x<br>" +
                    "- <col=ff9c24>Skilling Exp Rate:</col> "+ Settings.SKILLING_XP_RATE+40.25+"x<br>" +
                    "- <col=ff9c24>Base Drop Bonus:</col> 0,70%", COMP_ROYAL);
        }

        if (componentId == COMP_STANDARD_IRON) {//standard iron
            sendDescription(player, STANDARD_IRONMAN_MODE, "<col=ff9c24>"+GameModes.IRONMAN_STANDARD_NAME+"</col> is a special mode created for" +
                    " players who doesn't like the cooperativity and wants to play as a lone wolf, we tie them as a astute player. So, considering they're a one man army, they has " +
                    "<col=ff9c24>higher drop rates</col>, making their choose hard but bearable. <br><br>- But there are some restrictions applied to this game mode, and a ironman: " +
                    "<col=ff9c24>cannot trade, stake, receive PKing loots, scavenge dropped items, nor play certain multiplayer minigames</col>! <br><br>" +
                    "- <col=ff9c24>Combat Exp Rate:</col> "+ Settings.COMBAT_XP_RATE+"x<br>" +
                    "- <col=ff9c24>Skilling Exp Rate:</col> "+Settings.SKILLING_XP_RATE+"x<br>" +
                    "- <col=ff9c24>Base Drop Bonus:</col> 3,90%", COMP_STANDARD_IRON);
        }
        if (componentId == COMP_HARDCORE_IRON) {//hardcore iron
            sendDescription(player, HARDCORE_IRONMAN_MODE, "<col=ff9c24>"+GameModes.IRONMAN_HARDCORE_NAME+"</col> is a special mode created for" +
                    " players who doesn't like the cooperativity and wants to play as a lone wolf, also, <col=ff9c24>they don't count on dying, because dying in this mode will " +
                    "instantly convert their account into a standard ironman</col>. We also tie them as a astute player. So, considering they're a one man army, they has " +
                    "<col=ff9c24>higher drop rates</col>, making their choose harcore. <br><br>- But there are some restrictions applied to this game mode, and a ironman: " +
                    "<col=ff9c24>cannot trade, stake, receive PKing loots, scavenge dropped items, nor play certain multiplayer minigames. Also, you only have 1 life," +
                    "so dying in this mode will result in being downgraded to standard ironman</col>! <br><br>" +
                    "- <col=ff9c24>Combat Exp Rate:</col> "+ Settings.COMBAT_XP_RATE+"x<br>" +
                    "- <col=ff9c24>Skilling Exp Rate:</col> "+Settings.SKILLING_XP_RATE+"x<br>" +
                    "- <col=ff9c24>Base Drop Bonus:</col> 4,40%", COMP_HARDCORE_IRON);
        }
        if (componentId == COMP_ULTIMATE_IRON) {//ultimate iron
            sendDescription(player, ULTIMATE_IRONMAN_MODE, "<col=ff9c24>"+GameModes.IRONMAN_ULTIMATE_NAME+"</col> is a special mode created for" +
                    " players who doesn't like the cooperativity and wants to play as a lone wolf, also, <col=ff9c24>they doesn't have a bank</col>. We tie them as a astute player. So, considering they're a one man army, they has " +
                    "<col=ff9c24>higher drop rates</col>, making their choose the hardest one. <br><br>- But there are some restrictions applied to this game mode, and a ironman: " +
                    "<col=ff9c24>cannot trade, stake, receive PKing loots, scavenge dropped items, nor play certain multiplayer minigames</col> also, players on this" +
                    "mode <col=ff9c24>doesnt have access to bank</col>, and will only use inventory to stock up items! <br><br>" +
                    "- <col=ff9c24>Combat Exp Rate:</col> "+ Settings.COMBAT_XP_RATE+"x<br>" +
                    "- <col=ff9c24>Skilling Exp Rate:</col> "+Settings.SKILLING_XP_RATE+"x<br>" +
                    "- <col=ff9c24>Base Drop Rate:</col> 4,60%", COMP_ULTIMATE_IRON);
        }

        if (componentId == 6) {//close
            gameMode = 0;
            player.closeInterfaces();
        }

        if (componentId == 45) {//confirm
            player.closeInterfaces();
            switch (gameMode) {
                case PERSISTANT_MODE:
                    player.setGameMode(GameModes.HARD_XP);
                    receiveStarter(player, PERSISTANT_ITEMS, false);
                    break;
                case NATURAL_MODE:
                    player.setGameMode(GameModes.MID_XP);
                    receiveStarter(player, NATURAL_ITEMS, false);
                    break;
                case ROYAL_MODE:
                    player.setGameMode(GameModes.EASY_XP);
                    receiveStarter(player, ROYAL_ITEMS, false);
                    break;
                case STANDARD_IRONMAN_MODE:
                    player.setGameMode(GameModes.IRONMAN_STANDARD);
                    receiveStarter(player, STD_IRONMAN_ITEMS, false);
                    break;
                case HARDCORE_IRONMAN_MODE:
                    player.setGameMode(GameModes.IRONMAN_HARDCORE);
                    receiveStarter(player, HDC_IRONMAN_ITEMS, false);
                    break;
                case ULTIMATE_IRONMAN_MODE:
                    player.setGameMode(GameModes.IRONMAN_ULTIMATE);
                    receiveStarter(player, ULT_IRONMAN_ITEMS, true);
                    break;
            }
            player.getControlerManager().removeControlerWithoutCheck();
            player.receiveStarter();
        }
    }

    /*
    @param Player - means the player to receive reward
    @param items - the array (itemId,itemquantity) to be received.
    @param ultimateIron - true if player is ultimateIronman
     */
    public static void receiveStarter(Player player, int[][] items, boolean ultimateIron) {
        if (ultimateIron) {
            for (int i = 0; i < items.length;i++) {
                player.getInventory().addItem(items[i][0], items[i][1]);
            }
        } else {
            for (int i = 0; i < items.length;i++) {
                player.getBank().addItem(items[i][0], items[i][1] ,true);
            }
        }
    }
}