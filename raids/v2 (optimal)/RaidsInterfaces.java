package com.rs.game.player.content.raids;

import com.rs.game.player.Player;
import com.rs.game.player.content.raids.impl.KarkathsTemple;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.Utils;

import java.util.List;

public class RaidsInterfaces {
    private static final int[] ENCOUNTERS_COMPONENTS = {20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47};
    private static final int[] RAID_MEMBERS_COMPONENTS = {23,24,25,26,27};
    private static final int[] PLAYER_ICONS = {28,29,30,31,32};
    private static final int[] PLAYER_MARKED = {3893, 3895, 3897, 3899, 3901};
    private static final int[] PLAYER_UNMARKED = {3894, 3896, 3898, 3900, 3902};
    private static final String UNMARKED = "<col=bf751d>";
    private static final String MARKED = "<col=ff981f>";

    public static void sendInterface(Player player) {
        if (player.getRaidsManager().hasRaidGroup()) {
            if (player != player.getRaidsManager().getGroupOwner()) {
                sendInvitingComponents(player);
            } else {
                if (player.getRaidsManager().getEncounter() != null) {
                    sendInvitingComponents(player);
                } else {
                    sendCreatingComponents(player);
                }
            }
        } else {
            if (player.getTemporaryAttributes().getOrDefault("SEEN_RAIDS_EXPLANATION", Boolean.FALSE) == Boolean.FALSE) {
                sendExplanationComponents(player);
            } else {
                sendCreatingComponents(player);
            }
        }
    }


    public static void handleButtons(Player player, int componentId, int slotId, int slotId2, int packetId) {
        Screens screen = (Screens) player.getTemporaryAttributes().getOrDefault("SCREEN", Screens.EXPLANATION);
        switch (screen) {
            case EXPLANATION:
                if (componentId == 10) { // Create a group button
                    sendCreatingComponents(player);
                }
                break;
            case CREATING:
                if (componentId == 10) {
                    //send raid creation here
                    RaidsEncounter store = (RaidsEncounter) player.getTemporaryAttributes().get("SELECTED_RAID_ENCOUNTER");
                    player.getRaidsManager().createGroup(player, store);
                    sendInvitingComponents(player);
                }
                for (int i = 0; i < ENCOUNTERS_COMPONENTS.length; i++) {
                    if (ENCOUNTERS_COMPONENTS[i] == componentId) {
                        int finalI = i;
                        RaidsManager.getRaidsEncountersList().forEach(encounter-> {
                            if (encounter.id() == finalI) {
                                selectEncounter(player, encounter, finalI);
                                markSelected(player, encounter, finalI);
                            }
                        });
                    }
                }
                break;
            case INVITING:
                if (componentId == 13) {// invite a member
                    player.getPackets().sendRunScript(109, "Please enter the name of the Player");
                    player.getTemporaryAttributes().put("RAID_INVITING_MEMBER", Boolean.TRUE);
                }

                if (componentId == 15) {// leave/disband
                    if (player.getRaidsManager().hasRaidGroup()) {
                        player.getRaidsManager().leave(player, RaidsManager.LeaveType.RESIGN);
                    } else {
                        player.sm("You must be in a raid group to do that.");
                    }
                }

                if (componentId == 10) {// Start fight
                    if (player.getRaidsManager().hasRaidGroup()) {
                        if (player != player.getRaidsManager().getGroupOwner()) {
                            player.sm("You must be the raid group owner to do that.");
                        } else {
                            player.getRaidsManager().start(player);
                            player.closeInterfaces();
                        }
                    } else {
                        player.sm("You must be in a raid group to do that.");
                    }
                }

                for (int i = 0; i < RAID_MEMBERS_COMPONENTS.length; i++) { // kick from raid
                    if (RAID_MEMBERS_COMPONENTS[i] == componentId) {
                        if (player.getRaidsManager().getGroupOwner() != player) {
                            player.sm("You aren't the group owner!");
                            return;
                        }
                        Player target = player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup().get(i);
                        if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) { //Kick
                            if (player == target) {
                                player.sm("You can't kick yourself! try disbanding instead");
                                return;
                            }
                            player.getDialogueManager().startDialogue("RaidsKickD", target);
                        } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {//Promote leader
                            //TODO
                            player.sm("TODO");
                        }
                    }
                }
                break;
        }
    }

    private static void sendExplanationComponents(Player player) {
        player.getTemporaryAttributes().put("SCREEN", Screens.EXPLANATION);
        player.getTemporaryAttributes().put("SEEN_RAIDS_EXPLANATION", Boolean.TRUE);
        player.getPackets().sendIComponentText(INTERFACES[0], 8, RaidsManager.RAID_DESCRIPTION);
        player.getInterfaceManager().sendInterface(INTERFACES[0]); // sends raid selection interface
    }

    private static void sendCreatingComponents(Player player) {
        player.getTemporaryAttributes().put("SELECTED_RAID_ENCOUNTER", new KarkathsTemple());
        player.getTemporaryAttributes().put("SCREEN", Screens.CREATING);
        player.getPackets().sendIComponentText(INTERFACES[1], 10, "Confirm and Create");
        for (int i = 0; i <= ENCOUNTERS_COMPONENTS.length-1; i++) { // hide components
            player.getPackets().sendHideIComponent(INTERFACES[1], ENCOUNTERS_COMPONENTS[i], true);
        }
        sendRaidEncounters(player);
        selectEncounter(player, new KarkathsTemple(), 0);
        markSelected(player, new KarkathsTemple(), 0);
        RaidsEncounter store = (RaidsEncounter) player.getTemporaryAttributes().get("SELECTED_RAID_ENCOUNTER");
        player.getPackets().sendIComponentText(INTERFACES[1], 17, Utils.formatPlayerNameForDisplay(store.raidName()));
        player.getPackets().sendIComponentText(INTERFACES[1], 18, store.raidDescription());
        player.getInterfaceManager().sendInterface(INTERFACES[1]);
    }

    private static void selectEncounter(Player player, RaidsEncounter store, int i) {
        player.getTemporaryAttributes().put("SELECTED_RAID_ENCOUNTER", store);
        player.getPackets().sendIComponentText(INTERFACES[1], 17, Utils.formatPlayerNameForDisplay(store.raidName()));
        player.getPackets().sendIComponentText(INTERFACES[1], 18, store.raidDescription());
    }

    private static void sendInvitingComponents(Player player) {
        player.getTemporaryAttributes().put("SCREEN", Screens.INVITING);
        sendRaidMembers(player);
        player.getInterfaceManager().sendInterface(INTERFACES[2]);
    }

    private static void sendRaidEncounters(Player player) {
        for (int i = 0; i < RaidsManager.getRaidsEncountersList().size(); i++) {

            int finalI = i;
            RaidsManager.getRaidsEncountersList().forEach(raid-> {
                if (raid == null)
                    return;
                if (raid.id() == -1)
                    return;
                if (raid.id() == finalI) {
                    player.getPackets().sendHideIComponent(INTERFACES[1], ENCOUNTERS_COMPONENTS[finalI], false);
                    player.getPackets().sendIComponentText(INTERFACES[1], ENCOUNTERS_COMPONENTS[finalI], UNMARKED+Utils.formatPlayerNameForDisplay(raid.raidName()));
                }
            });
        }
        player.getInterfaceManager().resizeScrollbar(player, INTERFACES[1], 19, 14, getScrollbarSize(14));
    }

    private static int getScrollbarSize(int eachComponentHeight) {
        return (getTotalItemsByCategory(RaidsManager.getRaidsEncountersList()) * eachComponentHeight);
    }

    private static int getTotalItemsByCategory(List<RaidsEncounter> array) {
        int cont = 0;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) != null) {
                cont++;
            }
        }
        return cont;
    }

    public static void sendRaidMembers(Player player) {
        List<Player> groupMembers = player.getRaidsManager().getRaidGroup();
        Player groupOwner = player.getRaidsManager().getGroupOwner();
        for (int i = 0; i < RAID_MEMBERS_COMPONENTS.length; i++) {
            player.getPackets().sendHideIComponent(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], true);
        }
        for (int i =0; i < PLAYER_ICONS.length;i++) {
            player.getPackets().sendIComponentSprite(INTERFACES[2], PLAYER_ICONS[i], PLAYER_UNMARKED[i]);
        }
        if (player.getRaidsManager().getGroupOwner() != player) {
            player.getPackets().sendHideIComponent(INTERFACES[2], 13, true);
            player.getPackets().sendHideIComponent(INTERFACES[2], 12, true);
            player.getPackets().sendHideIComponent(INTERFACES[2], 10, true);
            player.getPackets().sendHideIComponent(INTERFACES[2], 9, true);
        } else {
            player.getPackets().sendIComponentText(INTERFACES[2], 15, "Disband");
        }
        if (player.getRaidsManager().hasRaidGroup()) {
            player.getPackets().sendIComponentText(INTERFACES[2], 18, Utils.formatPlayerNameForDisplay(player.getRaidsManager().getGroupOwner().getRaidsManager().getEncounter().raidName()));
            for (int i = 0; i < groupMembers.size(); i++) {
                if (groupMembers.get(i) == null)
                    continue;
                player.getPackets().sendIComponentSprite(INTERFACES[2], PLAYER_ICONS[i], PLAYER_MARKED[i]);
                player.getPackets().sendHideIComponent(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], false);
                player.getPackets().sendIComponentText(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], (groupMembers.get(i) == groupOwner ? groupMembers.get(i).getDisplayName() + " (<col=ffb300>Owner</col>)" : groupMembers.get(i).getDisplayName()));
            }
        }
    }

    public static void updateLobbyGroupList(Player player) {
        List<Player> groupMembers = player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();
        Player groupOwner = player.getRaidsManager().getGroupOwner();
        for (int raidMembersComponent : RAID_MEMBERS_COMPONENTS) {
            player.getPackets().sendHideIComponent(INTERFACES[2], raidMembersComponent, true);
        }
        for (int i =0; i < PLAYER_ICONS.length;i++) {
            player.getPackets().sendIComponentSprite(INTERFACES[2], PLAYER_ICONS[i], PLAYER_UNMARKED[i]);
        }
        if (player.getRaidsManager().hasRaidGroup()) {
            player.getPackets().sendIComponentText(INTERFACES[2], 18, Utils.formatPlayerNameForDisplay(player.getRaidsManager().getGroupOwner().getRaidsManager().getEncounter().raidName()));
            for (int i = 0; i < groupMembers.size(); i++) {
                if (groupMembers.get(i) == null)
                    continue;
                player.getPackets().sendIComponentSprite(INTERFACES[2], PLAYER_ICONS[i], PLAYER_MARKED[i]);
                player.getPackets().sendHideIComponent(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], false);
                player.getPackets().sendIComponentText(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], (groupMembers.get(i) == groupOwner ? groupMembers.get(i).getDisplayName() + " (<col=ffb300>Owner</col>)" : groupMembers.get(i).getDisplayName()));
            }
        }
    }

    private static void markSelected(Player player, RaidsEncounter store, int i2) {
        sendRaidEncounters(player);
        player.getPackets().sendIComponentText(INTERFACES[1], ENCOUNTERS_COMPONENTS[i2], MARKED+Utils.formatPlayerNameForDisplay(store.raidName()));
    }

    public enum Screens {EXPLANATION, CREATING,INVITING};

    private static final int[] INTERFACES = {3041,3042,3043};
}