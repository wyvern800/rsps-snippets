package com.rs.game.player.content.collectionlog.impl;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.content.collectionlog.CollectionItems;

public class CollectionLogBosses {
    static final int INTER = 3006;
    static final String SELECTED = "<col=ffb70f>";
    static final String SELECTED2 = "<col=c4bfc3>";
    static final int CONTAINER_ID = 90;

    public static ItemsContainer<Item> Rewards = new ItemsContainer<Item>(200, true);

    public static void sendInterface(Player player) {
        /*player.getPackets().sendHideIComponent(INTER, 81, true);
        player.getPackets().sendHideIComponent(INTER, 79, true);*/
        player.getPackets().sendIComponentText(INTER, 22, SELECTED2 + "Bosses</col>");
        sendItems(player, "General Graardor", CollectionItems.CollectionType.BANDOS, CollectionItems.CollectionType.GWD, player.getBandoskills(), "Kills", 52);
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
                    sendItems(player, "General Graardor", CollectionItems.CollectionType.BANDOS, CollectionItems.CollectionType.GWD, player.getBandoskills(), "Kills", componentId);
                    break;
                case 77:
                    sendItems(player, "Kree'arra", CollectionItems.CollectionType.ARMADYL, CollectionItems.CollectionType.GWD, player.getArmakills(), "Kills", componentId);
                    break;
                case 36:
                    sendItems(player, "K'ril Tsusaroth", CollectionItems.CollectionType.ZAMORAK, CollectionItems.CollectionType.GWD, player.getZamykills(), "Kills", componentId);
                    break;
                case 78: //sara
                    sendItems(player, "Commander Zyliana", CollectionItems.CollectionType.SARADOMIN, CollectionItems.CollectionType.GWD, player.getSarakills(), "Kills", componentId);
                    break;
                case 53: //nex
                    sendItems(player, "Nex", CollectionItems.CollectionType.ZAROS, null, player.getNexkills(), "Kills", componentId);
                    break;
                case 54:
                    sendItems(player, "Tormented Demon", CollectionItems.CollectionType.TORMENTED, null, player.getTormKills(), "Kills", componentId);
                    break;
                case 55:
                    sendItems(player, "Giant Mole", CollectionItems.CollectionType.GIANT_MOLE, null, player.getgiantmollykills(), "Kills", componentId);
                    break;
                case 56:
                    sendItems(player, "Dagannoth Kings", CollectionItems.CollectionType.DKS, null, player.getDksKills(), "Kills", componentId);
                    break;
                case 57:
                    sendItems(player, "Corporeal Beast", CollectionItems.CollectionType.CORPOREAL, null, player.getCorpkills(), "Kills", componentId);
                    break;
                case 58:
                    sendItems(player, "Kalphite Queen", CollectionItems.CollectionType.KQ, null, player.getKqkills(), "Kills", componentId);
                    break;
                case 59:
                    sendItems(player, "Queen Black Dragon", CollectionItems.CollectionType.QBD, null, player.getQbdkills(), "Kills", componentId);
                    break;
                case 60:
                    sendItems(player, "King Black Dragon", CollectionItems.CollectionType.KBD, null, player.getKbdkills(), "Kills", componentId);
                    break;
                case 61:
                    sendItems(player, "Chaos Elemental", CollectionItems.CollectionType.CHAOS_ELEMENTAL, null, player.getChaoselekills(), "Kills", componentId);
                    break;
                case 62:
                    sendItems(player, "Blink", CollectionItems.CollectionType.BLINK, null, player.getBlinkKills(), "Kills", componentId);
                    break;
                case 63:
                    sendItems(player, "Sunfreet", CollectionItems.CollectionType.SUNFREET, null, player.getSunfreetkills(), "Kills", componentId);
                    break;
                /*categories*/
                case 18: //Clues
                    CollectionLogClues.sendInterface(player);
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
        player.getPackets().sendItems(CONTAINER_ID, false, Rewards);
        sendCattegoryNames(player, component, bossName);
        player.getPackets().sendIComponentText(INTER, component, SELECTED + bossName+"</col>");
    }

    public static void sendCattegoryNames(Player player, int componentId, String bossName) {
                player.getPackets().sendIComponentText(INTER, componentId, bossName);
                player.getPackets().sendIComponentText(INTER, 52, "General Graardor");
                player.getPackets().sendIComponentText(INTER, 77, "Kree'arra");
                player.getPackets().sendIComponentText(INTER, 36, "K'ril Tsusaroth");
                player.getPackets().sendIComponentText(INTER, 78, "Commander Zyliana");
                player.getPackets().sendIComponentText(INTER, 53, "Nex");
                player.getPackets().sendIComponentText(INTER, 54, "Tormented Demon");
                player.getPackets().sendIComponentText(INTER, 55, "Giant Mole");
                player.getPackets().sendIComponentText(INTER, 56, "Dagannoth Kings");
                player.getPackets().sendIComponentText(INTER, 57, "Corporeal Beast");
                player.getPackets().sendIComponentText(INTER, 58, "Kalphite Queen");
                player.getPackets().sendIComponentText(INTER, 59, "Queen Black Dragon");
                player.getPackets().sendIComponentText(INTER, 60, "King Black Dragon");
                player.getPackets().sendIComponentText(INTER, 61, "Chaos Elemental");
                player.getPackets().sendIComponentText(INTER, 62, "Blink");
                player.getPackets().sendIComponentText(INTER, 63, "Sunfreet");
                for (int i = 64; i <= 76; i++) {
                    player.getPackets().sendHideIComponent(INTER, i, true);
                }
                for (int i2 = 44; i2 <= 51; i2++) {
                    player.getPackets().sendHideIComponent(INTER, i2, true);
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
        player.getPackets().sendInterSetItemsOptionsScript(INTER, 31, CONTAINER_ID, 7, 8, "Check"); //12 5
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, 31, 0, 160, 0);
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