package com.rs.game.player.content.raids;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.*;
import com.rs.game.Graphics;
import com.rs.game.communication.discord.DiscordConstant;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.raids.data.RaidDrops;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.Colors;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

import java.awt.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Custom Raids System
 *
 * @author Sagacity - http://rune-server.ee/members/sagacity
 * Date: 07/15/2019
 */

public class RaidsManager implements Serializable {
    public static final String RAID_DESCRIPTION = "A minigame where a team of one to five members come together to slay a difficult BOSS," +
            " its health/level may change taking in consideration the group size, which means more HP if more members are in. Remember: " +
            "this is in " + Colors.Data.NON_STARTED.getColor() + "ETA</col> and " + Colors.Data.NON_STARTED.getColor() +
            "WILL HAVE MANY BUGS</col>, please remember to " + Colors.Data.NON_STARTED.getColor() + "REPORT</col> them to discord and be " +
            "rewarded later on! :)";
    public static final int INTERFACE = 3006;
    public static final WorldTile RAIDS_LOBBY = new WorldTile(3436, 3731, 1);
    static final int RESIZABLE_TAB_ID = 8;
    static final int FIXED_TAB_ID = 10; //8
    private static final int DROPS_PER_CHEST = 5; // Changes the amount of items the player receive for completing a
    public static List<RaidsEncounter> raidsEncountersList = new ArrayList<>();
    private static final boolean debugMode = true; // Enable to see debugging messages
    private final int MAX_MEMBERS = 5;
    private final int[] components = {6, 7, 8, 9, 10};
    private transient Player player;
    private NPC resigner;
    private NPC boss;
    private int[] raidBounds;
    private Stages stage;
    private boolean hasReceivedPurpleChest;
    private boolean hasReceivedRewards;
    private boolean bossKilled;
    private List<Player> raidGroup;
    private final ItemsContainer<Item> playerRewards = new ItemsContainer<>(DROPS_PER_CHEST, false);
    private List<Player> graveYard;
    private Player groupOwner;
    private RaidsEncounter encounter;
    private List<NPC> raidNpcs;
    private List<WorldObject> raidObjects;
    private final List<FloorItem> raidsItems = new ArrayList<>(500);
    private final HashMap<Integer, Duration> personalBests;

    /**
     * The serial version for the collection log.
     */
    private static final long serialVersionUID = -1;

    /**
     * Constructor method to initiate a RaidsManager for a player
     * @param player The player we are initiating a raids manager
     */
    public RaidsManager(Player player) {
        this.player = player;
        personalBests = new HashMap<>();
    }

    /**
     * Shows the view invite dialogue to another player in the world
     *
     * @param player The raid group owner
     * @param p2     The player we are inviting
     */
    public static void viewInvite(Player player, Player p2) {
        if (!(player.getControlerManager().getControler() instanceof RaidsLobbyController)) {
            return;
        }
        if (!(p2.getControlerManager().getControler() instanceof RaidsLobbyController)) {
            return;
        }
        if (player.getTemporaryAttributes().remove("raidinvite") != null)
            player.getDialogueManager().startDialogue("RaidsInviteD", p2, player);
        if (debugMode)
            System.out.println("viewInvite(owner: " + player.getDisplayName().toLowerCase() + ", invited: " + p2.getRaidsManager()
                    .getEncounter().raidName().toLowerCase() + ");");
    }

    /**
     * The method that loads the raids instancing classes
     */
    public static void loadOptions() {
        try {
            Class[] classes = Utils.getClasses("com.rs.game.player.content.raids.impl");
            for (Class c : classes) {
                if (c.isAnonymousClass()) // next
                    continue;
                Object o = c.newInstance();
                if (!(o instanceof RaidsEncounter))
                    continue;
                RaidsManager.raidsEncountersList.add((RaidsEncounter) o);
            }
        } catch (Throwable e) {
            Logger.handle(e);
        } finally {
            if (Settings.DEBUG)
                System.out.println("[RaidsManager] Loaded " + RaidsManager.raidsEncountersList.size() + " raids encounters.");
        }
    }

    /*private boolean isVeryRareItem(Player player, Item item) {
        RaidDrops raidDrops = player.getRaidsManager().getEncounter().raidDrops();

        for (Item drop : raidDrops.getVeryRareDrops()) {
            if (drop == item) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }*/

    public static List<RaidsEncounter> getRaidsEncountersList() {
        return raidsEncountersList;
    }

    /**
     * Creates a raid group with a all needed attributtes
     *
     * @param owner     The owner of the raid group being created
     * @param encounter The raid encounter we are selecting
     */
    public void createGroup(Player owner, RaidsEncounter encounter) {
        this.groupOwner = owner;
        this.raidGroup = new ArrayList<>(5);
        this.graveYard = new ArrayList<>(5);
        this.raidGroup.add(owner);
        this.encounter = encounter;
        owner.sm("A party for for " + encounter.raidName().toLowerCase() + " was created!");
        if (debugMode)
            System.out.println("createGroup(" + owner.getDisplayName().toLowerCase() + ", " + encounter.raidName().toLowerCase() + ")");
    }

    /**
     * Invites another player from world to participate our raid group
     *
     * @param target The player we are inviting for our raid group
     */

    public void invite(Player target) {
        if (target == null || !target.hasStarted() || target.hasFinished()) {
            player.getPackets().sendGameMessage("Player offline or doesn't exists!");
            return;
        }
        if (!(player.getControlerManager().getControler() instanceof RaidsLobbyController)) {
            return;
        }
        if (!(target.getControlerManager().getControler() instanceof RaidsLobbyController)) {
            return;
        }
        if (player.getUsername().equalsIgnoreCase(target.getUsername())) {
            player.getPackets().sendGameMessage("You cannot invite yourself!");
            return;
        }
        /*if (player.getSession().getIP().equals(target.getSession().getIP())) {
            player.getPackets().sendGameMessage("You cannot invite players on same IP as you!");
            return;
        }*/
        if (player.getRaidsManager().getRaidGroup() == null) {
            player.getPackets().sendGameMessage("You must be in a Raid group to do that.");
            return;
        }
        if (player.getRaidsManager().getRaidGroup().size() >= 5) {
            player.getPackets().sendGameMessage("Group is already full.");
            return;
        }
        if (target.getRaidsManager().hasRaidGroup()) {
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

    /**
     * Recreates the whole instance and sends all players to it
     *
     * @param owner The raid group owner
     */
    private void restart(Player owner) {
        player.getTemporaryAttributes().put("RAIDS_STATE", Stages.RESTARTING);
        List<Player> raidGroup = owner.getRaidsManager().getRaidGroup();
        WorldTasksManager.schedule(new WorldTask() {
            int tick = 0;

            @Override
            public void run() {
                switch (tick) {
                    case 0:
                        raidGroup.forEach(groupMember -> {
                            groupMember.getPackets().sendBlackOut(2);
                            groupMember.getAppearence().setHidden(true);
                            groupMember.sm("<col=ff0000>Please wait... The instance is being restarted!</col>");
                            groupMember.lock();
                            groupMember.stopAll(true);
                            processLobbyTeleport(groupMember);
                        });
                        destroyInstanceCompletely();
                        break;
                    case 1:
                        start(owner);
                        raidGroup.forEach(groupMember -> {
                            groupMember.getPackets().sendBlackOut(0);
                            groupMember.getAppearence().setHidden(false);
                        });
                        stop();
                        break;
                }
                tick++;
            }
        }, 0, 1);
        if (debugMode)
            System.out.println("restart(owner, " + owner.getDisplayName().toLowerCase() + ", encounter: " + owner.getRaidsManager().
                    getEncounter().raidName().toLowerCase() + ");");
    }

    /**
     * Adds the invited player to the owner's raid group
     *
     * @param owner   The raid group owner that is inviting the invited
     * @param invited The invited player we are adding to the owner's group
     */
    public void addInvited(Player owner, Player invited) {
        if (!owner.getRaidsManager().hasRaidGroup()) {
            invited.sm("This group no longer exists!");
            return;
        }
        if (owner.getRaidsManager().getRaidGroup().size() > MAX_MEMBERS) {
            owner.sm("Your raid group is full!");
            return;
        }
        if (invited.getRaidsManager().getRaidGroup() != null) {
            invited.sm("You are already in a raid group!");
            owner.sm(invited.getDisplayName() + " is already in a raid group!");
            return;
        }
        if (owner.getUsername().equalsIgnoreCase(invited.getUsername())) {
            owner.getPackets().sendGameMessage("You cannot invite yourself!");
            return;
        }
        /*if (player.getSession().getIP().equals(invited.getSession().getIP())) {
            player.getPackets().sendGameMessage("You cannot invite players on same IP as you!");
            return;
        }*/
        if (owner.getRaidsManager().getRaidGroup() == null) {
            owner.getPackets().sendGameMessage("You must be in a Raid group to do that.");
            return;
        }
        if (invited.getRaidsManager().getRaidGroup() != null && !invited.getRaidsManager().getRaidGroup().isEmpty()) {
            owner.getPackets().sendGameMessage("This player is already a member of another Raid group.");
            return;
        }
        if (invited.getInterfaceManager().containsScreenInter()) {
            owner.getPackets().sendGameMessage("The other player is busy.");
            return;
        }
        if (!owner.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup().contains(invited)) { // create the
            // raid group
            owner.sm(owner.getColorByDisplay(Colors.Data.EASY_ACHIEVEMENT) + invited.getDisplayName() +
                    " has accepted your raid group invitation!");
            invited.sm(owner.getColorByDisplay(Colors.Data.RAIDS) + "You accepted joining " + owner.getDisplayName() +
                    "'s raid group.");

            owner.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup().forEach(groupMember -> {
                if (groupMember == null || !groupMember.hasStarted() || groupMember.hasFinished())
                    return;
                if (groupMember != invited && groupMember != owner) {
                    groupMember.sm("<col=ff0000>" + invited.getDisplayName() + "</col> just joined your party.");
                }
            });
            owner.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup().add(invited);
            invited.getTemporaryAttributes().put("raidinvite", Boolean.TRUE);
            invited.getTemporaryAttributes().put("RAIDS_GROUP_ATTR", owner.getRaidsManager().getRaidGroup());
            invited.getRaidsManager().setGroupOwner(owner);
            invited.getRaidsManager().setRaidGroup(owner.getRaidsManager().getRaidGroup());
        } else {
            owner.sm("User <col=ff0000>" + invited.getDisplayName() + "</col> is already in your group.");
            invited.sm("You are already on <col=ff0000>" + owner.getDisplayName() + "'s group");
        }
        if (debugMode)
            System.out.println("addInvited(owner: " + owner.getDisplayName().toLowerCase() + ", invited: " + invited.
                    getDisplayName().toLowerCase() + ");");
    }

    /**
     * Starts the fight with all other raid group members
     *
     * @param owner The raid group owner
     */
    public void start(Player owner) {
        if (player.getTemporaryAttributes().get("RAIDS_STATE") != Stages.RESTARTING) {
            player.sm("<col=ff0000>Please wait... The instance is being loaded!</col>");
        }
        player.getTemporaryAttributes().put("RAIDS_STATE", Stages.RUNNING);
        setBossKilled(false);
        player.getRaidsManager().setHasReceivedRewards(false);
        player.getRaidsManager().getRaidsItems().clear();
        if (player.getRaidsManager().getGroupOwner() != player) {
            player.sm("You must be the group owner to start the raid");
            return;
        }
        player.getRaidsManager().getRaidGroup().forEach(groupMember -> {
            if (groupMember == null || !groupMember.hasStarted() || groupMember.hasFinished())
                return;
            groupMember.lock();
            if (groupMember == groupMember.getRaidsManager().getGroupOwner()) {
                buildMap(owner);
                spawnPlayer(groupMember);
            } else {
                spawnPlayer(groupMember);
            }
            groupMember.getTemporaryAttributes().put("RAIDS_COMPLETION_TIME", LocalTime.now());
        });
        if (debugMode)
            System.out.println("start(owner: " + owner.getDisplayName().toLowerCase() + ", encounter: " + owner.getRaidsManager().
                    getEncounter().raidName().toLowerCase() + ");");
    }

    /**
     * Process all the leaving ways of the raid group
     *
     * @param leaver    The group leaver
     * @param leaveType The leaving type that the leaver is using
     */
    public void leave(Player leaver, LeaveType leaveType) {
        leaver.stopAll();
        leaver.getPackets().sendBlackOut(0);
        if (leaveType == LeaveType.RESTART) {
            if (leaver.getRaidsManager().getGroupOwner() == leaver) {
                restart(leaver);
            }
        } else {
            List<Player> raidGroup = leaver.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();
            //if I am raid owner, destroy it
            if (leaver.getRaidsManager().getGroupOwner() == leaver) {
                raidGroup.forEach(groupMember -> {
                    if (groupMember == null || !groupMember.hasStarted() || groupMember.hasFinished()
                            || groupMember == leaver)
                        return;
                    groupMember.sm("You were removed from the group as the owner decided to resign!");
                    processLobbyTeleport(groupMember);
                });
                destroyInstanceCompletely();
                removeAllGroupMembers(leaver);
                setHasReceivedPurpleChest(false);
                if (raidGroup.size() > 1) {
                    leaver.sm("Your group was disbanded as you left!");
                }
            }
        }
        switch (leaveType) {
            case LOGOUT:
            case RESIGN:
                processLobbyTeleport(leaver);
                removeOneGroupMember(leaver);
                break;
            case KICKED:
                processLobbyTeleport(leaver);
                removeOneGroupMember(leaver);
                leaver.sm(leaver.getColorByDisplay(Colors.Data.RAIDS) + "You were removed from " +
                        leaver.getDisplayName() + "'s raid group!");
                break;
            case TELEPORT:
                removeOneGroupMember(leaver);
                break;
            case COMPLETE_AND_QUIT:
                leaver.sm("The raid was completed!");
                processLobbyTeleport(leaver);
                removeOneGroupMember(leaver);
                break;
        }
        if (debugMode)
            System.out.println("leave(leaver: " + leaver.getDisplayName().toLowerCase() + ", leaveType: " + leaveType + ");");
    }

    /**
     * Process the teleports to Lobby as we use the method in many cases
     *
     * @param teleported The teleported player
     */
    private void processLobbyTeleport(Player teleported) {
        if (teleported.getControlerManager().getControler() instanceof RaidsLobbyController)
            return;
        FadingScreen.fade(teleported, 3, () -> {//9
            teleported.setNextWorldTile(new WorldTile(RAIDS_LOBBY, 1));
        });
    }

    /**
     * Process a teleport of a player to a specific coordinate
     *
     * @param teleported The teleported player
     * @param coords     The WorldTile coords
     */
    private void processTeleport(Player teleported, WorldTile coords) {
        //FadingScreen.fade(teleported, 3, () -> {//9
        teleported.setNextWorldTile(new WorldTile(coords, 1));

        //Magic.sendNormalTeleportSpell(teleported, 0, 0.0D, new WorldTile(coords, 1), new int[0]);
        //});
    }

    /**
     * Removes a single raid group member and reset their attributes
     *
     * @param removed The player that is being removed from raid group
     */
    private void removeOneGroupMember(Player removed) {
        List<Player> raidGroup = removed.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();
        List<Player> graveYard = removed.getRaidsManager().getGroupOwner().getRaidsManager().getGraveYard();
        if (raidGroup == null || graveYard == null)
            return;
        if (raidGroup.contains(removed)) {
            if (debugMode)
                System.out.println("removeOneGroupMember(owner: " + removed.getRaidsManager().getGroupOwner().getDisplayName().
                        toLowerCase() + ", leaver: " + removed.getDisplayName().toLowerCase() + ");");

            raidGroup.remove(removed);
            resetGroupAttributes(removed);
        }
        if (graveYard.contains(removed)) {
            graveYard.remove(removed);
        }
    }

    /**
     * Remove all members from the raid group and reset their attributes
     *
     * @param owner The raid group owner
     */
    private void removeAllGroupMembers(Player owner) {
        if (debugMode)
            System.out.println("removeAllGroupMembers(owner: " + owner.getDisplayName().toLowerCase() + ");");

        List<Player> raidGroup = owner.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();

        raidGroup.forEach(groupMember -> {
            if (groupMember == owner) {
                return;
            }
            resetGroupAttributes(groupMember);
        });
    }

    /**
     * Resets the player raid attributes
     *
     * @param p - The player we are removing the attributes from
     */
    public void resetGroupAttributes(Player p) {
        p.getRaidsManager().setRaidBounds(null);
        p.getRaidsManager().setGroupOwner(null);
        p.getRaidsManager().setRaidGroup(null);
        if (debugMode)
            System.out.println("resetGroupAttributes(leaver: " + p.getDisplayName().toLowerCase() + ");");
    }

    /**
     * The method that holds the spawning for the raids npcs
     */
    private void spawnNpcs() {
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();
        CoresManager.slowExecutor.schedule(() -> {
            boss = World.spawnNPC(raidsManager.getEncounter().raidBoss().getNPCId(),
                    getWorldTile(raidsManager.getEncounter().raidTiles().getBossTile()[0],
                            raidsManager.getEncounter().raidTiles().getBossTile()[1]), -1,
                    true, true, false);
            resigner = World.spawnNPC(4549, getWorldTile(raidsManager.getEncounter().raidTiles().getResignTile()[0],
                    raidsManager.getEncounter().raidTiles().getResignTile()[1]), -1, true,
                    true, false);
            if (raidsManager.getEncounter().raidBoss().getBaseHitpoints() != -1) {
                boss.setHitpoints(getHPByGroupSize());
                boss.getCombatDefinitions().setHitpoints(getHPByGroupSize());
            }
            if (raidsManager.getEncounter().raidBoss().getNpcLevel() != -1) {
                boss.setCombatLevel(raidsManager.getEncounter().raidBoss().getNpcLevel());
            } else {
                boss.setCombatLevel(getNpcLevelByGroupSize());
            }
            boss.setForceAgressiveDistance(1);
            boss.setForceAgressive(true);
            boss.setForceMultiArea(true);
            boss.setAtMultiArea(true);
            boss.setForceMultiAttacked(true);
            boss.setIntelligentRouteFinder(true);
            boss.setForceFollowClose(true);
            boss.setRun(true);
            boss.setLureDelay(0);
            boss.setCapDamage(600);
            boss.setCantInteract(false);
            boss.setSpawned(true);
            resigner.setRandomWalk(false);
            if (debugMode)
                System.out.println("spawnNpcs(owner: " + raidsManager.getGroupOwner().getDisplayName().toLowerCase()
                        + ", encounter: " + raidsManager.getGroupOwner().getRaidsManager().getEncounter().raidName().toLowerCase() + ");");
        }, 1, TimeUnit.MILLISECONDS);
    }

    /**
     * Update the NPC names when entering the instanced map
     *
     * @param raidsManager The player itself
     */
    private void updateNpcNames(RaidsManager raidsManager) {
        if (raidsManager.getEncounter().raidBoss().getNpcName() != null) {
            boss.setName("<col=00c4c7>" + raidsManager.getEncounter().raidBoss().getNpcName());
        }
        resigner.setName("<col=4cff00>Resigner</col>");
    }

    /**
     * The method that holds the destroying for the raids npcs
     */
    private void destroyNpcs() {
        resigner.finish();
        boss.finish();
    }

    /**
     * The method that holds the spawning for the raids items
     */
    private void destroyItems() {
        player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().forEach(groundItem -> {
            player.getPackets().sendRemoveGroundItem(groundItem);
            World.removeGroundItem(groundItem.getOwner(), groundItem);
            if (debugMode)
                System.out.println("destroyItems(owner: " + groundItem.getOwner().getDisplayName().toLowerCase() + "', item: " +
                        "" + groundItem.getName().toLowerCase() + ");");
        });
        raidsItems.clear();
    }

    /**
     * The method that holds the destruction for whole instance completely
     */
    public void destroyInstanceCompletely() {
        if (getRaidBounds() != null) {
            destroyNpcs();
            destroyItems();
            spawnObjects(false);
            destroyMap();
            if (debugMode)
                System.out.println("destroyInstanceCompletely(owner: " + player.getRaidsManager().getGroupOwner().
                        getDisplayName().toLowerCase() + ");");
        }
    }

    ;

    /**
     * The method that holds the spawning for the raids map
     */
    public void destroyMap() {
        final int widthRegion = player.getRaidsManager().getGroupOwner().getRaidsManager().getEncounter().raidMap().
                getChunkSize()[0];
        final int heightRegion = player.getRaidsManager().getGroupOwner().getRaidsManager().getEncounter().raidMap().
                getChunkSize()[1];
        final int[] raidBounds = player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidBounds();
        /*if (stage != RaidManager.Stages.RUNNING && stage != RaidManager.Stages.RECEIVING_REWARDS
            && stage != RaidManager.Stages.RECEIVED_REWARDS)
            return;
        stage = RaidManager.Stages.DESTROYING;*/
        player.doAfterDelay(2, () -> {
            RegionBuilder.destroyMap(raidBounds[0], raidBounds[1], widthRegion, heightRegion);
        });

            /*for (Player groupMember : player.getRaidsManager().getRaidGroup()) {
                if (groupMember == null || !groupMember.hasStarted() || groupMember.hasFinished())
                    continue;
                resetGroupAttributes(groupMember);
            }*/
        if (debugMode)
            System.out.println("destroyMap(owner: " + player.getRaidsManager().getGroupOwner().getDisplayName().
                    toLowerCase() + ");");
    }


    /**
     * The method that holds the building of the raids map
     *
     * @param owner The owner of the instanced map
     */
    private void buildMap(Player owner) {
        player.getTemporaryAttributes().put("RAIDS_STATE", Stages.LOADING);
        CoresManager.slowExecutor.schedule(() -> {
            final int widthRegion = player.getRaidsManager().getEncounter().raidMap().getChunkSize()[0];
            final int heightRegion = player.getRaidsManager().getEncounter().raidMap().getChunkSize()[1];
            final int fromRegionX = player.getRaidsManager().getEncounter().raidMap().getBoundChuncks()[0];
            final int fromRegionY = player.getRaidsManager().getEncounter().raidMap().getBoundChuncks()[1];

            raidBounds = RegionBuilder.findEmptyChunkBound(widthRegion, heightRegion);
            RegionBuilder.copyMap(fromRegionX, fromRegionY, raidBounds[0],
                    raidBounds[1], widthRegion, heightRegion, new int[1], new int[1]);

            raidGroup.forEach(groupMember -> {
                if (groupMember == null || !groupMember.hasStarted() || groupMember.hasFinished())
                    return;
                groupMember.getRaidsManager().setGroupOwner(owner);
                groupMember.getRaidsManager().setEncounter(
                        groupMember.getRaidsManager().getGroupOwner().getRaidsManager().getEncounter());
                groupMember.getRaidsManager().setRaidBounds(
                        groupMember.getRaidsManager().getGroupOwner().getRaidsManager().getRaidBounds());
                if (groupMember == owner) {
                    spawnNpcs();
                }
            });
            if (debugMode)
                System.out.println("buildMap(owner: " + owner.getDisplayName().toLowerCase() + ", encounter: " +
                        owner.getRaidsManager().getEncounter().raidName().toLowerCase() + ");");
        }, 15, TimeUnit.MILLISECONDS);
    }

    /**
     * The method that holds the spawning for the raids group members
     *
     * @param player The player to be spawned
     */
    private void spawnPlayer(Player player) {
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if (player == null || !player.hasStarted() || player.hasFinished())
                    return;
                if (raidGroup.contains(player)) {
                    /*player.setNextWorldTile(new WorldTile(getWorldTile(player.getRaidsManager().getEncounter().raid
                    Tiles().getEntranceTile()[0],
                            player.getRaidsManager().getEncounter().raidTiles().getEntranceTile()[1]), 1));*/

                    processTeleport(player, new WorldTile(getWorldTile(player.getRaidsManager().getEncounter().
                                    raidTiles().getEntranceTile()[0],
                            player.getRaidsManager().getEncounter().raidTiles().getEntranceTile()[1])));

                    player.getControlerManager().startControler("RaidsController");
                    player.sm("- Group size: " + player.getRaidsManager().getGroupOwner().getRaidsManager().
                            getRaidGroup().size() + ".");
                    player.sm("- Difficulty: " + getDifficulty() + ".");
                    player.sm("- Encounter: " + Utils.formatPlayerNameForDisplay(player.getRaidsManager().getGroupOwner()
                            .getRaidsManager().getEncounter().raidName()) + ".");
                    player.unlock();
                    player.stopAll(false);
                    updateNpcNames(player.getRaidsManager().getGroupOwner().getRaidsManager());
                    player.getRaidsManager().setHasReceivedRewards(false);
                    player.getTemporaryAttributes().remove("RAIDS_DROPPED_VERY_RARE");
                } else {
                    System.out.println(player.getDisplayName() + " isn't in a group to be added to instance!");
                }
                if (debugMode)
                    System.out.println("spawnPlayer(owner: " + player.getRaidsManager().getGroupOwner().getDisplayName().
                            toLowerCase() + ", player: " + player.getDisplayName().toLowerCase() + ");");
            }
        }, 2);
    }

    /**
     * The method that holds the processing for the raids actions
     *
     * @param player The player that is processing the action
     * @param type   The ActionType must be an action a player is being submitted to
     */
    public void processAction(Player player, ActionType type) {
        List<Player> raidGroup = player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();
        List<Player> graveYard = player.getRaidsManager().getGroupOwner().getRaidsManager().getGraveYard();

        switch (type) {
            case ONE_PLAYER_DEAD:
                WorldTasksManager.schedule(new WorldTask() {
                    int loop;

                    @Override
                    public void run() {
                        if (loop == 0) {
                            player.lock();
                            player.setNextAnimation(new Animation(836));
                            player.getPackets().sendGameMessage("Oh dear, you died!");
                        } else if (loop == 1) {
                            raidGroup.forEach(groupMember -> {
                                if (groupMember == player)
                                    return;
                                groupMember.sm(player.getDisplayName() + " has just died, fool!");
                            });
                        } else if (loop == 3) {
                            int[] graveYartTiles = player.getRaidsManager().getGroupOwner().getRaidsManager().
                                    getEncounter().raidTiles().getGraveyardTile();
                            //player.setNextWorldTile(new WorldTile(getWorldTile(graveYartTiles[0],
                            // graveYartTiles[1]), 1));
                            processTeleport(player, new WorldTile(getWorldTile(graveYartTiles[0], graveYartTiles[1])));
                        } else if (loop == 4) {
                            player.getPackets().sendMusicEffect(90);
                            player.reset();
                            player.setNextAnimation(new Animation(-1));
                            if (player.getPet() != null) {
                                player.getPet().pickup();
                            }
                            graveYard.add(player);
                            stop();
                        }
                        loop++;
                    }
                }, 0, 1);
                break;
            case ALL_PLAYERS_DEAD:
                raidGroup.forEach(groupMember -> {
                    processLobbyTeleport(groupMember);
                    graveYard.remove(groupMember);
                    groupMember.sm("You and your team-mates have failed, and were kicked hence that!");
                    groupMember.getControlerManager().forceStop();
                    groupMember.unlock();
                });
                break;
            case KILLED_THE_BOSS:
                if (isBossKilled())
                    return;
                graveYard.forEach(ded -> {
                    int[] victoryTiles = player.getRaidsManager().getGroupOwner().getRaidsManager().getEncounter().
                            raidTiles().getBossTile();
                    //ded.setNextWorldTile(new WorldTile(getWorldTile(victoryTiles[0], victoryTiles[1]), 1));
                    processTeleport(ded, new WorldTile(getWorldTile(victoryTiles[0], victoryTiles[1])));
                    ded.unlock();
                    ded.sm("You are now free as your team-mates were sucessfull!");
                });
                raidGroup.forEach(groupMember -> {
                    generateDrops(groupMember);
                    groupMember.getRaidsManager().increaseRaidKillcount(groupMember.getRaidsManager().
                            getEncounter().id());

                    groupMember.getRaidsManager().savePersonalBest(groupMember.getRaidsManager().getEncounter().id());

                    if (graveYard.contains(groupMember))
                        return;
                    groupMember.sm("Congratulations on killing " + groupMember.getRaidsManager().getEncounter().
                            raidBoss().getNpcName() + "!");
                });
                graveYard.clear();
                spawnObjects(true);
                setBossKilled(true);
                break;
        }
        if (debugMode)
            System.out.println("processAction(actionType: " + type + ");");
    }

    /**
     * The method that holds the spawning for the raids objects
     *
     * @param spawn Whether the objects are being spawned or despawned
     */
    private void spawnObjects(boolean spawn) {
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();
        final WorldTile chestTile = getWorldTile(raidsManager.getEncounter().raidTiles().getChestTile()[0],
                raidsManager.getEncounter().raidTiles().getChestTile()[1]);
        final WorldTile portalExitTile = getWorldTile(raidsManager.getEncounter().raidTiles().getExitTile()[0],
                raidsManager.getEncounter().raidTiles().getExitTile()[1]);
        WorldObject chest = (new WorldObject(73894,
                10, 0, chestTile));
        WorldObject portal = (new WorldObject(11368,
                10, 0, portalExitTile));
        if (spawn) {
            World.spawnObject(chest, true);
            World.spawnObject(portal, true);
        } else {
            World.removeObject(chest, true);
            World.removeObject(portal, true);
        }
        if (debugMode)
            System.out.println("spawnObjects(spawn: " + spawn + ");");
    }

    /**
     * The method that holds the process the player rewards
     */
    void processRewards() {
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();
        Item veryRareItem = (Item) player.getTemporaryAttributes().get("RAIDS_DROPPED_VERY_RARE");
        if (raidsManager.isBossKilled()) {
            if (player.getRaidsManager().isHasReceivedRewards()) {
                player.sm("Rewards already received");
                return;
            }
            if (veryRareItem != null) {
                World.sendWorldWideMsg(player, veryRareItem.getId(), " has received " + veryRareItem.getName() +
                                " at " + Utils.formatPlayerNameForDisplay(raidsManager.getEncounter().raidName()) + " Raid in "
                                + player.getGameModeName(false).toLowerCase() + " mode!", Color.RED, true,
                        DiscordConstant.DROPS_CHANNEL_ID);
                player.sm("<col=ff0000>Congratulations on your " + veryRareItem.getName() + "!");
            }
            player.getInterfaceManager().sendInterface(364);
            player.getPackets().sendItems(141, player.getRaidsManager().getPlayerRewards());
            player.getRaidsManager().setHasReceivedRewards(true);
            player.setCloseInterfacesEvent(() -> {
                player.getBank().addItems(player.getRaidsManager().getPlayerRewards().toArray(), true);
                player.sm("Your rewards were sent to bank!", true);
            });
        }
        if (debugMode)
            System.out.println("processRewards(player: " + player.getDisplayName().toLowerCase() + ");");
    }

    /**
     * The method that holds generation of raid drops
     *
     * @param player The player we are generating drops for
     */
    private void generateDrops(Player player) {
        ItemsContainer<Item> currentDrops = new ItemsContainer<>(DROPS_PER_CHEST, false);
        RaidDrops raidDrops = player.getRaidsManager().getEncounter().raidDrops();

        for (int i = 0; i < DROPS_PER_CHEST; i++) {
            Random rand = new Random();
            int chance = rand.nextInt(255);
            if (chance < 3) { // very rare
                final WorldTile chestTile = getWorldTile(player.getRaidsManager().getEncounter().raidTiles().getChestTile()[0], player.getRaidsManager().getEncounter().raidTiles().getChestTile()[1]);
                Item randomItem = getRandomItem(raidDrops.getVeryRareDrops());
                if (player.getTemporaryAttributes().get("RAIDS_DROPPED_VERY_RARE") != null
                        && player.getRaidsManager().getGroupOwner().getRaidsManager().isHasReceivedPurpleChest()) {
                    Item randomIte = getRandomItem(raidDrops.getRareDrops());
                    currentDrops.add(randomIte);
                    return;
                }
                currentDrops.add(randomItem);
                player.getTemporaryAttributes().put("RAIDS_DROPPED_VERY_RARE", randomItem);
                player.getRaidsManager().getGroupOwner().getRaidsManager().setHasReceivedPurpleChest(true);
                player.sendCollectionDrop(randomItem, false);
                World.sendPrivateGraphics(player, new Graphics(1849), chestTile);
                player.getRaidsManager().getGroupOwner().
                        getRaidsManager().getRaidGroup().forEach(p2 -> {
                    if (player == p2) {
                        p2.sm("<col=a000a3>You received a purple chest! good luck on your drops " + (p2.getAppearence().isMale() ? "<col=a000a3>boy" : "<col=a000a3>girl") + "!</col>");
                        return;
                    }
                    p2.sm("<col=a000a3>" + player.getDisplayName() + " just received a purple chest! wish " + (p2.getAppearence().isMale() ? "<col=a000a3>him" : "<col=a000a3>her") + " luck!</col>");
                });
                if (debugMode)
                    System.out.println("return(very_rare);");
            } else if (chance < 4) { // rare
                Item randomItem = getRandomItem(raidDrops.getRareDrops());
                currentDrops.add(randomItem);
                if (debugMode)
                    System.out.println("return(rare);");
            } else if (chance < 25) { // uncommon
                Item randomItem = getRandomItem(raidDrops.getUncommonDrops());
                currentDrops.add(randomItem);
                if (debugMode)
                    System.out.println("return(uncommon);");
            } else { // common
                Item randomItem = getRandomItem(raidDrops.getCommonDrops());
                currentDrops.add(randomItem);
                if (debugMode)
                    System.out.println("return(common);");
            }

            if (debugMode)
                System.out.println("generateDrops();");

            // Populates the drop table
            if (i == DROPS_PER_CHEST - 1) {
                populateDropTable(player, currentDrops);
            }
        }
    }

    /**
     * The method that holds the populating for the player rewards
     *
     * @param player       The player we are populating the item container for
     * @param currentDrops The item container that holds the player's drops
     */
    private void populateDropTable(Player player, ItemsContainer<Item> currentDrops) {
        player.getRaidsManager().getPlayerRewards().clear();
        player.getRaidsManager().getPlayerRewards().addAll(currentDrops);
        currentDrops.clear();
        if (debugMode)
            System.out.println("populateDropTable();");
    }

    /**
     * The method that gets a random item from a Item array
     *
     * @param array The type of the drop we are getting (common,uncommon,rare,very rare)
     * @return An item to be added to the rewards item container
     */
    private Item getRandomItem(Item[] array) {
        int randomIndex = Utils.random(array.length);
        Item randomItem = array[randomIndex];
        /*if (isVeryRareItem(player, randomItem)) {
            System.out.println("is rare item");
            player.getTemporaryAttributes().put("RAIDS_DROPPED_VERY_RARE", randomItem);
            player.sendCollectionDrop(randomItem);
        }*/
        if (debugMode)
            System.out.println("getRandomItem(item: " + randomItem.getName().toLowerCase() + ", amount: " + randomItem.getAmount() + ");");
        return randomItem;
    }

    /**
     * The method that sends the raid group info interface for each raid member
     */
    public void sendGroupMembersInterface() {
        boolean hasGroup = player.getRaidsManager().hasRaidGroup();

        if (hasGroup) {
            updateGroupMembersInterface();
            if (player.getInterfaceManager().containsInterface(player.getInterfaceManager().hasResizableScreen() ? RESIZABLE_TAB_ID : FIXED_TAB_ID)) {
                return;
            }
            if (player.getInterfaceManager().containsTab(player.getInterfaceManager().hasResizableScreen() ? RESIZABLE_TAB_ID : FIXED_TAB_ID)) {
                return;
            }
            player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? RESIZABLE_TAB_ID : FIXED_TAB_ID, INTERFACE);
        } else {
            player.getRaidsManager().closeGroupMembersInterface();
        }
    }

    /**
     * The method that updates the raid panel members list for each group member
     */
    private void updateGroupMembersInterface() {
        List<Player> raidGroup = player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidGroup();
        RaidsManager raidsManager = player.getRaidsManager().getGroupOwner().getRaidsManager();
        Player groupOwner = player.getRaidsManager().getGroupOwner();
        for (int component : components) {
            player.getPackets().sendHideIComponent(INTERFACE, component, true);
        }
        if (player.getRaidsManager().hasRaidGroup()) {
            player.getPackets().sendIComponentText(INTERFACE, 3, Utils.formatPlayerNameForDisplay(raidsManager.getEncounter().raidName()));
            for (int i = 0; i < raidGroup.size(); i++) {
                if (raidGroup.get(i) == null)
                    continue;
                player.getPackets().sendHideIComponent(INTERFACE, components[i], false);
                player.getPackets().sendIComponentText(INTERFACE, components[i], (raidGroup.get(i) == groupOwner ? raidGroup.get(i).getDisplayName() + " (<col=ffb300>Owner</col>)" : raidGroup.get(i).getDisplayName()));
            }
        }
    }

    /**
     * The method that updates the raid group panel interface for each raid member
     */
    public void updateGroupListInterface() {
        if (!player.getRaidsManager().hasRaidGroup())
            return;

        if (!player.getInterfaceManager().containsInterface(3043)) {
            return;
        }

        List<Player> groupMembers = player.getRaidsManager().getRaidGroup();

        groupMembers.forEach(groupMember -> {
            if (groupMember == null || !groupMember.hasStarted() || groupMember.hasFinished())
                return;
            RaidsInterfaces.updateLobbyGroupList(groupMember);
        });
    }

    /**
     * The method that closes the group members interface
     */
    public void closeGroupMembersInterface() {
        player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? RESIZABLE_TAB_ID : FIXED_TAB_ID);
    }

    /**
     * The method that returns if the player has a raid group
     *
     * @return true if player has a current raid group, false if not
     */
    public boolean hasRaidGroup() {
        return player.getRaidsManager().getRaidGroup() != null && !player.getRaidsManager().getRaidGroup().isEmpty();
    }

    /**
     * The method that returns the raid WorldTile corrected with offset
     *
     * @param mapX The X coordinate for the instanced map
     * @param mapY The Y coordinate for the instanced map
     * @return return the corrected WorldTile for the raid instanced map
     */
    private WorldTile getWorldTile(final int mapX, final int mapY) {
        final WorldTile tile = new WorldTile(player.getRaidsManager().getRaidBounds()[0] * 8 + mapX,
                player.getRaidsManager().getRaidBounds()[1] * 8 + mapY, 0);
        return tile;
    }

    /**
     * The method that returns the raid boss hitpoints
     *
     * @return The boss hitpoints based on group size
     */
    public int getHPByGroupSize() {
        int baseHp = player.getRaidsManager().getEncounter().raidBoss().getBaseHitpoints();
        int groupSize = player.getRaidsManager().getRaidGroup().size();
        //return ((groupSize == 1 ? baseHp : groupSize == 2 ? baseHp*2 : groupSize == 3 ? baseHp*3 : groupSize == 4 ? baseHp*4 : baseHp*5));
        return 500;
    }

    /**
     * The method that returns the raid boss level
     *
     * @return - the npc level based on group-size
     */
    private int getNpcLevelByGroupSize() {
        int groupSize = player.getRaidsManager().getRaidGroup().size();
        return ((groupSize == 1 ? 500 : groupSize == 2 ? 600 : groupSize == 3 ? 700 : groupSize == 4 ? 800 : 900));
    }

    /**
     * The method that returns the raid difficulty string
     *
     * @return - the raid difficulty string based on group-size
     */
    private String getDifficulty() {
        int groupSize = player.getRaidsManager().getRaidGroup().size();
        return ((groupSize == 1 ? "Ultra Difficult"
                : groupSize == 2 ? "Very Difficult"
                : groupSize == 3 ? "Difficult"
                : groupSize == 4 ? "Average"
                : "Normal"));
    }

    /**
     * The method that holds the raid killcount getter
     *
     * @param index - the index for the encounter we are getting our killcount
     * @return - the amount of times we completed a raid succesfully
     */
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

    /**
     * The method that holds the raid killcount increasing
     *
     * @param index - the index for the encounter we are increasing our killcount
     */
    public void increaseRaidKillcount(int index) {
        switch (index) {
            case 0: // Leunii
                player.setLeeuniKilled(getRaidKillcount(0) + 1);
                break;
            case 1: // Mercenary mage
                player.setWinterKilled(getRaidKillcount(1) + 1);
                break;
        }
    }

    /**
     * The method that holds the raid personal bests for each encounter
     *
     * @param i - the index for the encounter we are saving our personal best time
     */
    public void savePersonalBest(int i) {
        LocalTime completionTime = (LocalTime) player.getTemporaryAttributes().get("RAIDS_COMPLETION_TIME");

        HashMap<Integer, Duration> personalBests = player.getRaidsManager().getPersonalBests();

        Duration between = Duration.between(completionTime, LocalTime.now());

        String time = between.toString().replace("PT", "").replace("H", ":").replace("M", ":").replace("S", "");
        //String s = time.split("\\.")[0].concat("");

        LocalTime t = LocalTime.MIDNIGHT.plus(between);
        String s = DateTimeFormatter.ofPattern("mm:ss").format(t);

        if (personalBests != null && personalBests.get(i) != null) {
            Duration bestTime = personalBests.get(i);
            if (bestTime.compareTo(between) > 0) {
                personalBests.put(i, between);
                player.sm("You got a new personal best: " + s + ", amazing!");
            }
        } else {
            personalBests.put(i, between);
            player.sm("Congratulations on your first completion, your personal best is: " + s + "!");
        }

        //Duration between = Duration.between(LocalDateTime.now(), getPersonalBest(i));
        /*if (player.getRaidsManager().getPersonalBests().get(i).isBefore(LocalTime.now())) {
            player.getRaidsManager().getPersonalBests().put(i, LocalTime.now());
            player.sm("Congratulations, you got a new PB: "+getPersonalBests().get(i).toString());
        }*/
    }

    public String getPersonalBest(int i) {
        LocalTime t = LocalTime.MIDNIGHT.plus(player.getRaidsManager().getPersonalBests().get(i));
        String s = DateTimeFormatter.ofPattern("mm:ss").format(t);
        return s;
    }

    /**
     * The method that holds the raid item drops
     *
     * @param player - the player that is dropping
     * @param item   - the FloorItem we are dropping on ground
     */
    public void dropGroundItem(Player player, FloorItem item) {
        player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().add(item);
        World.addGroundItem(item, player.getTile(), player, false, 0, false);
        player.getPackets().sendSound(2739, 0, 1);
        if (debugMode)
            System.out.println("dropGroundItem(item: " + item.getName().toLowerCase() + ", quant: " + item.getAmount() + ");");
    }

    public void removeGroundItems(Player player, FloorItem item) {
        /*FloorItem floorItem = new FloorItem(item, item.getTile(), item.getOwner(),
                false, false);*/

        /*for (FloorItem raidsItem : player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems()) {
            if (raidsItem.getOwner() != item.getOwner())
                continue;
            if (raidsItem.getTile() != item.getTile())
                continue;
            System.out.println("removeGroundItems(item: "+item.getName().toLowerCase()+", quant: "+item.getAmount()+");");
            player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().remove(item);
            break;
        }*/

        /*for (int i = 0; i < player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().size(); i++) {
                if (player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().get(i).getTile() != item.getTile())
                    continue;
                if (player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().get(i).getOwner() != item.getOwner())
                    continue;
                if (player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().get(i).isGrave() != item.isGrave())
                    continue;
                if (player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().get(i).isInvisible() != item.isInvisible())
                    continue;
                System.out.println("removeGroundItems(item: "+item.getName().toLowerCase()+", quant: "+item.getAmount()+");");
                player.getRaidsManager().getGroupOwner().getRaidsManager().getRaidsItems().remove(i);
                return;
        }*/

        /*if (raidsManager.getRaidsItems().equals(item)) {
            System.out.println("removeGroundItems(item: "+item.getName().toLowerCase()+", quant: "+item.getAmount()+");");
            raidsManager.getRaidsItems().remove(item);
        }*/

        /*raidsManager.getRaidsItems().forEach(fItem-> {
            if (fItem == item) {
                if (raidsManager.getRaidsItems().contains(item)) {
                    World.removeGroundItem(player, item);
                    raidsManager.getRaidsItems().remove(item);
                    System.out.println("removeGroundItems(item: "+item.getName().toLowerCase()+", quant: "+item.getAmount()+");");
                }
            }
        });*/
    }

    public NPC getResigner() {
        return resigner;
    }

    public NPC getBoss() {
        return boss;
    }

    public int[] getRaidBounds() {
        return raidBounds;
    }

    public void setRaidBounds(int[] raidBounds) {
        this.raidBounds = raidBounds;
    }

    public boolean isHasReceivedPurpleChest() {
        return hasReceivedPurpleChest;
    }

    public void setHasReceivedPurpleChest(boolean hasReceivedPurpleChest) {
        this.hasReceivedPurpleChest = hasReceivedPurpleChest;
    }

    public boolean isHasReceivedRewards() {
        return hasReceivedRewards;
    }

    public void setHasReceivedRewards(boolean hasReceivedRewards) {
        this.hasReceivedRewards = hasReceivedRewards;
    }

    public boolean isBossKilled() {
        return bossKilled;
    }

    public void setBossKilled(boolean bossKilled) {
        this.bossKilled = bossKilled;
    }

    public List<Player> getRaidGroup() {
        return raidGroup;
    }

    public void setRaidGroup(List<Player> raidGroup) {
        this.raidGroup = raidGroup;
    }

    public ItemsContainer<Item> getPlayerRewards() {
        return playerRewards;
    }

    public List<Player> getGraveYard() {
        return graveYard;
    }

    public Player getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(Player groupOwner) {
        this.groupOwner = groupOwner;
    }

    public RaidsEncounter getEncounter() {
        return encounter;
    }

    public void setEncounter(RaidsEncounter encounter) {
        this.encounter = encounter;
    }

    public List<NPC> getRaidNpcs() {
        return raidNpcs;
    }

    public List<WorldObject> getRaidObjects() {
        return raidObjects;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<FloorItem> getRaidsItems() {
        return raidsItems;
    }

    public HashMap<Integer, Duration> getPersonalBests() {
        return personalBests;
    }

    public enum Stages {
        LOADING, RUNNING, RECEIVING_REWARDS, RECEIVED_REWARDS, DESTROYING, RESTARTING
    }

    public enum LeaveType {TELEPORT, COMPLETE_AND_QUIT, RESIGN, LOGOUT, RESTART, PARTY_DEPLETED, KICKED}

    public enum ActionType {ALL_PLAYERS_DEAD, ONE_PLAYER_DEAD, KILLED_THE_BOSS}
}
