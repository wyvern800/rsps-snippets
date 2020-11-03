package plugin.interaction.inter.custom;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;

/**
 * Holds the commands list interface
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 09/10/2020 - 03:14
 * @project insidious530
 */
@InitializablePlugin
public class CommandsList extends ComponentPlugin {
    /**
     * The interface id
     */
    private static final int INTER = 838;

    /**
     * The component ids
     */
    private static final int[] commandsComponents = {22, 27, 32, 37, 42, 47, 52, 57, 62, 67, 72, 77, 82,
            87, 92, 97, 102, 107, 112, 117, 122, 127, 132, 137, 142, 147};

    private enum Commands {
        // Players
        HOME("Sends player to home", Category.PLAYERS),
        GAMEMODES("Opens the gamemodes interface", Category.PLAYERS),

        // Mods
        KICK("Kicks another player", Category.MODS),

        BAN("Bans another player", Category.ADMINS)
        ;

        /**
         * The description
         */
        private final String description;

        /**
         * The category
         */
        private final Category category;

        /**
         * Gets the command description
         * @return The description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the category
         * @return The category
         */
        public Category getCategory() {
            return category;
        }

        /**
         * Creates the command
         * @param description The command description
         * @param category The command category
         */
        Commands(String description, Category category) {
            this.description = description;
            this.category = category;
        }
    }

    /**
     * Opens the command list
     * @param player The player
     */
    public static void open(Player player) {
        player.getInterfaceManager().openComponent(INTER);
        // Gets the category to show when opening
        Category category = (Category) player.getAttributes().getOrDefault("cmdlist:cat", Category.PLAYERS);

        // Hide all other panels before starting
        for (int commandsComponent : commandsComponents) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, commandsComponent, true);
        }

        // Loops through the existing commands list
        int i = 0;
        for (Commands command : Commands.values()) {
            if (command.getCategory() != category)
                continue;
                player.getPacketDispatch().sendInterfaceConfig(INTER, commandsComponents[i], false);
                player.getPacketDispatch().sendString("<col=ffe100>;;"+command.toString().toLowerCase()+"</col>", INTER, commandsComponents[i]+3);
                player.getPacketDispatch().sendString(command.getDescription(), INTER, commandsComponents[i]+4);
            i++;
        }
    }

    /**
     * Wrapper open method to open commands list by category id
     * @param p The player
     * @param category The category
     */
    public static void open(Player p, Category category) {
        p.getAttributes().put("cmdlist:cat", category);
        open(p);
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(INTER, this);
        return this;
    }

    /**
     * Creates a command object
     */
    private static class Command {
        private final String description;
        private final Category category;

        /**
         * Gets the description
         * @return The description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the category
         * @return The category
         */
        public Category getCategory() {
            return category;
        }

        /**
         * Crates the command object
         * @param description The command description
         * @param category The category id
         */
        public Command(String description, Category category) {
            this.description = description;
            this.category = category;
        }

    }

    /**
     * Creates a category object
     */
    public enum Category {
        PLAYERS(0), MODS(1), ADMINS(2);

        /**
         * The category id
         **/
        private final int id;

        /**
         * Gets the category id
         * @return The category id
         */
        public int getId() {
            return id;
        }

        /**
         * The category object
         * @param id The category id
         */
        Category(int id) {
            this.id = id;
        }
    }
}
