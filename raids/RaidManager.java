package com.rs.game.minigames.raids;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.Graphics;
import com.rs.game.*;
import com.rs.game.communication.discord.DiscordConstant;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.raids.data.RaidDrops;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.Colors;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class RaidManager {

    private int raidsOpened;

    /**
     * @author Sagacity - 04/12/2019
     * Raids System
     * Must have one team (with max five members);
     * Different bosses (1 at the start)
     * No time limit to kill the bosses
     */
    public static final WorldTile RAIDS_LOBBY = new WorldTile(3436, 3731, 1);

    static final int RESIZABLE_TAB_ID = 8;
    static final int FIXED_TAB_ID = 10; //8
    public static final int INTERFACE = 3006;
    private int[] components = {6, 7, 8, 9, 10};

    public void setBoundChuncks(int[] boundChuncks) {
        this.boundChuncks = boundChuncks;
    }

    private int[] boundChuncks;

    public void setStage(Stages stage) {
        this.stage = stage;
    }

    private Stages stage;

    private transient Player player;

    public RaidManager(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setRaidRewards(ItemsContainer<Item> raidRewards) {
        this.raidRewards = raidRewards;
    }

    private ItemsContainer<Item> raidRewards = new ItemsContainer<Item>(3, false);

    public boolean isHasReceivedRewards() {
        return hasReceivedRewards;
    }

    public void setHasReceivedRewards(boolean hasReceivedRewards) {
        this.hasReceivedRewards = hasReceivedRewards;
    }

    private boolean hasReceivedRewards;

    public boolean hasRareDrop() {
        return hasRareDrop;
    }

    public void setHasRareDrop(boolean hasRareDrop) {
        this.hasRareDrop = hasRareDrop;
    }

    private boolean hasRareDrop;

    public ItemsContainer<Item> getRaidRewards() {
        return raidRewards;
    }

    /**
     * Add group owner to its own team
     */
    public void createRaidGroup(RaidData.Store selectedBoss) {
        if (!(player.getControlerManager().getControler() instanceof RaidLobby)) {
            return;
        }
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
            player.sm("You are already in a Raid group.");
            return;
        }
        player.setRaidGroup(new RaidGroup(player, selectedBoss));
        player.sm("You just created a Raid group for: <col=f00000>" + Utils.formatPlayerNameForDisplay(selectedBoss.name()) + "</col>.");
    }

    /**
     * @param target sends the oarty invite to target
     */
    public void invite(Player target) {
        if (!(player.getControlerManager().getControler() instanceof RaidLobby)) {
            return;
        }
        if (!(target.getControlerManager().getControler() instanceof RaidLobby)) {
            return;
        }
        if (player.getUsername().equalsIgnoreCase(target.getUsername())) {
            player.getPackets().sendGameMessage("You cannot invite yourself!");
            return;
        }
        if (player.getSession().getIP().equals(target.getSession().getIP())) {
            player.getPackets().sendGameMessage("You cannot invite players on same IP as you!");
            return;
        }
        if (player.getRaidGroup().getGroupMembers() == null) {
            player.getPackets().sendGameMessage("You must be in a Raid group to do that.");
            return;
        }
        if (player.getRaidGroup().isGroupFull()) {
            player.getPackets().sendGameMessage("Clans can't have over 5 members.");
            return;
        }
        if (target.getRaidGroup() != null && !target.getRaidGroup().getGroupMembers().isEmpty()) {
            player.getPackets().sendGameMessage("This player is already a member of another Raid group.");
            return;
        }
        if (target.getInterfaceManager().containsScreenInter()) {
            player.getPackets().sendGameMessage("The other player is busy.");
            return;
        }
        player.getPackets().sendGameMessage("Sending " + target.getDisplayName() + " a invitation...");
        target.getPackets().sendRaidInviteMessage(player);
        target.getTemporaryAttributes().put("raidinvite", Boolean.TRUE);
    }


    public static void viewInvite(Player player, Player p2) {
        if (!(player.getControlerManager().getControler() instanceof RaidLobby)) {
            return;
        }
        if (!(p2.getControlerManager().getControler() instanceof RaidLobby)) {
            return;
        }
        if (player.getTemporaryAttributes().remove("raidinvite") != null)
            player.getDialogueManager().startDialogue("RaidInviteD", p2, player);
    }

    public void startRaid() {
        if (player.getRaidGroup().getGroupOwner() != player) {
            player.sm("You must be the group owner to start the raid");
            return;
        }
        generateMap();
        sendPlayers();
        sendNpcs(false);
        stage = Stages.RUNNING;
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] " + player.getDisplayName() + "'s Raid just started!");
        }
    }

    public void restartRaid() {
        WorldTasksManager.schedule(new WorldTask() {
            int i = 0;

            @Override
            public void run() {
                i++;
                if (i == 1) {
                    player.sm("<col=f00000>[Raid] The instance has started to reset...", true);
                    player.getRaidGroup().getGroupMembers().forEach(p -> {
                        p.lock();
                        p.getAppearence().setHidden(true);
                        p.getAppearence().generateAppearenceData();
                        p.getControlerManager().startControler("RaidController", true);
                        p.getInterfaceManager().sendFadingInterface(115);
                    });
                    completelyDestroyRaid();
                } /*else if (i == 2) {
                    player.getRaidGroup().getGroupMembers().forEach(p -> {
                        p.getControlerManager().startControler("RaidController", true);
                    });
                    completelyDestroyRaid();
                }*/ else if (i == 2) {
                    generateMap();
                    player.sm("<col=f00000>[Raid] Raid is now resetted...", true);
                    player.getRaidGroup().getGroupMembers().forEach(p -> {
                        p.getAppearence().setHidden(false);
                        p.getAppearence().generateAppearenceData();
                        p.unlock();
                        p.getInterfaceManager().closeFadingInterface();
                    });
                    sendPlayers();
                    sendNpcs(false);
                }
                stage = Stages.RUNNING;
            }
        }, 1, 3);
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] Raid was requested to be reset!");
        }
    }

    public void getBackToRaid() {
        sendPlayers();
    }

    private void generateMap() {
        final int widthRegion = player.getRaidGroup().getRaidEncounter().getRaidMap().getChunkSize()[0];
        final int heightRegion = player.getRaidGroup().getRaidEncounter().getRaidMap().getChunkSize()[1];
        final int fromRegionX = player.getRaidGroup().getRaidEncounter().getRaidMap().getBoundChuncks()[0];
        final int fromRegionY = player.getRaidGroup().getRaidEncounter().getRaidMap().getBoundChuncks()[1];
        raidsOpened++;
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] Generating " + player.getDisplayName() + "'s Raid map (maps: " + raidsOpened + ")");
        }
        stage = Stages.LOADING;
        boundChuncks = RegionBuilder.findEmptyChunkBound(widthRegion, heightRegion);
        RegionBuilder.copyMap(fromRegionX, fromRegionY, boundChuncks[0], boundChuncks[1], widthRegion, heightRegion, new int[1], new int[1]);
        for (Player group : player.getRaidGroup().getGroupMembers()) {
            if (group == null)
                continue;
            group.getRaidManager().setBoundChuncks(boundChuncks);
        }
    }

    public void destroyMap() {
        final int widthRegion = player.getRaidGroup().getRaidEncounter().getRaidMap().getChunkSize()[0];
        final int heightRegion = player.getRaidGroup().getRaidEncounter().getRaidMap().getChunkSize()[1];
        if (boundChuncks == null)
            return;
        raidsOpened--;
        if (stage != Stages.RUNNING && stage != Stages.RECEIVING_REWARDS && stage != Stages.RECEIVED_REWARDS)
            return;
        stage = Stages.DESTROYING;
        CoresManager.slowExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                RegionBuilder.destroyMap(boundChuncks[0], boundChuncks[1], widthRegion, heightRegion);
                for (Player group : player.getRaidGroup().getGroupMembers()) {
                    if (group == null)
                        continue;
                    group.getRaidManager().setBoundChuncks(null);
                }
            }
        }, 1200, TimeUnit.MILLISECONDS);
    }

    public void completelyDestroyRaid() {
        destroyMap();
        spawnChest(true);
        spawnExitPortal(true);
        sendNpcs(true);
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] Completely destroying " + player.getDisplayName() + "'s raid!");
        }
    }

    /*private void removePlayersFromMap() {
        final ArrayList<Player> list = player.getRaidGroup().getGroupMembers();
        Iterator<Player> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Player groupMember = iterator.next();
            if (groupMember.getControlerManager().getControler() instanceof RaidController) {
                ((RaidController) player.getControlerManager().getControler()).getGroup().remove(groupMember);

                final Controller controller = groupMember.getControlerManager().getControler();
                if (controller != null) {
                    controller.removeControler();
                }
                if (!(groupMember.getControlerManager().getControler() instanceof RaidLobby)) {
                    groupMember.getControlerManager().startControler("RaidLobby");
                }
            }
        }
    }*/


    private void sendPlayers() {
        final ArrayList<Player> list = player.getRaidGroup().getGroupMembers();
        Iterator<Player> iterator = list.iterator();
        while (iterator.hasNext()) {
            Player groupMember = iterator.next();
            addPlayerToMap(groupMember);
            groupMember.getRaidGroup().updateOrRemoveInterface(groupMember, true);
        }
    }

    private void addPlayerToMap(Player groupMember) {
        /*final ArrayList<Player> list = player.getRaidGroup().getGroupMembers();
        Iterator<Player> iterator = list.iterator();
        while (iterator.hasNext()) {
            Player groupMember = iterator.next();*/
        groupMember.getControlerManager().getControler().removeControler();
        groupMember.getControlerManager().startControler("RaidController");

        ((RaidController) player.getControlerManager().getControler()).getGroup().add(groupMember);
        groupMember.reset();
        groupMember.setNextWorldTile(getWorldTile(groupMember.getRaidGroup().getRaidEncounter().getRaidTiles().getEntranceTile()[0],
                groupMember.getRaidGroup().getRaidEncounter().getRaidTiles().getEntranceTile()[1]));
        groupMember.setNextAnimation(new Animation(-1));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                groupMember.getMusicsManager().playMusic(683);
                groupMember.unlock();
            }
        }, 1);
        groupMember.sm(groupMember.getColorByDisplay(Colors.Data.RAIDS) + "The " + Utils.formatPlayerNameForDisplay(groupMember.getRaidGroup().getRaidEncounter().name()) + " Raid has just started, good luck!");
        //}
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] Sending (" + player.getRaidGroup().getGroupList() + ") to (" + player.getDisplayName() + "'s) raid instance.");
        }
    }

    private void sendNpcs(boolean remove) {
        if (remove) {
            if (stage != Stages.DESTROYING)
                return;
            if (!(player.getControlerManager().getControler() instanceof RaidController))
                return;
            for (Iterator<NPC> it = ((RaidController) player.getControlerManager().getControler()).getNpcs().iterator(); it.hasNext(); ) {
                final NPC npc = it.next();
                if (npc != null)
                    npc.finish();
                it.remove();
            }
            if (player.getControlerManager().getControler() instanceof RaidController) {
                if (!((RaidController) player.getControlerManager().getControler()).getNpcs().isEmpty())
                    ((RaidController) player.getControlerManager().getControler()).getNpcs().clear();
                if (Settings.DEBUG) {
                    System.out.println("[" + getClass().getSimpleName() + "] Removing " + player.getDisplayName() + "'s raid NPCS!");
                }
            }
        } else {
            // Spawns the Raid BOSS
            final NPC n = World.spawnNPC(player.getRaidGroup().getRaidEncounter().getRaid().getNpcId(),
                    getWorldTile(player.getRaidGroup().getRaidEncounter().getRaidTiles().getBossTile()[0],
                            player.getRaidGroup().getRaidEncounter().getRaidTiles().getBossTile()[1]), -1,
                    true, true, false);
            if (player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getBaseHitpoints() != -1) {
                n.setHitpoints(getHPByGroupSize());
            }
            if (player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getNpcName() != null) {
                n.setName("<col=00c4c7>" + player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getNpcName());
            }
            if (player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getNpcLevel() != -1) {
                n.setCombatLevel(player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getNpcLevel());
            } else {
                n.setCombatLevel(getNpcLevelByGroupSize());
            }
            n.setForceAgressiveDistance(1);
            n.setForceAgressive(true);
            n.setForceMultiArea(true);
            n.setAtMultiArea(true);
            n.setForceMultiAttacked(true);
            n.setIntelligentRouteFinder(true);
            n.setForceFollowClose(true);
            n.setRun(true);
            n.setLureDelay(0);
            n.setCapDamage(600);
            n.setCantInteract(false);
            n.setSpawned(true);
            ((RaidController) player.getControlerManager().getControler()).getNpcs().add(n);
            // Spawn the resigning NPC (must be stopped)
            final NPC resigner = World.spawnNPC(4549, getWorldTile(player.getRaidGroup().getRaidEncounter().getRaidTiles().getResignTile()[0],
                    player.getRaidGroup().getRaidEncounter().getRaidTiles().getResignTile()[1]), -1, true,
                    true, false);
            resigner.setName("<col=4cff00>Resigner</col>");
            resigner.setRandomWalk(false);
            ((RaidController) player.getControlerManager().getControler()).getNpcs().add(resigner);
            if (Settings.DEBUG)
            System.out.println("[" + getClass().getSimpleName() + "] Spawning " + player.getDisplayName() + " raid NPCS!");
        }
    }

    private int getNpcLevelByGroupSize() {
        int groupSize = player.getRaidGroup().getGroupMembers().size();
        return ((groupSize == 1 ? 500 : groupSize == 2 ? 1000 : groupSize == 3 ? 1500 : groupSize == 4 ? 2000 : 2500));
    }

    public int getHPByGroupSize() {
        int baseHp = player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getBaseHitpoints();
        int groupSize = player.getRaidGroup().getGroupMembers().size();
        return ((groupSize == 1 ? baseHp : groupSize == 2 ? baseHp*2 : groupSize == 3 ? baseHp*3 : groupSize == 4 ? baseHp*4 : baseHp*5));
    }

    public boolean containsRare() {
            for (int i = 0; i < player.getRaidManager().getRaidRewards().getSize(); i++) {
                if (player.getRaidManager().getRaidRewards().containsOne(player.getRaidGroup().getRaidEncounter().getRaidDrops().getVeryRareDrops()[i])) {
                    World.sendWorldWideMsg(player, player.getRaidGroup().getRaidEncounter().getRaidDrops().getVeryRareDrops()[i].getId(), " has received " + new Item(player.getRaidGroup().getRaidEncounter().getRaidDrops().getVeryRareDrops()[i]).getName() + " at " + Utils.formatPlayerNameForDisplay(player.getRaidGroup().getRaidEncounter().name()) + " Raid in " + player.getGameModeName(false).toLowerCase() + " mode!", Color.RED, true, DiscordConstant.DROPS_CHANNEL_ID);
                    player.sm("Woah! you were lucky, congratulations!");
                    return true;
                }
            }
        return false;
    }

    public void receiveRewards() {
        player.getInterfaceManager().sendInterface(364);
        player.getPackets().sendItems(141, player.getRaidManager().getRaidRewards());
        player.getRaidManager().setHasReceivedRewards(true);
        if (containsRare());
        if (player.isDebugMode()) {
            player.sm("" + Arrays.toString(player.getRaidManager().getRaidRewards().toArray()));
        }
        player.setCloseInterfacesEvent(new Runnable() {
            @Override
            public void run() {
                player.getBank().addItems(player.getRaidManager().getRaidRewards().toArray(), true);
                player.sm("Your rewards were sent to bank!", true);
            }
        });
        stage = Stages.RECEIVED_REWARDS;
    }

    public void processBoss() {
        final ArrayList<NPC> list = ((RaidController) player.getControlerManager().getControler()).getNpcs();
        Iterator<NPC> iterator = list.iterator();
        while (iterator.hasNext()) {
            NPC npc = iterator.next();
            if (npc == null)
                continue;
            if (npc.getId() == player.getRaidGroup().getRaidEncounter().getRaid().getRaidBoss().getNPCId()) {
                if (npc.hasFinished()) {
                    if (stage == Stages.RUNNING) {
                        CoresManager.slowExecutor.schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (stage == Stages.RECEIVING_REWARDS)
                                    return;
                                for (Player p : ((RaidController) player.getControlerManager().getControler()).getGroup()) {
                                    p.getRaidManager().addRewards(p);
                                    p.getRaidManager().increaseRaidKillcount(p.getRaidGroup().getRaidEncounter().getId());
                                }
                                player.getRaidManager().processRaidAction(ActionType.KILLED_THE_BOSS);
                                spawnExitPortal(false);
                                spawnChest(false);
                            }
                        }, 1200, TimeUnit.MILLISECONDS);
                        if (Settings.DEBUG) {
                            System.out.println("[" + getClass().getSimpleName() + "] " + player.getDisplayName() + "'s Raid boss was killed!");
                        }
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void spawnChest(boolean remove) {
        if (!(player.getControlerManager().getControler() instanceof RaidController))
            return;
        final WorldTile chestTile = getWorldTile(player.getRaidGroup().getRaidEncounter().getRaidTiles().getChestTile()[0], player.getRaidGroup().getRaidEncounter().getRaidTiles().getChestTile()[1]);
        final WorldObject chest = (new WorldObject(73894,
                10, 0, chestTile));
        final WorldObject chestPurple = (new WorldObject(73895,
                10, 0, chestTile));
        final WorldObject chestOpened = (new WorldObject(73896, 10, 0, chestTile));
        if (remove) {
            World.removeObject(chest, true);
            if (Settings.DEBUG) {
                System.out.println("[" + getClass().getSimpleName() + "] Removing " + player.getDisplayName() + "'s rewards chest!");
            }
        } else {
            World.spawnObject(chest, true);
            if (Settings.DEBUG) {
                System.out.println("[" + getClass().getSimpleName() + "] Spawning " + player.getDisplayName() + "'s rewards chest!");
            }
            stage = Stages.RECEIVING_REWARDS;
        }
    }

    private void spawnExitPortal(boolean remove) {
        if (!(player.getControlerManager().getControler() instanceof RaidController))
            return;
        final WorldTile chestTile = getWorldTile(player.getRaidGroup().getRaidEncounter().getRaidTiles().getExitTile()[0], player.getRaidGroup().getRaidEncounter().getRaidTiles().getExitTile()[1]);
        final WorldObject chest = (new WorldObject(11368,
                10, 0, chestTile));
        if (remove) {
            World.removeObject(chest, true);
            if (Settings.DEBUG) {
                System.out.println("[" + getClass().getSimpleName() + "] Removing " + player.getDisplayName() + "'s raid exit portal!");
            }
        } else {
            World.spawnObject(chest, true);
            if (Settings.DEBUG) {
                System.out.println("[" + getClass().getSimpleName() + "] Spawning " + player.getDisplayName() + "'s raid exit portal!");
            }
            stage = Stages.RECEIVING_REWARDS;
        }
    }

    public enum ActionType {ALL_PLAYERS_DEAD, ONE_PLAYER_DEAD, KILLED_THE_BOSS};

    void processRaidAction(ActionType actionType) {
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
            if (player.getControlerManager().getControler() instanceof RaidController) {
                switch (actionType) {
                    case KILLED_THE_BOSS:
                        if (player.getControlerManager().getControler() instanceof RaidController) {
                            ((RaidController) player.getControlerManager().getControler()).getGroup().forEach(p -> {
                                if (player.getRaidGroup().getGraveYard().contains(p)) {
                                    p.sm("You are now free as your teammates managed to kill the boss without you, newbie!");
                                    p.setNextWorldTile(getWorldTile(p.getRaidGroup().getRaidEncounter().getRaidTiles().getBossTile()[0],
                                            p.getRaidGroup().getRaidEncounter().getRaidTiles().getBossTile()[1]));
                                    p.unlock();
                                }
                                p.sm("Boss is now killed, now collect the rewards and wait for the group owner call!");
                            });
                            if (player.getRaidGroup().getGraveYard().contains(player)) {
                                player.getRaidGroup().getGraveYard().remove(player);
                            }
                        }
                        break;
                    case ONE_PLAYER_DEAD:
                        String[] randomDeathMessages = {"What a noob!", "Stop carrying him!", "Bastard!", "Pathetic", "NoOoOoOb!"};
                        final int i = Utils.random(5);
                        WorldTasksManager.schedule(new WorldTask() {
                            int loop;

                            @Override
                            public void run() {
                                if (loop == 0) {
                                    player.lock();
                                    player.setNextAnimation(new Animation(836));
                                } else if (loop == 1) {
                                    player.getPackets().sendGameMessage("Oh dear, you died!");
                                    player.getRaidGroup().getGroupMembers().forEach(p -> {
                                        if (player == p)
                                            return;
                                        p.sm(player.getDisplayName() + " has just died, " + randomDeathMessages[i] + "!");
                                    });
                                } else if (loop == 3) {
                                    player.setNextWorldTile(getWorldTile(player.getRaidGroup().getRaidEncounter().getRaidTiles().getGraveyardTile()[0], player.getRaidGroup().getRaidEncounter().getRaidTiles().getGraveyardTile()[1]));
                                } else if (loop == 4) {
                                    player.getPackets().sendMusicEffect(90);
                                    player.reset();
                                    player.setNextAnimation(new Animation(-1));
                                    if (player.getPet() != null) {
                                        player.getPet().pickup();
                                    }
                                    if (!player.getRaidGroup().getGraveYard().contains(player)) {
                                        player.getRaidGroup().getGraveYard().add(player);
                                    }
                                    stop();
                                }
                                loop++;
                            }
                        }, 0, 1);
                        break;
                    case ALL_PLAYERS_DEAD:
                        player.getRaidGroup().getGroupMembers().forEach(p -> {
                            if (player.getRaidGroup().getGroupMembers().size() == 1) {
                                p.sm("You have failed the battle and was kicked in the butt hence that!");
                            } else {
                                p.sm("You and your teammates have failed the battle and were kicked in the butt hence that!");
                            }
                            p.unlock();
                            /*if (p == player)
                                return;*/
                            p.getRaidGroup().leave(p, RaidGroup.LeaveType.FAIL);
                        });
                        break;
                }
            }
        }
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] " + "ActionType: [" + actionType + "]");
        }
    }

    private void addRewards(Player p) {
        String dropType = "";
        final WorldTile chestTile = getWorldTile(p.getRaidGroup().getRaidEncounter().getRaidTiles().getChestTile()[0], p.getRaidGroup().getRaidEncounter().getRaidTiles().getChestTile()[1]);
        RaidDrops raidDrops = p.getRaidGroup().getRaidEncounter().getRaidDrops();
        int raidGroupSize = p.getRaidGroup().getGroupMembers().size();
        for (int i = 0; i < p.getRaidManager().getRaidRewards().getSize(); i++) {
            int chance = Utils.random(0, (raidGroupSize == 5 ? 100 :
                    raidGroupSize == 4 ? 105 :
                            raidGroupSize == 3 ? 110 :
                                    raidGroupSize == 2 ? 115 : 120));
            if (chance == 0) { // Very rare drop
                Item veryRareDrop = getRandomItem(raidDrops.getVeryRareDrops());
                if (p.getRaidManager().hasRareDrop()) {
                    dropType = "Very rare replaced";
                    if (Settings.DEBUG) {
                        System.out.println(dropType);
                    }
                    p.getRaidManager().getRaidRewards().add(getRandomItem(raidDrops.getCommonDrops()));
                } else {
                    dropType = "Very rare";
                    if (Settings.DEBUG) {
                        System.out.println(dropType);
                    }
                    World.sendPrivateGraphics(p, new Graphics(1849), chestTile);
                    p.getRaidGroup().getGroupMembers().forEach(p2 -> {
                        if (p == p2) {
                            p2.sm("<col=a000a3>You received a purple chest! good luck on your drops " + (p2.getAppearence().isMale() ? "<col=a000a3>boy" : "<col=a000a3>girl") + "!</col>");
                            return;
                        }
                        p2.sm("<col=a000a3>" + p.getDisplayName() + " just received a purple chest! wish " + (p2.getAppearence().isMale() ? "<col=a000a3>him" : "<col=a000a3>her") + " luck!</col>");
                    });
                    p.getRaidManager().setHasRareDrop(true);
                    p.getRaidManager().getRaidRewards().add(veryRareDrop);
                    p.sendOneUniqueItem(veryRareDrop.getId(), false);
                    //World.sendWorldWideMsg(p, getRandomItem(raidDrops.getRareDrops()).getDefinitions().getId(), " has received " + getRandomItem(raidDrops.getRareDrops()).getDefinitions().getName() + " at "+Utils.formatPlayerNameForDisplay(player.getRaidGroup().getRaidEncounter().name())+" Raid in " + player.getGameModeName(false).toLowerCase()+" mode!", Color.RED, true, DiscordConstant.DROPS_CHANNEL_ID);
                }
            } else if (chance == 1) { // Rare drop
                if (p.getRaidManager().hasRareDrop()) {
                    dropType = "Rare replaced";
                    if (Settings.DEBUG) {
                        System.out.println(dropType);
                    }
                    p.getRaidManager().getRaidRewards().add(getRandomItem(raidDrops.getCommonDrops()));
                } else {
                    Item rareDrop = getRandomItem(raidDrops.getRareDrops());
                    dropType = "Rare";
                    World.sendPrivateGraphics(p, new Graphics(1849), chestTile);
                    p.getRaidGroup().getGroupMembers().forEach(p2 -> {
                        if (p == p2) {
                            p2.sm("<col=a000a3>You received a purple chest! good luck on your drops " + (p2.getAppearence().isMale() ? "<col=a000a3>boy" : "<col=a000a3>girl") + "!</col>");
                            return;
                        }
                        p2.sm("<col=a000a3>" + p.getDisplayName() + " just received a purple chest! wish " + (p2.getAppearence().isMale() ? "<col=a000a3>him" : "<col=a000a3>her") + " luck!</col>");
                    });
                    p.getRaidManager().setHasRareDrop(true);
                    if (Settings.DEBUG) {
                        System.out.println(dropType);
                    }
                    p.getRaidManager().getRaidRewards().add(rareDrop);
                    //World.sendWorldWideMsg(p, getRandomItem(raidDrops.getRareDrops()).getDefinitions().getId(), " has received " + getRandomItem(raidDrops.getRareDrops()).getDefinitions().getName() + " at "+Utils.formatPlayerNameForDisplay(player.getRaidGroup().getRaidEncounter().name())+" Raid in " + player.getGameModeName(false).toLowerCase()+" mode!", Color.RED, true, DiscordConstant.DROPS_CHANNEL_ID);
                }
            } else if (chance >= 30 && chance <= 50) { // Uncommon
                dropType = "Uncommon";
                if (Settings.DEBUG) {
                    System.out.println(dropType);
                }
                p.getRaidManager().getRaidRewards().add(getRandomItem(raidDrops.getUncommonDrops()));
            } else  { // Common
                dropType = "Common";
                if (Settings.DEBUG) {
                    System.out.println(dropType);
                }
                p.getRaidManager().getRaidRewards().add(getRandomItem(raidDrops.getCommonDrops()));
            }
        }
        if (Settings.DEBUG) {
            System.out.println("[" + getClass().getSimpleName() + "] (" + p.getDisplayName() + "'s raid) " + p.getDisplayName() + " rolled " + dropType + " drops!");
        }
    }

    private Item getRandomItem(Item[] drop) {
        int index = Utils.random(drop.length-1);
        Item item = new Item(drop[index].getId(), drop[index].getAmount());
        if (Settings.DEBUG) {
            System.out.println("" + item.getId()+ "-" + item.getAmount());
        }
        return item;
    }

    public void sendInterface() {
        updateInterface();
        if (player.getInterfaceManager().containsInterface(player.getInterfaceManager().hasResizableScreen() ? RaidManager.RESIZABLE_TAB_ID : RaidManager.FIXED_TAB_ID)) {
            return;
        }
        if (player.getInterfaceManager().containsTab(player.getInterfaceManager().hasResizableScreen() ? RaidManager.RESIZABLE_TAB_ID : RaidManager.FIXED_TAB_ID)) {
            return;
        }
        player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? RaidManager
                .RESIZABLE_TAB_ID : RaidManager.FIXED_TAB_ID, INTERFACE);
    }

    private void updateInterface() {
        ArrayList<Player> groupMembers = player.getRaidGroup().getGroupMembers();
        Player groupOwner = player.getRaidGroup().getGroupOwner();
        for (int i = 0; i < components.length; i++) {
            player.getPackets().sendHideIComponent(INTERFACE, components[i], true);
        }
        if (player.getRaidGroup() != null && !player.getRaidGroup().getGroupMembers().isEmpty()) {
            player.getPackets().sendIComponentText(INTERFACE, 3, Utils.formatPlayerNameForDisplay(player.getRaidGroup().getRaidEncounter().name()));
            for (int i = 0; i < groupMembers.size(); i++) {
                if (groupMembers.get(i) == null)
                    continue;
                player.getPackets().sendHideIComponent(INTERFACE, components[i], false);
                player.getPackets().sendIComponentText(INTERFACE, components[i], (groupMembers.get(i) == groupOwner ? groupMembers.get(i).getDisplayName() + " (<col=ffb300>Owner</col>)" : groupMembers.get(i).getDisplayName()));
            }
        }
    }

    private WorldTile getWorldTile(final int mapX, final int mapY) {
        final WorldTile tile = new WorldTile(boundChuncks[0] * 8 + mapX, boundChuncks[1] * 8 + mapY, 0);
        if (Settings.DEBUG) {
            System.err.println("TileX: " + tile.getX() + ", TileY: " + tile.getY());
        }
        return tile;
    }

    public void closeInterface() {
        player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? RaidManager
                .RESIZABLE_TAB_ID : RaidManager.FIXED_TAB_ID);
    }

    public Stages getStage() {
        return stage;
    }

    public static enum Stages {
        LOADING, RUNNING, RECEIVING_REWARDS, RECEIVED_REWARDS, DESTROYING
    }

    public int getRaidKillcount(int index) {
        switch (index) {
            case 0:
                return player.getLeeuniKilled();
            case 1:
                return player.getWinterKilled();
            default:
                return 0;
        }
    }

    public void increaseRaidKillcount(int index) {
        switch (index) {
            case 0: // Leunii
                player.setLeeuniKilled(getRaidKillcount(0) +1);
                break;
            case 1: // Mercenary mage
                player.setWinterKilled(getRaidKillcount(1) +1);
                break;
        }
    }
}