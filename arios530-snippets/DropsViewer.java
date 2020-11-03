package plugin.interaction.inter.custom;

import core.Util;
import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.npc.drop.NPCDropTables;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.WeightedChanceItem;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Holds the drops viewer interface
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 15/10/2020 - 11:27
 * @project insidious530
 */
@InitializablePlugin
public class DropsViewer extends ComponentPlugin {
    /**
     * The inter id
     */
    private static final int INTER = 840;

    /**
     * Constants
     */
    private static final int SEARCH_BUTTON = 18;
    private static final int NAME = 0, MINIMUM_AMOUNT= 1, WEIGHT=2;

    /**
     * The list components
     */
    private static final int[] listComponents = {28, 35, 42, 49, 56, 63, 70, 77, 84, 91, 98, 105, 112, 119,
    129, 136, 143, 150, 157, 164, 171, 178, 185, 192, 199, 206, 213, 220, 227, 234, 241, 248, 255, 262, 269,
    276, 283, 290, 297, 304, 311, 318, 325, 332, 339, 346, 353, 360, 367, 374, 381, 388, 395, 402, 409, 416,
    423, 430, 437, 444, 451};

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        switch(button) {
            case 25:
                boolean sortByName = (boolean) player.getAttributes().getOrDefault("dropsviewer:sortbyName", true);
                player.sendMessage("name ascended: "+sortByName);
                player.getAttributes().put("dropsviewer:sortby", NAME);
                player.getAttributes().put("dropsviewer:sortbyName", !(sortByName));
                view(player);
                break;
            case 26:
                boolean sortByMin = (boolean) player.getAttributes().getOrDefault("dropsviewer:sortbyMin", true);
                player.sendMessage("min ascended: "+sortByMin);
                player.getAttributes().put("dropsviewer:sortby", MINIMUM_AMOUNT);
                player.getAttributes().put("dropsviewer:sortbyMin", !(sortByMin));
                view(player);
                break;
            case 27:
                boolean sortByWeight = (boolean) player.getAttributes().getOrDefault("dropsviewer:sortbyWeight", true);
                player.sendMessage("weight ascended: "+sortByWeight);
                player.getAttributes().put("dropsviewer:sortby", WEIGHT);
                player.getAttributes().put("dropsviewer:sortbyWeight", !(sortByWeight));
                view(player);
                break;
            case SEARCH_BUTTON:
                player.setAttribute("runscript", new RunScript() {
                    @Override
                    public boolean handle() {
                        String npcName = (String) value;
                        findDropsByNpcName(player, npcName);
                        return true;
                    }
                });
                player.getDialogueInterpreter().sendLongInput("Enter the npc name you wish to look the drops for?");
                break;
        }
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(INTER, this);
        return this;
    }

    /**
     * Opens the npc drops
     * @param player The player
     * @param id The npc od
     */
    public static void open(Player player, int id) {
        player.getAttributes().put("dropsviewer:npcId", id);
        player.getAttributes().put("dropsviewer:searchButton", false);
        view(player);
    }

    /**
     * Opens the npc drops
     * @param player The player
     * @param id The npc id
     * @param searchButton true to show search button | false to not
     */
    public static void open(Player player, int id, boolean searchButton) {
        player.getAttributes().put("dropsviewer:npcId", id);
        player.getAttributes().put("dropsviewer:searchButton", searchButton);
        view(player);
    }

    /**
     * Opens the searching interface
     * @param player The player
     */
    public static void openSearch(Player player) {
        player.getInterfaceManager().open(new Component(INTER));
        player.getPacketDispatch().sendInterfaceConfig(INTER, 126, false);
        player.getAttributes().put("dropsviewer:searchButton", false);
    }

    /**
     * View the drops of the desired npc
     * @param player The player
     */
    private static void view(Player player) {
        player.getPacketDispatch().sendInterfaceConfig(INTER, 126, true);

        int npcId = (int) player.getAttributes().get("dropsviewer:npcId");
        List<WeightedChanceItem> list = retrieveDropsList(npcId);

        int sortBy = (int) player.getAttributes().getOrDefault("dropsviewer:sortby", NAME);

        boolean showSearchButton = (boolean) player.getAttributes().getOrDefault("dropsviewer:searchButton", false);

        player.getPacketDispatch().sendInterfaceConfig(INTER, SEARCH_BUTTON, !showSearchButton);

        switch (sortBy) {
            case NAME:
                boolean sortByNameOrder = (boolean) player.getAttributes().getOrDefault("dropsviewer:sortbyName", true);
                list.sort(sortByNameOrder ? (s1, s2) -> s2.getItem().getName().compareToIgnoreCase(s1.getItem().getName()) : (s1, s2) -> s1.getItem().getName().compareToIgnoreCase(s2.getItem().getName()));
                break;
            case MINIMUM_AMOUNT:
                boolean sortByMinOrder = (boolean) player.getAttributes().getOrDefault("dropsviewer:sortbyMin", true);
                list.sort(sortByMinOrder ? (s1, s2) -> Double.compare(s2.getMinimum_amount(), s1.getMinimum_amount()) : Comparator.comparingDouble(WeightedChanceItem::getMinimum_amount));
                break;
            case WEIGHT:
                boolean sortByWeightOrder = (boolean) player.getAttributes().getOrDefault("dropsviewer:sortbyWeight", true);
                list.sort(sortByWeightOrder ? (s1, s2) -> Double.compare(s2.getWeight(), s1.getWeight()) : Comparator.comparingDouble(WeightedChanceItem::getWeight));
                break;
        }

        if (list.isEmpty()) {
            player.sendMessage("There are no drops to be shown!");
            return;
        }

        player.sendMessage("Drops list for <col=ff0000>"+NPCDefinition.forId(npcId).getName()+"</col>!");

        refreshList(player, list);

        /*list.forEach(drop-> {
            player.sendMessage("item=" + drop.getItem().getName() + ", amount=" + drop.getItem().getAmount() + ", weight=" + drop.weight);
        });*/
    }

    /**
     * Refreshes the list
     * @param player The player
     * @param list The list
     */
    private static void refreshList(Player player, List<WeightedChanceItem> list) {
        player.getInterfaceManager().open(new Component(INTER));

        // Hide others
        for (Integer listComponent : listComponents) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, listComponent, true);
        }

        // sends the list
        for (int i = 0; i < list.size(); i++) {
            if (i >= 61)
                continue;
            if (list.get(i) == null || list.get(i).getItem() == null)
                continue;;
            player.getPacketDispatch().sendInterfaceConfig(INTER, listComponents[i], false);
            player.getPacketDispatch().sendItemOnInterface(list.get(i).getItem().getId(), 1, INTER, listComponents[i]+3);
            player.getPacketDispatch().sendString(list.get(i).getItem().getName(), INTER, listComponents[i]+4);
            player.getPacketDispatch().sendString("Min: <col=d13100>"+ Util.intToKOrMil(list.get(i).getMinimum_amount())+"</col><br>Max: <col=438f00>"+Util.intToKOrMil(list.get(i).getMaximum_amount())+"</col>", INTER, listComponents[i]+5);
            player.getPacketDispatch().sendString(list.get(i).getWeight()+"", INTER, listComponents[i]+6);
        }
    }

    /**
     * Retrieves the drops list
     * @param id The drops list
     */
    private static List<WeightedChanceItem> retrieveDropsList(int id) {
        NPCDropTables def = NPCDefinition.forId(id).getDropTables();

        List<WeightedChanceItem> fullList = new ArrayList<>();

        fullList.addAll(def.getMainTable());
        fullList.addAll(def.getCharmTable());
        fullList.addAll(def.getDefaultTable());

        for (WeightedChanceItem weightedChanceItem : fullList) {
            if (weightedChanceItem == null) {
                System.out.println("null item");
                continue;
            }
            //if (player.isDebug())
            //player.sendMessage("item=" + weightedChanceItem.getItem().getName() + ", amount=" + weightedChanceItem.getItem().getAmount() + ", weight=" + weightedChanceItem.weight);
        }
        return fullList;
    }

    /**
     * Find drops by npc name
     * @param player The player
     * @param npcName The item name
     */
    private static void findDropsByNpcName(Player player, String npcName) {
            for (int i = 0; i < NPCDefinition.getDefinitions().size(); i++) {
                if (NPCDefinition.forId(i).getName().toLowerCase().contains(npcName.toLowerCase())) {
                    open(player, NPCDefinition.forId(i).getId(), true);
                    break;
                }
         }
    }
}
