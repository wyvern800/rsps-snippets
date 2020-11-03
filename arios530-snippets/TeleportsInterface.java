package plugin.interaction.inter.custom.teleports;


import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles the Teleports interface
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 01/10/2020 - 02:29
 * @project 530-rsps
 */

@InitializablePlugin
public final class TeleportsInterface extends ComponentPlugin {
    /**
     * Constants
     */
    private static final int INTER = 836;
    private static final int locationName = 36, smallDescription = 37;
    private static final String unMarked = "<col=bf751d>", marked = "<col=ff981f>";

    /**
     * The previous components
     */
    private static final Integer[] previousComponents = {28, 30, 32}; //i+1

    /**
     * The category components
     */
    private static final Integer[] categoryComponents = {43, 50, 57, 64, 71, 78, 85, 92}; //select comp: i+4 || text: i +6

    /**
     * The locations componennts
     */
    private static final Integer[] locationsComponents = {101, 108, 115, 122, 129, 136, 143, 150, 157, 164, 171, 178, 185,
            192, 199, 206, 213, 220, 227, 234, 241, 248, 255, 262, 269, 276} ;

    /**
     * the teleporter
     */
    private static NPC theTeleporter;

    /**
     * The teleport is busy
     */
    private static boolean teleporterBusy;

    /**
     * Checks if the teleporter is busy
     * @return true if yes | false if not
     */
    public static boolean isTeleporterBusy() {
        return teleporterBusy;
    }

    /**
     * Sets the teleports busy
     * @param teleporterBusy true or false
     */
    public static void setTeleporterBusy(boolean teleporterBusy) {
        TeleportsInterface.teleporterBusy = teleporterBusy;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(INTER, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        // Handle the categories clicks
        for (int i = 0; i < categoryComponents.length; i++) {

            // Checks if button we clicking equals to the categories components
            if (button != categoryComponents[i])
                continue;

            // Handles the clicks
            handleCategoriesClick(player, i);
            break;
        }

        // Handle the locations clicks
        for (int i = 0; i < locationsComponents.length; i++) {

            // Checks if button we clicking equals to the categories components
            if (button != locationsComponents[i])
                continue;

            // Handles the clicks
            handleLocationsClick(player, i);
            break;
        }

        // Previous teleports clicks
        for (int i = 0; i < previousComponents.length; i++) {
            if (button == previousComponents[i]) {
                List<Teleports> previous = new ArrayList<>(player.getPreviousTeleports());
                Collections.reverse(previous);

                // Loops through the list 
                for (Teleports tempTele : Teleports.values()) {
                    if (tempTele != previous.get(i))
                        continue;

                    player.getAttributes().put("teleports:location", tempTele);
                    processTeleport(player);
                    // Teleport index equals
                    System.out.println("Clicking into "+previous.get(i).name());
                }
            }
        }

        // Teleport
        if (button == 38) {
            processTeleport(player);
        }
        return true;
    }

    /**
     * Opens the interface for the player
     * @param player The player
     */
    public static void open(Player player) {
        player.getInterfaceManager().openComponent(INTER);
        showDescriptionPanel(player, false);
        showLocationsPanel(player, false);
        refresh(player);
    }

    /**
     * Opens the interface for the player - with npc
     * @param player The player
     * @param npc The npc
     */
    public static void open(Player player, NPC npc) {
        theTeleporter = npc;
        player.getInterfaceManager().openComponent(INTER);
        showDescriptionPanel(player, false);
        showLocationsPanel(player, false);
        refresh(player);
    }

    /**
     * Refreshes the components on the screenn
     * @param player
     */
    private static void refresh(Player player) {
        drawCategories(player);
        drawPrevious(player);
    }

    /**
     * Draws the categories on the left panel
     * @param player The player
     */
    private static void drawCategories(Player player) {
        int count = 0;
        for (Categories cat : Categories.values()) {
            if (cat == null)
                continue;

            // Draw the component
            player.getPacketDispatch().sendInterfaceConfig(INTER, categoryComponents[count], false);

            // Sets the name to it
            player.getPacketDispatch().sendString(unMarked+cat.toString()+"</col>", INTER, categoryComponents[count]+6);

            count++;
        }
    }

    /**
     * Selects the category on the left panel
     * @param player The player
     */
    private static void selectCategory(Player player) {
        // Cleans the locations highlight
        for (Integer locationsComponent : locationsComponents) {
            player.getPacketDispatch().sendHideIComponent(INTER, locationsComponent+4, true);
        }

        // Shows the locations panel
        showLocationsPanel(player, true);

        // Gets the player current category
        Categories category = (Categories) player.getAttributes().get("teleports:category");
        highlightCategory(player, category);
        drawLocations(player, category);
    }

    /**
     * Selects the location on the middle panel
     * @param player The player
     */
    private static void selectLocation(Player player) {
        Categories category = (Categories) player.getAttributes().get("teleports:category");
        Teleports teleport =  (Teleports) player.getAttributes().get("teleports:location");

        // Shows the description panel
        showDescriptionPanel(player, true);

        // Gets the player current category
        highlightLocation(player, category, teleport);
        drawDescription(player, category, teleport);
    }

    /**
     * Highlights the location
     * @param player The player
     * @param category The category
     */
    private static void highlightLocation(Player player, Categories category, Teleports teleport) {
        // Cleans the other highlights
        for (Integer locationsComponent : locationsComponents) {
            player.getPacketDispatch().sendHideIComponent(INTER, locationsComponent+4, true);
        }

        // Loops trough the categories to check if we are pointing to the correct one
        for (Categories catLoop : Categories.values()) {
            if (catLoop != category)
                continue;

                // Loops through the teleports to check if we are pointing to the correct one
                for (Teleports teleLoop : Teleports.values()) {
                    if (teleLoop != teleport)
                        continue;

                    // Highlight the location
                    player.getPacketDispatch().sendHideIComponent(INTER, locationsComponents[teleport.getId()]+4, false);

                    System.out.println("highlightLocation(cat="+category.name()+", tele="+teleport.name()+");");
                    break;
                }
            break;
        }
    }

    /**
     * Highlights the player category
     * @param player The player
     */
    private static void highlightCategory(Player player, Categories category) {
        // Cleans the other highlights
        for (Integer categoriesComponent : categoryComponents) {
            player.getPacketDispatch().sendHideIComponent(INTER, categoriesComponent+4, true);
        }

        // Loops trough the categories to check if we are pointing to the correct one
        for (Categories catLoop : Categories.values()) {
            if (catLoop != category)
                continue;

            // Highlight the category
            player.getPacketDispatch().sendHideIComponent(INTER, categoryComponents[category.getId()]+4, false);

            System.out.println("highlightCategory(cat="+category.name()+");");
            break;
        }
    }

    /**
     * Draws the locations on the middle panel
     * @param player The player
     * @param category The location category
     */
    private static void drawLocations(Player player, Categories category) {
        // Hide all other components
        for (Integer locationsComponent : locationsComponents) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, locationsComponent, true);
        }

        int count = 0;
        for (Teleports teleport : Teleports.values()) {

            if (teleport.getCategory() != category)
                continue;

            // Unnhid the component
            player.getPacketDispatch().sendInterfaceConfig(INTER, locationsComponents[count], false);

            // Sets the name to it
            player.getPacketDispatch().sendString(teleport.toString().replace("_", " "), INTER, locationsComponents[count]+6);

            count++;
        }
    }

    /**
     * Draws the location description on the bottom panel
     * @param player The player
     */
    private static void drawDescription(Player player, Categories category, Teleports tele) {
        for (Teleports teleport : Teleports.values()) {
            if (teleport.getCategory() != category)
                continue;
            if (teleport != tele)
                continue;

            // Builds the location name
            StringBuilder stb = new StringBuilder();
            stb.append(teleport.toString().replace("_", " ")).append(" ");
            if (teleport.isWilderness()) {
                stb.append("<col=ff0000>(Wildy)</col>");
            }
            if (teleport.isMulti()) {
                stb.append("<col=ffae00>(Multi)</col>");
            }
            if (!teleport.isMulti() && !teleport.isWilderness()) {
                stb.append("<col=11ad00>(Safe)</col>");
            }

            // Sets the location name
            player.getPacketDispatch().sendString(stb.toString(), INTER, locationName);

            // Sets the description of the location
            player.getPacketDispatch().sendString(teleport.getDescription().replace("_", " "), INTER, smallDescription);
        }
    }

    /**
     * Draws the previous teleportation we used
     * @param player The player
     */
    private static void drawPrevious(Player player) {
        List<Teleports> previous = new ArrayList<>(player.getPreviousTeleports());
        Collections.reverse(previous); // Reverses the list

        // Loops trough the previous teleports we did
        for (int i = 0; i < previous.size(); i++) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, previousComponents[i], false);
            player.getPacketDispatch().sendString(previous.get(i).name().replace("_", " "), INTER, previousComponents[i]+1);
        }
        //TODO
    }

    /**
     * Handles the clicking on categories components
     * @param player The player
     * @param index The position on the locations array
     */
    private static void handleCategoriesClick(Player player, int index) {
        // Loops trough the categories
        for (Categories category : Categories.values()) {

            // Checks if the id of category we clicking equals to the pointer one we are looping trough
            if (category.getId() != index)
                continue;

            // Processes the category selection
            player.getAttributes().put("teleports:category", category);
            selectCategory(player);
            refresh(player);

            System.out.println("Clicked category: " + category.toString());
            break;
        }
    }

    /**
     * Handles the clicking on location components
     * @param player The player
     * @param index The position on the locations array
     */
    private static void handleLocationsClick(Player player, int index) {
        // Loops trough the categories
        for (Teleports teleport : Teleports.values()) {
            // Gets the player current category
            Categories category = (Categories) player.getAttributes().get("teleports:category");

            if (teleport.getCategory() != category)
                continue;
            
            // Checks if the id of category we clicking equals to the pointer one we are looping trough
            if (teleport.getId() != index)
                continue;

            // Processes the location selection
            player.getAttributes().put("teleports:location", teleport);
            selectLocation(player);
            refresh(player);

            System.out.println("Clicked location: " + teleport.toString());
            break;
        }
    }

    /**
     * Show the description panel and show the message instead
     * @param player The player
     * @param show Should the panel be shown
     */
    private static void showDescriptionPanel(Player player, boolean show) {
        if (show) {
            // Shows the description panel
            player.getPacketDispatch().sendInterfaceConfig(INTER, 17, false);

            // Hides the message informing player to select a location before
            player.getPacketDispatch().sendInterfaceConfig(INTER, 283, true);
        } else {
            // Hides the description panel
            player.getPacketDispatch().sendInterfaceConfig(INTER, 17, true);

            // Shows the message informing player to select a location before
            player.getPacketDispatch().sendInterfaceConfig(INTER, 283, false);
        }
    }

    /**
     * Show the locations panel and show the message instead
     * @param player The player
     * @param show Should the panel be shown
     */
    private static void showLocationsPanel(Player player, boolean show) {
        if (show) {
            // Show the locations panel
            player.getPacketDispatch().sendInterfaceConfig(INTER, 16, false);

            // Hide the message informing player to select a category before
            player.getPacketDispatch().sendInterfaceConfig(INTER, 286, true);
        } else {
            // Hide the locations panel
            player.getPacketDispatch().sendInterfaceConfig(INTER, 16, true);

            // Shows the message informing player to select a category before
            player.getPacketDispatch().sendInterfaceConfig(INTER, 286, false);
        }
    }

    /**
     * Processes the player teleport
     * @param player The player
     */
    private static void processTeleport(Player player) {
        Teleports teleport =  (Teleports) player.getAttributes().get("teleports:location");

        if (teleport == null) {
            player.sendMessage("Please select a location before teleporting!");
            return;
        }
        
        if (!player.getPreviousTeleports().contains(teleport)) {
            player.getPreviousTeleports().add(teleport);
            if (player.getPreviousTeleports().size() > 3)
                player.getPreviousTeleports().remove(0); // Removes the first element
        } else {
            System.out.println("tele exists");
        }

        player.getInterfaceManager().close(new Component(INTER));

        if (!isTeleporterBusy()) {
            GameWorld.Pulser.submit(new Pulse(1, player) {
                int tick = 0;
                @Override
                public boolean pulse() {
                    if (tick  == 0) {
                        player.lock();
                        theTeleporter.faceTemporary(player, 3);
                        setTeleporterBusy(true);
                    } else if (tick == 1) {
                        theTeleporter.animate(new Animation(437));
                        theTeleporter.graphics(new Graphics(108));
                        player.getAudioManager().send(195);
                        theTeleporter.sendChat("Seventior Disthine Molenko!");
                        Projectile.create(theTeleporter, player, 109).send();
                    } else if (tick == 3) {
                        player.teleport(teleport.getLocation());
                        player.unlock();
                        setTeleporterBusy(false);
                    }
                    tick++;
                    return false;
                }
            });
        } else {
            player.teleport(teleport.getLocation());
        }

        System.out.println("Teleported "+player.getName()+ ", to "+teleport.toString()+"!");
    }

    /**
     * Processes the previous teleport
     * @param player  The player
     */
    public static void previous(Player player) {
        List<Teleports> previous = player.getPreviousTeleports();
        if (previous.isEmpty()) {
            player.sendMessage("You did not teleport yet!");
            return;
        }
        player.getAttributes().put("teleports:location", previous.get(0));
        processTeleport(player);

        System.out.println("previous");
    }
}
