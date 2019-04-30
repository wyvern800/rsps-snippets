package com.rs.game.player.content.collectionlog.impl;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.content.collectionlog.CollectionItems;

public class CollectionLogClues {
    static final int INTER = 3007;
    static final String SELECTED = "<col=ffb70f>";
    static final String SELECTED2 = "<col=c4bfc3>";

    //blinks scribblings livrinho
    //componentes black line 34,35,38,39,40,41,43,44,45,46,47,48,49,50

    public static ItemsContainer<Item> Rewards = new ItemsContainer<Item>(500, true);

    public static void sendInterface(Player player) {
        Rewards.clear();
        Rewards.reset();
        /*player.getPackets().sendHideIComponent(INTER, 81, true);
        player.getPackets().sendHideIComponent(INTER, 79, true);*/
        player.getPackets().sendIComponentText(INTER, 23, SELECTED2 + "Clues</col>");
        sendItems(player, "Easy Treasure Trails", CollectionItems.CollectionType.CLUE_EASY, CollectionItems.CollectionType.ALL_CLUES, player.getCluescompleted(), "Easy clues completed", 52);
        player.getInterfaceManager().sendInterface(INTER);
        sendOptions(player);
    }

    public static void handleButtons(Player player, int componentId, int slotId2) {
        Item item = new Item(slotId2);
        switch (componentId) {
            case 31: //examine
                String itemName = new Item(slotId2).getName();
                if (player.getUniqueItemQuantity(slotId2) == 0) {
                    player.sm("I did not get any " + itemName + " yet.");
                } else {
                    player.sm("I have obtained <col=f00000>" + player.getUniqueItemQuantity(slotId2) + "</col>x " + itemName + ".");
                }
                break;
            case 52:
                sendItems(player, "Easy Treasure Trails", CollectionItems.CollectionType.CLUE_EASY, null, player.getCluescompleted(), "Easy completed", componentId);
                break;
            case 77:
                sendItems(player, "Medium Treasure Trails", CollectionItems.CollectionType.CLUE_MEDIUM, null, player.getCluescompleted(), "Medium completed", componentId);
                break;
            case 36:
                sendItems(player, "Hard Treasure Trails", CollectionItems.CollectionType.CLUE_HARD, null, player.getCluescompleted(), "Hard completed", componentId);
                break;
            case 78:
                sendItems(player, "Elite Treasure Trails", CollectionItems.CollectionType.CLUE_ELITE, null, player.getCluescompleted(), "Elite completed", componentId);
                break;
            /*categories*/
            case 17: //Bosses
                CollectionLogBosses.sendInterface(player);
                break;
            case 19: //Minigames
                CollectionLogMinigames.sendInterface(player);
                break;
            case 20: //Others
                CollectionLogOthers.sendInterface(player);
                break;
            case 79: //Raids
                CollectionLogRaids.sendInterface(player);
                break;
        }
    }

    public static void sendItems(Player player, String bossName, CollectionItems.CollectionType collectionType1, CollectionItems.CollectionType collectionType2, Object bossKills, String killOrCompleted, int component) {
        Rewards.clear();
        Rewards.reset();
        player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>"+bossName+"</col>");
        player.getPackets().sendIComponentText(INTER, 27, "Obtained: <col=ffd900>" + getObtainedItems(player, collectionType1) + "/"+getMax(collectionType1)+"</col>");
        player.getPackets().sendIComponentText(INTER, 28, killOrCompleted+": <col=ffffff>" + bossKills);
        getItemsTable(player, collectionType1);
        if (collectionType2 != null) {
            getItemsTable(player, collectionType2);
        }
        player.getPackets().sendItems(3, false, Rewards);
        sendCattegoryNames(player, component, bossName);
        player.getPackets().sendIComponentText(INTER, component, SELECTED + bossName+"</col>");
    }

    public static void sendCattegoryNames(Player player, int componentId, String bossName) {
        player.getPackets().sendIComponentText(INTER, componentId, bossName);
        player.getPackets().sendIComponentText(INTER, 52, "Easy Treasure Trails");
        player.getPackets().sendIComponentText(INTER, 77, "Medium Treasure Trails");
        player.getPackets().sendIComponentText(INTER, 36, "Hard Treasure Trails");
        player.getPackets().sendIComponentText(INTER, 78, "Elite Treasure Trails");
        //componentes black line 34,35,38,39,40,41,43,44,45,46,47,48,49,50
        player.getPackets().sendHideIComponent(INTER, 38, true);
        for (int i = 53; i <= 76; i++) {
            player.getPackets().sendHideIComponent(INTER, i, true);
        }
        for (int i2 = 38; i2 <= 51; i2++) {
            player.getPackets().sendHideIComponent(INTER, i2, true);
        }
    }

    public static void sendOptions(Player player) {
        player.getPackets().sendInterSetItemsOptionsScript(INTER, 31, 3, 7, 16, "Check"); //12 5
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, 31, 0, 160, 0);
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