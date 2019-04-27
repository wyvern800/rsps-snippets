package com.rs.game.player.content.interfaces.achievements;

import com.rs.game.player.Player;
import com.rs.game.player.content.Colors;

public class Tab {
	public static final int[] CLICKABLE_COMPONENTS = {3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22};
	
	/**
	 * @author Sagacity with help of Andreas - https://www.rune-server.ee/members/sagacity/
	 */

	public static void open(Player player) {
		int i2 = 0;
			for (int i = 0; i < CLICKABLE_COMPONENTS.length; i++) {
				player.getPackets().sendHideIComponent(3001, CLICKABLE_COMPONENTS[i], true);
			}
			int cat = player.getTemporaryAttributes().get("cat") != null ? (int) player.getTemporaryAttributes().get("cat") : 0;
			player.getPackets().sendIComponentText(3001, 24, (cat == 0 ? "Easy" : cat == 1 ? "Medium" : cat == 2 ? "Hard" : "Elite"));
			player.getPackets().sendIComponentText(3001, 25, "Total Points: " + player.getTasksdone());
			player.getTemporaryAttributes().put("cat", cat);
			for (Achievements.Achievement achievement : Achievements.Achievement.values()) {
				if (achievement != null) {
					if (achievement.getDifficulty() != cat)
						continue;
					if (achievement.getCompId() != CLICKABLE_COMPONENTS[i2])
						continue;
					player.getPackets().sendHideIComponent(3001, CLICKABLE_COMPONENTS[i2], false);
					player.getPackets().sendIComponentText(3001, CLICKABLE_COMPONENTS[i2],
							(player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) >= achievement.getAchievementQuantity() ? Colors.COMPLETE+ achievement.getAchievementName()+"</col>"
									: player.getAchievements(Achievements.getAchievementOffset(achievement)+i2) > 0 ? Colors.STARTED + achievement.getAchievementName()+"</col>" : Colors.NONSTARTED+ achievement.getAchievementName())+"</col>"
						);
				i2++;
			}
		}
	}

	public static void handleButtons(Player player, int compId) {
		for (int i = 0; i < CLICKABLE_COMPONENTS.length; i++) {
		if (compId == CLICKABLE_COMPONENTS[i]) {
			for (Achievements.Achievement store : Achievements.Achievement.values()) {
				if (store != null) {
					if ((compId - 3) != store.getAchievementId())
						continue;
					if ((int) player.getTemporaryAttributes().get("cat") != store.getDifficulty())
						continue;
					Description.sendDescription(player, store);
					}
				}
			}
		}
		switch (compId) {
		case 27:
			int cat = (int) player.getTemporaryAttributes().get("cat");
			player.getTemporaryAttributes().put("cat", (cat == 3 ? 0 : (cat += 1)));
			open(player);
			break;
		default:
			break;
		}
	}
}
