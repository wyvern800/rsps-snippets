package com.rs.game.player.content.interfaces;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.magic.Magic;

public class TeleportsInterface {
    private static int cattegoryId = 0;
    private static Object[][] selectedTeleports = null;
    private static int selectedTPComponent = -1;

    private static int INTER = 3015;
    private static final int[] CAT_COMPONENTS = {35, 39, 43, 47, 51, 55, 59, 63};
    private static final int[] CAT_CLICKABLE_COMPONENTS = {37, 41, 45, 49, 53, 57, 61, 65};
    private static final int[] CAT_SPRITES_COMPONENTS = {69, 70, 71, 72, 73, 74, 75, 76};
    private static final int[] TP_CLICKABLE_COMPONENTS = {79, 84, 89, 94, 99, 104, 109, 114, 119, 124, 129, 134, 139, 144, 149, 154, 159, 164, 169};
    private static final int[] TP_COMPONENTS = {77, 82, 87, 92, 97, 102, 107, 112, 117, 122, 127, 132, 137, 142, 147, 152, 157, 162, 167};
    private static final int[] TP_SPRITES_COMPONENTS = {81, 86, 91, 96, 101, 106, 111, 116, 121, 126, 131, 136, 141, 146, 151, 156, 161, 166, 171};

    private static int getCattegoryId() {
        return cattegoryId;
    }

    private static void setCattegoryId(int cattegoryId) {
        TeleportsInterface.cattegoryId = cattegoryId;
    }
    private static Object[][] getSelectedTeleports() {
        return selectedTeleports;
    }

    private static void setSelectedTeleports(Object[][] selectedTeleports) {
        TeleportsInterface.selectedTeleports = selectedTeleports;
    }
    private static int getSelectedTPComponent() {
        return selectedTPComponent;
    }

    private static void setSelectedTPComponent(int selectedTPComponent) {
        TeleportsInterface.selectedTPComponent = selectedTPComponent;
    }

    /*Cattegories*/
    //Cat name-SpriteID
    private static final Object[][] CATTEGORIES = {{"Cities", 20}, {"Player Killing", 523}, {"Combat Training", 226},
            {"Skilling", 1818}, {"Monsters", 1946}, {"Minigames", 1833}, {"Bosses", 838}, null};

    /*Cities*/
    private static final Object[][] TP_CITIES = {
            //Name,Desc,SpriteId,Location
            {"Grand Exchange", "The Grand Exchange is a trading system for players to buy and sell almost all tradeable items. Members have eight grand exchange slots in which they may trade items, however free players are limited to three. Traders do not need to advertise, meet each other, or even wait at the Grand Exchange for their trades to complete.", 0, new WorldTile(3165,3472,0)}, {"Varrock", "", 0 , new WorldTile(3213,3429,0)},
            {"Edgeville", "jojfoasiufoaisufasoipasufasoiufa", 0 , new WorldTile(3085,3493,0)}, {"Falador", "", 0 , new WorldTile(2965,3381,0)}, {"Draynor Village", "", 0 , new WorldTile(3093,3261,0)}, {"Rimmington", "", 0 , new WorldTile(2958,3213,0)},
            {"Brihaven", "tyrutrutyrytrytryt", 0 , new WorldTile(2805,3184,0)}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}};

    private static final Object[][] TP_PK = {
            //Name,Desc,SpriteId,Location
            {"fasufhua", "Blablabla blablabal blablabal",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    private static final Object[][] TP_COMBAT = {
            //Name,Desc,SpriteId,Location
            {"hjkhkjh", "Blablabla blablabal blablabal <br> fhuasifhasiufhsufiah <br> fhauifhsfuisahfuia <br> hfsaiufhsaufiashfia",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    private static final Object[][] TP_SKILLING = {
            //Name,Desc,SpriteId,Location
            {"hjhjkhjkhjkh", "Blablabla blablabal blablabal",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    private static final Object[][] TP_MONSTERS = {
            //Name,Desc,SpriteId,Location
            {"hjkhkjh", "Blablabla blablabal blablabal",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    private static final Object[][] TP_MINIGAMES = {
            //Name,Desc,SpriteId,Location
            {"hjnbkbh", "Blablabla blablabal blablabal",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    private static final Object[][] TP_BOSSES = {
            //Name,Desc,SpriteId,Location
            {"hjuytuy", "Blablabla blablabal blablabal",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    private static final Object[][] TP_OTHERS = {
            //Name,Desc,SpriteId,Location
            {"hjhgfhgfh", "Blablabla blablabal blablabal",   0   , new WorldTile(121,121,0)},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null}, {null, "", 0 , null},
            {null, "", 0 , null}, {null, "", 0 , null}};

    public static void sendInterface(Player player) {
        setCattegoryId(getCattegoryId());
        sendCattegories(player);
        sendTeleports(player, TP_CITIES, 0);
        setSelectedTeleports(TP_CITIES);
        player.getInterfaceManager().sendInterface(INTER);
    }

    private static void sendCattegories(Player player) {
        for (int i = 0; i < CAT_COMPONENTS.length; i++) {
            if (CATTEGORIES[i] == null) {
                player.getPackets().sendHideIComponent(INTER, CAT_COMPONENTS[i], true);
            } else {
                player.getPackets().sendHideIComponent(INTER, CAT_COMPONENTS[i], false);
                player.getPackets().sendIComponentText(INTER, CAT_CLICKABLE_COMPONENTS[i], (String) CATTEGORIES[i][0]);
                if (CAT_SPRITES_COMPONENTS[i] != 0) {
                    player.getPackets().sendHideIComponent(INTER, CAT_SPRITES_COMPONENTS[i], false);
                    if ((Integer) CATTEGORIES[i][1] != 0) {
                        player.getPackets().sendIComponentSprite(INTER, CAT_SPRITES_COMPONENTS[i], (Integer) CATTEGORIES[i][1]);
                    } else {
                        player.getPackets().sendHideIComponent(INTER, CAT_SPRITES_COMPONENTS[i], true);
                    }
                }
            }
        }
    }

    public static void sendTeleports(Player player, Object[][] teleport, int id) {
        player.getPackets().sendHideIComponent(INTER, 28, false);
        player.getPackets().sendIComponentText(INTER, 28, "Teleports - "+ CATTEGORIES[id][0]);
        for (int i = 0; i < TP_COMPONENTS.length; i++) {
            if (teleport[i][0] == null) {
                player.getPackets().sendHideIComponent(INTER, TP_COMPONENTS[i], true);
            } else {
                player.getPackets().sendIComponentText(INTER, TP_CLICKABLE_COMPONENTS[i], teleport[i][0]+"</col>");
                if (TP_SPRITES_COMPONENTS[i] != 0) {
                    player.getPackets().sendHideIComponent(INTER, TP_COMPONENTS[i], false);
                    if ((Integer) teleport[i][2] != 0) {
                        player.getPackets().sendHideIComponent(INTER, TP_SPRITES_COMPONENTS[i], false);
                        player.getPackets().sendIComponentSprite(INTER, TP_SPRITES_COMPONENTS[i], (Integer) teleport[i][2]);
                    } else {
                        player.getPackets().sendHideIComponent(INTER, TP_SPRITES_COMPONENTS[i], true);
                    }
                }
                player.getPackets().sendHideIComponent(INTER, 19, false); //Show teleports panel
            }
        }
    }

    @SuppressWarnings("Duplicates")
    public static void handleButtons(Player player, int componentId) {
        handleCattegoryButton(player, componentId);
        switch (getCattegoryId()) {
            case 0: //Cities
                openTeleportDescription(player, componentId, TP_CITIES);
                break;
            case 1: //Pk
                openTeleportDescription(player, componentId, TP_PK);
                break;
            case 2: //Combat
                openTeleportDescription(player, componentId, TP_COMBAT);
                break;
            case 3: //Skilling
                openTeleportDescription(player, componentId, TP_SKILLING);
                break;
            case 4: //Monsters
                openTeleportDescription(player, componentId, TP_MONSTERS);
                break;
            case 5: //Minigames
                openTeleportDescription(player, componentId, TP_MINIGAMES);
                break;
            case 6: //Bosses
                openTeleportDescription(player, componentId, TP_BOSSES);
                break;
            case 7: //Others
                openTeleportDescription(player, componentId, TP_OTHERS);
                break;
        }
        if (componentId == 184) { //Close description panel
            sendTeleports(player, getSelectedTeleports(), getCattegoryId());
        }
        if (componentId == 193) {
            processTeleport(player, getSelectedTeleports());
        }
    }

    public static void handleCattegoryButton(Player player, int componentId) {
        player.getPackets().sendHideIComponent(INTER, 177, true);
        switch (componentId) {
            case 37: //cities
                setCattegoryId(0);
                sendTeleports(player, TP_CITIES, 0);
                setSelectedTeleports(TP_CITIES);
                break;
            case 41: //Pk
                setCattegoryId(1);
                sendTeleports(player, TP_PK, 1);
                setSelectedTeleports(TP_PK);
                break;
            case 45: //combat
                setCattegoryId(2);
                sendTeleports(player, TP_COMBAT, 2);
                setSelectedTeleports(TP_COMBAT);
                break;
            case 49: //skill
                setCattegoryId(3);
                sendTeleports(player, TP_SKILLING, 3);
                setSelectedTeleports(TP_SKILLING);
                break;
            case 53: //Monster
                setCattegoryId(4);
                sendTeleports(player, TP_MONSTERS, 4);
                setSelectedTeleports(TP_MONSTERS);
                break;
            case 57: //Minigames
                setCattegoryId(5);
                sendTeleports(player, TP_MINIGAMES, 5);
                setSelectedTeleports(TP_MINIGAMES);
                break;
            case 61: //Bosses
                setCattegoryId(6);
                sendTeleports(player, TP_BOSSES, 6);
                setSelectedTeleports(TP_BOSSES);
                break;
            case 65: //Others
                setCattegoryId(7);
                sendTeleports(player, TP_OTHERS, 7);
                setSelectedTeleports(TP_OTHERS);
                break;
        }
    }

    public static void openTeleportDescription(Player player, int componentId, Object[][] teleport) {
        for (int i = 0; i < TP_CLICKABLE_COMPONENTS.length; i++) {
            if (componentId == TP_CLICKABLE_COMPONENTS[i]) {
                String description = (String) teleport[i][1];
                player.getPackets().sendHideIComponent(INTER, 19, true); //Hide teleports panel
                player.getPackets().sendHideIComponent(INTER, 176, false);
                player.getPackets().sendHideIComponent(INTER, 177, false);
                player.getPackets().sendHideIComponent(INTER, 67, false);
                player.getPackets().sendIComponentText(INTER, 183, (String) teleport[i][0]);
                player.getPackets().sendIComponentText(INTER, 191, description.replaceAll(".{39}", "$0<br>")); //(String) teleport[i][1];
                setSelectedTPComponent(componentId);
                if (player.isDebugMode())
                player.sm("Component id clicked: "+ TP_CLICKABLE_COMPONENTS[i]);
            }
        }
    }

    public static void processTeleport(Player player, Object[][] teleport) {
        for (int i = 0; i < TP_CLICKABLE_COMPONENTS.length; i++) {
            if (getSelectedTPComponent() == TP_CLICKABLE_COMPONENTS[i]) {
                Magic.sendLunarTeleportSpell(player, 0, 0, (WorldTile) teleport[i][3]);
                if (player.isDebugMode())
                player.sm("Component id clicked: "+ TP_CLICKABLE_COMPONENTS[i]);
            }
        }
    }
}