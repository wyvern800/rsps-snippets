package com.rs.game.minigames.raids;

import com.rs.Settings;
import com.rs.game.player.Player;
import com.rs.game.player.content.Colors;

import java.util.ArrayList;

public class RaidGroup {
    public void setGroupOwner(Player groupOwner) {
        this.groupOwner = groupOwner;
    }

    private transient Player groupOwner;
    private ArrayList<Player> groupMembers;
    private RaidData.Store raidEncounter;
    private ArrayList<Player> graveYard;

    public ArrayList<Player> getGraveYard() {
        return graveYard;
    }

    private static final int MAX_MEMBERS = 5;

    RaidGroup(Player owner, RaidData.Store boss) {
        if (owner == null)
            return;
        this.groupOwner = owner;
        this.raidEncounter = boss;
        groupMembers = new ArrayList<Player>(5);
        this.graveYard = new ArrayList<Player>(5);
        groupMembers.add(owner);
        owner.setRaidGroup(this);
        //sendInterface(owner);
    }

    /**
     * @param invited the player that is being added to the group
     */
    public void addInvitedMember(Player invited) {
        if (groupOwner.getRaidGroup().isGroupFull()) {
            invited.sm("Group is already full!");
            return;
        }
        if (groupOwner.getRaidGroup().isGroupFull()) {
            invited.sm("Group is already full!");
            return;
        }
        if (groupMembers.contains(invited)) {
            groupOwner.sm("Player already on group!");
            return;
        }
        groupMembers.add(invited);
        invited.setRaidGroup(this);
        groupOwner.sm(groupOwner.getColorByDisplay(Colors.Data.EASY_ACHIEVEMENT) + invited.getDisplayName() + " has accepted your raid group invitation!");
        invited.sm(groupOwner.getColorByDisplay(Colors.Data.RAIDS) + "You accepted joining " + groupOwner.getDisplayName() + "'s raid group.");
        groupMembers.forEach(p -> {
            updateOrRemoveInterface(p, false);
            p.getRaidGroup().setGroupOwner(groupOwner);
            if (p == invited) {
                return;
            }
            p.sm(invited.getDisplayName() + " has joined the raid group!");
        });
        updateOrRemoveInterface(invited, true);
    }

    /**
     * @param player  the raid group owner
     * @param invited the raid group member that was invited before to be removed
     */
    public void removeInvitedMember(Player player, Player invited) {
        player.sm(player.getColorByDisplay(Colors.Data.RAIDS) + "You removed " + invited.getDisplayName() + " from your group!");
        invited.sm(invited.getColorByDisplay(Colors.Data.RAIDS) + "You were removed from " + player.getDisplayName() + "'s raid group!");
        groupMembers.remove(invited);
        invited.setRaidGroup(null);
        invited.getRaidManager().closeInterface();
        groupMembers.forEach(p -> {
            updateOrRemoveInterface(p, false);
        });
        updateOrRemoveInterface(invited, true);
        //sendInterface(groupOwner);
    }


    public enum LeaveType {TELEPORT, COMPLETION, RESIGN, LOGOUT, FAIL, RESTART};

    public void leave(Player leaver, LeaveType leaveType) {
        if (leaver.getRaidGroup() != null && !leaver.getRaidGroup().getGroupMembers().isEmpty()) {
            leaver.getRaidManager().setHasRareDrop(false);
            leaver.getRaidManager().getRaidRewards().clear();
            leaver.getRaidManager().setHasReceivedRewards(false);
            leaver.getRaidGroup().getGroupMembers().forEach(p -> {
                updateOrRemoveInterface(p, false);
            });
            updateOrRemoveInterface(leaver, true);
            if (leaver.equals(groupOwner)) {
                sendLeaveMessage(leaver, leaveType, true);
                switch (leaveType) {
                    case RESIGN:
                    case LOGOUT:
                    case TELEPORT:
                        if (leaver.getControlerManager().getControler() instanceof RaidController) {
                            ((RaidController) leaver.getControlerManager().getControler()).getGroup().clear();
                        }
                        if (leaver.getRaidGroup().getGroupMembers().size() == 1) {
                            leaver.sm("You disbanded your raid group and was kicked!");
                        } else {
                            leaver.sm("You disbanded your raid group and all members were kicked!");
                        }
                        leaver.getRaidGroup().getGroupMembers().clear();
                        leaver.setRaidGroup(null);
                        break;
                    case FAIL:
                        if (!(leaver.getControlerManager().getControler() instanceof RaidLobby)) {
                            leaver.setNextWorldTile(RaidManager.RAIDS_LOBBY);
                        }
                        if (leaver.getControlerManager().getControler() instanceof RaidController) {
                            leaver.getRaidGroup().getGraveYard().remove(leaver);
                            ((RaidController) leaver.getControlerManager().getControler()).getGroup().remove(leaver);
                        }
                        leaver.getRaidManager().setStage(null);
                        break;
                    case COMPLETION:
                        if (!(leaver.getControlerManager().getControler() instanceof RaidLobby)) {
                            leaver.setNextWorldTile(RaidManager.RAIDS_LOBBY);
                        }
                        leaver.sm("Congratulations, you managed to slay the boss and complete the Raid!");
                        break;
                    case RESTART:
                        leaver.getRaidManager().restartRaid();
                        break;
                }
                if (Settings.DEBUG) {
                    System.out.println("[" + getClass().getSimpleName() + "] LeaveType: [" + leaveType + "] - Player: [" + leaver.getDisplayName() + "] (Group OWNER)");
                }
            } else {
                sendLeaveMessage(leaver, leaveType, false);
                switch (leaveType) {
                    case RESIGN:
                    case LOGOUT:
                    case TELEPORT:
                        if (!(leaver.getControlerManager().getControler() instanceof RaidLobby)) {
                            leaver.setNextWorldTile(RaidManager.RAIDS_LOBBY);
                        }
                        if (leaver.getControlerManager().getControler() instanceof RaidController) {
                            ((RaidController) leaver.getControlerManager().getControler()).getGroup().remove(leaver);
                        }
                        groupMembers.remove(leaver);
                        leaver.getRaidManager().setBoundChuncks(null);
                        leaver.setRaidGroup(null);
                        break;
                    case FAIL:
                        if (!(leaver.getControlerManager().getControler() instanceof RaidLobby)) {
                            leaver.setNextWorldTile(RaidManager.RAIDS_LOBBY);
                        }
                        if (leaver.getControlerManager().getControler() instanceof RaidController) {
                            leaver.getRaidGroup().getGraveYard().remove(leaver);
                            ((RaidController) leaver.getControlerManager().getControler()).getGroup().remove(leaver);
                        }
                        leaver.stopAll();
                        break;
                    case COMPLETION:
                        leaver.sm("The raid was reset!");
                        break;
                }
                if (Settings.DEBUG) {
                    System.out.println("[" + getClass().getSimpleName() + "] LeaveType: [" + leaveType + "] - Player: [" + leaver.getDisplayName() + "] (Group MEMBER)");
                }
            }
            leaver.getPackets().sendBlackOut(0);
            leaver.getControlerManager().removeControlerWithoutCheck();
            leaver.getRaidManager().closeInterface();
            leaver.unlock();
            leaver.getTemporaryAttributes().remove("SELECTED_RAID_ENCOUNTER");
            leaver.getTemporaryAttributes().remove("SCREEN");
            leaver.reset();
            updateOrRemoveInterface(leaver, true);
        }
    }

    public void updateOrRemoveInterface(Player p, boolean remove) {
        if (remove) {
            if (p.getInterfaceManager().containsInterface(3041) || p.getInterfaceManager().containsInterface(3042) || p.getInterfaceManager().containsInterface(3043)) {
                p.closeInterfaces();
            }
        } else {
            if (p.getInterfaceManager().containsInterface(3041) || p.getInterfaceManager().containsInterface(3042) || p.getInterfaceManager().containsInterface(3043)) {
                RaidInterfaces.sendInterface(p);
            }
        }
    }

    private void sendLeaveMessage(Player leaver, LeaveType leaveType, boolean owner) {
        if (owner) {
            for (Player member : groupMembers) {
                if (member == groupOwner) {
                    if (!(member.getControlerManager().getControler() instanceof RaidLobby)) {
                        member.setNextWorldTile(RaidManager.RAIDS_LOBBY);
                    }
                    member.stopAll();
                    if (leaveType != LeaveType.RESTART) {
                        member.getRaidManager().completelyDestroyRaid();
                    }
                } else {
                    if (leaveType == LeaveType.COMPLETION) {
                        member.sm(member.getColorByDisplay(Colors.Data.RAIDS) + "You were removed from the raid group as the leader has left!");
                    } else {
                        if (leaveType != LeaveType.FAIL && leaveType != LeaveType.RESTART) {
                            member.sm(member.getColorByDisplay(Colors.Data.RAIDS) + "You were teleported out of the instance as the leader did the same!");
                        }
                    }
                    if (!(member.getControlerManager().getControler() instanceof RaidLobby)) {
                        member.setNextWorldTile(RaidManager.RAIDS_LOBBY);
                    }
                    member.stopAll();
                    member.getRaidManager().setBoundChuncks(null);
                    if (leaveType != LeaveType.COMPLETION && leaveType != LeaveType.RESTART && leaveType != LeaveType.FAIL) {
                        member.setRaidGroup(null);
                    }
                }
                member.getRaidManager().setHasRareDrop(false);
                member.getRaidManager().getRaidRewards().clear();
                member.getRaidManager().setHasReceivedRewards(false);
            }
        } else {
            for (Player member : groupMembers) {
                if (leaveType != LeaveType.FAIL) {
                    member.sm(leaver.getColorByDisplay(Colors.Data.RAIDS) + leaver.getDisplayName() + " just left your raid group!");
                }
            }
        }
    }

    /*
     * Returns the raid group player names
     */
    public void getGroupList(Player player) {
        StringBuilder stb = new StringBuilder();
        for (Player invitedMember : groupMembers) {
            stb.append(invitedMember.getDisplayName()).append(", ");
        }
        String members = stb.toString();
        player.sm(members);
    }

    public String getGroupList() {
        StringBuilder stb = new StringBuilder();
        for (Player invitedMember : groupMembers) {
            stb.append(invitedMember.getDisplayName()).append(", ");
        }
        return (stb.toString());
    }

    boolean isGroupFull() {
        return (groupMembers.size() >= MAX_MEMBERS);
    }

    public Player getGroupOwner() {
        return groupOwner;
    }

    public ArrayList<Player> getGroupMembers() {
        return groupMembers;
    }

    public RaidData.Store getRaidEncounter() {
        return raidEncounter;
    }
}