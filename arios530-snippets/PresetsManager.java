package plugin.interaction.inter.custom.presets;

import core.Util;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.container.Container;
import core.game.container.access.InterfaceContainer;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles the presets manager
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 27/10/2020 - 17:38
 * @project insidious530
 */
public class PresetsManager {
    /**
     * The player
     */
    private final Player player;

    /**
     * The presets the player have
     */
    private List<Preset> presets;

    /**
     * The first quick slot
     */
    private Preset firstQuickSlot;

    /**
     * The second quick slot
     */
    private Preset secondQuickSlot;

    /**
     * Gets the player's presets
     *
     * @return the presets
     */
    public List<Preset> getPresets() {
        return presets;
    }

    /**
     * Gets the first quick slot
     *
     * @return the firstQuickSlot
     */
    public Preset getFirstQuickSlot() {
        return firstQuickSlot;
    }

    /**
     * sets the first quick slot
     */
    public void setFirstQuickSlot(Preset firstQuickSlot) {
        this.firstQuickSlot = firstQuickSlot;
    }

    /**
     * Gets the second quick slot
     *
     * @return the secondQuickSlot
     */
    public Preset getSecondQuickSlot() {
        return secondQuickSlot;
    }

    /**
     * Sets the second quick slot
     *
     * @param secondQuickSlot The second quick slot
     */
    public void setSecondQuickSlot(Preset secondQuickSlot) {
        this.secondQuickSlot = secondQuickSlot;
    }

    /**
     * Represents the empty list
     */
    private static final Preset EMPTY = new Preset("Empty",
            -1, -1, null, new PresetSet(new Item[]{null,
            null, null, null, null, null,
            null, null, null, null, null}));
    ;

    /**
     * The constructor
     *
     * @param player The player
     */
    public PresetsManager(Player player) {
        this.player = player;
        presets = new ArrayList<>(20);
    }

    public void parse(JSONObject data) {
        JSONArray savedPresets = (JSONArray) data.get("savedPresets");

        if (savedPresets != null) {
            savedPresets.forEach(preset-> {
                JSONObject savedPreset = (JSONObject) preset;

                Container inv = new Container(28);
                JSONArray inventory = (JSONArray) savedPreset.get("inventory");

                if (inventory != null) {
                    inventory.forEach(item -> {
                        JSONObject e = (JSONObject) item;
                        inv.add(new Item(Integer.parseInt(e.get("itemId").toString()), Integer.parseInt(e.get("amount").toString())));
                    });
                }

                Item[] equips = new Item[11];
                JSONArray equipments = (JSONArray) data.get("equipments");

                if (equipments != null) {
                    equipments.forEach(item -> {
                        JSONObject e = (JSONObject) item;
                        equips[equipments.indexOf(e)] = new Item(Integer.parseInt(e.get("itemId").toString()), Integer.parseInt(e.get("amount").toString()));
                    });
                }

                Preset presetToBeAdded = new Preset(
                        savedPreset.get("name").toString(),
                        Integer.parseInt(savedPreset.get("spellBook").toString()),
                        Integer.parseInt(savedPreset.get("prayerBook").toString()),
                        inv, new PresetSet(new Item[]{equips[0], equips[1],
                        equips[2], equips[3], equips[4], equips[5], equips[6], equips[7],
                        equips[8], equips[9], equips[10]}

                ));
                presets.add(presetToBeAdded);

                if (data.get("firstQuickSlot") != null) {
                    JSONObject firstQuickSlot = (JSONObject) data.get("firstQuickSlot");

                    Container fqsInv = new Container(28);
                    JSONArray fqsInventory = (JSONArray) firstQuickSlot.get("inventory");

                    if (fqsInventory != null) {
                        fqsInventory.forEach(item -> {
                            JSONObject e = (JSONObject) item;
                            fqsInv.add(new Item(Integer.parseInt(e.get("itemId").toString()), Integer.parseInt(e.get("amount").toString())));
                        });
                    }

                    Item[] fqsEquips = new Item[11];
                    JSONArray fqsEquipments = (JSONArray) firstQuickSlot.get("equipments");

                    if (fqsEquipments != null) {
                        fqsEquipments.forEach(item -> {
                            JSONObject e = (JSONObject) item;
                            fqsEquips[fqsEquipments.indexOf(e)] = new Item(Integer.parseInt(e.get("itemId").toString()), Integer.parseInt(e.get("amount").toString()));
                        });
                    }

                    Preset fqsPreset = new Preset(
                            firstQuickSlot.get("name").toString(),
                            Integer.parseInt(firstQuickSlot.get("spellBook").toString()),
                            Integer.parseInt(firstQuickSlot.get("prayerBook").toString()),
                            fqsInv, new PresetSet(new Item[]{fqsEquips[0], fqsEquips[1],
                            fqsEquips[2], fqsEquips[3], fqsEquips[4], fqsEquips[5], fqsEquips[6], fqsEquips[7],
                            fqsEquips[8], fqsEquips[9], fqsEquips[10]}

                    ));
                    setFirstQuickSlot(fqsPreset);
                }

                if (data.get("secondQuickSlot") != null) {
                    JSONObject secondQuickSlot = (JSONObject) data.get("secondQuickSlot");

                    Container sqsInvContainer = new Container(28);
                    JSONArray sqsInventoryArray = (JSONArray) secondQuickSlot.get("inventory");

                    if (sqsInventoryArray != null) {
                        sqsInventoryArray.forEach(item -> {
                            JSONObject e = (JSONObject) item;
                            sqsInvContainer.add(new Item(Integer.parseInt(e.get("itemId").toString()), Integer.parseInt(e.get("amount").toString())));
                        });
                    }

                    Item[] sqsEquipArray = new Item[11];
                    JSONArray sqsEquipmentsArray = (JSONArray) secondQuickSlot.get("equipments");

                    if (sqsEquipmentsArray != null) {
                        sqsEquipmentsArray.forEach(item -> {
                            JSONObject e = (JSONObject) item;
                            sqsEquipArray[sqsEquipmentsArray.indexOf(e)] = new Item(Integer.parseInt(e.get("itemId").toString()), Integer.parseInt(e.get("amount").toString()));
                        });
                    }

                    Preset sqsPreset = new Preset(
                            secondQuickSlot.get("name").toString(),
                            Integer.parseInt(secondQuickSlot.get("spellBook").toString()),
                            Integer.parseInt(secondQuickSlot.get("prayerBook").toString()),
                            sqsInvContainer, new PresetSet(new Item[]{sqsEquipArray[0], sqsEquipArray[1],
                            sqsEquipArray[2], sqsEquipArray[3], sqsEquipArray[4], sqsEquipArray[5], sqsEquipArray[6], sqsEquipArray[7],
                            sqsEquipArray[8], sqsEquipArray[9], sqsEquipArray[10]}

                    ));
                    setSecondQuickSlot(sqsPreset);
                }
            });
        }
    }

    /**
     * Opens the interface
     */
    public void showInterface() {
        PresetsInterface.open(player);
    }

    /**
     * Quick loads the presets definitions
     * @param button The button
     */
    public void quickLoad(int button) {
        switch (button) {
            case 112: // Quick load 1
                if (player.getPresetsManager().getFirstQuickSlot() == null || player.getPresetsManager().getFirstQuickSlot().equals(EMPTY)) {
                    player.sendMessage("You must set a 1st quick-slot within presets manager before quick-loading one!");
                    return;
                }
                PresetsInterface.switchEquipments(player, player.getPresetsManager().getFirstQuickSlot());
                player.getInterfaceManager().close();
                break;
            case 114: // Quick load 2
                if (player.getPresetsManager().getSecondQuickSlot() == null || player.getPresetsManager().getSecondQuickSlot().equals(EMPTY)) {
                    player.sendMessage("You must set a 2nd quick-slot within presets manager before quick-loading one!");
                    return;
                }
                PresetsInterface.switchEquipments(player, player.getPresetsManager().getSecondQuickSlot());
                player.getInterfaceManager().close();
                //player.sendMessage("quickLoad(2)");
                break;
        }
    }

    /**
     * Creates a preset object
     */
    public static class Preset {
        /**
         * The preset nme
         */
        private String name;

        /**
         * The spellbook
         */
        private int spellBook;

        /**
         * The prayer book
         */
        private int prayersBook;

        /**
         * The inventory container
         */
        private Container inventory;

        /**
         * The equipments
         */
        private PresetSet equipment;

        public String getName() {
            return name;
        }

        public int getSpellBook() {
            return spellBook;
        }

        public int getPrayersBook() {
            return prayersBook;
        }

        public Container getInventory() {
            return inventory;
        }

        public PresetSet getEquipment() {
            return equipment;
        }

        /**
         * The constructor
         * @param name The preset name
         * @param spellBook The spellbook
         * @param prayersBook The player's book
         * @param inventory The inventory
         * @param equipment The equipment used
         */
        public Preset(String name, int spellBook, int prayersBook, Container inventory, PresetSet equipment) {
            this.name = name;
            this.spellBook = spellBook;
            this.prayersBook = prayersBook;
            this.inventory = inventory;
            this.equipment = equipment;
        }
    }

    /**
     * Holds the presets interface
     */
    @InitializablePlugin
    public static class PresetsInterface extends ComponentPlugin implements PresetsConstants {

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            List<Preset> presets = player.getPresetsManager().getPresets().stream().filter(Objects::nonNull).collect(Collectors.toList());
            presets.forEach(p-> {
                if (button == PRESETS_LIST[presets.indexOf(p)]) {
                    if (opcode == 155) { // select
                        player.sendMessage("clicked " + p.getName());
                        selectPreset(player, presets.get(presets.indexOf(p)));
                    } else if (opcode == 196) { // Remove
                        // Remove
                        player.getDialogueInterpreter().sendOptions("Are you sure you want to remove preset: <col=ff0000>"+p.getName()+"?", "Yes", "No");
                        player.getDialogueInterpreter().addAction((player1, buttonId) -> {
                            if (buttonId == 2) {
                                removePreset(player, presets.indexOf(p));
                            }
                        });
                    } else if (opcode == 124 || opcode == 199) { // mark slots
                        markPreset(player, opcode, p);
                    }
                }
            });

            switch (button) {
                case ADD_PRESET_BUTTON:
                    createPreset(player);
                    break;
                case EQUIP_PRESET_BUTTON:
                    equipPreset(player);
                    break;
                case UPDATE_PRESET_BUTTON:
                    player.getDialogueInterpreter().sendOptions("Are you sure you want to update your preset?", "Yes", "No");
                    player.getDialogueInterpreter().addAction((player1, buttonId) -> {
                        if (buttonId == 2) {
                            updatePreset(player1);
                        }
                    });
                    break;
                case SWITCH_PRAYERS_COMP:
                case SWITCH_SPELLBOOK_COMP:
                    player.sendMessage("WIP");
                    break;
                /*case SWITCH_PRAYERS_COMP:
                    switchPrayerBook(player);
                    break;
                case SWITCH_SPELLBOOK_COMP:
                    switchSpellBook(player);
                    break;*/
                case 113:// return to bank
                    player.getBank().open();
                    break;
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
         * @param player The player
         */
        public static void open(Player player) {
            player.getInterfaceManager().open(new Component(INTER));
            sendPresetsList(player);
        }

        /**
         * Refreshes the interface
         * @param player The player
         */
        private static void refresh(Player player) {
            sendPresetsList(player);
            sendPresetInventory(player);
            sendPresetEquipments(player);
            sendPresetBooks(player);
        }

        /**
         * Sends the presets list
         * @param player The player
         */
        private static void sendPresetsList(Player player) {
            List<Preset> presets = player.getPresetsManager().getPresets().stream().filter(Objects::nonNull).collect(Collectors.toList());

            // Clears the list
            for (int value : PRESETS_LIST) {
                player.getPacketDispatch().sendInterfaceConfig(INTER, value, true);
            }

            // Sends the preset list
            presets.forEach(p-> {
                player.getPacketDispatch().sendInterfaceConfig(INTER, PRESETS_LIST[presets.indexOf(p)], false);
                player.getPacketDispatch().sendString(Util.formatPlayerNameForDisplay(p.getName()), INTER, PRESETS_LIST[presets.indexOf(p)]+2);
            });
        }

        /**
         * Sends the preset's books
         * @param player The player
         */
        private static void sendPresetBooks(Player player) {
            Preset selectedPreset = (Preset) player.getAttributes().get("presetsManager:selected");
            player.getPacketDispatch().sendInterfaceConfig(INTER, PRAYERSBOOK_STRING_COMP, false);
            player.getPacketDispatch().sendInterfaceConfig(INTER, SPELLBOOK_STRING_COMP, false);
            if (selectedPreset.getPrayersBook() != -1) {
                player.getPacketDispatch().sendString(selectedPreset.getPrayersBook() == 0 ? "Curses" : "Normal", INTER, PRAYERSBOOK_STRING_COMP);
            } else {
                player.getPacketDispatch().sendInterfaceConfig(INTER, PRAYERSBOOK_STRING_COMP, true);
            }
            if (selectedPreset.getSpellBook() != -1) {
                player.getPacketDispatch().sendString(selectedPreset.getSpellBook() == 0 ? "Normal" : selectedPreset.getSpellBook() == 1 ? "Ancient" : "Lunar", INTER, SPELLBOOK_STRING_COMP);
            } else {
                player.getPacketDispatch().sendInterfaceConfig(INTER, SPELLBOOK_STRING_COMP, true);
            }
        }

        /**
         * Sends the preset's inventory
         */
        private static void sendPresetInventory(Player player) {
            Preset theSelectedPreset = (Preset) player.getAttributes().get("presetsManager:selected");
            if (theSelectedPreset.equals(EMPTY)) {
                InterfaceContainer.generateItems(player,new Item[]{null},new String[]{ "Examine" },INTER,INVENTORY_CONTAINER_ID, 7, 4);
                return;
            }
            InterfaceContainer.generateItems(player,theSelectedPreset.getInventory().toArray(),new String[]{ "Examine" },INTER,INVENTORY_CONTAINER_ID, 7, 4);
        }

        /**
         * Sends the preset's equipments
         */
        private static void sendPresetEquipments(Player player) {
            Preset selectedPreset = (Preset) player.getAttributes().get("presetsManager:selected"); // selects the preset

            if (selectedPreset.getEquipment() != null) {
                for (int i = 0; i < selectedPreset.getEquipment().getSlots().size(); i++) {
                    player.getPacketDispatch().sendInterfaceConfig(INTER, selectedPreset.getEquipment().getSlots().get(i).getSlotId() + 2, false);
                    player.getPacketDispatch().sendInterfaceConfig(INTER, selectedPreset.getEquipment().getSlots().get(i).getComponentId() + 2, false);

                    if (selectedPreset.getEquipment().getSlots().isEmpty())
                        continue;
                    if (selectedPreset.getEquipment().getEquipments()[i] == null) {
                        player.getPacketDispatch().sendItemOnInterface(-1, 0, INTER, selectedPreset.getEquipment().getSlots().get(i).getComponentId() + 3);
                        continue;
                    }
                    player.getPacketDispatch().sendInterfaceConfig(INTER, selectedPreset.getEquipment().getSlots().get(i).getComponentId() + 2, true);
                    player.getPacketDispatch().sendItemOnInterface(selectedPreset.getEquipment().getEquipments()[i].getId(), selectedPreset.getEquipment().getEquipments()[i].getAmount(), INTER, selectedPreset.getEquipment().getSlots().get(i).getComponentId() + 3);
                }
            }
        }

        /**
         * Creates a preset
         * @param player The player
         */
        private static void createPreset(Player player) {
            if (player.getPresetsManager().getPresets().size() >= PRESETS_LIST.length) {
                player.sendMessage("You can't have more than "+PRESETS_LIST.length+" presets!");
                return;
            }
            player.setAttribute("runscript", new RunScript() {
                @Override
                public boolean handle() {
                    String presetName = (String) value;
                    if (((String) value).isEmpty()) {
                        player.sendMessage("The name can't be empty, you must specify a name for the preset!");
                        return false;
                    }
                    // The preset we just created
                    Container invContent = new Container(28);
                    //invContent.addAll(player.getInventory());

                    for (int i = 0; i < player.getInventory().itemCount(); i++) {
                        if (player.getInventory().get(i).getDefinition().isUnnoted()) {
                            invContent.add(player.getInventory().get(i));
                            player.sendMessage("<col=006b10>"+player.getInventory().get(i).getName()+" saved to preset inventory!");
                        } else {
                            player.sendMessage("<col=ff0000>Noted Item: "+player.getInventory().get(i).getName()+" could not be saved cause only unnoted items can saved!");
                        }
                    }

                    Preset preset = new Preset(presetName,
                            player.getSpellBookManager().getSpellBook(),
                            1, invContent, generatePresetEquips(player));

                    // Equipment - Adds the player's equipment to the preset
                    player.getPresetsManager().getPresets().add(preset);

                    // Selects the preset to update the screen
                    selectPreset(player, preset);
                    return true;
                }
            });
            player.getDialogueInterpreter().sendInput(true, "Choose the name for the new preset");
        }

        /**
         * Handles the preset selection
         * @param player The player
         */
        private static void selectPreset(Player player, Preset theSelectedPreset) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, 95, false);
            player.getPacketDispatch().sendInterfaceConfig(INTER, 99, false);
            player.getAttributes().put("presetsManager:selected", theSelectedPreset); // selects the preset
            refresh(player);

            List<Preset> presets = player.getPresetsManager().getPresets().stream().filter(Objects::nonNull).collect(Collectors.toList());
            // unmarks the others
            presets.forEach(p-> player.getPacketDispatch().sendString(Util.formatPlayerNameForDisplay(UNSELECTED+ p.getName()), INTER, PRESETS_LIST[presets.indexOf(p)]));
            // Marks the selected preset name
            player.getPacketDispatch().sendString(Util.formatPlayerNameForDisplay(SELECTED+ theSelectedPreset.getName()), INTER, PRESETS_LIST[presets.indexOf(theSelectedPreset)]);
        }

        /**
         * Generates preset equips
         * @param player The player
         */
        public static PresetSet generatePresetEquips(Player player) {
            return new PresetSet(new Item[] {player.getEquipment().getHat(player), player.getEquipment().getCape(player),
                    player.getEquipment().getAmulet(player), player.getEquipment().getWeapon(player),
                    player.getEquipment().getChest(player), player.getEquipment().getShield(player),
                    player.getEquipment().getLegs(player), player.getEquipment().getHands(player),
                    player.getEquipment().getFeet(player), player.getEquipment().getRing(player), player.getEquipment().getArrows(player)});
            /*return new PresetSet(new Item[] {
                    (player.getEquipment().getHat(player) != null ? player.getEquipment().getHat(player).getId() : -1),
                    (player.getEquipment().getCape(player) != null ? player.getEquipment().getCape(player).getId() : -1),
                    (player.getEquipment().getAmulet(player) != null ? player.getEquipment().getAmulet(player).getId() : -1),
                    (player.getEquipment().getWeapon(player) != null ? player.getEquipment().getWeapon(player).getId() : -1),
                    (player.getEquipment().getChest(player) != null ? player.getEquipment().getChest(player).getId() : -1),
                    (player.getEquipment().getShield(player) != null ? player.getEquipment().getShield(player).getId() : -1),
                    (player.getEquipment().getLegs(player) != null ? player.getEquipment().getLegs(player).getId() : -1),
                    (player.getEquipment().getHands(player) != null ? player.getEquipment().getHands(player).getId() : -1),
                    (player.getEquipment().getFeet(player) != null ? player.getEquipment().getFeet(player).getId() : -1),
                    (player.getEquipment().getRing(player) != null ? player.getEquipment().getRing(player).getId() : -1),
                    (player.getEquipment().getArrows(player) != null ? player.getEquipment().getArrows(player).getId() : -1)});*/
        }

        /**
         * removes a preset
         * @param player The player
         */
        private static void removePreset(Player player, int index) {
            player.sendMessage("The preset <col=ff0000>"+player.getPresetsManager().getPresets().get(index).getName()+"</col> was removed successfully!");

            player.getAttributes().put("presetsManager:selected", EMPTY);

            player.getPacketDispatch().sendInterfaceConfig(INTER, 95, true);
            player.getPacketDispatch().sendInterfaceConfig(INTER, 99, true);

            player.getPacketDispatch().sendInterfaceConfig(INTER, PRAYERSBOOK_STRING_COMP, true);
            player.getPacketDispatch().sendInterfaceConfig(INTER, SPELLBOOK_STRING_COMP, true);

            Preset firstQuickSlot = player.getPresetsManager().getFirstQuickSlot();
            List<Preset> first = player.getPresetsManager().getPresets().stream().filter(p->p != null && p == firstQuickSlot).collect(Collectors.toList());

            Preset secondQuickSlot = player.getPresetsManager().getSecondQuickSlot();
            List<Preset> second = player.getPresetsManager().getPresets().stream().filter(p->p != null && p == secondQuickSlot).collect(Collectors.toList());

            if (first.contains(firstQuickSlot)) {
                player.getPresetsManager().setFirstQuickSlot(null);
                System.out.println("first setted to null");
            }
            if (second.contains(secondQuickSlot)) {
                player.getPresetsManager().setSecondQuickSlot(null);
                System.out.println("second setted to null");
            }
            player.getPresetsManager().getPresets().remove(index);
            refresh(player);
        }

        /**
         * Marks the quick preset slot
         * @param player The player
         * @param packetId The button we clicked
         */
        private static void markPreset(Player player, int packetId, Preset p) {
            switch(packetId) {
                case 124: // Mark as 1st slot
                    player.getPresetsManager().setFirstQuickSlot(p);
                    player.sendMessage("markPreset(1, "+p.getName()+")");
                    break;
                case 199: // Mark as 2nd slot
                    player.getPresetsManager().setSecondQuickSlot(p);
                    player.sendMessage("markPreset(2, "+p.getName()+")");
                    break;
            }
        }

        /**
         * Equips a preset
         * @param player The player
         */
        private static void equipPreset(Player player) {
            Preset selectedPreset = (Preset) player.getAttributes().getOrDefault("presetsManager:selected", EMPTY);
            player.sendMessage("equipPreset()");
            //TODO
            switchEquipments(player, selectedPreset);
            player.sendMessage("The preset <col=ff0000>"+selectedPreset.getName()+"</col> was loaded!");
        }

        /**
         * Switches the prayer book
         */
        private static void switchPrayerBook(Player player) {
            //TODO
            player.sendMessage("switchPrayerBook()");
        }

        /**
         * Switches the spell book
         * @param player The player
         */
        private static void switchSpellBook(Player player) {
            //TODO
            player.sendMessage("switchSpellBook()");
        }

        /**
         * Updates the selected preset with current's players set
         * @param player The player
         */
        private static void updatePreset(Player player) {
            player.sendMessage("updatePreset()");

            Preset selectedPreset = (Preset) player.getAttributes().get("presetsManager:selected");
            List<Preset> presets = player.getPresetsManager().getPresets().stream().filter(Objects::nonNull).collect(Collectors.toList());

            // Updates the preset info
            if (presets.contains(selectedPreset)) {
                // Creates a new preset
                /*Container invContent = new Container(28);
                invContent.addAll(player.getInventory());*/

                // The preset we just created
                Container invContent = new Container(28);
                //invContent.addAll(player.getInventory());

                for (int i = 0; i < player.getInventory().itemCount(); i++) {
                    if (player.getInventory().get(i).getDefinition().isUnnoted()) {
                        invContent.add(player.getInventory().get(i));
                        player.sendMessage("<col=006b10>"+player.getInventory().get(i).getName()+" saved to preset inventory!");
                    } else {
                        player.sendMessage("<col=ff0000>Noted Item: "+player.getInventory().get(i).getName()+" could not be saved cause only unnoted items can saved!");
                    }
                }

                Preset preset = new Preset(selectedPreset.getName(),
                        player.getSpellBookManager().getSpellBook(),
                        1, invContent, generatePresetEquips(player));

                // Updates the quick slots with the new preset
                if (selectedPreset == player.getPresetsManager().getFirstQuickSlot()) {
                    player.getPresetsManager().setFirstQuickSlot(preset);
                } else if (selectedPreset == player.getPresetsManager().getSecondQuickSlot()) {
                    player.getPresetsManager().setSecondQuickSlot(preset);
                }

                player.getPresetsManager().getPresets().set(presets.indexOf(selectedPreset), preset);
                player.getAttributes().put("presetsManager:selected", preset);
                player.sendMessage("Your preset was updated successfully!");
                refresh(player);
            }
        }

        /**
         * Used when player receives his preset equipment
         * @param player The player
         */
        private static void switchEquipments(Player player, Preset selectedPreset) {
            StringBuilder missingItems = new StringBuilder();
            Item[] presetEquipments = selectedPreset.getEquipment().getEquipments();

            for (int i = 0; i < selectedPreset.getEquipment().getSlots().size(); i++) {

                // The current equipped item by player in the slot
                int equipSlotId = selectedPreset.getEquipment().getSlots().get(i).getSlotId();

                /**- se eu estiver equipado com algum item, trocar pelo item do preset*/
                if (player.getEquipment().get(equipSlotId) != null) {
                    // Se o preset equipment não for nulo ou tiver item presetado
                    if (presetEquipments[i] != null) {
                        /**
                         * - se o cara tiver o item de preset no banco, prosseguir:
                         > p.getEquipments() - setar o item do preset no cara
                         > add o item q o cara ta equipado no banco
                         */
                        if (player.getBank().contains(presetEquipments[i].getId(), presetEquipments[i].getAmount())) {
                            Item equippedItem = player.getEquipment().get(equipSlotId);
                            player.getBank().remove(presetEquipments[i], true);
                            player.getBank().add(equippedItem, true);
                            player.getEquipment().replace(presetEquipments[i], equipSlotId, true);
                            //System.out.println("Preset item: "+presetEquipments[i].getName()+" was in bank, swapping it for "+equippedItem.getName()+"!");
                        } else {
                            // Senão falar que ele não tinha o item e não trocar o item equipado.
                            //System.out.println("Preset item:" + presetEquipments[i].getName() + " was missing in your bank, breaking loop!");
                            missingItems.append("<col=ff0000>").append(presetEquipments[i].getName()).append("</col>/n");
                        }
                    } else {
                            // Senão, rancar o equipamento do slot do cara e guardar no banco
                            Item equippedItem = player.getEquipment().get(equipSlotId);
                            player.getBank().add(equippedItem, true);
                            player.getEquipment().remove(equippedItem, equipSlotId, true);
                            //System.out.println("Swapped " + equippedItem.getName() + " for an empty preset slot.");
                    }
                } else {
                    if (presetEquipments[i] == null) {
                        //System.out.println("Preset slot was empty!");
                    } else {
                        if (player.getBank().contains(presetEquipments[i].getId(), presetEquipments[i].getAmount())) {
                            player.getBank().remove(presetEquipments[i], true);
                            player.getEquipment().replace(presetEquipments[i], equipSlotId, true);
                            //System.out.println("(Preset item was on bank) Player wasn't equipping any item, then only set the preset item (" + presetEquipments[i].getName() + ") to player's empty slot");
                        } else {
                            //System.out.println("Tried to equip a item on an empty player slot, failed due to the item not being present in bank!");
                        }
                    }
                }
                // Last index of the equipments
                    if (i == selectedPreset.getEquipment().getSlots().size()-1) {
                        if (missingItems.length() >= 1) {
                            //player.sendMessage("The current inventory items could not be added: " + missingItems.toString() + "!");
                    }
                 }
            }

            switchInventory(player, selectedPreset);
        }

        /**
         * Used when player receives his preset inventory
         * @param player The player
         * @param selectedPreset The selected preset
         */
        private static void switchInventory(Player player, Preset selectedPreset) {
            StringBuilder missingItems = new StringBuilder();
            Item[] presetInventory = selectedPreset.getInventory().toArray();

            for (int i = 0; i < player.getBank().itemCount(); i++) {
                /**- se eu estiver equipado com algum item, trocar pelo item do preset*/
                if (player.getInventory().get(i) != null) {
                    // Se o preset equipment não for nulo ou tiver item presetado
                    if (presetInventory[i] != null) {
                        /**
                         * - se o cara tiver o item de preset no banco, prosseguir:
                         > p.getEquipments() - setar o item do preset no cara
                         > add o item q o cara ta equipado no banco
                         */
                        if (player.getBank().contains(presetInventory[i].getId(), presetInventory[i].getAmount())) {
                            Item inventoryItem = player.getInventory().get(i);
                            player.getBank().remove(presetInventory[i], true);
                            player.getBank().add(inventoryItem, true);
                            player.getInventory().replace(presetInventory[i], i, true);
                            //System.out.println("Preset item: "+presetInventory[i].getName()+" was in bank, swapping it for "+inventoryItem.getName()+"!");
                        } else {
                            // Senão falar que ele não tinha o item e não trocar o item equipado.
                            //System.out.println("Preset item:" + presetInventory[i].getName() + " was missing in your bank, breaking loop!");
                            missingItems.append("<col=ff0000>").append(presetInventory[i].getName()).append("</col>/n");
                        }
                    } else {
                        // Senão, rancar o equipamento do slot do cara e guardar no banco
                        Item inventoryItem = player.getInventory().get(i);
                        //System.out.println("Swapped " + inventoryItem.getName() + " for an empty preset slot.");
                        player.getBank().add(inventoryItem, true);
                        player.getInventory().remove(inventoryItem, i, true);
                    }
                } else {
                    if (presetInventory[i] == null) {
                        //System.out.println("Preset slot was empty!");
                    } else {
                        if (player.getBank().contains(presetInventory[i].getId(), presetInventory[i].getAmount())) {
                            player.getBank().remove(presetInventory[i], true);
                            player.getInventory().replace(presetInventory[i], i);
                            //System.out.println("(Preset item was on bank) Player wasn't equipping any item, then only set the preset item (" + presetInventory[i].getName() + ") to player's empty slot");
                        } else {
                            //System.out.println("Tried to equip a item on an empty player slot, failed due to the item not being present in bank!");
                        }
                    }
                }
                // Last index of the equipments
                if (i == selectedPreset.getInventory().itemCount()) {
                    if (missingItems.length() >= 1) {
                        player.sendMessage("The bank was missing items were not sent to inventory: " + missingItems.toString() + "!");
                    }
                }
            }
        }
    }
}
