package com.rs.game.player.content.raids;

import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.consumables.Foods;
import com.rs.game.player.content.consumables.Pots;
import com.rs.game.player.controllers.Controller;

public class RaidsLobbyController extends Controller {
    @Override
    public void start() {
        player.getPackets().sendBlackOut(2);
        process();
    }


    @Override
    public void process() {
        player.getRaidsManager().sendGroupMembersInterface();
        player.getRaidsManager().updateGroupListInterface();
    }

    @Override
    public void forceClose() {
        player.getPackets().sendBlackOut(0);
    }

    @Override
    public void magicTeleported(int type) {
        boolean hasGroup = player.getRaidsManager().hasRaidGroup();

        if (hasGroup) {
            player.getRaidsManager().leave(player, RaidsManager.LeaveType.TELEPORT);
        }
    }

    @Override
    public boolean logout() {
        boolean hasGroup = player.getRaidsManager().hasRaidGroup();
        if (hasGroup) {
            player.getRaidsManager().leave(player, RaidsManager.LeaveType.LOGOUT);
        }
        return true;
    }

    @Override
    public boolean canEat(Foods.Food food) {
        return false;
    }

    @Override
    public boolean canPot(Pots.Pot pot) {
        return false;
    }

    @Override
    public boolean canAttack(Entity target) {
        return false;
    }

    public static boolean isAtRaidArea(WorldTile tile) {
        int x = tile.getX();
        int y = tile.getY();

        if ((x > 3429 && x < 3439) && (y >= 3725 && y <= 3736) && tile.getPlane() == 1)
            return true;
        return false;
    }

    @Override
    public boolean processObjectTeleport(WorldTile toTile) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processItemTeleport(WorldTile toTile) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processItemOnNPC(NPC npc, Item item) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processItemOnPlayer(Player p2, Item item) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processObjectClick5(WorldObject object) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processObjectClick3(WorldObject object) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processNPCClick3(NPC npc) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }

    @Override
    public boolean processNPCClick4(NPC npc) {
        player.sm("You can not do that inside the Raid Lobby!");
        return false;
    }
}
