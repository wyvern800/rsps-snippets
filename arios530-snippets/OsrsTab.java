package plugin.interaction.inter.custom.osrstab;

import core.Util;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.world.repository.Repository;
import core.plugin.InitializablePlugin;
import core.plugin.Plugin;
import plugin.interaction.inter.custom.goodwill.goodwill.WellOfGoodwill;

import java.util.ArrayList;
import java.util.List;

import static core.game.node.entity.player.link.quest.QuestRepository.QUESTS;
/**
 * Represents a component plugin to handle the OSRS tab interface.
 * @author Sagacity - https://rune-server.org/members/sagacity
 * @version 1.3
 */

@InitializablePlugin
public final class OsrsTab extends ComponentPlugin {
    /**
     * The useful links component ids definition
     */
    public static final Integer[] usefulLinksCompos = {82, 85, 88, 91, 94, 97, 100};

    /**
     * The quest components ids definition
     */
    public static final Integer[] questsCompos = {46, 49, 52, 55, 58, 61, 64, 67, 70, 73, 76, 79, 103, 106, 109, 112, 115, 118, 121, 124, 127, 130, 133, 136, 139, 142, 145, 148, 151, 154, 157, 160, 163, 166};

    /**
     * The interface Id
     */
    private static final int INTER = 834;

    /**
     * The useful links object
     */
    private static final List<Clickable> usefulLinks = new ArrayList<>();

    /**
     * The Categories storage
     */
    private enum Categories {
        ACCOUNT_INFO(0,new Comp(2,169), new Panel(23,
                new Header("Some Player Info", 26, "Account Info"),
                43, null), "Account Info"),
        SERVER_INFO(1,new Comp(5, 171), new Panel(28,
                new Header("Some Informations", 31, "Server Info"),
                44, null),"Server Info"),
        QUESTS(2,new Comp(8, 173), new Panel(33,
                new Header("Quests List", 36, "Quests"),
                null, null),"Quests"),
        USEFUL_LINKS(3,new Comp(11, 175), new Panel(38,
                new Header("Some useful Links", 41, "Links"),
                null, usefulLinks),"Useful Links");

        /**
        Gets the category id
         */
        public int getId() {
            return id;
        }

        /**
        Gets the category name
         */
        public String getCategoryName() {
            return categoryName;
        }

        /**
        The panel id
         */
        private final int id;

        /**
        Gets the panel
         */
        public Panel getPanel() {
            return panel;
        }

        /**
        The panel
         */
        private final Panel panel;

        /**
        Gets the component
        @return The component
         */
        public Comp getComp() {
            return comp;
        }

        /**
         * The comp
         */
        private final Comp comp;

        /**
         * The category name
         */
        private final String categoryName;

        /**
         * Creates a list of categories
         * @param id The category id
         * @param comp Comp object
         * @param panel Panel object
         * @param categoryName Category name
         */
        Categories(int id, Comp comp, Panel panel, String categoryName) {
            this.id = id;
            this.panel = panel;
            this.comp = comp;
            this.categoryName = categoryName;
        }
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(INTER, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        for (Categories cat : Categories.values()) {

            // Processes the clicking on the clickable options
            if (cat.getPanel().getClickables() != null) {
                for (int i = 0; i < cat.getPanel().getClickables().size(); i++) {
                    if (cat.getPanel().getClickables().get(i) == null)
                        continue;
                    if (cat.getPanel().getClickables().get(i).compId() != button)
                        continue;

                    cat.getPanel().getClickables().get(i).exec(player);
                }
            }

            if (cat.getComp().getNormalCompId() != button)
                continue;

            player.getAttributes().put("OSRS_TAB_CATEGORY", getCategories(button));
            selectCategory(player);
        }


        // Holds the click-to-open on the quests of the interface
        int i = 0;
        for (Quest quest : QUESTS.values()) {
            if (i > questsCompos.length-1)
                continue;
            if (button == questsCompos[i]) {
                openQuestInfo(player, quest);
            }
            i++;
        }
        return true;
    }

    /**
     * Opens the quest info for the selected quest
     * @param player The player
     * @param quest The quest
     */
    private void openQuestInfo(Player player, Quest quest) {
        player.getInterfaceManager().open(new Component(275));
        quest.drawJournal(player, quest.getStage(player));
    }

    /**
     * Gets the category to be shown on interface
     * @param button The button we are clicking
     * @return The category to show on interface
     */
    private Categories getCategories(int button) {
        Categories tempCat = null;
        if (button == Categories.ACCOUNT_INFO.getComp().getNormalCompId()) {
            tempCat = Categories.ACCOUNT_INFO;
        } else if (button == Categories.SERVER_INFO.getComp().getNormalCompId()) {
            tempCat = Categories.SERVER_INFO;
        } else if (button == Categories.QUESTS.getComp().getNormalCompId()) {
            tempCat = Categories.QUESTS;
        } else if (button == Categories.USEFUL_LINKS.getComp().getNormalCompId()){
            tempCat = Categories.USEFUL_LINKS;
        }
        return tempCat;
    }

    /**
     * Opens the interface
     * @param player The player
     */
    public static void openInterface(Player player) {
        selectCategory(player);
    }

    /**
     * Selects the category to be shown on interface
     * @param player The player
     */
    private static void selectCategory(Player player) {
        Categories osrs_tab_category = (Categories) player.getAttributes().getOrDefault("OSRS_TAB_CATEGORY", Categories.ACCOUNT_INFO);

        for (Categories cat : Categories.values()) {
            if (cat != osrs_tab_category)
                continue;

            highlightTab(player, osrs_tab_category);
            openTab(player);
            break;
        }
    }

    /**
     * Opens the tab and set the interface details to it
     * @param player The player
     */
    private static void openTab(Player player) {
        Categories storedCat = (Categories) player.getAttributes().getOrDefault("OSRS_TAB_CATEGORY", Categories.ACCOUNT_INFO);
        player.getPacketDispatch().sendString(storedCat.getCategoryName(), INTER, 25);

        for (Categories cat : Categories.values()) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, cat.getPanel().getPanelId(), true);
        }
        player.getPacketDispatch().sendInterfaceConfig(INTER, storedCat.getPanel().getPanelId(), false);


        if (storedCat.getPanel().getHeader() != null) {
            player.getPacketDispatch().sendString(storedCat.getPanel().getHeader().getMainHeader(), INTER, 14);
            player.getPacketDispatch().sendString(storedCat.getPanel().getHeader().getText(), INTER, storedCat.getPanel().getHeader().getComponentId());
        }

        if (storedCat.getPanel().getPanelDescCompId() != null) {
            player.getPacketDispatch().sendString(getDescriptionByCategory(player, storedCat), INTER, storedCat.getPanel().getPanelDescCompId());
        }

        if (storedCat == Categories.USEFUL_LINKS) {
            sendUsefulLinksList(player);
        }

        if (storedCat == Categories.QUESTS) {
            sendQuestList(player);
        }
    }

    /**
     * Highlights the tab and unhighlight the others
     * @param player The player
     * @param storedCat The category tab we want to highlight
     */
    private static void highlightTab(Player player, Categories storedCat) {
        for (Categories cat : Categories.values()) {
            player.getPacketDispatch().sendInterfaceConfig(INTER, cat.getComp().getHighlightCompId(), true);
            if (cat == storedCat) {
                player.getPacketDispatch().sendInterfaceConfig(INTER, cat.getComp().getHighlightCompId(), false);
            }
        }

    }

    /**
     * Sends the useful links to the interface
     * @param player The player
     */
    private static void sendUsefulLinksList(Player player) {
        Categories storedCat = (Categories) player.getAttributes().getOrDefault("OSRS_TAB_CATEGORY", Categories.USEFUL_LINKS);

        for (Integer usefulLinksCompo : usefulLinksCompos) {
            player.getPackets().sendHideIComponent(INTER, usefulLinksCompo, true);
        }

        if (storedCat.getPanel().getClickables() != null) {
            for (int i = 0; i < storedCat.getPanel().getClickables().size(); i++) {
                if (storedCat.getPanel().getClickables().get(i) == null)
                    continue;

                player.getPackets().sendHideIComponent(INTER,storedCat.getPanel().getClickables().get(i).compId(), false);
                player.getPackets().sendString("> "+storedCat.getPanel().getClickables().get(i).string(player), INTER, storedCat.getPanel().getClickables().get(i).compId()+2);
            }
        }
    }

    /**
     * Sends the quest list to the interface
     * @param player The player
     */
    private static void sendQuestList(Player player) {
        //quests = new ArrayList<>(quests);
        //Categories storedCat = (Categories) player.getAttributes().getOrDefault("OSRS_TAB_CATEGORY", Categories.QUESTS);
        for (Integer questsCompo : questsCompos) {
            player.getPackets().sendHideIComponent(INTER, questsCompo, true);
        }

        int i = 0;
        for (Quest quest : QUESTS.values()) {
            if (i > questsCompos.length-1)
                continue;

            //System.out.println(QUESTS.values().size()); // 34
            player.getPackets().sendHideIComponent(INTER, questsCompos[i], false);

            player.getPackets().sendString(getQuestStageColor(player, quest)+quest.getName()+"</col>", INTER, questsCompos[i] + 2);
            i++;
        }
        /*for (int i = 0; i < questsCompos.length; i++) {
            player.getPackets().sendHideIComponent(INTER,questsCompos[i], true);
        }
        if (storedCat.getPanel().getClickables() != null) {
            for (int i = 0; i < storedCat.getPanel().getClickables().size(); i++) {
                if (storedCat.getPanel().getClickables().get(i) == null)
                    continue;

                player.getPackets().sendHideIComponent(INTER,storedCat.getPanel().getClickables().get(i).compId(), false);
                player.getPackets().sendString(storedCat.getPanel().getClickables().get(i).string(player), INTER, storedCat.getPanel().getClickables().get(i).compId()+2);
            }
        }*/
    }

    /**
     * Gets the quest stage color
     * @param player The Player
     * @param quest The quest
     * @return The color of the stage of the quest we are looking for
     */
    private static String getQuestStageColor(Player player, Quest quest) {
        String stageColor = "";
        if (quest.notStarted(player)) {
            stageColor = "ff2929";
        } else if (quest.hasStarted(player)) {
            stageColor = "ffea29";
        } else  {
            stageColor = "29ff42";
        }
        return "<col="+stageColor+">";
    }

    /**
     * Gets the description to send to screen basing on player's temporary category
     * @param player The player
     * @param cat The category we are getting the description from
     * @return The category
     */
    private static String getDescriptionByCategory(Player player, Categories cat) {
        StringBuilder stb = new StringBuilder();
        String highlightColor = "d1bc00", headerColor = "ff3d3d";

        switch (cat) {
            case ACCOUNT_INFO:
                addHeaderLine("Character", headerColor, stb);
                addDoubleLine(new LineBlock("Username", highlightColor), new LineBlock(player.getUsername()), stb);
                if (player.getDonatorManager().isDonator()) {
                    addDoubleLine(new LineBlock("Donator", highlightColor), new LineBlock("<img="+player.getDonatorManager().getType().getIcon()+">"+Util.formatPlayerNameForDisplay(player.getDonatorManager().getTypeName())), stb);
                }
                if (player.getGamemodeManager().getMode() != null) {
                    addEmptyLine(stb);
                    addHeaderLine("Gamemode Info", headerColor, stb);
                    addDoubleLine(new LineBlock("Gamemode", highlightColor), new LineBlock(player.getGamemodeManager().getModeName()), stb);
                    addEmptyLine(stb);
                    addHeaderLine("XP Modifiers", headerColor, stb);
                    double globalXp = player.getSkills().getExperienceMutiplier();
                    double gamemodeXp = player.getGamemodeManager().getMode().getXpRate();
                    boolean isWellActive = WellOfGoodwill.isWellActive();
                    double expModifier = globalXp+gamemodeXp;
                    addDoubleLine(new LineBlock("Server rate", highlightColor), new LineBlock("" + (int) globalXp+"x"), stb);
                    addDoubleLine(new LineBlock("Mode rate", highlightColor), new LineBlock("" + (int) gamemodeXp+"x"), stb);
                    addEmptyLine(stb);
                    addDoubleLine(new LineBlock("Total", highlightColor), new LineBlock("" + (isWellActive ? (int)expModifier+"x <col=269900>("+(int)expModifier*2+"x w/ DXP)!</col>" : (int)expModifier+"x")), stb);
                    //addDoubleLine(new LineBlock("Drop Rate", highlightColor), new LineBlock("" + player. + "%"), stb);
                }
                addEmptyLine(stb);
                addHeaderLine("Points", headerColor, stb);
                addDoubleLine(new LineBlock("Slayer Points", highlightColor), new LineBlock(""+player.getSlayer().getSlayerPoints()), stb);
                //addEmptyLine(stb);
                //addLine("testing", stb);
                break;
            case SERVER_INFO:
                addHeaderLine("Insidious", headerColor, stb);
                addDoubleLine(new LineBlock("Online", highlightColor), new LineBlock(""+Repository.getPlayers().size()), stb);
                break;
        }
        return stb.toString();
    }

    /**
     * Adds a normal text line with specified color in hex format
     * @param textLine The text we are sending
     * @param color The color in hex format
     * @param s The StringBuilder instance
     */
    private static void addLine(String textLine, String color, StringBuilder s) {
        s.append("<col=").append(color).append(">").append(textLine).append("</col><br>");
    }

    /**
     * Adds a normal white-colored text line
     * @param textLine The text we are sending
     * @param s The StringBuilder instance
     */
    private static void addLine(String textLine, StringBuilder s) {
        s.append("<col=ffffff>").append(textLine).append("</col><br>");
    }

    /**
     * Adds a doubled line containing a left parameter and a right parameter to the String we are building
     * @param lineOne <LineBlock object containing text and color as parameter>
     * @param lineTwo <LineBlock object containing text and color as parameter>
     * @param s The StringBuilder instance
     */
    private static void addDoubleLine(LineBlock lineOne, LineBlock lineTwo, StringBuilder s) {
        s.append("<col=").append(lineOne.getColor()).append(">-").append(lineOne.getText()).append("</col>: <col=").append(lineTwo.getColor()).append(">").append(lineTwo.getText()).append("</col><br>");
    }

    /**
     * Adds an empty line to the String we are building
     * @param s The StringBuilder instance
     */
    private static void addEmptyLine(StringBuilder s) {
        s.append("<br>");
    }

    /**
     * Adds a header-styled line to the StringBuilder
     * @param textLine The text we are sending
     * @param color The color that we are sending
     * @param s The StringBuilder instance
     */
    private static void addHeaderLine(String textLine, String color, StringBuilder s) {
        s.append("<u><col=").append(color).append(">").append(textLine).append("</u></col><br>");
    }

    /**
     * Creates a header line with default color
     * @param textLine The text line
     * @param s The StringBuilder object
     */
    private static void addHeaderLine(String textLine, StringBuilder s) {
        s.append("<u><col=").append("ff3d3d").append(">").append(textLine).append("</u></col><br>");
    }

    private static class LineBlock {
        /**
         * Creates a LineBlock object with text color defined
         * @param text The line text
         * @param color The line color in hex format
         */
        public LineBlock(String text, String color) {
            this.text = text;
            this.color = color;
        }

        /**
         * Creates a LineBlock object with default text color without defining it
         * @param text The line text
         */
        public LineBlock(String text) {
            this.text = text;
            this.color = "ffffff";
        }

        /**
         * The text
         */
        private String text;

        /**
         * Gets the line text
         * @return The text
         */
        public String getText() {
            return text;
        }

        /**
         * Gets the line color
         * @return The color in hex format
         */
        public String getColor() {
            return color;
        }

        /**
         * The color
         */
        private String color;
    }

    /**
     * Loads a clickables list normally used in Server bootstrap module
     */
    public static void loadClickables() {
        try {
            Class[] classes = Util.getClasses("plugin.interaction.inter.custom.osrstab.links");
            for (Class c : classes) {
                if (c.isAnonymousClass()) // next
                    continue;
                Object o = c.newInstance();
                if (!(o instanceof Clickable))
                    continue;
                usefulLinks.add((Clickable) o);
            }
        } catch (Throwable e) {
            System.out.println(e);
        } finally {
            System.out.println("[Useful links] Loaded "+ usefulLinks.size()+ " clickables.");
        }
    }

    public static class Loader {
        /**
         * The Alias
         */
        private String alias;

        private String path;
        /**
         * The list of clickables
         */
        private List<OsrsTab.Clickable> clickables;

        /**
         * Gets the alias
         * @return The alias
         */
        public String getAlias() {
            return alias;
        }

        /**
         * Gets the path
         * @return The path
         */
        public String getPath() {
            return path;
        }

        /**
         * Gets the list of clickables
         * @return List<Clickable>
         */
        public List<Clickable> getClickables() {
            return clickables;
        }

        /**
         * Creates a Loader object
         * @param alias The loader alias
         * @param path The path where the clickables are at
         * @param clickables The List<Clickable> object
         */
        public Loader(String alias, String path, List<Clickable> clickables) {
            this.alias = alias;
            this.path = path;
            this.clickables = clickables;
        }
    }

    private static class Panel {
        /**
         * The panel id
         */
        private final int panelId;

        /**
         * The header
         */
        private final Header header;

        /**
         * Gets the header
         * @return The header
         */
        public Header getHeader() {
            return header;
        }

        /**
         * Gets the panel description comp id
         * @return Panel description comp id
         */
        public Integer getPanelDescCompId() {
            return panelDescCompId;
        }

        /**
         * The description component id
         */
        private Integer panelDescCompId;

        /**
         * Gets the panel id
         * @return The panel Id
         */
        public int getPanelId() {
            return panelId;
        }

        /**
         * Gets the List<Clickable> object
         * @return The clickables list
         */
        public List<Clickable> getClickables() {
            return clickables;
        }

        /**
         * The clickables list
         */
        private final List<Clickable> clickables;

        /**
         *
         * @param panelId The panel ID to be shown
         * @param header The header component
         * @param panelDescCompId The panel description component ID
         * @param clickables The clickables object list
         */
        public Panel(int panelId, Header header, Integer panelDescCompId, List<Clickable> clickables) {
            this.panelId = panelId;
            this.header = header;
            if (panelDescCompId != null) {
                this.panelDescCompId = panelDescCompId;
            }
            this.clickables = clickables;
        }
    }

    private static class Header {
        /**
         * Gets the main header title (Bigger header text)
         * @return The header text
         */
        public String getMainHeader() {
            return mainHeader;
        }

        /**
         * Gets the actual componentId
         * @return The componentId
         */
        public int getComponentId() {
            return componentId;
        }

        /**
         * Gets the text that will be attributed to the header
         * @return The text
         */
        public String getText() {
            return text;
        }

        /**
         * The main header title (Bigger text on interface)
         */
        private final String mainHeader;

        /**
         * Creates a Header object used for each tab category
         * @param mainHeader The main header title
         * @param componentId The component to attributed the title to
         * @param text The string to be attributed to the header component
         */
        public Header(String mainHeader, int componentId, String text) {
            this.mainHeader = mainHeader;
            this.componentId = componentId;
            this.text = text;
        }

        private final int componentId;
        private String text;

    }

    /**
     * Represents a component class
     */
    private static class Comp {
        /**
         * The normal component id
         */
        private int normalCompId;

        /**
         * The highlight component id
         */
        private int highlightCompId;

        /**
        Gets the normal component id
         */
        public int getNormalCompId() {
            return normalCompId;
        }

        /**
        Gets the highlight component id
         */
        public int getHighlightCompId() {
            return highlightCompId;
        }

        /**
         * Creates a component for the categories tab
         * @param normalCompId The normal component id
         * @param highlightCompId The hightlight component id
         */
        public Comp(int normalCompId, int highlightCompId) {
            this.normalCompId = normalCompId;
            this.highlightCompId = highlightCompId;
        }
    }

    /**
     * Interface that represents a Clickable, used for useful links
     */
    public interface Clickable {
        int compId();
        String string(Player player);
        void exec(Player player);
    }
}