package plugin.interaction.inter.custom;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Handles the mini timers interface
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 12/10/2020 - 08:17
 * @project insidious530
 */
public class MiniTimers {
    /**
     * The constants for screen sizes
     */
    private static int FIXED = 548, RESIZEABLE = 746;

    /**
     * The entity
     */
    private final Entity entity;

    /**
     * When we last used our barrage
     */
    private LocalTime lastBarrage;

    /**
     * When we last used our veng
     */
    private LocalTime lastVeng;

    /**
     * Gets the last barrage
     * @return The lastBarrage
     */
    public LocalTime getLastBarrage() {
        return lastBarrage;
    }

    /**
     * Gets the last veng
     * @return lastVeng
     */
    public LocalTime getLastVeng() {
        return lastVeng;
    }

    /**
     * The constructor
     * @param player The player
     */
    public MiniTimers(Entity player) {
        this.entity = player;
    }

    /**
     * Called every pulse (600ms).
     */
    public void pulse() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;

        updateInterface(player);
    }

    /**
     * Starts the barrage timer
     */
    public void startBarrageTimer(int seconds) {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        this.lastBarrage = LocalTime.now().plusSeconds(seconds);
        System.out.println("barrage timer started");
    }

    /**
     * Starts the vengeance timer
     */
    public void startVengTimer(int seconds) {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        this.lastVeng = LocalTime.now().plusSeconds(seconds);
        System.out.println("veng timer started");
    }

    /**
     * Gets the interface based by size
     * @param player The player
     * @return the interfaceId
     */
    private int getInterface(Player player) {
        return player.getInterfaceManager().isResizable() ? RESIZEABLE : FIXED;
    }

    /**
     * Gets the barrage icon id
     * @param player The player
     * @return The barrage icon id
     */
    private int getBarrageIconId(Player player) {
        return player.getInterfaceManager().isResizable() ? 121 : 110;
    }

    /**
     * Gets the barrage string id
     * @param player The player
     * @return The barrage strinng id
     */
    private int getBarrageStringId(Player player) {
        return player.getInterfaceManager().isResizable() ? 124 : 113;
    }

    /**
     * Gets the veng icon id
     * @param player The player
     * @return The veng icon id
     */
    private int getVengIconId(Player player) {
        return player.getInterfaceManager().isResizable() ? 125 : 114;
    }

    /**
     * Gets the veng string id
     * @param player The player
     * @return The veng string id
     */
    private int getVengStringId(Player player) {
        return player.getInterfaceManager().isResizable() ? 128 : 117;
    }


    /**
     * Updates the interface components
     * @param player The entity
     */
    private void updateInterface(Player player) {
        if (lastBarrage != null) {
            // Show the barrage icon
            player.getPacketDispatch().sendInterfaceConfig(getInterface(player),
                    getBarrageIconId(player), false);

            Duration between = Duration.between(LocalTime.now(), lastBarrage);

            String time = between.toString().replace("PT", "").replace("H", "h ").replace("M", "m ").replace("S", "s ");
            String s = time.split("\\.")[0].concat("s");

           // Time has passed
           if (between.isZero() || between.isNegative()) {
               lastBarrage = null;
               System.out.println("Time has passed, reseted lastBarrage");
               player.sendMessage("<col=eb5a00>Your can now cast another ice barrage spell!</col>");
               return;
           }

            // Sends the time left to screen
            player.getPacketDispatch().sendString(s, getInterface(player), getBarrageStringId(player));
        } else {
            player.getPacketDispatch().sendInterfaceConfig(player.getInterfaceManager().isResizable() ? RESIZEABLE : FIXED,
                    getBarrageIconId(player), true);
        }

        if (lastVeng != null) {
            // Show the veng icon
            player.getPacketDispatch().sendInterfaceConfig(getInterface(player),
                    getVengIconId(player), false);

            Duration between = Duration.between(LocalTime.now(), lastVeng);

            String time = between.toString().replace("PT", "").replace("H", "h ").replace("M", "m ").replace("S", "s ");
            String s = time.split("\\.")[0].concat("s");

            // Time has passed
            if (between.isZero() || between.isNegative()) {
                lastVeng = null;
                System.out.println("Time has passed, reseted lastVeng");
                player.sendMessage("<col=eb5a00>Your can now cast another taste vengeance spell!</col>");
                return;
            }

            // Sends the time left to screen
            player.getPacketDispatch().sendString(s, getInterface(player), getVengStringId(player));
        } else {
            player.getPacketDispatch().sendInterfaceConfig(player.getInterfaceManager().isResizable() ? RESIZEABLE : FIXED,
                    getVengIconId(player), true);
        }
    }
}
