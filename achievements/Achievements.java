package com.rs.game.player.content.interfaces.achievements;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.communication.discord.DiscordConstant;
import com.rs.game.communication.discord.DiscordManager;
import com.rs.game.player.Player;
import com.rs.game.player.content.custom.tasks.impl.MediumTasksChecks;

/**
 * @author Sagacity - https://www.rune-server.ee/members/sagacity/
 * Date: 20/04/2019
 */

public class Achievements {
    public static final int EASY = 0, MEDIUM = 1, HARD = 2, ELITE = 3;

    public static final int OFFSET_EASY =0, OFFSET_MED = 20, OFFSET_HARD = 40, OFFSET_ELITE = 60;

    public static final int LARGE_LAMP = 23716;

    public static final int TOTAL_TASK_POINTS = 210;

    public static final int TOTAL_TASKS = 60;

    public enum Store {
        /*easy*/
        POOR_WOODCUTTER(0, EASY), BURIED_ALIVE(1, EASY),IMPETUOUS(2, EASY), ALCHIMICUS(3, EASY), FLETCHUP_ING(4, EASY), THIEF_STEALING_THIEF(5, EASY),
        HERBALISM_FOR_LOVE(6, EASY), OPAL_PAL(7, EASY), PREPARING_FOR_ATTACK(8, EASY), BEEFY_ONE(9, EASY), DIRTY_WORK(10, EASY), PROTECTIVE_BEHAVIOR(11, EASY),
        WARMING_UP(12, EASY), POWERTATOES(13, EASY), WILDERNESS_CHESTER(14, EASY),
        /*medium*/
        DRAGONES_LIBERTATEM(0, MEDIUM), MASTER_ALCHIMICUS(1, MEDIUM), NETTLE_CLEANSER(2, MEDIUM), GOLD_LOVER(3, MEDIUM), LOBBIE_FISHER(4, MEDIUM), DA_RUNE_THIEF(5, MEDIUM),
        THE_CRAFTER(6, MEDIUM), THE_TREASURE_HUNTER(7, MEDIUM), TIRELESS_RUNNER(8, MEDIUM), NO_FLAX_ZONE(9, MEDIUM), THE_WILLPOWER(10, MEDIUM), IRON_PRODIGY(11, MEDIUM),
        DONT_BE_AFRAID_OF_FROSTS(12, MEDIUM), WILDERRUNER(13, MEDIUM), MAPLE_STORY(14, MEDIUM),
        /*hard*/
        FOURGIVENESS(0, HARD), IN_SEARCH_OF_BROTHERS(1, HARD), THE_FURNISHED(2, HARD), VOIDLY_PRESENCE(3, HARD), SQIRKING(4, HARD), THE_INFERNAL(5, HARD),
        MAGICHOLIC(6, HARD), THE_TERMINED(7, HARD), SHARKNATOR(8, HARD), ASH_ME_A_QUESTION(9, HARD), JUNIOR_MINER(10, HARD), HARD_BONE_TO_GNAW(11, HARD),
        HOLY_BREWING(12, HARD), THE_ROYAL_ONE(13, HARD), MAGICUTTER(14, HARD),
        /*elite*/
        MAGIC_BURNER(0, ELITE), HERB_MASTER(1, ELITE), DRAGONCUTTER(2, ELITE), FROM_THE_DEEP(3, ELITE), MASTER_MINER(4, ELITE), UNDERGROUNDER(5, ELITE), REVENANTIOUS(6, ELITE),
        ADVANCED_STRINGER(7, ELITE), OUT_OF_CONTROL(8, ELITE), RUNELMS_AT_ITS_FINEST(9, ELITE), THE_DRAGONBORN(10, ELITE), THE_MAHOGANT(11, ELITE), CHINCHOMPER(12, ELITE),
        THE_LEPORTER(13, ELITE), FRUIT_LOOPS(14, ELITE)
        ;

        private int id;
        private int difficulty;

        Store(int taskId, int difficultyId) {
            this.id = taskId;
            this.difficulty = difficultyId;
        }

        public int getDifficulty() {
            return difficulty;
        }
        public int getId() {
            return id;
        }
    }

    public enum Achievement {
    /* Easy */
    CUT_500_LOGS(0, 500 ,EASY, "Poor woodcutter", new String[][]{{"Cut some trees at any spot to collect some logs"}}, new String[][]{{"2 Task points"},},
            new int[][]{{LARGE_LAMP,2}, {6182,1}, {6181, 1}, {6180, 1}}, 2, 3),
    BURY_300_BONES(1, 300 ,EASY, "Buried alive", new String[][]{{"Bury a bunch of normal bones"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {605,25}}, 2, 4),
    HUNT_150_IMPS(2, 150 ,EASY, "Impetuous", new String[][]{{"Hunt some puro-puro imps while they're wandering"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {10171,1}}, 2, 5),
    LOW_1K_ALCH(3, 1000 ,EASY, "Alchimicus", new String[][]{{"Convert some items into gold using low alchemy spell"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {18341,1}}, 2, 6),
    STRING_500_OAK_LONGS(4, 500 ,EASY, "Fletchup Ing", new String[][]{{"Are you up for some fletching skills, eh? Then string some oak longbows"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {20859,1}}, 2, 7),
    STEAL_300_ROGUES(5, 300 ,EASY, "Thief stealing thief", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {5553,1}, {5554, 1}, {5555, 1}, {5556, 1}, {5557, 1}}, 2, 8),
    CLEAN_500_GUAMS(6, 500 ,EASY, "Herbalism is good", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {19675,1}}, 2, 9),
    CUT_500_OPAL(7, 500 ,EASY, "Opal pal", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {19545,1}}, 2, 10),
    MAKE_500_ATT_POT(8, 500 ,EASY, "Preparing for attack", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {1154,1}}, 2, 11),
    COOK_300_BEEFS(9, 300 ,EASY, "Beefy one", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {10822,1}, {10824, 1}}, 2, 12),
    MINE_250_CLAY(10, 250 ,EASY, "Dirty work", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {20780,1}}, 2, 13),
    SMITH_250_BPBODY(11, 250 ,EASY, "Protective behavior", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {20780,1}}, 2, 14),
    GNOME_50_LAPS(12, 50 ,EASY, "Warming up", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {751,1}}, 2, 15),
    COLLECT_350_POTATOES(13, 350 ,EASY, "Powertatoes", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {11582,1}}, 2, 16),
    THIEV_150_WILDCHEST(14, 150 ,EASY, "Wilderness chester", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
            new int[][]{{LARGE_LAMP,2}, {11514,1}}, 2, 17),

        /*THIEV_1500_WILDCdEST(15, 2000 ,EASY, "Vapor br", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
                new int[][]{{LARGE_LAMP,2}, {11514,1}}, 2, 18),
        THIEV_1500_WfILDCdEST(16, 2000 ,EASY, "Vapor bretre", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
                new int[][]{{LARGE_LAMP,2}, {11514,1}}, 2, 19),
        THIEV_150g_WILDCdEST(17, 2000 ,EASY, "Vapor brghfghf", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
                new int[][]{{LARGE_LAMP,2}, {11514,1}}, 2, 20),
        THIEV_1500_WfDCdEST(18, 2000 ,EASY, "Vapor fhgf", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
                new int[][]{{LARGE_LAMP,2}, {11514,1}}, 2, 21),
        THIEV_150d0f_jWILDCkdEST(19, 2000 ,EASY, "Vapor bhhhr", new String[][]{{"Steal some rogues at wilderness"}}, new String[][]{{"2 Task points"}},
                new int[][]{{LARGE_LAMP,2}, {11514,1}}, 2, 22),*/

    /* Medium */
        BURY_300_DBONES(0, 300 ,MEDIUM, "Dracones libertatem", new String[][]{{"Bury some dragon bones"}}, new String[][]{{"3 Task points"},},
            new int[][]{{LARGE_LAMP,3}, {6182,1}, {6181, 1}, {6180, 1}}, 3, 3),
        HIGH_1500_ALCH(1, 1500 ,MEDIUM, "Master alchimicus", new String[][]{{"Transform some items into coins using high alchemy"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {7386,1}, {7390, 1}, {7394, 1}}, 3, 4),
        CLEAN_250_NETTLES(2, 250 ,MEDIUM, "Nettle cleanser", new String[][]{{"Collect some nettle plants near edgeville yews"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {10171,1}}, 3, 5),
        SUPERHEAT_500_GBAR(3, 500 ,MEDIUM, "Gold lover", new String[][]{{"Transform some gold ores into gold bars using superheating spell"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {18341,1}}, 3, 6),
        FISH_400_LOBSTER(4, 400 ,MEDIUM, "Lobbie fisher", new String[][]{{"Fish some raw lobsters"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {20859,1}}, 3, 7),
        THIEV_300_MAGIC_STALL(5, 300 ,MEDIUM, "Da rune thief", new String[][]{{"Thiev some magic stalls at home"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {5553,1}, {5554, 1}, {5555, 1}, {5556, 1}, {5557, 1}}, 2, 8),
        CRAFT_1_FURY(6, 1 ,MEDIUM, "The crafter", new String[][]{{"Craft a amulet of fury from scratch"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {19675,1}}, 3, 9),
        OPEN_100_CRYSTAL_CHESTS(7, 100 ,MEDIUM, "The treasure hunter", new String[][]{{"Open a considerable amount of crystal chests"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {19545,1}}, 3, 10),
        MAKE_500_TERRORBIRD(8, 500 ,MEDIUM, "Tireless runner", new String[][]{{"Infuse some terrorbird pouches into timeless runner scrolls"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {1154,1}}, 3, 11),
        PICK_500_FLAX(9, 500 ,MEDIUM, "No flax zone", new String[][]{{"Collect a small quantity of flaxes at any spot"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {1154,1}}, 3, 12),
        CHECK_20_WILLOW(10, 25 ,MEDIUM, "The willpower", new String[][]{{"Check the health of a small quantity of willow trees that you grown"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {7409,1}}, 3, 13),
        MINE_350_IRON(11, 350 ,MEDIUM, "Iron prodigy", new String[][]{{"Mine a small amount of iron ores at any spot"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {11512,1}}, 3, 14),
        KILL_100_FROSTS(12, 100 ,MEDIUM, "Don't be afraid of frost", new String[][]{{"Kill some frost dragons at Asgarnian ice dungeon"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {11515,1}}, 3, 15),
        RUN_350_WILD_LAPS(13, 500 ,MEDIUM, "Wilderrunner", new String[][]{{"Run some laps at Wilderness agility course"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {11582,1}}, 3, 16),
        CUT_600_MAPLES(14, 600 ,MEDIUM, "Maple story", new String[][]{{"Cut some maple trees at any spot to collect some maple logs"}}, new String[][]{{"3 Task points"}},
                new int[][]{{LARGE_LAMP,3}, {11514,1}}, 3, 17),

    /* Hard */
        BURY_500_OURGBONES(0, 500 ,HARD, "Fourgiveness", new String[][]{{"Bury some ourg bones"}}, new String[][]{{"4 Task points"},},
            new int[][]{{LARGE_LAMP,4}, {6182,1}, {6181, 1}, {6180, 1}}, 4, 3),
        OPEN_500_BARROWS_CHESTS(1, 500 ,HARD, "In search of brothers", new String[][]{{"Open a mid quantity of barrows chests"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {7386,1}, {7390, 1}, {7394, 1}}, 3, 4),
        COMPLETE_50_AB_DUNGEONS(2, 50 ,HARD, "The Abandoned", new String[][]{{"Complete some abandoned dungeons"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {10171,1}}, 4, 5),
        COMPLETE_25_PC(4, 25 ,HARD, "Voidly presence", new String[][]{{"Complete some pest control games"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {18341,1}}, 4, 6),
        STEAL_250_SQIRK(4, 250 ,HARD, "Sq'irking", new String[][]{{"Steal some sq'irk fruits playing Sorceress Garden"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {20859,1}}, 3, 7),
        SOULS_250_OFFERED(5, 250 ,HARD, "The Infernal", new String[][]{{"- Collect pure souls in a box near level 55 Volcano"}, {"- Run to the other side of the wilderness to the altar near The Forgotten Cemetery"}, {"- Bury the soul on the altar"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {5553,1}, {5554, 1}, {5555, 1}, {5556, 1}, {5557, 1}}, 2, 8),
        CHECK_200_MTREE(6, 200 ,HARD, "Magicholic", new String[][]{{"Check the health of a small quantity of magic trees that you grown"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {19675,1}}, 4, 9),
        RUN_400_BARB_LAPS(7, 750 ,HARD, "The Termined", new String[][]{{"Run some laps at Advanced barbarian agility course"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {19545,1}}, 4, 10),
        FISH_500_SHARKS(8, 5000 ,HARD, "Sharknator", new String[][]{{"Fish some raw sharks at any spot"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {1154,1}}, 4, 11),
        BURN_600_YEWS(9, 600 ,HARD, "Ash me a question", new String[][]{{"Burn a good amount of yew logs"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {1154,1}}, 4, 12),
        MINE_600_MITHRILS(10, 600 ,HARD, "Junior miner", new String[][]{{"Mine some mithril ores at any spot"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {7409,1}}, 4, 13),
        OPEN_250_BONECHEST(11, 250 ,HARD, "Hard bone to gnaw", new String[][]{{"Open a considerable amount of bone chests"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {11512,1}}, 4, 14),
        MAKE_150_OVL(12, 500 ,HARD, "Out of Control", new String[][]{{"Decant some overload potions"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {11515,1}}, 4, 15),
        CRAFT_300_ROYALPL8(13, 1000 ,HARD, "The Royal One", new String[][]{{"Craft some royal plate bodies"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {11582,1}}, 4, 16),
        CUT_550_MLOGS(14, 550 ,HARD, "kkkkkkk", new String[][]{{"Cut some magic trees at any spot to collect some magic logs"}}, new String[][]{{"4 Task points"}},
                new int[][]{{LARGE_LAMP,4}, {11514,1}}, 4, 17),

    /* Elite*/
    BURN_1500_MLOGS(0, 1500 ,ELITE, "Magic burner", new String[][]{{"Burn some magic logs"}}, new String[][]{{"5 Task points"}},
            new int[][]{{29850,3}, {13661,1}}, 5, 3),
    CLEAN_1K_RANARS(1, 1000 ,ELITE, "Ranarrling", new String[][]{{"Clean a little pack of ranar weeds"}}, new String[][]{{"5 Task points"}},
            new int[][]{{29850,3}, {13661,1}}, 5, 4),
    CUT_1K_DSTONES(2, 1000 ,ELITE, "Dragoncutter", new String[][]{{"Cut some dragonstones"}}, new String[][]{{"5 Task points"}},
            new int[][]{{29850,3}, {13661,1}}, 5, 5),
    FISH_1K_ROCKTAILS(3, 1000 ,ELITE, "From the deep", new String[][]{{"Fish some raw rocktails"}}, new String[][]{{"5 Task points"}},
            new int[][]{{29850,3}, {13661,1}}, 5, 6),
    FISH_350_RUNITE_ORES(4, 350 ,ELITE, "Master miner", new String[][]{{"Mine a good quantity of runite ores"}}, new String[][]{{"5 Task points"}},
           new int[][]{{29850,3}, {13661,1}}, 5, 7),
    FISH_150_RUNITE_ORES(5, 150 ,ELITE, "Undergrounder", new String[][]{{"Complete some warped dungeons"}}, new String[][]{{"5 Task points"}},
           new int[][]{{29850,3}, {13661,1}}, 5, 8),
    KILL_200_REVENANTS(6, 200 ,ELITE, "Revenantious", new String[][]{{"Kill some of any revenants at Forinthry dungeon"}}, new String[][]{{"5 Task points"}},
           new int[][]{{29850,3}, {13661,1}}, 5, 9),
    STRING_1K_MAGIC_LONGS(7, 1000 ,ELITE, "Advanced stringer", new String[][]{{"String a big amount of magic longbows"}}, new String[][]{{"5 Task points"}},
           new int[][]{{29850,3}, {13661,1}}, 5, 10),
    ;

    private int achievementId;
    private int achievementQuantity;
    private int achievementDifficulty;
    private String achievementName;
    private String[][] achievementSteps;
    private String[][] achievementRewards;
    private int[][] itemRewards;
    private int pointsReward;
    public int compId;


        Achievement(int id, int quantity, int difficulty, String name, String[][] steps, String[][] reward, int[][] rewards, int point, int componentId) {
        this.achievementId = id;
        this.achievementQuantity = quantity;
        this.achievementDifficulty = difficulty;
        this.achievementName = name;
        this.achievementSteps = steps;
        this.achievementRewards = reward;
        this.itemRewards = rewards;
        this.pointsReward = point;
        this.compId = componentId;
    }

    public int getId(int ikl) {
        return itemRewards[ikl][0];
    }

    public int getAchievementQuantity() {
        return achievementQuantity;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public String[][] getAchievementSteps() {
        return achievementSteps;
    }

    public String[][] getAchievementRewards() {
        return achievementRewards;
    }

    public int[][] getItemRewards() {
        return itemRewards;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public int getPointsReward() {
        return pointsReward;
    }

    public int getDifficulty() {return achievementDifficulty;}

    public int getCompId() {return compId; }}

    public static int getAchievementOffset(Achievements.Achievement achievement) {
                return achievement.getDifficulty() == Achievements.EASY ? Achievements.OFFSET_EASY :
                        achievement.getDifficulty() == Achievements.MEDIUM ? Achievements.OFFSET_MED : achievement.getDifficulty() == Achievements.HARD ?
                                Achievements.OFFSET_HARD : Achievements.OFFSET_ELITE;
    }

    public static void checkAchievementStatus(Player player, Achievements.Store selectedAchievement, int quant) {
        for (Achievements.Achievement achievement : Achievements.Achievement.values()) {
            if (achievement.getDifficulty() == selectedAchievement.getDifficulty()) {
                if (achievement.getAchievementId() == selectedAchievement.getId()) {
                    if (player.getAchievements(Achievements.getAchievementOffset(achievement) + selectedAchievement.getId()) < 1) { //first value
                        player.sm("<img="+Settings.ACHIEVEMENTS_ICON+"> Your achievement: " + achievement.getAchievementName() + " was updated!");
                    } else {
                        if (player.getAchievements(Achievements.getAchievementOffset(achievement) + selectedAchievement.getId()) < achievement.getAchievementQuantity())
                        player.getPackets().sendGameMessage("<col=4150be>[Task Update] "+achievement.getAchievementName() + ": ("+player.getAchievements(Achievements.getAchievementOffset(achievement) + selectedAchievement.getId())+" / "+achievement.getAchievementQuantity()+").", true);
                    }
                    if (player.getAchievements(Achievements.getAchievementOffset(achievement) + selectedAchievement.getId()) == (achievement.getAchievementQuantity() / 2)-1) { //half number
                        player.sm("<img="+Settings.ACHIEVEMENTS_ICON+"> <col=f00000>Your achievement: " + achievement.getAchievementName() + " was updated, you are now on 50% of completion!</col>");
                    }
                    if (player.getAchievements(Achievements.getAchievementOffset(achievement) + selectedAchievement.getId()) == (achievement.getAchievementQuantity()) -1) {//last value
                        Description.sendCollectRewards(player, false);
                        player.setTasksdone(player.getTasksdone() + 1);
                        player.setAchievementPoints(player.getAchievementPoints()+ achievement.getPointsReward());
                        player.sm("<col=f00000>Congratulations, you just completed the achievement: "+achievement.getAchievementName()+ "!</col>");
                        World.sendWorldMessage(
                                player.getColorByDisplayMode(9) + "<img=" + Settings.ICON + "> News: " + player.getDisplayName()
                                        + " has completed the "+(achievement.getDifficulty() == Achievements.EASY ? "easy" : achievement.getDifficulty() == Achievements.MEDIUM ? "medium"
                                        : achievement.getDifficulty() == Achievements.HARD ? "hard" : "elite")+" achievement: " + achievement.getAchievementName() + " in "+player.getGameModeName(false).toLowerCase()+" mode, Congratulations!",
                                false);
                        new DiscordManager(DiscordConstant.GENERAL_CHANNEL_ID, player, (achievement.getDifficulty() == Achievements.EASY ? "Easy Achievements" : achievement.getDifficulty() == Achievements.MEDIUM ? "Medium Achievements"
                                : achievement.getDifficulty() == Achievements.HARD ? "Hard Achievements" : "Elite Achievements"), "\uD83D\uDCAC Has just completed the achievement " +  MediumTasksChecks.TasksName.CLEAN_250_NETTLES.getName() + " in " + player.getGameModeName(false).toLowerCase() + " mode \uD83E\uDD70! - @everyone").logOrange();

                    }
                    player.setAchievements(Achievements.getAchievementOffset(achievement) + selectedAchievement.getId(), quant);
                    Tab.open(player);
                }
            }
        }
    }
}
