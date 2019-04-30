package com.rs.game.player.content.collectionlog;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;

public class CollectionLog {
    static final int INTER = 3006;
    static final String SELECTED = "<col=ffb70f>";

    public static final int BOSSES = 0, CLUES = 1, MINIGAMES = 2, OTHERS = 3, RAIDS = 4;
    public static int pages;

    public static ItemsContainer<Item> Rewards = new ItemsContainer<Item>(200, true);


    public static void sendInterface(Player player, int page) {
        pages = page;
        if (pages == BOSSES)
            switch (pages) {
                case BOSSES:
                    sendCattegoryNames(player, "bosses");
                    sendItems(player, "bandos");
                    player.getPackets().sendItems(90, false, Rewards);
                    player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>General Graardor</col>");
                    player.getPackets().sendIComponentText(INTER, 27, "Obtained: <col=ffd900>" + checkObtained(player, "bandos") + "/8</col>");
                    player.getPackets().sendIComponentText(INTER, 28, "Kills: <col=ffffff>" + player.getBandoskills());
                    player.getInterfaceManager().sendInterface(INTER);
                    sendOptions(player);
                    break;
                case CLUES:
                    sendCattegoryNames(player, "bosses");
                    sendItems(player, "bandos");
                    player.getPackets().sendItems(90, false, Rewards);
                    player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>General Graardor</col>");
                    player.getPackets().sendIComponentText(INTER, 27, "Obtained: <col=ffd900>" + checkObtained(player, "bandos") + "/8</col>");
                    player.getPackets().sendIComponentText(INTER, 28, "Kills: <col=ffffff>" + player.getBandoskills());
                    player.getInterfaceManager().sendInterface(INTER);
                    sendOptions(player);
                    break;
            }
    }


    public static void handleButtons(Player player, int componentId, int slotId2) {
        Item item = new Item(slotId2);
        if (componentId == 52) {//bandos
            sendItems(player, "bandos");
        }
        if (componentId == 77) {//armadyl
            sendItems(player, "armadyl");
        }
        if (componentId == 36) {//zamorak
            sendItems(player, "zamorak");
        }
        if (componentId == 31) {
            player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
        }
    }

    public static void sendOptions(Player player) {
        player.getPackets().sendInterSetItemsOptionsScript(INTER, 31, 90, 7, 8, "Examine"); //12 5
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, 31, 0, 160, 0);
    }


    public static void sendItems(Player player, String bossName) {
        Rewards.clear();
        Rewards.reset();
        switch (bossName.toLowerCase()) {
            case "bandos":
                player.getPackets().sendIComponentText(INTER, 22, SELECTED + "Bosses</col>");
                player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>General Graardor</col>");
                player.getPackets().sendIComponentText(INTER, 27, "Obtained: <col=ffd900>" + checkObtained(player, "bandos") + "/8</col>");
                player.getPackets().sendIComponentText(INTER, 28, "Kills: <col=ffffff>" + player.getBandoskills());
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_HILT.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_HILT.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_CHESTPLATE.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_CHESTPLATE.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_TASSETS.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_TASSETS.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_BOOTS.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_BOOTS.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_PAGE1.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_PAGE1.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_PAGE2.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_PAGE2.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_PAGE3.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_PAGE3.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_PAGE4.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_PAGE4.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD1.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD1.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD2.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD2.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD3.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD3.getId())));
                Rewards.add(new Item(CollectionItems.Collection.BANDOS_BABY.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.BANDOS_BABY.getId())));
                player.getPackets().sendItems(90, false, Rewards);
                sendCattegoryNames(player, "bosses");
                player.getPackets().sendIComponentText(INTER, 52, SELECTED + "General Graardor</col>");
                break;
            case "armadyl":
                player.getPackets().sendIComponentText(INTER, 22, SELECTED + "Bosses</col>");
                player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>Kree'arra</col>");
                player.getPackets().sendIComponentText(INTER, 27, "Obtained: <col=ffd900>"+checkObtained(player, "armadyl")+"/5</col>");
                player.getPackets().sendIComponentText(INTER, 28, "Kills: <col=ffffff>" + player.getArmakills());
                Rewards.add(new Item(CollectionItems.Collection.ARMADYL_HILT.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ARMADYL_HILT.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ARMADYL_HELMET.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ARMADYL_HELMET.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ARMADYL_CHESTPLATE.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ARMADYL_CHESTPLATE.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ARMADYL_CHAINSKIRT.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ARMADYL_CHAINSKIRT.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ARMADYL_BABY.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ARMADYL_BABY.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD1.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD1.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD2.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD2.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD3.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD3.getId())));
                player.getPackets().sendItems(90, false, Rewards);
                sendCattegoryNames(player, "bosses");
                player.getPackets().sendIComponentText(INTER, 77, SELECTED + "Kree'arra</col>");
                break;
            case "zamorak":
                player.getPackets().sendIComponentText(INTER, 22, SELECTED + "Bosses</col>");
                player.getPackets().sendIComponentText(INTER, 26, "<col=ff9c24>K'ril Tsusaroth</col>");
                player.getPackets().sendIComponentText(INTER, 27, "Obtained: <col=ffd900>"+checkObtained(player, "zamorak")+"/8</col>");
                player.getPackets().sendIComponentText(INTER, 28, "Kills: <col=ffffff>" + player.getZamykills());
                Rewards.add(new Item(CollectionItems.Collection.ZAMORAK_HILT.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMORAK_HILT.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ZAMORAK_SPEAR.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMORAK_SPEAR.getId())));
                Rewards.add(new Item(CollectionItems.Collection.VECNA_SKULL.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.VECNA_SKULL.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ZAMY_BABY.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMY_BABY.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ZAMY_PAGE1.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMY_PAGE1.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ZAMY_PAGE2.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMY_PAGE2.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ZAMY_PAGE3.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMY_PAGE3.getId())));
                Rewards.add(new Item(CollectionItems.Collection.ZAMY_PAGE4.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.ZAMY_PAGE4.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD1.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD1.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD2.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD2.getId())));
                Rewards.add(new Item(CollectionItems.Collection.GODSWORD_SHARD3.getId(), player.getUniqueItemQuantity(CollectionItems.Collection.GODSWORD_SHARD3.getId())));
                player.getPackets().sendItems(90, false, Rewards);
                sendCattegoryNames(player, "bosses");
                player.getPackets().sendIComponentText(INTER, 36, SELECTED + "K'ril Tsusaroth</col>");
                break;
        }

    }

    public static void sendCattegoryNames(Player player, String category) {
        switch (category.toLowerCase()) {
            case "bosses":
                player.getPackets().sendIComponentText(INTER, 52, "General Graardor");
                player.getPackets().sendIComponentText(INTER, 77, "Kree'arra");
                player.getPackets().sendIComponentText(INTER, 36, "K'ril Tsusaroth");
                player.getPackets().sendIComponentText(INTER, 78, "Commander Zyliana");
                player.getPackets().sendIComponentText(INTER, 53, "Nex");
                player.getPackets().sendIComponentText(INTER, 54, "Tormented Demon");
                player.getPackets().sendIComponentText(INTER, 55, "Giant Mole");
                player.getPackets().sendIComponentText(INTER, 56, "Daganoth Kings");
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
                break;
        }
    }

    public static int checkObtained(Player player, String faction) {
        switch (faction.toLowerCase()) {
            case "bandos":
                int quant = 0;
                for (CollectionItems.Collection item : CollectionItems.Collection.values()) {
                    if (item.getType() == CollectionItems.CollectionType.BANDOS) {
                        if (player.containsUniqueItem(item.getId())) {
                            quant++;
                        }
                    }
                }

                /*for (final CollectionItems.ShopType type : CollectionItems.ShopType.values()) {
                    if (type == CollectionItems.ShopType.BANDOS) {
                        if (player.containsUniqueItem(CollectionItems.ShopType))
                    }
                }

                int quant1 = 0;
                for (int i = 0; i < CollectionItems.bandosItems.length; i++) {
                    if (player.containsUniqueItem(CollectionItems.bandosItems[i])) {
                        quant1++;
                    }
                }*/
                return quant;
            case "armadyl":
                int quant2 = 0;
                for (CollectionItems.Collection item : CollectionItems.Collection.values()) {
                    if (item.getType() == CollectionItems.CollectionType.ARMADYL) {
                        if (player.containsUniqueItem(item.getId())) {
                            quant2++;
                        }
                    }
                }
                return quant2;
            default:
                return 0;
        }
    }
}