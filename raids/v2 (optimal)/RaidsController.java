package com.rs.game.player.content.raids;

import com.rs.game.*;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

import java.util.List;


public class RaidsController extends Controller {

    @Override
    public void start() {
        moved();
        process();
    }

    @Override
    public void process() {
        boolean hasGroup = player.getRaidsManager().hasRaidGroup();
        if (hasGroup) {
        processGraveyard();
        processBoss();
            player.getRaidsManager().sendGroupMembersInterface();
        } else {
            player.getRaidsManager().closeGroupMembersInterface();
        }
    }

    private void processBoss() {
        NPC boss = player.getRaidsManager().getGroupOwner().getRaidsManager().getBoss();
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();

        if (World.getNPCs().contains(boss)) {
            if (boss.getHitpoints() <= 0) { //boss is dead
                WorldTasksManager.schedule(new WorldTask() {
                    int tick = 0;
                    @Override
                    public void run() {
                        if (tick == 0) {
                            boss.setNextAnimation(new Animation(boss.getCombatDefinitions().getDeathEmote()));
                        } else if (tick == 2) {
                            boss.finish();
                            raidsManager.processAction(player, RaidsManager.ActionType.KILLED_THE_BOSS);
                            stop();
                        }
                        tick++;
                    }
                }, 0, 1);
                //boss.finish();
                //raidsManager.processAction(player, RaidsManager.ActionType.KILLED_THE_BOSS);
            }
        }
    }

    private void processGraveyard() {
        List<Player> graveYard = player.getRaidsManager().getGroupOwner().getRaidsManager().getGraveYard();
        List<Player> raidGroup = player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();

        boolean allDead;

        for (Player playerAtGraveyard : graveYard) {
            if (playerAtGraveyard == null || playerAtGraveyard.hasFinished()) {
                continue;
            }
            allDead = graveYard.containsAll(raidGroup);

            if (allDead) {
                    playerAtGraveyard.getRaidsManager().processAction(playerAtGraveyard, RaidsManager.ActionType.ALL_PLAYERS_DEAD);
                    break;
            }
        }
    }

    @Override
    public boolean processNPCClick1(NPC npc) {
        //if (player.getTemporaryAttributes().get("RAIDS_STATE") == RaidsManager.Stages.RUNNING) {
            player.getDialogueManager().startDialogue("RaidResignerD");
        //}
        return true;
    }


    @Override
    public boolean sendDeath() {
        if (player == null || !player.hasStarted() || player.hasFinished())
            return false;

        addToGraveyard();
        return false;
    }

    private void addToGraveyard() {
        List<Player> graveYard = player.getRaidsManager().getGroupOwner().getRaidsManager().getGraveYard();
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();
        if (!graveYard.contains(player)) {
            raidsManager.processAction(player, RaidsManager.ActionType.ONE_PLAYER_DEAD);
        }
    }

    @Override
    public void moved() {
        //player.sm("inside raid controller");
    }

    @Override
    public boolean logout() {

            player.getRaidsManager().leave(player, RaidsManager.LeaveType.LOGOUT);
     return true;
    }

    @Override
    public void forceClose() {
    }

    @Override
    public boolean processNPCClick3(NPC npc) {
        //if (player.getTemporaryAttributes().get("RAIDS_STATE") == RaidsManager.Stages.RUNNING) {

       // }
        return true;
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        switch (object.getId()) {
            case 73894: //chest
                player.getRaidsManager().processRewards();
                break;

            case 11368: //exit
                player.getDialogueManager().startDialogue("RaidsLeaveD");
                break;
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
        player.getDialogueManager().startDialogue("RaidResignerD");
        return true;
    }
}
