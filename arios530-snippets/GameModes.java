package plugin.interaction.inter.custom.gamemodes;

import core.ServerConstants;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.login.PlayerParser;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.item.Item;
import core.net.packet.in.ExaminePacket;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;

/**
 * Represents a component plugin to handle the gamemodes interface.
 * @author Sagacity - https://rune-server.org/members/sagacity
 * @version 1.2
 */

@InitializablePlugin
public class GameModes extends ComponentPlugin {
    /**
     * Represents the interface id
     */
    private static final int INTER = 835;
    /**
     * Holds all the gamemodes list component ids
     */
    private static final Integer[] gameModesListComponents = {122, 127, 132, 137, 142, 147, 152, 165, 170, 175, 180, 185};

    /**
     * Holds the skills section component ids
     */
    private static final Integer[] skillsComponents = {90, 86, 94, 98, 102, 106, 110, 114};

    /**
     * Holds the colors
     */
    private static final String UNMARKED = "<col=bf751d>", MARKED = "<col=ff981f>";

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(835, this);
        return this;
    }

    /**
     * Opens the interface
     * @param player The player
     */
    public static void open(Player player) {
        /*if (player.getSavedData().getActivityData().isChosenGamemode()) {
            player.sendMessage("You already chosen your gamemode!");
            return;
        }*/
        player.getInterfaceManager().openComponent(835);
        drawList(player);
        selectGameMode(player);
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
                switch (button) {
                    case 119: // Confirm & Play
                        Modes mod = (Modes) player.getAttributes().getOrDefault("gamemodes:selected", Modes.Regular);
                        if (mod == null) {
                            player.sendMessage("You must select a gamemode before confirming!");
                            return true;
                        }
                        confirmAndPlay(player, mod);
                        break;

                    case 41: // Examining
                        Modes mode = (Modes) player.getAttributes().get("gamemodes:selected");

                        for (int x = 0; x < mode.getStartingItems().length; x++) {
                            if (mode.getStartingItems()[x].getSlot() == x) {
                                player.sendMessage(ExaminePacket.getItemExamine(mode.getStartingItems()[x].getId()));
                                break;
                            }
                        }
                        break;
        }

        // Holds the selection of the gamemode on the left panel
        //int cont = 0;
        for (Modes mode : Modes.values()) {
            if (mode.getId() == -1)
                continue;
            if (mode.getComponentId() != button)
                continue;

                player.getAttributes().put("gamemodes:selected", mode);
                selectGameMode(player);
                if (player.isDebug()) {
                    System.out.println("selectGamemode=" + mode.name().replace("_"," "));
                    player.sendMessage("You have selected the mode: <col=ff0000>" + mode.name().replace("_", " ") + "</col>");
                }

            //cont++;
        }
        return true;
    }

    /**
     * Sends the gamemodes list to the interface
     * @param player The player
     */
    public static void drawList(Player player) {
        int cont = 0;

        // Loops through the list
       for (Modes mode : Modes.values()) {
           if (mode == null) { // If gamemode not exists, then hide it
               player.getPacketDispatch().sendInterfaceConfig(INTER, gameModesListComponents[cont], true);
               continue;
           }

           player.getPacketDispatch().sendInterfaceConfig(INTER, gameModesListComponents[cont], false);
           player.getPacketDispatch().sendString(mode.name().replace("_"," "), INTER, gameModesListComponents[cont]+4);
           cont++;
       }
    }

    /**
     * Selects the gamemodes
     * @param player The player
     */
    private static void selectGameMode(Player player) {
        Modes mode = (Modes) player.getAttributes().getOrDefault("gamemodes:selected", Modes.Regular);

        drawRates(player, mode);
        drawEquipments(player, mode);
        drawStarterItems(player, mode);
        drawSkills(player, mode);
        highlight(mode, player);
    }

    /**
     * Sends all the skills of the gamemode
     * @param player The player
     * @param mode The gamemode we selected
     */
    private static void drawSkills(Player player, Modes mode) {
        if (mode.getStartingSkills() != null) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, 23, false);
            player.getPacketDispatch().sendInterfaceConfig(INTER, 164, true);
            // Fills all skills with level 1
            for (int x = 0; x < skillsComponents.length; x++) {
                player.getPacketDispatch().sendString("1", INTER, skillsComponents[x] + 3);
            }

            // Sends the gamemode levels corrected
            for (int i = 0; i < skillsComponents.length; i++) {
                if (mode.getStartingSkills().getSkillList().get(i).getComponentId() == null)
                    continue;
                player.getPacketDispatch().sendString("" + mode.getStartingSkills().getLevel()[i], INTER,
                        mode.getStartingSkills().getSkillList().get(i).getComponentId() + 3);
            }
        } else {
            player.getPacketDispatch().sendInterfaceConfig(INTER, 23, true);
            player.getPacketDispatch().sendInterfaceConfig(INTER, 164, false);
        }
    }

    /**
     * Sends the equipments of the gamemode
     * @param player The player
     * @param mode The gamemode we selected
     */
    private static void drawEquipments(Player player, Modes mode) {
              for (int i = 0; i < mode.getStartingSet().getSlots().size(); i++) {
                  player.getPacketDispatch().sendInterfaceConfig(INTER, mode.getStartingSet().getSlots().get(i).getSlotId()+2, false);
                  player.getPacketDispatch().sendInterfaceConfig(INTER, mode.getStartingSet().getSlots().get(i).getComponentId()+2, false);

                  if (mode.getStartingSet().getSlots().isEmpty())
                      continue;
                  if (mode.getStartingSet().getEquipments()[i] == null) {
                      player.getPacketDispatch().sendItemOnInterface(-1, 0, INTER, mode.getStartingSet().getSlots().get(i).getComponentId()+3);
                      continue;
                  }
                      player.getPacketDispatch().sendInterfaceConfig(INTER, mode.getStartingSet().getSlots().get(i).getComponentId()+2, true);
                      player.getPacketDispatch().sendItemOnInterface(mode.getStartingSet().getEquipments()[i], 1, INTER, mode.getStartingSet().getSlots().get(i).getComponentId()+3);
              }
        //player.getPacketDispatch().sendItemOnInterface(mode.getStartingSet().getHead(), 1, INTER, mode.getStartingSet().getSlots()[0].getComponentId());
    }

    /**
     * Draws the exp rate to the screen
     * @param player
     * @param mode The gamemode we selected
     */
    private static void drawRates(Player player, Modes mode) {
        StringBuilder stb = new StringBuilder();
        stb.append("- XP Rate: ").append(mode.getXpRate()).append("x").append("<br>");
        stb.append("- Drop Rate: ").append(mode.getDropRate()).append("%").append("<br>");
        player.getPacketDispatch().sendString(stb.toString(), INTER, 118);
    }

    /**
     * Sends the items to the interface container
     * @param player The player
     * @param mode The gamemode we selected
     */
    private static void drawStarterItems(Player player, Modes mode) {
        Item[] rewards = mode.getStartingItems();

        InterfaceContainer.generateItems(player,rewards,new String[]{ "Examine" },INTER,41);
    }

    /**
     * Highlights the selection we are clicking on
     * @param mode The gamemode we clicked
     * @param player The player
     */
    private static void highlight(Modes mode, Player player) {
        // Unmark the others
        int x = 0;
        for (Modes modAux: Modes.values()) {
            if (mode.getId() == -1)
                continue;
            player.getPacketDispatch().sendString(UNMARKED+modAux.name().replace("_"," ")+"</col>", INTER,gameModesListComponents[x]+4);
            x++;
        }

        // Highlights the mode we clicked on the left-panel list
        for (int i = 0; i < gameModesListComponents.length; i++) {
            // Hides the red selection to the others
            player.getPacketDispatch().sendInterfaceConfig(INTER, gameModesListComponents[i]+3, true);

            if (mode.getId() == -1)
                continue;

            if (mode.getId() == i) {
                player.getPacketDispatch().sendInterfaceConfig(INTER, gameModesListComponents[i]+3, false);
                player.getPacketDispatch().sendString(MARKED+mode.name().replace("_"," ")+"</col>", INTER,gameModesListComponents[i]+4);
            }
        }
    }

    /**
     * Used to give the player the items related to it's gamemode
     * @param mode The gamemode
     * @param player The player
     */
    private static void receiveItems(Modes mode, Player player) {
        for (int i = 0; i < mode.getStartingItems().length; i++) {
            player.getInventory().add(mode.getStartingItems()[i]);
        }
        if (player.isDebug())
            System.out.println("receiveItems(mode=" + mode + ", player=" + player + ");");
    }

    /**
     * Used to give the player the equipments related to it's gamemode
     * @param mode The gamemode
     * @param player The player
     */
    private static void receiveEquipments(Modes mode, Player player) {
        for (int i = 0; i <mode.getStartingSet().getSlots().size(); i++) {
            //player.getEquipment().get(i).setId(mode.getStartingSet().getEquipments()[i]);
            if (mode.getStartingSet().getEquipments()[i] !=null) {
                player.getEquipment().replace(new Item(mode.getStartingSet().getEquipments()[i]), mode.getStartingSet().getSlots().get(i).getSlotId());
            }
        }
        if (player.isDebug())
            System.out.println("receiveEquipments(mode="+mode+", player="+player+");");
    }

    /**
     * Used to give the player the skills related to it's gamemode
     * @param mode The gamemode
     * @param player The player
     */
    private static void receiveSkills(Modes mode, Player player) {
        if (mode.getStartingSkills() == null)
            return;
        for (int i = 0; i < mode.getStartingSkills().getSkillList().size(); i++) {
            player.getSkills().setStaticLevel(mode.getStartingSkills().getSkillList().get(i).getSkill(), mode.getStartingSkills().getLevel()[i]);
            System.out.println("setLevel("+mode.getStartingSkills().getSkillList().get(i).getSkill()+","+ mode.getStartingSkills().getLevel()[i]+")");
        }
        if (player.isDebug())
            System.out.println("receiveSkills(mode="+mode+", player="+player+");");
    }

    /**
     * Confirms the player's selection and start playing
     * @param player The player
     */
    private static void confirmAndPlay(Player player, Modes mode) {
        receiveItems(mode, player);
        receiveEquipments(mode, player);
        receiveSkills(mode, player);
        player.getSkills().experienceMutiplier = mode.getXpRate();
        player.getGamemodeManager().setMode(mode);
        player.getIronmanManager().setMode(IronmanMode.NONE);

        // Handles the modes
        switch (mode) {
            case Standard_Ironman:
                player.getIronmanManager().setMode(IronmanMode.STANDARD);
                PlayerParser.save(player);
                break;
            case Hardcore_Ironman:
                player.getIronmanManager().setMode(IronmanMode.HARDCORE);
                PlayerParser.save(player);
                break;
            case Ultimate_Ironman:
                player.getIronmanManager().setMode(IronmanMode.ULTIMATE);
                PlayerParser.save(player);
                break;
            default:
                player.getIronmanManager().setMode(IronmanMode.NONE);
                PlayerParser.save(player);
                break;
        }

        player.sendMessage("You have selected the mode: <col=ff0000>"+mode.name().replace("_"," ")+"</col>, goodluck!");
        player.getInterfaceManager().close(new Component(835));
        player.getSavedData().getActivityData().setChosenGamemode(true);
        player.unlock();
        player.teleport(ServerConstants.HOME_LOCATION);
        PlayerParser.save(player);
    }
}
