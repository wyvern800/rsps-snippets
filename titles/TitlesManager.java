package com.rs.game.player.content.interfaces.titles;

import com.rs.game.player.Player;
import com.rs.game.player.content.Colors;
import com.rs.game.player.content.interfaces.achievements.Achievements;
import com.rs.net.decoders.WorldPacketsDecoder;

public class TitlesManager {
    private static final int INTER = 3032, TITLE_REQS = 46, HELP_BUTT = 51, TITLE_DESC = 42;

    public static final int[] TITLE_COMPONENTS = {33, 52, 55, 58, 61, 64, 67, 70, 73, 76, 79, 82,
    85, 88, 91, 94, 97, 100, 103, 106, 109, 112, 115, 118, 121};

    public static final int[] TITLE_CLICKABLE_COMPONENTS = {35, 54, 57, 60, 63, 66, 69, 72, 75, 78, 81, 84,
            87, 90, 93, 96, 99, 102, 105, 108, 111, 114, 117, 120, 123};

    public static void sendInterface(Player player) {
        if (player.isIronmanMode()){
            player.sm("Ironmans cannot switch title, sorry for the inconvenience!");
            return;
        }
        sendTitleWithDescription(player, 0);
        sendTitleList(player);
        sendSelectedTitle(player, TITLE_CLICKABLE_COMPONENTS[0]);
        player.getInterfaceManager().sendInterface(INTER);
    }


    private static void sendTitleWithDescription(Player player, int titleId) {
        final StringBuilder stb2 = new StringBuilder();
        for (Titles.Title title : Titles.Title.values()) {
            if (title.getTitleId() == titleId) {
                player.getPackets().sendIComponentText(INTER, 24,  "Description");
                player.getPackets().sendIComponentText(INTER, TITLE_DESC,  "<col=bd6500>Name:</col> "+title.getTitleName()+"<br><col=bd6500>Status:</col> "+(getRequirements(player, titleId) ? Colors.COMPLETE+"Unlocked</col>":Colors.NONSTARTED+"Locked</col>")+"<br><br>You'l unlock this title after " +title.getTitleDescription()+".");
                if (title.getTitleRequirements() != null) {
                    for (int x2 = 0; x2 < title.getTitleRequirements().length; x2++) {
                        stb2.append((title.getTitleRequirements().length > 1 ? "- " + title.getTitleRequirements()[x2][0] + ".<br>" : title.getTitleRequirements()[x2][0] + ".<br>"));
                    }
                } else {
                    stb2.append("None.<br>");
                }
                player.getPackets().sendIComponentText(INTER,
                        TITLE_REQS, stb2.toString());
            }
        }
    }

    private static void sendTitleList(Player player) {
        int i2 = 0;

        for (int i = 0; i < TITLE_COMPONENTS.length; i++) {
            player.getPackets().sendHideIComponent(INTER, TITLE_COMPONENTS[i], true);
        }


            for (Titles.Title title : Titles.Title.values()) {
                if (title != null) {
                    if (title.getTitleId() != i2)
                        continue;
                    player.getPackets().sendHideIComponent(INTER, TITLE_COMPONENTS[i2], false);
                    player.getPackets().sendIComponentText(INTER, TITLE_CLICKABLE_COMPONENTS[i2], title.getTitleName());
                    i2++;
                }
            }
    }

    private static void sendSelectedTitle(Player player, int compId) {
        for (Titles.Title title : Titles.Title.values()) {
            if (title.getTitleCompId() == compId) {
                player.getPackets().sendIComponentText(INTER, compId, "<col=ff981f>" + title.getTitleName());
            }
        }
    }

    public static void handleButtons(Player player, int compId, int packetId) {
        for (int i = 0; i < TITLE_COMPONENTS.length; i++) {
            if (compId == TITLE_CLICKABLE_COMPONENTS[i]) {
                if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) { //Select title
                    sendTitleWithDescription(player, i);
                    sendTitleList(player);
                    sendSelectedTitle(player, compId);
                }
                if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) { //Apply Title
                    sendTitleWithDescription(player, i);
                    sendTitleList(player);
                    sendSelectedTitle(player, compId);
                    if (getRequirements(player, i)) {
                        for (Titles.Title title : Titles.Title.values()) {
                            if (title != null) {
                                if (title.getTitleId() != i)
                                    continue;
                                if (title.getTitleCompId() != compId)
                                    continue;
                                player.getAppearence().setTitle(title.getTitleRealId());
                                player.getAppearence().generateAppearenceData();
                                player.getDialogueManager().startDialogue("SimpleMessage", "Your title was succesfully set to: <col=f00000>"+title.getTitleName()+"</col>.");
                            }
                        }
                    } else {
                        player.getDialogueManager().startDialogue("SimpleMessage", "Sorry, but you haven't unlocked this title yet!");
                        sendTitleWithDescription(player, i);
                        sendTitleList(player);
                        sendSelectedTitle(player, compId);
                    }
                }
            }
        }
        if (compId == HELP_BUTT)
            player.getDialogueManager().startDialogue("SimpleMessage", "To apply a title to your character, right-click the title name from the list and then left-click the option 'Apply'!");
    }

    private static boolean getRequirements(Player player, int index) {
        switch(index) {
            case 0:
                return player.hasCompletedAchievement(Achievements.Store.OPAL_PAL);
            case 1:
                return player.hasCompletedAchievement(Achievements.Store.POWERTATOES);
            case 2:
                return player.hasCompletedAchievement(Achievements.Store.DA_RUNE_THIEF);
            case 3:
                return player.hasCompletedAchievement(Achievements.Store.NO_FLAX_ZONE);
            case 4:
                return player.hasCompletedAchievement(Achievements.Store.UNDERGROUNDER);
            case 5:
                return player.hasCompletedAchievement(Achievements.Store.THE_INFERNAL);
            case 6:
                return player.hasCompletedAchievement(Achievements.Store.MAGICHOLIC);
            case 7:
                return player.hasCompletedAchievement(Achievements.Store.THE_TREASURE_HUNTER);
            case 8:
                return player.hasCompletedAchievement(Achievements.Store.OUT_OF_CONTROL);
            case 9:
                return player.hasQuestsDone();
            case 10:
                return player.isMaxedWithoutSlayer();
            case 11:
                return player.hasNormalCompReq();
            case 12:
                return player.hasTrimmedCompReq();
            case 13:
                return player.getJadkills() >= 2;
            case 14:
                return player.getHarAkenKills() >= 2;
            case 15:
                return player.getArmakills() >= 100;
            case 16:
                return player.getBandoskills() >= 100;
            case 17:
                return player.getSarakills() >= 100;
            case 18:
                return player.getZamykills() >= 100;
            case 19:
                return player.getNexkills() >= 100;
            case 20:
                return player.getCorpkills() >= 100;
            case 21:
                return player.getTotalBossKills() >= 500;
            case 22:
                return player.getTotalBossKills() >= 1000;
            case 23:
                return player.getTasksdone() >= 100;
            case 24:
                return player.hasAllUniqueItems();
            default:
                return false;
        }
    }

}