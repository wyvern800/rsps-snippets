package com.rs.game.player.content.interfaces.titles;

/**
 * @author Sagacity - https://www.rune-server.ee/members/sagacity/
 * Date: 25/08/2019
 */

public class Titles {

    public enum Title {
        ThePoor(0, "The Poor", "cutting 500 uncut opals while training Crafting",
                new String[][]{{"Complete The Opal Pal task"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[0], 924),
        ThePotato(1, "The Potato", "harvesting 350 potatoes while training Farming",
                new String[][]{{"Complete Powertatoes achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[1], 925),
        DaRuneThief(2, "Master Thief", "steal 300 magic stalls while training Thieving",
                new String[][]{{"Complete Da Rune Thief achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[2], 926),
        NoFlaxZone(3, "The Flaxible", "collecting 500 flaxes at any place",
                new String[][]{{"Complete No Flax Zone achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[3], 927),
        Undergrounder(4, "Of Daemonheim", "completing 150 warped dungeons while training Dungeoneering",
                new String[][]{{"Complete Undergrounder achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[4], 1982),
        TheInfernal(5, "The Infernal", "collecting and cleansing 50 pure souls while training Prayer",
                new String[][]{{"Complete The Infernal achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[5], 1984),
        Magicholic(6, "The Green", "checking-health of 50 magic trees while training Farming",
                new String[][]{{"Complete Magicholic achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[6], 1985),
        TreasureHunter(7, "Treasure Hunter", "unlocking 50 crystal chests at home",
                new String[][]{{"Complete The Treasure Hunter achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[7], 1983),
        TheOverloaded(8, "The Overloaded", "decanting 150 overload potions while training Herblore",
                new String[][]{{"Complete Out of Control achievement"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[8], 1986),
        TheAdventurer(9, "The Adventurer", "completing every miniquests while adventuring thyself",
                new String[][]{{"Completing all the existing mini-quests"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[9], 1987),
        Maxed(10, "The Maxed", "maximizing all skills that exists",
                new String[][]{{"Have at least level 99 in all skills"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[10], 2000),
        TheCompletionist(11, "The Completionist", "completing all tasks, quests, maximizing skills and having bunch of " +
                "other requirements completed, check max for a full list",
                new String[][]{{"Have all untrimmed completionist requirements fullfiled"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[11], 2001),
        TheTrimmed(12, "The Trimmed", "completing all tasks, quests, maximizing skills and having bunch of " +
                "other requirements completed, check max for a full list",
                new String[][]{{"Have all trimmed completionist requirements fullfiled"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[12], 2002),
        TzTok(13, "Tz-Tok", "fighting & killing jad at least 2 times",
                new String[][]{{"Complete fight caves 2 times"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[13], 911),
        OfKiln(14, "Of Kiln", "fighting & killing har-aken at least 2 times",
                new String[][]{{"Complete fight kiln 2 times"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[14], 912),
        OfArmadyl(15, "Of Armadyl", "fighting & killing 100 kree'arras",
                new String[][]{{"Defeat at least 100 kree'arras"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[15], 904),
        OfBandos(16, "Of Bandos", "fighting & killing 100 general graardors",
                new String[][]{{"Defeat at least 100 general graaardors"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[16], 906),
        OfSaradomin(17, "Of Saradomin", "fighting & killing 100 commander zylianas",
                new String[][]{{"Defeat at least 100 commander zylianas"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[17], 907),
        OfZamorak(18, "Of Zamorak", "fighting & killing 100 k'ril tsusaroths",
                new String[][]{{"Defeat at least 100 k'ril tsusaroths"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[18], 908),
        OfZaros(19, "Of Zaros", "fighting & killing 100 nexes",
                new String[][]{{"Defeat at least 100 nexes"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[19], 909),
        TheCorporeal(20, "The Corporeal", "fighting & killing 100 corporeal beasts",
                new String[][]{{"Defeat at least 100 corporeal beasts"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[20], 917),
        FinalBoss(21, "Final Boss", "fighting & killing 500 bosses",
                new String[][]{{"Defeat at least 500 bosses of any kind or faction"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[21], 914),
        InsaneFinalBoss(22, "Insane Final Boss", "fighting & killing 1000 bosses",
                new String[][]{{"Defeat at least 1000 bosses of any kin dor faction"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[22], 1981),
        TheSlayer(23, "The Slayer", "completing 100 slayer tasks",
                new String[][]{{"Finish at least 100 slayer tasks assigned from any master"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[23], 910),
        TheCollector(24, "The Collector", "collecting unique items",
                new String[][]{{"Have at least one of each item on the collection log marked"}}, TitlesManager.TITLE_CLICKABLE_COMPONENTS[24], 915),
        ;

        private int titleId;
        private String titleName;
        private String titleDescription;
        private String[][] titleRequirements;
        private int titleCompId;
        private int titleRealId;

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getTitleDescription() {
            return titleDescription;
        }

        public void setTitleDescription(String titleDescription) {
            this.titleDescription = titleDescription;
        }

        public String[][] getTitleRequirements() {
            return titleRequirements;
        }

        public void setTitleRequirements(String[][] titleRequirements) {
            this.titleRequirements = titleRequirements;
        }


        public int getTitleCompId() {
            return titleCompId;
        }

        public void setTitleCompId(int titleCompId) {
            this.titleCompId = titleCompId;
        }

        Title(int titleId, String titleName, String titleDescription, String[][] titleRequirements, int titleCompId, int titleRealId) {
            this.titleId = titleId;
            this.titleName = titleName;
            this.titleDescription = titleDescription;
            this.titleRequirements = titleRequirements;
            this.titleCompId = titleCompId;
            this.titleRealId = titleRealId;
        }

        public int getTitleRealId() {
            return titleRealId;
        }

        public void setTitleRealId(int titleRealId) {
            this.titleRealId = titleRealId;
        }
    }
}