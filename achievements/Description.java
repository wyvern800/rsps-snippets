package com.rs.game.player.content.interfaces.achievements;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.content.Colors;
import com.rs.utils.ItemExamines;

/**
 * @author Sagacity with help of Andreas - https://www.rune-server.ee/members/sagacity/ 
 * Date: 20/04/2019
 */

public class Description {
    public static final int INTER = 3019;
    private static Achievements.Achievement taskId = null;

    public static ItemsContainer<Item> Rewards = new ItemsContainer<Item>(20, true);

    public static Achievements.Achievement getTaskId() {
        return taskId;
    }

    
    public static void setTaskId(Achievements.Achievement taskId) {
        Description.taskId = taskId;
    }

    /**
     * @param player - the Player
     * @param task - the task that will get the description sent
     */
    public static void sendDescription(Player player, Achievements.Achievement task) {
        setTaskId(task);
        player.closeInterfaces();
        final StringBuilder stb = new StringBuilder();
        final StringBuilder stb2 = new StringBuilder();
        for (Achievements.Achievement achievement : Achievements.Achievement.values()) {
            player.getPackets().sendIComponentText(INTER, 16, task.getAchievementName());
            for (int i2 = 0; i2 < Achievements.Achievement.values().length; i2++) {
                if (achievement.getAchievementId() == i2) {
                    if (achievement == task) {
                            player.getPackets().sendHideIComponent(INTER, 54, (player.getAchievements(Achievements.getAchievementOffset(achievement) + i2) < achievement.getAchievementQuantity()));
                        if (achievement.getAchievementSteps() != null) {
                            for (int x = 0; x < achievement.getAchievementSteps().length; x++) {
                                stb.append(achievement.getAchievementSteps()[x][0] + ".<br>");
                            }
                        } else {
                            stb.append("None.<br>");
                        }
                        if (achievement.getAchievementRewards() != null) {
                            for (int x2 = 0; x2 < achievement.getAchievementRewards().length; x2++) {
                                stb2.append(achievement.getAchievementRewards()[x2][0] + ".<br>");
                            }
                        } else {
                            stb2.append("None.<br>");
                        }
                        player.getPackets().sendIComponentText(INTER,
                                58, "<col=bd6500>Name:</col><br>" +
                                    (achievement.getAchievementName()) + ".<br><br>" +
                                "<col=bd6500>Step(s):</col><br>" +
                                        stb.toString() +
                                        "<br>" +
                                        "<col=bd6500>Rewards:</col><br>" +
                                        stb2.toString());
                        if (achievement.getItemRewards() != null) {
                            Rewards.reset();
                            Rewards.clear();
                            for (int x3 = 0; x3 < achievement.getItemRewards().length; x3++) {
                                Rewards.add(new Item(achievement.getItemRewards()[x3][0], achievement.getItemRewards()[x3][1]));
                            }
                            player.getPackets().sendItems(90, false, Rewards);
                            player.getPackets().sendInterSetItemsOptionsScript(INTER, 60, 90, 4, 10, "Examine"); //12 5
                            player.getPackets().sendUnlockIComponentOptionSlots(INTER, 60, 0, 160, 0);
                        } else {
                            Rewards.reset();
                            Rewards.clear();
                        }
                        player.getPackets().sendIComponentText(INTER, 65,
                                (player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) >= achievement.getAchievementQuantity() ? Colors.COMPLETE + "Achievement completed! - Total: ("+player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) +")</col>"
                                        : player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) > 0 ? Colors.STARTED+ ((getPercentage(player, achievement, i2) * 100) / achievement.getAchievementQuantity()) +"% (" + player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) + "/" + achievement.getAchievementQuantity() + ")" : Colors.NONSTARTED+ "Not started - ("+player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) + "/" + achievement.getAchievementQuantity()+")"+"</col>")
                        );
                        break;
                    }
                }
            }
        }
        player.getPackets().sendIComponentText(INTER, 66, "<col=ffc300>This meter indicates  your progress for the objective.</col><br>" +
                "Once complete, you'll receive a casket and infernal points.");
        player.getInterfaceManager().sendInterface(INTER);
    }

    /**
     * 
     * @param player - the Player
     * @param achievement - the Achievement that we are getting the percentage from
     * @param index - the index of the achievement at the player's storage array
     * @return
     */
    public static int getPercentage(Player player, Achievements.Achievement achievement, int index) {
        return player.getAchievements(Achievements.getAchievementOffset(achievement)+index);
    }

    /**
     * 
     * @param player - the Player
     * @param componentId - the interface component ID
     * @param slotId2 - the slotId2 that in this case, we are using to get the rewards id
     */
    public static void handleButtons(Player player, int componentId, int slotId2) {
        switch (componentId) {
            case 9: //close
                player.closeInterfaces();
                break;
            case 55: //collect item
                sendCollectRewards(player, true);
                break;
            case 60: //examine item
                for (Achievements.Achievement achievement : Achievements.Achievement.values()) {
                    for (int i2 = 0; i2 < achievement.getItemRewards().length; i2++) {
                        if (achievement.getId(i2) == slotId2) {
                            player.sm(ItemExamines.getExamine(new Item(slotId2)));
                            return;
                        }
                    }
                }
                break;
        }
    }

    /**
     * 
     * @param player - the Player
     * @param withoutMoney - if true will, rewards will be sent without the first item on the array, which means the 1 time rewarding
     */
    public static void sendCollectRewards(Player player, boolean withoutMoney) {
        for (Achievements.Achievement achievement : Achievements.Achievement.values()) {
            for (int ix = 0; ix < achievement.values().length; ix++) {
                if (achievement.getAchievementId() == ix) {
                    if (achievement == getTaskId()) {
                        if (!withoutMoney ? player.getAchievements(Achievements.getAchievementOffset(achievement) + ix) >= (achievement.getAchievementQuantity())-1 :player.getAchievements(Achievements.getAchievementOffset(achievement) + ix) >= achievement.getAchievementQuantity()) {
                            for (int i2 = (withoutMoney ? 1 : 0); i2 < achievement.getItemRewards().length; i2++) {
                                //player.sm(achievement.getItemRewards()[i2][1]+"x of "+new Item(achievement.getItemRewards()[i2][0]).getName()+" was added to your bank!");
                                player.getBank().addItem(achievement.getItemRewards()[i2][0], achievement.getItemRewards()[i2][1], true);
                            }
                            player.sm("Your rewards were added to your bank!");
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }
}
