package com.rs.game.minigames.raids;

import com.rs.game.player.Player;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.Utils;

import java.util.ArrayList;

public class RaidInterfaces {
    private static final int[] ENCOUNTERS_COMPONENTS = {20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47};
    private static final int[] RAID_MEMBERS_COMPONENTS = {23,24,25,26,27};
    private static final int[] PLAYER_ICONS = {28,29,30,31,32};
    private static final int[] PLAYER_MARKED = {3893, 3895, 3897, 3899, 3901};
    private static final int[] PLAYER_UNMARKED = {3894, 3896, 3898, 3900, 3902};
    private static final String UNMARKED = "<col=bf751d>";
    private static final String MARKED = "<col=ff981f>";

    public static void sendInterface(Player player) {
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
            if (player != player.getRaidGroup().getGroupOwner()) {
                sendInvitingComponents(player);
            } else {
                if (player.getRaidGroup().getRaidEncounter() != null) {
                    sendInvitingComponents(player);
                } else {
                    sendCreatingComponents(player);
                }
            }
        } else {
            sendExplanationComponents(player);
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
                        RaidData.Store store = (RaidData.Store) player.getTemporaryAttributes().get("SELECTED_RAID_ENCOUNTER");
                        player.getRaidManager().createRaidGroup(store);
                        sendInvitingComponents(player);
                }
                for (int i = 0; i < ENCOUNTERS_COMPONENTS.length; i++) {
                    if (ENCOUNTERS_COMPONENTS[i] == componentId) {
                        for (RaidData.Store raid : RaidData.Store.values()) {
                            if (raid.getId() == i) {
                                selectEncounter(player, raid, i);
                                markSelected(player, raid, i);
                            }
                        }
                    }
                }
                break;
            case INVITING:
                if (componentId == 13) {// invite a member
                    player.getPackets().sendRunScript(109, "Please enter the name of the Player");
                    player.getTemporaryAttributes().put("RAID_INVITING_MEMBER", Boolean.TRUE);
                }

                if (componentId == 15) {// leave/disband
                    if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
                        player.getRaidGroup().leave(player, RaidGroup.LeaveType.RESIGN);
                    } else {
                        player.sm("You must be in a raid group to do that.");
                    }
                }

                if (componentId == 10) {// Start fight
                    if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
                        if (player != player.getRaidGroup().getGroupOwner()) {
                            player.sm("You must be the raid group owner to do that.");
                        } else {
                            player.getRaidManager().startRaid();
                            player.closeInterfaces();
                        }
                    } else {
                        player.sm("You must be in a raid group to do that.");
                    }
                }

                for (int i = 0; i < RAID_MEMBERS_COMPONENTS.length; i++) { // kick from raid
                    if (RAID_MEMBERS_COMPONENTS[i] == componentId) {
                        if (player.getRaidGroup().getGroupOwner() != player) {
                            player.sm("You aren't the group owner!");
                            return;
                        }
                        Player target = player.getRaidGroup().getGroupMembers().get(i);
                        if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) { //Kick
                            if (player == target) {
                                player.sm("You can't kick yourself! try disbanding instead");
                                return;
                            }
                            player.getDialogueManager().startDialogue("RaidKickD", target);
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
        player.getPackets().sendIComponentText(INTERFACES[0], 8, RaidData.RAID_DESCRIPTION);
        player.getInterfaceManager().sendInterface(INTERFACES[0]); // sends raid selection interface
    }

    private static void sendCreatingComponents(Player player) {
        player.getTemporaryAttributes().put("SELECTED_RAID_ENCOUNTER", RaidData.Store.TEMPLE_OF_KARKATH);
        player.getTemporaryAttributes().put("SCREEN", Screens.CREATING);
        for (int i = 0; i <= ENCOUNTERS_COMPONENTS.length-1; i++) { // hide components
            player.getPackets().sendHideIComponent(INTERFACES[1], ENCOUNTERS_COMPONENTS[i], true);
        }
        sendRaidEncounters(player);
        selectEncounter(player, RaidData.Store.TEMPLE_OF_KARKATH, 0);
        markSelected(player, RaidData.Store.TEMPLE_OF_KARKATH, 0);
        RaidData.Store store = (RaidData.Store) player.getTemporaryAttributes().get("SELECTED_RAID_ENCOUNTER");
        player.getPackets().sendIComponentText(INTERFACES[1], 17, Utils.formatPlayerNameForDisplay(store.name()));
        player.getPackets().sendIComponentText(INTERFACES[1], 18, store.getRaidDescription());
        player.getInterfaceManager().sendInterface(INTERFACES[1]);
    }

    private static void selectEncounter(Player player, RaidData.Store store, int i) {
        player.getTemporaryAttributes().put("SELECTED_RAID_ENCOUNTER", store);
        player.getPackets().sendIComponentText(INTERFACES[1], 17, Utils.formatPlayerNameForDisplay(store.name()));
        player.getPackets().sendIComponentText(INTERFACES[1], 18, store.getRaidDescription());
    }

    private static void sendInvitingComponents(Player player) {
        player.getTemporaryAttributes().put("SCREEN", Screens.INVITING);
        sendRaidMembers(player);
        player.getInterfaceManager().sendInterface(INTERFACES[2]);
    }

    private static void sendRaidEncounters(Player player) {
        for (int i = 0; i < RaidData.Store.values().length; i++) {
            for (RaidData.Store raid : RaidData.Store.values()) {
                if (raid == null)
                    continue;
                if (raid.getId() == -1)
                    continue;
                if (raid.getId() == i) {
                    player.getPackets().sendHideIComponent(INTERFACES[1], ENCOUNTERS_COMPONENTS[i], false);
                    player.getPackets().sendIComponentText(INTERFACES[1], ENCOUNTERS_COMPONENTS[i], UNMARKED+Utils.formatPlayerNameForDisplay(raid.name()));
                }
            }
        }
    }

    private static void sendRaidMembers(Player player) {
        ArrayList<Player> groupMembers = player.getRaidGroup().getGroupMembers();
        Player groupOwner = player.getRaidGroup().getGroupOwner();
        for (int i = 0; i < RAID_MEMBERS_COMPONENTS.length; i++) {
            player.getPackets().sendHideIComponent(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], true);
        }
        for (int i =0; i < PLAYER_ICONS.length;i++) {
            player.getPackets().sendIComponentSprite(INTERFACES[2], PLAYER_ICONS[i], PLAYER_UNMARKED[i]);
        }
        if (player.getRaidGroup().getGroupOwner() != player) {
            player.getPackets().sendHideIComponent(INTERFACES[2], 13, true);
            player.getPackets().sendHideIComponent(INTERFACES[2], 12, true);
            player.getPackets().sendHideIComponent(INTERFACES[2], 10, true);
            player.getPackets().sendHideIComponent(INTERFACES[2], 9, true);
        } else {
            player.getPackets().sendIComponentText(INTERFACES[2], 15, "Disband");
        }
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
            player.getPackets().sendIComponentText(INTERFACES[2], 18, Utils.formatPlayerNameForDisplay(player.getRaidGroup().getRaidEncounter().name()));
            for (int i = 0; i < groupMembers.size(); i++) {
                if (groupMembers.get(i) == null)
                    continue;
                player.getPackets().sendIComponentSprite(INTERFACES[2], PLAYER_ICONS[i], PLAYER_MARKED[i]);
                player.getPackets().sendHideIComponent(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], false);
                player.getPackets().sendIComponentText(INTERFACES[2], RAID_MEMBERS_COMPONENTS[i], (groupMembers.get(i) == groupOwner ? groupMembers.get(i).getDisplayName() + " (<col=ffb300>Owner</col>)" : groupMembers.get(i).getDisplayName()));
            }
        }
    }

    private static void markSelected(Player player, RaidData.Store store, int i2) {
        sendRaidEncounters(player);
        player.getPackets().sendIComponentText(INTERFACES[1], ENCOUNTERS_COMPONENTS[i2], MARKED+Utils.formatPlayerNameForDisplay(store.name()));
    }

    public enum Screens {EXPLANATION, CREATING,INVITING};

    private static final int[] INTERFACES = {3041,3042,3043};
}