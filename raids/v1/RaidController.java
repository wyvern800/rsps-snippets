package com.rs.game.minigames.raids;

import com.rs.Settings;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;

import java.util.ArrayList;
import java.util.Iterator;


public class RaidController extends Controller {
    public ArrayList<Player> getGroup() {
        return group;
    }

    public void setChestSpawned(boolean chestSpawned) {
        this.chestSpawned = chestSpawned;
    }

    private boolean chestSpawned;

    public boolean isPortalSpawned() {
        return portalSpawned;
    }

    public void setPortalSpawned(boolean portalSpawned) {
        this.portalSpawned = portalSpawned;
    }

    private boolean portalSpawned;

    private ArrayList<Player> group = new ArrayList<Player>(5);

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    private ArrayList<NPC> npcs = new ArrayList<NPC>(2);

    @Override
    public void start() {
        process();
        moved();
    }

    @Override
    public void process() {
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
            player.getRaidManager().processBoss();
            player.getRaidManager().sendInterface();
            if (player.getControlerManager().getControler() instanceof RaidController) {
                processGraveYard();
            }
        } else {
            player.getRaidManager().closeInterface();
        }
    }

    private void processGraveYard() {
        boolean allPlayersDead = false;
        final ArrayList<Player> list = player.getRaidGroup().getGraveYard();

        Iterator<Player> intd = list.iterator();
        while (intd.hasNext()) {
            Player playerAtGraveyard = intd.next();
            if (playerAtGraveyard == null || playerAtGraveyard.hasFinished() || !(playerAtGraveyard.getControlerManager().getControler() instanceof RaidController)) {
                continue;
            }
            allPlayersDead = player.getRaidGroup().getGraveYard().containsAll(player.getRaidGroup().getGroupMembers());
            if (allPlayersDead) {
                if (playerAtGraveyard.getControlerManager().getControler() instanceof RaidController) {
                    playerAtGraveyard.getRaidManager().processRaidAction(RaidManager.ActionType.ALL_PLAYERS_DEAD);
                    if (Settings.DEBUG) {
                        System.out.println("Removed a dead player: " + playerAtGraveyard.getDisplayName());
                    }
                    if (Settings.DEBUG) {
                        System.out.println("[" + getClass().getSimpleName() + "] Removed a dead player: (" + playerAtGraveyard.getDisplayName() + ")");
                    }
                    break;
                }
            }

        }
    }

    @Override
    public boolean processNPCClick1(NPC npc) {
        if (npc.getId() == 4549) { // Raid resigner
            player.getDialogueManager().startDialogue("RaidResignD");
        }
        return true;
    }


    @Override
    public boolean sendDeath() {
        player.getRaidManager().processRaidAction(RaidManager.ActionType.ONE_PLAYER_DEAD);
        return false;
    }

    @Override
    public void moved() {
        //player.sm("inside raid controller");
    }

    @Override
    public boolean logout() {
        if (player.getRaidGroup().getGraveYard().contains(player)) {
            player.getRaidGroup().getGraveYard().remove(player);
        }
        if (group.contains(player)) {
            group.remove(player);
        }
        player.getRaidGroup().leave(player, RaidGroup.LeaveType.LOGOUT);
        return true;
    }

    @Override
    public void forceClose() {
        player.getPackets().sendBlackOut(0);
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGraveYard().isEmpty()) {
            if (player.getRaidGroup().getGraveYard().contains(player)) {
                player.getRaidGroup().getGraveYard().remove(player);
            }
        }
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGraveYard().isEmpty()) {
            if (group.contains(player)) {
                group.remove(player);
                player.getRaidGroup().leave(player, RaidGroup.LeaveType.LOGOUT);
            }
        }

    }

    @Override
    public boolean processNPCClick3(NPC npc) {
        if (npc.getId() == 4549) { // Raid resigner
            player.getRaidGroup().leave(player, RaidGroup.LeaveType.RESIGN);
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        if (object.getId() == 11368) { // Portal to leave
            player.getDialogueManager().startDialogue("RaidLeaveD");
        }
        if (object.getId() == 73894 || object.getId() == 73895) {
            object.setId(73896);
        }
        if (object.getId() == 73896) {
            if (player.getRaidManager().isHasReceivedRewards()) {
                player.sm("You already received your rewards.");
                return false;
            }
            player.getRaidManager().receiveRewards();
        }
        return true;
    }

    @Override
    public boolean processMagicTeleport(WorldTile toTile) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processObjectTeleport(WorldTile toTile) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processItemTeleport(WorldTile toTile) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processItemOnNPC(NPC npc, Item item) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processItemOnPlayer(Player p2, Item item) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processObjectClick5(WorldObject object) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processObjectClick3(WorldObject object) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processObjectClick2(WorldObject object) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processNPCClick4(NPC npc) {
        player.sm("You can not do that inside the Raid!");
        return false;
    }

    @Override
    public boolean processNPCClick2(NPC npc) {
        return true;
    }

    public boolean isChestSpawned() {
        return chestSpawned;
    }
}
