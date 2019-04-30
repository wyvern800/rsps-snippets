package com.rs.game.player.content.interfaces.shops;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Shops {
    private static final int INTER = 3013;
    private static final String SELECTED = "<col=ffb70f>";
    private static final int CONTAINER_ID = 90;
    private static final int SHOP_NAME_COMPONENT = 16;
    private static final int CURRENCY_NAME_COMPONENT = 19;
    private static final int PRICE = 17;
    private static final int CURRENCY_ICON = 18;

    public static final int INFERNAL_SHOP = 0, CREDITS_SHOP = 1, VOTE_SHOP = 2, DUNGEON_SHOP = 3, PK_SHOP = 4, PRESTIGE_SHOP = 5, IRONMAN_SHOP = 6, LOYALTY_SHOP = 7;

    /* Objects that will construct the shop itself*/
    public static final int[] SHOPS = {INFERNAL_SHOP, CREDITS_SHOP, VOTE_SHOP, DUNGEON_SHOP, PK_SHOP, PRESTIGE_SHOP, IRONMAN_SHOP, LOYALTY_SHOP};
    private static final int[] SPRITE_IDS = {6277, 6273, 1663, 2184, 564, 553, 6277, 8653};
    private static final String[] SHOP_NAME = {"Infernal Shop", "Credits Shop", "Voting Shop", "Dungeoneering Shop", "PKers Shop", "Prestiges Shop", "Ironmans Shop", "Loyalty Shop"};
    private static final String[] CURRENCY_NAME = {"Infernal Points", "Credits", "Vote Points", "Dungeon Tokens", "PK Points", "Prestige Points", "Infernal Points", "Loyalty Points"};
    private static final ShopsItems.ShopType shopType[] = {ShopsItems.ShopType.INFERNAL, ShopsItems.ShopType.CREDITS,
            ShopsItems.ShopType.VOTE, ShopsItems.ShopType.DUNGEONEERING, ShopsItems.ShopType.PK, ShopsItems.ShopType.PRESTIGES, ShopsItems.ShopType.IRONMAN, ShopsItems.ShopType.LOYALTY};

    /*
    * The container itself
     */
    public static ItemsContainer<Item> Rewards = new ItemsContainer<Item>(200, true);

    /*
    * The method that will send the interface
     */
    public static void sendInterface(Player player, int interId) {
        final Object[] CURRENCY = {player.getCloudInPoints(), player.getGoldPoints(), player.getVotePoints(), player.getDungTokens(), player.getPKP(),
        player.getPrestigePoints(), player.getInfernalPoints(), player.getLoyaltypoints()};
        for (int i = 0; i < SHOPS.length; i++) {
                if (i == interId) {
                    sendItems(player, SHOP_NAME[i], CURRENCY_NAME[i], CURRENCY[i], SPRITE_IDS[i], shopType[i]);
                }
        }
        player.getInterfaceManager().sendInterface(INTER);
        sendOptions(player);
    }

    public static void handleButtons(Player player, int componentId, int slotId2, int packetId) {
        for (int i = 0; i < SHOPS.length; i++) {
            switch (componentId) {
                case 23: //value
                    switch (packetId) {
                        case 14: //value
                            getPrice(player, shopType[i], slotId2, CURRENCY_NAME[i]);
                            break;
                        case 67: //buy 1
                            buyItem(player, shopType[i], slotId2, CURRENCY_NAME[i], 1);
                            break;
                        case 5:  //buy 10
                            buyItem(player, shopType[i], slotId2, CURRENCY_NAME[i], 10);
                            break;
                        case 55:  //buy 100
                            buyItem(player, shopType[i], slotId2, CURRENCY_NAME[i], 100);
                            break;
                    }
                    break;
            }
        }
    }

    public static void sendItems(Player player, String shopName,String currencyName, Object currency, int spriteId, ShopsItems.ShopType shopType) {
        Rewards.clear();
        Rewards.reset();
        player.getPackets().sendIComponentSprite(INTER, CURRENCY_ICON, spriteId);
        player.getPackets().sendIComponentText(INTER, SHOP_NAME_COMPONENT,   shopName);
        player.getPackets().sendIComponentText(INTER, CURRENCY_NAME_COMPONENT,   currencyName+": "+SELECTED+currency+"</col>");
        player.getPackets().sendHideIComponent(INTER, PRICE, true);
        getItemsTable(shopType);
        player.getPackets().sendItems(CONTAINER_ID, false, Rewards);
    }

    public static void sendOptions(Player player) {
        player.getPackets().sendInterSetItemsOptionsScript(INTER, 23, CONTAINER_ID, 8, 12, "Value", "Buy 1", "Buy 10","Buy 100"); //12 5
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, 23, 0, 160, 0,1,2,3);
    }

    public static void getItemsTable(ShopsItems.ShopType shopType) {
        for (ShopsItems.Shop item : ShopsItems.Shop.values()) {
            if (item.getType() == shopType) {
                Rewards.add(new Item(item.getId(), 1));
            }
        }
    }

    public static void getPrice(Player player, ShopsItems.ShopType shopType, int slotId2, String curName) {
        player.getPackets().sendHideIComponent(INTER, PRICE, false);
        for (ShopsItems.Shop item : ShopsItems.Shop.values()) {
            if (item.getType() == shopType) {
                if (item.getId() == slotId2) {
                    player.getPackets().sendIComponentText(INTER, PRICE,   SELECTED+new Item(slotId2).getDefinitions().getName()+"</col> costs "+SELECTED+ Utils.formatNumber(item.getPrice())+"</col> "+curName+".");
                }
            }
        }
    }

    public static void buyItem(Player player, ShopsItems.ShopType shopType, int slotId2, String currencyName, int quantity) {
        for (ShopsItems.Shop item : ShopsItems.Shop.values()) {
            if (item.getType() == shopType) {
                if (item.getId() == slotId2) {
                    player.getDialogueManager().startDialogue("ShopBuyConfirm", slotId2, item.getPrice(), currencyName, quantity, shopType);
                }
            }
        }
    }

}