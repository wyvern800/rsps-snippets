package com.rs.game.player.content.interfaces;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.ItemExamines;
import com.rs.utils.Utils;

public class Shops2 {
     /*
     * @author Sagacity - 01/04/2019
     * To create new shops you have to follow these steps:
     * 1 - Create a array with the items (only 40 per shops)
     * 2 - Add the array name to CATTEGORIES array.
     * 3 - Add the curency check to the getPointsReward() and setPointsReward()
     * 4 - Shop is pretty much done :) (20 is the maximum amount of cattegories the interface can hold).
     */
     
    private static int cattegory = 0;

    public static int getPrice() {
        return price;
    }

    public static void setPrice(int price) {
        Shops2.price = price;
    }

    private static int price = -1;

    public static int getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(int selectedItem) {
        Shops2.selectedItem = selectedItem;
    }

    private static int selectedItem = -1;

    public static Object[][] getSelectedCattegory() {
        return selectedCattegory;
    }

    public static void setSelectedCattegory(Object[][] selectedCattegory) {
        Shops2.selectedCattegory = selectedCattegory;
    }

    private static Object[][] selectedCattegory = null;

    private static int INTER = 3018; //comecei no 228
    private static int SHOP_NAME = 16, ITEM_NAME = 324;
    private static final int[] ITM_COMPS = {107, 114, 121, 128, 135, 142, 149, 155, 161, 168, 174, 180, 186, 192, 198, 204, 210, 216, 222, 228, 234, 240, 246, 252, 258, 264, 270, 276, 282, 288, 294, 300, 306, 312, 318};
    private static final int[] ITM_PRICE_COMP = {109, 116, 123, 130, 137, 144, 151, 157, 163, 170, 176, 182, 188, 194, 201, 206, 212, 218, 224, 230, 236, 242, 248, 254, 260, 266, 272, 278, 284, 290, 296, 302, 308, 314, 320};
    private static final int[] ITM_SPRITE_COMPS = {110, 117, 124, 131, 138, 145, 152, 158, 164, 171, 177, 183, 189, 195, 201, 207, 213, 219, 225, 231, 237, 243, 249, 255, 261, 267, 273, 279, 285, 291, 297, 303, 309, 315, 321};
    private static final int[] ITM_SLOT_COMPS = {119, 120, 127, 134, 141, 148, 154, 166, 167, 173, 179, 185, 191, 197, 203, 209, 215, 221, 227, 233, 239, 245, 251, 257, 263, 269, 275, 281, 287, 293, 299, 305, 311, 317, 323};
    private static final int[] CAT_COMPS = {27, 31, 35, 39, 43, 47, 51, 55, 59, 63, 67, 71, 75, 79, 83, 87, 91, 95, 99, 103};
    private static final int[] CAT_CLICKABLE_COMPS = {29, 33, 37, 41, 45, 49, 53, 57, 61, 65, 69, 73, 77, 81, 85, 89, 93, 97, 101, 105};

    public static int getCattegory() {
        return cattegory;
    }

    public static void setCattegory(int cattegory) {
        Shops2.cattegory = cattegory;
    }

    private static final Object[][] PKP_ITEMS = {{14484, 230}, {4151, 350}, {9956, 500}, {18355, 300}, {29997, 250}, {null, 0},
            {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}
            , {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}};

    private static final Object[][] DONATOR_ITEMS = {{ 27853, 1 }, { 27850, 10 }, { 27851, 18 }, { 27852, 25 }, { 27854, 50 }, { 27855, 78 },{ 27857, 140 },
            { 1046, 50 }, { 1040, 56 }, { 1044, 61 }, { 1038, 63 }, { 1048, 65 }, { 1042, 65 },
            { 1053, 25 }, { 1055, 25 }, { 1057, 25 }, { 1050, 70 }, { 9920, 10 }, { 24437, 35 },
            { 10728, 10 }, { 10727, 10 }, { 10726, 10 }, { 10724, 10 }, { 10725, 10 },
            { 9470, 15 }, { 22215, 20 }, { 22217, 20 }, { 22218, 20 }, { 14595, 20 }, { 14603, 20 }, { 14605, 10 },  { 14602, 10 }, {null, 0}
            , {null, 0}
            , {null, 0}};

    private static final Object[][] VOTING_ITEMS = {{158, 5}, {128, 10}, {157, 15}, {1647, 25}, {184, 35}, {null, 0},
            {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}
            , {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}};

    private static final Object[][] PRESTIGE_ITEMS = {{1518, 55}, {1328, 100}, {1527, 10}, {16447, 154}, {1824, 124}, {null, 0},
            {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}
            , {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}};

    private static final Object[][] INFERNAL_ITEMS = {{1598, 554}, {13228, 800}, {1547, 584}, {1582, 647}, {3654, 254}, {null, 0},
            {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}
            , {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}, {null, 0}};


    private static final Object[][] CATTEGORIES = {{"PK Points", 439, PKP_ITEMS}, {"Donator Credits", 6273, DONATOR_ITEMS}, {"Voting Points", 251, VOTING_ITEMS}, {"Prestige Points", 256, PRESTIGE_ITEMS}, {"Infernal Points", 6277, INFERNAL_ITEMS}, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null};

    public static void sendInterface(Player player) {
        setCattegory(0);
        setSelectedItem(-1);
        setSelectedCattegory(PKP_ITEMS);
        sendCattegories(player);
        sendShopItems(player, PKP_ITEMS, 0);
        player.getInterfaceManager().sendInterface(INTER);
    }

    private static void sendShopItems(Player player, Object[][] cattegory, int id) {
        player.getPackets().sendIComponentText(INTER, SHOP_NAME, "" + CATTEGORIES[id][0] + " Shop");
        player.getPackets().sendHideIComponent(INTER, ITEM_NAME, false);
        updateCurrency(player);
        for (int i = 0; i < ITM_SLOT_COMPS.length; i++) {
            sendOptions(player, i);
            if (cattegory[i][0] == null) {
                player.getPackets().sendHideIComponent(INTER, ITM_COMPS[i], true);
                continue;
            } else {
                for (int i2 = 0; i2 < ITM_SPRITE_COMPS.length; i2++) {
                    player.getPackets().sendIComponentSprite(INTER, ITM_SPRITE_COMPS[i2], (Integer) CATTEGORIES[getCattegory()][1]);
                }
                player.getPackets().sendHideIComponent(INTER, ITM_COMPS[i], false);
                player.getPackets().sendIComponentText(INTER, ITM_PRICE_COMP[i], "" + cattegory[i][1]);
                player.getPackets().sendItems(i, new Item[]{new Item((Integer) cattegory[i][0], 1)});
            }
        }
    }

    public static void handleButtons(Player player, int componentId, int packetId, int slotId2) {
        handleCattegoryButton(player, componentId);
        for (int i3 = 0; i3 < ITM_SLOT_COMPS.length; i3++) {
            if (componentId == ITM_SLOT_COMPS[i3]) {
                handleSelectItem(player, componentId, slotId2, getSelectedCattegory());
                setSelectedItem(slotId2);
                switch (packetId) {
                    case 67: //buy 1
                        buyItem(player, getCattegory(), 1);
                        break;
                    case 5: //buy 10
                        buyItem(player, getCattegory(), 10);
                        break;
                    case 55: //buy 50
                        buyItem(player, getCattegory(), 50);
                        break;
                    case 68: //buy 100
                        buyItem(player, getCattegory(), 100);
                        break;
                }
            }
        }
        switch (componentId) {
            case 328: //Close info tab:
                player.getPackets().sendHideIComponent(INTER, 325, true); //full panel
                player.getPackets().sendHideIComponent(INTER, 17, false); //full panel
            break;
        }
    }

    public static void handleCattegoryButton(Player player, int componentId) {
        //player.getPackets().sendHideIComponent(INTER, 177, true);
        for (int i = 0; i < CATTEGORIES.length; i++) {
            if (componentId == CAT_CLICKABLE_COMPS[i]) {
                setCattegory(i);
                sendShopItems(player, (Object[][]) CATTEGORIES[i][2], i);
                setSelectedCattegory((Object[][]) CATTEGORIES[i][2]);
            }
        }
    }

    public static void sendItemInfo(Player player, int componentId, int slotId2, Object[][] cattegory) {
        player.getPackets().sendHideIComponent(INTER, 17, true); //full panel
        player.getPackets().sendHideIComponent(INTER, 336, true); //buy button
        Item item = new Item(slotId2);
        String bonuses = "<col=ff9c24>Attack Bonuses</col><br>" + "Stab: "+item.getDefinitions().getStabAttack()+" - Slash: "+item.getDefinitions().getSlashAttack()+"<br>" + "Crush: "+item.getDefinitions().getCrushAttack()+" - Magic: "+item.getDefinitions().getMagicAttack()+"<br>"+ "Ranged: "+item.getDefinitions().getRangeAttack()+"<br>"+"<col=ff9c24>Defence Bonuses</col><br>"+"Stab: "+item.getDefinitions().getStabAttack()+" - Slash: "+item.getDefinitions().getSlashAttack()+"<br>" +"Crush: "+item.getDefinitions().getCrushAttack()+" - Magic: "+item.getDefinitions().getMagicAttack()+"<br>"+"Ranged: "+item.getDefinitions().getRangeAttack()+" - Summ:" +item.getDefinitions().getSummoningDef()+"<br>"+"Ab. ML: "+item.getDefinitions().getAbsorveMeleeBonus()+" - Ab. MG: "+item.getDefinitions().getAbsorveMageBonus()+"<br>" +"Ab. RG: "+item.getDefinitions().getAbsorveRangeBonus()+"<br>"+"<col=ff9c24>Other Bonuses</col><br>"+"STR: "+item.getDefinitions().getStrengthBonus()+" - RSTR: "+item.getDefinitions().getRangedStrBonus()+"<br>" +"Pray: "+item.getDefinitions().getPrayerBonus()+" - MDMG: "+item.getDefinitions().getMagicDamage()+"<br>";
        player.getPackets().sendHideIComponent(INTER, 325, false);
        player.getPackets().sendIComponentText(INTER, 335, "<u><col=ff9c24>"+new Item(slotId2).getName()+"</col></u><br><br>"+ (new Item(slotId2).getDefinitions().isWearItem() ? bonuses : ItemExamines.getExamine(item).replaceAll(".{20}", "$0<br>")));
        player.getPackets().sendIComponentSprite(INTER, 332, (Integer) CATTEGORIES[getCattegory()][1]);
    }

    public static void updateCurrency(Player player) {
        player.getPackets().sendIComponentText(INTER, ITEM_NAME, "I have <col=ff9c24>"+ Utils.formatNumber(getPoints(player, getCattegory()))+"</col> "+ CATTEGORIES[getCattegory()][0]+"!");
    }

    public static void handleSelectItem(Player player, int componentId, int slotId2, Object[][] cattegory) {
        for (int i = 0; i < ITM_SLOT_COMPS.length; i++) {
            if (componentId == ITM_SLOT_COMPS[i]) {
                sendItemInfo(player, componentId, slotId2, cattegory);
                player.getPackets().sendInterSetItemsOptionsScript(INTER, 334, 90, 1, 1, "");
                player.getPackets().sendUnlockIComponentOptionSlots(INTER, 334, 0, 160, 0);
                player.getPackets().sendItems(90,new Item[]{new Item((Integer) cattegory[i][0], 1)});
                player.getPackets().sendIComponentText(INTER, 331, ""+cattegory[i][1]);
                setPrice((Integer) cattegory[i][1]);
            }
        }
    }

    private static void sendCattegories(Player player) {
        for (int i = 0; i < CAT_COMPS.length; i++) {
            if (CATTEGORIES[i] == null) {
                player.getPackets().sendHideIComponent(INTER, CAT_COMPS[i], true);
            } else {
                player.getPackets().sendHideIComponent(INTER, CAT_COMPS[i], false);
                player.getPackets().sendIComponentText(INTER, CAT_CLICKABLE_COMPS[i], (String) CATTEGORIES[i][0]);
                if ((Integer) CATTEGORIES[i][1] != 0) {
                    player.getPackets().sendHideIComponent(INTER, ITM_SPRITE_COMPS[i], false);
                    if ((Integer) CATTEGORIES[i][1] != 0) {
                    } else {
                        player.getPackets().sendHideIComponent(INTER, ITM_SPRITE_COMPS[i], true);
                    }
                }
            }
        }
    }

    public static void sendOptions(Player player, int i) {
        player.getPackets().sendInterSetItemsOptionsScript(INTER, ITM_SLOT_COMPS[i], i, 1, 1, "Info", "Buy 1", "Buy 10", "Buy 50","Buy 100");
        player.getPackets().sendUnlockIComponentOptionSlots(INTER, ITM_SLOT_COMPS[i], 0, 160, 0, 1, 2, 3, 4);
    }

    public static int getPoints(Player player, int type) {
        switch (type) {
            case 0: //PK points
                return player.getPKP();
            case 1: //Donator points
                return player.getGoldPoints();
            case 2: //Vote points
                return player.getVotePoints();
            case 3: //Prestige points
                return player.getPrestigePoints();
            case 4: //Infernal points
                return player.getInfernalPoints();
            default:
                return 0;
        }
    }

    public static void setPoints(Player player, int type, int quant) {
        switch (type) {
            case 0: //Pk points
                player.setPKP(getPoints(player, 0) - quant);
                break;
            case 1: //Donator points
                player.setGoldPoints(getPoints(player, 1) - quant);
                break;
            case 2: //Vote points
                player.setVotePoints(getPoints(player, 2) - quant);
                break;
            case 3: //Prestige points
                player.setPrestigePoints(getPoints(player, 3) - quant);
                break;
            case 4: //Infernal points
                player.setInfernalPoints(getPoints(player, 4) - quant);
                break;
        }
    }

    public static void buyItem(Player player, int cattegory, int quantity) {
            if (getSelectedItem() == -1 || getSelectedCattegory() == null || getPrice() == -1) {
                System.out.println("[buyItem] Null cattegory or selected item");
                return;
            }
            if (!new Item(getSelectedItem()).getDefinitions().isStackable() && quantity > 1) {
                player.sm("You can only buy stackable items with this option!");
                return;
            }
            if (getPoints(player, cattegory) < getPrice() * quantity) {
                player.sm("DOnt have money, you need at least: "+Utils.formatNumber(getPrice() * quantity)+" points!");
                return;
            }
            if (player.getInventory().getFreeSlots() < 1) {
                player.sm("free slots required");
                return;
            }
            setPoints(player, cattegory, getPrice() * quantity);
            updateCurrency(player);
            player.getInventory().addItem(getSelectedItem(), quantity);
            player.sm("Item bought");
    }
}