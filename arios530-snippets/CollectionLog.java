package plugin.interaction.inter.custom.clog;

import core.Util;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.Container;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The collection log system
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 16/10/2020 - 19:31
 * @project insidious530
 */

public class CollectionLog {
    public enum Store {
        // Bosses
        GENERAL_GRAARDOR(0, new Integer[] {2}, CollectionCategory.BOSSES, new Item[] {new Item (526), new Item(4151), new Item(4358)}, null, "Kills"),
        K$RIL_TSUTSAROTH(1, new Integer[] {5454}, CollectionCategory.BOSSES, new Item[] {new Item (768)}, null, "Kills"),

        // Minigames
        PEST_CONTROL(0, new Integer[] {4}, CollectionCategory.MINIGAMES, new Item[] {new Item (526)}, null, "Completed"),

        // Clues
        EASY_TREASURE_TRAILS(0, new Integer[] {435}, CollectionCategory.CLUES, new Item[] {new Item (543)}, null, "Trails Completed"),
        ;

        /**
         * The entry index
         */
        private final Integer id;

        /**
         * The collection
         */
        private final Collection collection;

        /**
         * Gets the entry id
         * @return The entry id
         */
        public Integer getId() {
            return id;
        }

        /**
         * Gets the collection
         * @return The collection
         */
        public Collection getCollection() {
            return collection;
        }

        /**
         * Creates a collection entry
         * @param npcsIds The npcs ids that will trigger the collection
         * @param category The category
         * @param main The main collection items
         * @param shared The shared collection items
         * @param alias The collection alias: Kills, Completions, Etc...
         */
        Store(Integer id, Integer[] npcsIds, CollectionCategory category, Item[] main, Item[] shared, String alias) {
            this.id = id;
            this.collection = new Collection(npcsIds, category, new CollectionItems(main), new CollectionItems(shared), alias);
        }
    }

    /**
     * The player
     */
    private transient Player player;

    /**
     * The unique items that will be obtained by the player;
     */
    private final Map<Integer, Integer> collectionItems;

    /**
     * The monsters killed by the player
     */
    private final Map<Integer, Integer> killCounts;

    /**
     * Gets the killcount
     * @param npcId The npc id of the killcount we wanting
     * @return The killcount id
     */
    public int getKillcount(int npcId) {
        return killCounts.getOrDefault(npcId, 0);
    }

    /**
     * The constructor
     */
    public CollectionLog(Player player) {
        this.player = player;
        collectionItems = new HashMap<>();
        killCounts = new HashMap<>(255);
    }

    /**
     * Returns if the npc we're killing is a killcount npc
     * @param npc The npc
     * @return true if yes | false if not
     */
    public boolean isKillcountNpc(Entity npc) {
        for (Store store : Store.values()) {
            if (store.getCollection().getNpcsIds() == null)
                continue;
            for (int i = 0; i < store.getCollection().getNpcsIds().length;i++) {
                if (store.getCollection().getNpcsIds()[i] == npc.getId())
                    return true;
            }
        }
        return false;
    }

    /**
     * Increases killcount
     * @param id The npc id
     */
    public void increaseKillcount(int id) {
        if (!killCounts.containsKey(id)) {
            killCounts.put(id, 1);
        } else {
            killCounts.put(id, killCounts.getOrDefault(id, 0)+1);
        }
        System.out.println("killcount increased for "+new NPC(id).getName()+", current: "+getKillcount(id));
    }

    /**
     * Gets the collection items
     * @return The collection items
     */
    public Map<Integer, Integer> getCollectionItems() {
        return collectionItems;
    }

    /**
     * Gets the killcounts
     * @return The killcount
     */
    public Map<Integer, Integer> getKillCounts() {
        return killCounts;
    }

    public void parse(JSONObject data){
        JSONArray clogArray = (JSONArray) data.get("collectionEntries");
        clogArray.forEach(entry -> {
            JSONObject e = (JSONObject) entry;
            collectionItems.put(Integer.parseInt( e.get("itemId").toString()),Integer.parseInt(e.get("amountObtained").toString()));
        });
        JSONArray kcsArray = (JSONArray) data.get("killcounts");
        kcsArray.forEach(kc -> {
            JSONObject k = (JSONObject) kc;
            killCounts.put(Integer.parseInt( k.get("npcId").toString()),Integer.parseInt(k.get("amountKilled").toString()));
        });
    }

    /**
     * Opens the interface for the player
     */
    public void showInterface() {
        CollectionLogInterface.open(player);
    }

    /**
     * Sends the collection drop
     * @param item The item
     */
    public void sendCollectionDrop(Entity npc, Item item) {
        for (Store data : Store.values()) {
            // The main collection
            for (int i = 0; i < data.getCollection().getNpcsIds().length; i++) {
                if (npc.getId() != data.getCollection().getNpcsIds()[i])
                    continue;

                if (data.getCollection().getMain().getItems() != null) {
                    for (int x = 0; x < data.getCollection().getMain().getItems().length; x++) {
                        if (data.getCollection().getMain().getItems()[x].getId() != item.getId())
                            continue;

                            if (!hasCollectionDrop(item)) {
                                player.sendMessage("The unique item: <col=f00000>" + new Item(item.getId()).getName() + "</col> was added to your Collection log!");
                            }
                            increaseCollectionDrop(item);
                            break;
                    }
                }
                // The shared collection
                if (data.getCollection().getShared().getItems() != null) {
                    for (int i2 = 0; i2 < data.getCollection().getShared().getItems().length; i2++) {
                        if (data.getCollection().getShared().getItems()[i2].getId() == item.getId()) {
                            increaseCollectionDrop(item);
                           break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Increases a collection drop to the log
     * @param item The item
     */
    private void increaseCollectionDrop(Item item) {
        if (hasCollectionDrop(item)) {
            int amountAlreadyObtained = getCollectionItems().getOrDefault(item.getId(), 0);
            getCollectionItems().put(item.getId(), amountAlreadyObtained + item.getAmount());
        } else {
            getCollectionItems().put(item.getId(), item.getAmount());
        }
        System.out.println("increaseCollectionDrop(item="+item.getName()+", quant="+item.getAmount()+")");
    }

    /**
     * Returns the amount of drops of that item
     * @param item The item
     * @return The amount
     */
    private int getCollectionDropAmount(Item item) {
        return getCollectionItems().getOrDefault(item.getId(), 0);
    }

    /**
     * Method used to get amount of items we obtained from each entry
     * @return Returns the amount of items we already obtained for this entry
     */
    private static int getAmountObtained(Player player, Store selectedCollection) {
        int quant = 0;

        for (Store store : Store.values()) {
            if (store != selectedCollection)
                continue;

            Item[] store_items = store.getCollection().getMain().getItems();
            Item[] selected_items = selectedCollection.getCollection().getMain().getItems();

            for (int i = 0; i < selected_items.length; i++) {
                if (store_items[i] != selected_items[i])
                    continue;
                if (player.getCollectionLog().hasCollectionDrop(store_items[i])) {
                    if (player.isDebug()) {
                        player.sendMessage("Entry on hashmap (id=" + store_items[i] + ")");
                    }
                    quant++;
                }
            }
        }
        if (player.isDebug()) {
            player.sendMessage("Count: " + quant);
        }
        return quant;
    }

    /**
     * Checks if player has the collection item
     * @param item The item
     * @return true if has | false if not
     */
    private boolean hasCollectionDrop(Item item) {
     return getCollectionItems().containsKey(item.getId());
    }

    /**
     * Resets the whole collection log
     */
    private void resetCollectionLog() {
        getCollectionItems().clear();
        player.sendMessage("Collection log was reset!");
    }
    /**
     * The collection object
     */
    public static class Collection {
        /**
         * The npc ids
         */
        private final Integer[] npcsIds;

        /**
         * The category ids
         */
        private final CollectionCategory category;

        /**
         * The main collection items
         */
        private final CollectionItems main;

        /**
         * the shared collection items
         */
        private final CollectionItems shared;

        /**
         * The alias
         */
        private final String alias;

        /**
         * Gets the npc ids
         * @return The npc ids
         */
        public Integer[] getNpcsIds() {
            return npcsIds;
        }

        /**
         * Gets the category
         * @return The category
         */
        public CollectionCategory getCategory() {
            return category;
        }


        /**
         * Gets the main collection items
         * @return The collection items
         */
        public CollectionItems getMain() {
            return main;
        }

        /**
         * Gets the shared collection items
         * @return The shared collection items
         */
        public CollectionItems getShared() {
            return shared;
        }

        /**
         * Gets the alias
         * @return The alias
         */
        public String getAlias() {
            return alias;
        }

        /**
         * The constructor
         * @param npcsIds List of npcIds that will trigger
         * @param category The category of the collection
         * @param main Main collection items
         * @param shared Shared collection items
         * @param alias whether if was Kills, Completions, etc
         */
        public Collection(Integer[] npcsIds, CollectionCategory category, CollectionItems main, CollectionItems shared, String alias) {
            this.npcsIds = npcsIds;
            this.category = category;
            this.main = main;
            this.shared = shared;
            this.alias = alias;
        }
    }

    /**
     * The collection items
     */
    private static class CollectionItems {
        // The collection items
        private final Item[] items;

        /**
         * Gets the collection items
         * @return The items
         */
        public Item[] getItems() {
            return items;
        }

        // The constructor
        public CollectionItems(Item[] items) {
            this.items = items;
        }
    }

    /**
     * The collection category
     */
    public enum CollectionCategory {
        BOSSES(0), MINIGAMES(1), CLUES(2), OTHERS(3), RAIDS(4);

        /**
         * The category id
         */
        private final int id;

        /**
         * Gets the category id
         * @return The category id
         */
        public int getId() {
            return id;
        }

        /**
         * Creates a category
         * @param id The category id
         */
        CollectionCategory(int id) {
            this.id = id;
        }
    }

    /**
     * The collection log interface
     */
    @InitializablePlugin
    public static class CollectionLogInterface extends ComponentPlugin implements CollectionLogConstants {

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
           /* Block represents the click on category components */
            for (int i = 0; i < CATEGORIES_COMPONENTS.length; i++) {
                if (button == CATEGORIES_COMPONENTS[i]) {
                    CollectionCategory category = (CollectionCategory) player.getAttributes().getOrDefault("clog:selectedCategory", CollectionCategory.BOSSES);

                    for (CollectionCategory categories : CollectionCategory.values()) {
                        if (categories.getId() != category.getId())
                            continue;
                        selectCategory(player, i);
                        for (Store data : Store.values()) {
                            if (data.getCollection().getCategory().getId() != i)
                                continue;
                            player.getAttributes().put("clog:selectedEntry", data);
                            break;
                        }
                        open(player);
                    }
                }
            }

             /*
          Block represents the click on list components
         */
            for (int i = 0; i < LIST_COMPONENTS.length; i++) {
                if (button == LIST_COMPONENTS[i] + 2) {
                    for (Store data : Store.values()) {
                        if (data.getId() != i)
                            continue;
                        CollectionCategory category = (CollectionCategory) player.getAttributes().getOrDefault("clog:selectedCategory", CollectionCategory.BOSSES);
                        if (data.getCollection().getCategory() != category)
                            continue;
                        if (player.getAttributes().get("clog:selectedEntry") == data) { //todo
                            return false;
                        }
                        player.getAttributes().put("clog:selectedEntry", data);
                        System.out.println("Select category: "+category.name());
                        break;
                    }
                    refresh(player);
                }
            }
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ComponentDefinition.put(INTER, this);
            return this;
        }

        /**
         * Opens the interface
         */
        private static void open(Player player) {
            player.getInterfaceManager().open(new Component(INTER));
            refresh(player);
        }

        /**
         * Refreshes the interface
         * @param player The player
         */
        private static void refresh(Player player) {
            sendCategoriesList(player);
            sendCollectionsList(player);
            sendCollectionItems(player);
        }

        /**
         * Draws the categories list
         */
        private static void sendCategoriesList(Player player) {
            // The selected category
            CollectionCategory category = (CollectionCategory) player.getAttributes().getOrDefault("clog:selectedCategory", CollectionCategory.BOSSES);

            int i = 0;
            for (CollectionCategory store : CollectionCategory.values()) {
                if (store == null) {
                   player.getPacketDispatch().sendInterfaceConfig(INTER, CATEGORIES_COMPONENTS[i], true);
                    continue;
                }
                // Re shows the component id
                player.getPacketDispatch().sendInterfaceConfig(INTER, CATEGORIES_COMPONENTS[i], false);

                // Sends the category name to component
                if (store == category) {
                    player.getPacketDispatch().sendString(SELECTED + Util.formatPlayerNameForDisplay(category.toString()), INTER, CATEGORIES_COMPONENTS[i] + 2);
                } else {
                    player.getPacketDispatch().sendString(UNSELECTED + Util.formatPlayerNameForDisplay(store.toString()), INTER, CATEGORIES_COMPONENTS[i] + 2);
                   }
            i++;
            }
        }

        /**
         * Draws the entries list
         */
        private static void sendCollectionsList(Player player)  {
            CollectionCategory category = (CollectionCategory) player.getAttributes().getOrDefault("clog:selectedCategory", CollectionCategory.BOSSES);
            // The selected entry
            Store selected_item = (Store) player.getAttributes().getOrDefault("clog:selectedEntry", Store.GENERAL_GRAARDOR);

            // Loops through the entries
            int i = 0;
            for (Store store : Store.values()) {
                if (store.getCollection().getCategory() != category)
                    continue;
                if (store.getCollection() == null) {
                    player.getPacketDispatch().sendInterfaceConfig(INTER, LIST_COMPONENTS[i], true);
                    continue;
                }
                // Re shows the component id
                player.getPacketDispatch().sendInterfaceConfig(INTER, LIST_COMPONENTS[i], false);


                if (store == selected_item) {
                    player.getPacketDispatch().sendString(SELECTED + Util.formatPlayerNameForDisplay(selected_item.toString().replace("$", "'").replace("_", " ")), INTER, LIST_COMPONENTS[i] + 2);
                } else {
                    player.getPacketDispatch().sendString(UNSELECTED + Util.formatPlayerNameForDisplay(store.toString().replace("$", "'").replace("_", " ")), INTER, LIST_COMPONENTS[i] + 2);
                }
                i++;
                // TODO
            }
        }

        /**
         * Selects the category on screen
         * @param player The player
         */
        private static void selectCategory(Player player, int i) {
            // The selected category
            player.getAttributes().put("clog:selectedCategory", CollectionCategory.values()[i]);
        }

        /**
         * Draws the selected entry on screen
         * @param player The player
         */
        private static void drawSelectedEntry(Player player) {
            Store selectedCollection = (Store) player.getAttributes().getOrDefault("clog:selectedEntry", Store.GENERAL_GRAARDOR);

            player.getPacketDispatch().sendString("<col=ff9c24>" + Util.formatPlayerNameForDisplay(selectedCollection.name().replace("$", "'").replace("_", " ")) + "</col>", INTER, ENTRY_NAME);
            String obtainedTxt = (getAmountObtained(player, selectedCollection) == selectedCollection.getCollection().getMain().getItems().length ? "<col=1ae042>All items were obtained!</col>" : "Obtained: <col=ffd900>" + getAmountObtained(player, selectedCollection) + "/" + selectedCollection.getCollection().getMain().getItems().length + "</col>");
            player.getPacketDispatch().sendString(obtainedTxt, INTER, AMOUNT_OBTAINED);
            if (selectedCollection.getCollection().getAlias() != null) {
                player.getPacketDispatch().sendInterfaceConfig(INTER, ALIAS, false);
                player.getPacketDispatch().sendString(selectedCollection.getCollection().getAlias() + ": <col=ffffff>" + player.getCollectionLog().getKillcount(selectedCollection.getCollection().getNpcsIds()[0]) /*+ bossKills*/, INTER, ALIAS);
            } else {
                player.getPackets().sendHideIComponent(INTER, 42, true);
            }
        }

        /**
         * Sends the collection items
         * @param player The player
         */
        private static void sendCollectionItems(Player player) {
            final Container underlay = new Container(200);
            final Container overlay = new Container(200);

            // The selected entry
            Store selectedCollection = (Store) player.getAttributes().getOrDefault("clog:selectedEntry", Store.GENERAL_GRAARDOR);

            drawSelectedEntry(player);
            selectCategory(player, selectedCollection.getCollection().getCategory().getId());

            for (Store store : Store.values()) {
                if (store != selectedCollection)
                    continue;

                // The main items
                if (store.getCollection().getMain().getItems() != null) {
                    Item[] mainCollectionItems = store.getCollection().getMain().getItems();
                    for (int i = 0; i < mainCollectionItems.length; i++) {
                        underlay.replace(mainCollectionItems[i], i);
                        if (player.getCollectionLog().getCollectionDropAmount(mainCollectionItems[i]) < 1) {
                            overlay.replace(null, i);
                        } else {
                            underlay.replace(null, i);
                            overlay.replace(new Item(mainCollectionItems[i].getId(),
                                    Math.min(player.getCollectionLog().getCollectionDropAmount(mainCollectionItems[i]), Integer.MAX_VALUE)), i);
                        }
                    }
                }

                // The shared items
                if (store.getCollection().getShared().getItems() != null) {
                    int lastSize = store.getCollection().getMain().getItems().length;
                    Item[] sharedCollectionItems = store.getCollection().getShared().getItems();
                    for (int i = 0; i < sharedCollectionItems.length; i++) {
                        underlay.replace(sharedCollectionItems[i], i+lastSize);
                        if (player.getCollectionLog().getCollectionDropAmount(sharedCollectionItems[i]) < 1) {
                            overlay.replace(null, i+lastSize);
                        } else {
                            underlay.replace(null, i+lastSize);
                            overlay.replace(new Item(sharedCollectionItems[i].getId(),
                                    Math.min(player.getCollectionLog().getCollectionDropAmount(sharedCollectionItems[i]), Integer.MAX_VALUE)), i+lastSize);
                        }
                    }
                }

                InterfaceContainer.generateItems(player,underlay.toArray(),new String[]{ "Examine" },INTER, UNDERLAY_CONTAINER_ID, 7, 6);
                InterfaceContainer.generateItems(player,overlay.toArray(),new String[]{ "Check" },INTER, OVERLAY_CONTAINER_ID, 7 ,6);
            }
        }
    }
}

