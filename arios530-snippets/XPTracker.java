package plugin.interaction.inter.custom.xptracker;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import org.json.simple.JSONObject;
import plugin.skill.Skills;

import java.text.NumberFormat;
import java.util.Locale;

import static plugin.stringtools.StringToolsKt.colorize;

/**
 * Experience tracker system
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 06/10/2020 - 15:04
 * @project 530-rsps
 */

/**
 * fixed:
 * full panel: 101
 * disabled: 104
 * enabled: 103
 * xp panel: 105
 * xp string: 108
 *
 *
 * resizable:
 * full panel: 112
 * xp panel: 116
 * disabled: 114
 * enabled: 115
 * xp string: 119
 */
public class XPTracker {
    /**
     * The constants for screen sizes
     */
    private static int FIXED = 548, RESIZEABLE = 746;

    /**
     * The entity
     */
    private final Entity entity;

    /**
     * The total XP we are storing
     */
    public double totalXP;

    /**
     * The constructor
     */
    public XPTracker(Entity entity) {
        this.entity = entity;
    }

    public void parse(JSONObject data){
        totalXP = Double.parseDouble(data.get("totalXP").toString());
    }

    /**
     * Opens the interface
     */
    /*public void open() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        boolean isResizeable = player.getInterfaceManager().isResizable();

        player.getSavedData().getActivityData().setXpTracker(true);
        //player.getAttributes().put("xptracker:open", Boolean.TRUE);

        if (isResizeable) {
            player.getPacketDispatch().sendInterfaceConfig(RESIZEABLE, 116, false); // XP panel
        } else {
            player.getPacketDispatch().sendInterfaceConfig(FIXED, 105, false); // XP panel
        }
        //toggleTab(false);

        drawXpComponents();
    }*/

    /**
     * Hides the XP tracker tab
     */
    /*public void hide() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        boolean isResizeable = player.getInterfaceManager().isResizable();
        //player.getAttributes().put("xptracker:open", Boolean.FALSE);
        player.getSavedData().getActivityData().setXpTracker(false);

        if (isResizeable) {
            player.getPacketDispatch().sendInterfaceConfig(RESIZEABLE, 116, true); // XP panel
        } else {
            player.getPacketDispatch().sendInterfaceConfig(FIXED, 105, true); // XP panel
        }
        //toggleTab(true);
    }*/

    /**
     * Toggles the XPTracker button with tab
     * @param show true to show - false to hide
     */
    public void toggle(boolean show) {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
            if (player == null || player.isArtificial())
                return;
            boolean isResizeable = player.getInterfaceManager().isResizable();

        // Toggles the tab sprite
        toggleTabSprite(show);

        // Sets the tab to whether opened or hidden
        player.getSavedData().getActivityData().setXpTracker(show);

        // Show or hide the mini panel
        player.getPacketDispatch().sendInterfaceConfig(isResizeable ? RESIZEABLE : FIXED, isResizeable ? 116 : 105, show); // XP panel
    }

    /**
     * Draws the components
     */
    private void drawXpComponents() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        boolean isResizeable = player.getInterfaceManager().isResizable();
        boolean hasXp = getTotalXP() > 0;

        // Sends the current xp to screen
        player.getPacketDispatch().sendString((hasXp ? (totalXP >= Integer.MAX_VALUE ? "Lots" : NumberFormat.getNumberInstance(Locale.US).format((int) getTotalXP())) : "None"),
                isResizeable ? RESIZEABLE : FIXED, isResizeable ? 119 : 108);

        player.getPacketDispatch().sendAnimationInterface( 25, isResizeable ? RESIZEABLE : FIXED, isResizeable ? 119 : 108);
    }

    /**
     * Switchs the XP orb sprite based on its status
     * @param show Show the enabled sprite?
     */
    private void toggleTabSprite(boolean show) {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        boolean isResizeable = player.getInterfaceManager().isResizable();
        player.getPacketDispatch().sendHideIComponent(isResizeable ? RESIZEABLE : FIXED, isResizeable ? 115 : 103, show);
        player.getPacketDispatch().sendHideIComponent(isResizeable ? RESIZEABLE : FIXED, isResizeable ? 114 : 104, !show);
    }

    /**
     * Resets the accumulated XP
     */
    public void resetXp() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        totalXP = 0;
        player.sendMessage("Your total XP was resetted!");
    }

    /**
     * Setups the XP tracker
     */
    public void setup() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        player.sendMessage("Work in progress");
    }

    /**
     * Called every pulse (600ms).
     */
    public void pulse() {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;

        toggleTabSprite(player.getSavedData().getActivityData().isXpTracker());

        //if (player.getSavedData().getActivityData().isXpTracker()) {
            drawXpComponents();
        //}
    }

    /**
     * Gets the total XP from our storage
     * @return The total XP
     */
    public double getTotalXP() {
        return totalXP;
    }

    /**
     * Sets the total XP to our storage
     * @param totalXP The totalXP
     */
    public void setTotalXP(double totalXP) {
        this.totalXP = totalXP;
    }

    /**
     * Gets the player
     * @return The player
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Increases the XP on the tracker
     * @param xp The xp
     * @param slot The skillId
     */
    public void increaseXp(double xp, int slot) {
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        if (player == null || player.isArtificial())
            return;
        if (xp <= 0) {
            if (player.isDebug())
            System.out.println("Xp gaining was zero so returned");
            return;
        }
        this.totalXP += xp;
        //if (player.getAttributes().getOrDefault("xptracker:open", Boolean.FALSE) == Boolean.TRUE) {
        if (!player.getSavedData().getActivityData().isXpTracker()) {
            player.sendMessage(colorize("%R"+NumberFormat.getNumberInstance(Locale.US).format((int) xp) + "</col> " + Skills.SKILL_NAME[slot] + " XP was tracked!"));
        }
    }
}
