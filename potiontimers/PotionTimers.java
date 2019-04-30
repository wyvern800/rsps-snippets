package com.rs.game.player.content.interfaces.timers;

import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class PotionTimers {
    public static int INTERFACE = 3011;
    public static int FULL_PANEL = 2, BLACK_BOX = 3;
    public static int OVL_SPRITE = 3, OVL_TEXT = 7;
    public static int RENEWAL_SPRITE = 4, RENEWAL_TEXT = 8;
    public static int ANTIFIRE_SPRITE = 5, ANTIFIRE_TEXT = 9;
    public static int ANTIPOISON_SPRITE = 6, ANTIPOISON_TEXT = 10;
    public static int SUMMON_SPRITE = 11, SUMMON_TEXT = 12;

    public static final int OVERLOAD = 0, RENEWAL = 1, ANTIFIRE = 2, ANTIPOISON = 3, SUMMON = 4;

    public static void startTime(final Player p, int potion) {
        switch (potion) {
            case OVERLOAD:
                p.getPackets().sendHideIComponent(PotionTimers.INTERFACE, PotionTimers.OVL_SPRITE, false);
                WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    if (p.getOverloadMin() >= 0 && p.getOverloadCount() > 0) {
                        p.setOverloadCount(p.getOverloadCount()-1);
                        p.getPackets().sendHideIComponent(INTERFACE, OVL_TEXT, false);
                        p.getPackets().sendIComponentText(INTERFACE, OVL_TEXT, "<col=d1d0d2><u>" + getTime(p, OVERLOAD) + "</u></col>");
                    } else {
                        p.getPackets().sendHideIComponent(INTERFACE, OVL_SPRITE, true);
                        p.getPackets().sendIComponentText(INTERFACE, OVL_TEXT, "");
                    }
                }
            }, 0, 1);
            break;
            case ANTIFIRE:
                p.getPackets().sendHideIComponent(PotionTimers.INTERFACE, PotionTimers.ANTIFIRE_SPRITE, false);
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        if (p.getAntifireMin() >= 0 && p.getAntifireCount() > 0) {
                            p.setAntifireCount(p.getAntifireCount()-1);
                            p.getPackets().sendHideIComponent(INTERFACE, ANTIFIRE_TEXT, false);
                            p.getPackets().sendIComponentText(INTERFACE, ANTIFIRE_TEXT, "<col=d1d0d2><u>" + getTime(p, ANTIFIRE) + "</u></col>");
                        } else {
                            p.getPackets().sendHideIComponent(INTERFACE, ANTIFIRE_SPRITE, true);
                            p.getPackets().sendIComponentText(INTERFACE, ANTIFIRE_TEXT, "");
                        }
                    }
                }, 0, 1);
            break;
            case RENEWAL:
                p.getPackets().sendHideIComponent(PotionTimers.INTERFACE, PotionTimers.RENEWAL_SPRITE, false);
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        if (p.getRenewalMin() >= 0 && p.getRenewalCount() > 0) {
                            p.setRenewalCount(p.getRenewalCount()-1);
                            p.getPackets().sendHideIComponent(INTERFACE, RENEWAL_TEXT, false);
                            p.getPackets().sendIComponentText(INTERFACE, RENEWAL_TEXT, "<col=d1d0d2><u>" + getTime(p, RENEWAL) + "</u></col>");
                        } else {
                            p.getPackets().sendHideIComponent(INTERFACE, RENEWAL_SPRITE, true);
                            p.getPackets().sendIComponentText(INTERFACE, RENEWAL_TEXT, "");
                        }
                    }
                }, 0, 1);
                break;
            case ANTIPOISON:
                p.getPackets().sendHideIComponent(PotionTimers.INTERFACE, PotionTimers.ANTIPOISON_SPRITE, false);
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        if (p.getAntipoisonMin() >= 0 && p.getAntipoisonCount() > 0) {
                            p.setAntipoisonCount(p.getAntipoisonCount()-1);
                            p.getPackets().sendHideIComponent(INTERFACE, ANTIPOISON_TEXT, false);
                            p.getPackets().sendIComponentText(INTERFACE, ANTIPOISON_TEXT, "<col=d1d0d2><u>" + getTime(p, ANTIPOISON) + "</u></col>");
                        } else {
                            p.getPackets().sendHideIComponent(INTERFACE, ANTIPOISON_SPRITE, true);
                            p.getPackets().sendIComponentText(INTERFACE, ANTIPOISON_TEXT, "");
                        }
                    }
                }, 0, 1);
                break;
            case SUMMON:
                p.getPackets().sendHideIComponent(INTERFACE, SUMMON_TEXT, false);
                p.getPackets().sendHideIComponent(PotionTimers.INTERFACE, PotionTimers.SUMMON_SPRITE, false);
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        if (p.getSummonMin() >= 0 && p.getSummonCount() > 0) {
                            p.setSummonCount(p.getSummonCount()-1);
                            p.getPackets().sendHideIComponent(INTERFACE, SUMMON_TEXT, false);
                            p.getPackets().sendIComponentText(INTERFACE, SUMMON_TEXT, "<col=d1d0d2><u>" + getTime(p, SUMMON) + "</u></col>");
                        } else {
                            p.getPackets().sendHideIComponent(INTERFACE, SUMMON_SPRITE, true);
                            p.getPackets().sendIComponentText(INTERFACE, SUMMON_TEXT, "");
                        }
                    }
                }, 0, 1);
                break;
        }
    }

    public static String getTime(Player player, int potion) {
        switch (potion) {
            case OVERLOAD:
                player.getPackets().sendHideIComponent(INTERFACE, OVL_SPRITE, false);
            if (player.getOverloadCount() == 0) {
                player.setOverloadMin(player.getOverloadMin() - 1);
                player.setOverloadCount(60);
            }
            if (player.getOverloadMin() == 0 && player.getOverloadCount() == 0) {
                player.getPackets().sendHideIComponent(INTERFACE, OVL_SPRITE, true);
                player.getPackets().sendIComponentText(INTERFACE, OVL_TEXT, "");
            }
            return player.getOverloadMin() + "m " + player.getOverloadCount() + "s";
            case ANTIFIRE:
                player.getPackets().sendHideIComponent(INTERFACE, ANTIFIRE_SPRITE, false);
                if (player.getAntifireCount() == 0) {
                    player.setAntifireMin(player.getAntifireMin() - 1);
                    player.setAntifireCount(60);
                }
                if (player.getAntifireMin() == 0 && player.getAntifireCount() == 0) {
                    player.getPackets().sendHideIComponent(INTERFACE, ANTIFIRE_SPRITE, true);
                    player.getPackets().sendIComponentText(INTERFACE, ANTIFIRE_TEXT, "");
                }
                return player.getAntifireMin() + "m " + player.getAntifireCount() + "s";
            case RENEWAL:
                player.getPackets().sendHideIComponent(INTERFACE, RENEWAL_SPRITE, false);
                if (player.getRenewalCount() == 0) {
                    player.setRenewalMin(player.getRenewalMin() - 1);
                    player.setRenewalCount(60);
                }
                if (player.getRenewalMin() == 0 && player.getRenewalCount() == 0) {
                    player.getPackets().sendHideIComponent(INTERFACE, RENEWAL_SPRITE, true);
                    player.getPackets().sendIComponentText(INTERFACE, RENEWAL_TEXT, "");
                }
                return player.getRenewalMin() + "m " + player.getRenewalCount() + "s";
            case ANTIPOISON:
                player.getPackets().sendHideIComponent(INTERFACE, ANTIPOISON_SPRITE, false);
                if (player.getAntipoisonCount() == 0) {
                    player.setAntipoisonMin(player.getAntipoisonMin() - 1);
                    player.setAntipoisonCount(60);
                }
                if (player.getAntipoisonMin() == 0 && player.getAntipoisonCount() == 0) {
                    player.getPackets().sendHideIComponent(INTERFACE, ANTIPOISON_SPRITE, true);
                    player.getPackets().sendIComponentText(INTERFACE, ANTIPOISON_TEXT, "");
                }
                return player.getAntipoisonMin() + "m " + player.getAntipoisonCount() + "s";
            case SUMMON:
                player.getPackets().sendHideIComponent(INTERFACE, SUMMON_SPRITE, false);
                if (player.getSummonCount() == 0) {
                    player.setSummonMin(player.getSummonMin() - 1);
                    player.setSummonCount(60);
                }
                if (player.getSummonMin() == 0 && player.getSummonCount() == 0) {
                    player.getPackets().sendHideIComponent(INTERFACE, SUMMON_SPRITE, true);
                    player.getPackets().sendIComponentText(INTERFACE, SUMMON_TEXT, "");
                }
                return player.getSummonMin() + "m " + player.getSummonCount() + "s";
        }
        return "0m 00s";
    }
}