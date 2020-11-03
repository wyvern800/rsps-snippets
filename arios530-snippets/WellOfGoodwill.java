package plugin.interaction.inter.custom.goodwill.goodwill;

import core.Util;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.RunScript;
import core.game.node.item.Item;
import core.game.system.SystemLogger;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Well of Goodwill System
 *
 * @author Sagacity - http://rune-server.org/members/Sagacity
 * @created 05/10/2020 - 12:33
 * @project 530-rsps
 */
public class WellOfGoodwill {
    // The path we are saving our file to
    private static final String filePath = "./Server/data/wog/"; // Must end up with /

    // The currency id
    private static final int currency = 995;

    // The minimum amount to donate to well
    private static final int minAmount = 100000;

    // The max amount to donate to well (Goal)
    private static final int maxAmount = 10000000;

    // The well activity boolean
    private static boolean wellActive;

    // The well amount
    private static int wellAmount;

    // The well active timer
    private static int currentWellTime;

    // The icon used for the system worldwide messages
    private static final int messagesIcon = 7;

    // The color used for the system worldwide messages
    public static final String messagesColor = "<col=a600ff>";

    // The time we will get double xp for (seconds)
    private static final int howLongActivatedFor = 3600; // 3600 represents an hour

    // The last donation
    private static Donation lastDonation ;

    /**
     * The constructor
     */
    public WellOfGoodwill() {
        lastDonation = null;
    }

    // Gets the last donation
    public static Donation getLastDonation() {
        return lastDonation;
    }

    // Checks if well of goodwill is active
    public static boolean isWellActive() {
        return wellActive;
    }

    // Gets the amount we need to donate to reach the goal
    public static int getAmountToReachGoal() {
        return maxAmount;
    }

    /**
     * Gets how long will the well be up for
     * */
    public static int getCurrentWellTime() {
        return currentWellTime;
    }

    /**
     * Gets the total donated to well
     * */
    public static int getTotalDonated() {
        return wellAmount;
    }

    /**
     * Gets how long the dxp will be activated for
     * @return The time the well of goodwill will be active for
     */
    public static int getHowLongActivatedFor() {
        return howLongActivatedFor;
    }

    /**
     * Sends a dialogue for the amount to give.
     *
     * @param player The Player giving the amount.
     */
    public static void give(Player player) {
        if (wellActive) {
            player.sendMessage("The XP well is already active! Go train!");
            return;
        }

        player.setAttribute("runscript", new RunScript() {
            @Override
            public boolean handle() {
                int amount = (int) value;
                if (amount < minAmount) {
                    player.sendMessage("You must donate at least "+minAmount+ " gold!");
                    return false;
                }
                donate(player, amount);
                return true;
            }
        });
        int remaining = maxAmount - wellAmount;
        player.getDialogueInterpreter().sendInput(false, "Progress: "+messagesColor + /*NumberFormat.getNumberInstance(Locale.US).format(*/Util.intToKOrMil(wellAmount)/*)*/ + "</col>; GP ("+messagesColor + ((wellAmount * 100) / maxAmount) + "%</col> of Goal); Remaining: "+messagesColor + /*NumberFormat.getNumberInstance(Locale.US).format(*/Util.intToKOrMil(remaining)/*)*/ + "</col> GP");
    }

    /**
     * Donates to the well the amount to give.
     *
     * @param player The Player donating.
     * @param amount The amount to give.
     */
    private static void donate(Player player, int amount) {
        if (amount < 0)
            return;
        if ((wellAmount + amount) > maxAmount) {
            amount = maxAmount - wellAmount;
        }
        if (amount < minAmount) {
            return;
        }
        if (player.getInventory().contains(currency, amount)) {
            wellAmount += amount;

            // Sets the last donator
            lastDonation = new Donation(player.getName(), amount);

            // Removes the gold from inventory
            player.getInventory().remove(new Item(currency, amount));

            // Increases the total the player donated to well
            player.getSavedData().getActivityData().setTotalDonatedToWell(
                    player.getSavedData().getActivityData().getTotalDonatedToWell()+amount);

            Repository.sendNews(Util.capitalize(player.getName())
                    + " has contributed "
                    + NumberFormat.getNumberInstance(Locale.US).format(
                    amount)
                    + " GP to the XP well! Progress now: "
                    + ((wellAmount * 100) / maxAmount)
                    + "%!</col>", messagesIcon, messagesColor);

            postDonation();

            if (player.isDebug())
            SystemLogger.log("donate(player="+ Util.capitalize(player.getName())+", amount="+
                    NumberFormat.getNumberInstance(Locale.US).format(
                            amount));
        } else {
            player.sendMessage("You don't have enough money!");
        }
    }

    /**
     * Loads the well
     */
    public static void loadWell() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath+"data.txt"));
        String line;
        String msg = null;
        while ((line = reader.readLine()) != null) {
            String[] args = line.split(" ");

            // If the file contains true, sends the well activity again
            if (args[0].contains("true")) {
                // Well active
                wellActive = true;
                currentWellTime = Integer.parseInt(args[1]);
                lastDonation = new Donation(args[2],  Integer.parseInt(args[3]), LocalDate.parse(args[4]));

                msg = "((wellActive=true, wellAmount="+ wellAmount +", lastDonator="+lastDonation.getDonator()+", amount:"+lastDonation.getAmount()+");";

                // Start the DXP with a pulse timer to stop after a while
                startWell();
            } else { // if not, sets the current values to the file's one
                msg = "(wellActive=false, totalDonated="+wellAmount+");";

                // Well not active yet
                wellAmount = Integer.parseInt(args[1]);
            }
        }
        SystemLogger.log("[WellOfGoodwill] is being loaded - "+msg);
    }

    /**
     * Saves the well
     */
    public static void saveWell() {
        File output = new File(filePath+"data.txt");

        if (output.canWrite()) {
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(output, false));
                if (wellActive) {
                    out.write("true " + currentWellTime);
                    out.write(" "+lastDonation.getDonator());
                    out.write( " "+ lastDonation.getAmount());
                    out.write( " "+ lastDonation.getDonatedAt().toString().replace("T", " - "));
                } else {
                    out.write("false " + wellAmount);
                }
            } catch (IOException ignored) {
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    /**
     * A check after donating to the well to see if the x2 XP should start.
     */
    private static void postDonation() {
        if (wellAmount >= maxAmount) {
            wellActive = true; // Sets the well to active
            startWell(); // Starts the well timer
            saveWell(); // Saves the well
            Repository.sendNews("The goal of " + NumberFormat.getNumberInstance(Locale.US).format(maxAmount) + " GP has been reached! Double XP for 1 hour begins now!", messagesIcon, messagesColor);
        }
    }

    /**
     * Starts the well for a period of time
     */
    private static void startWell() {
        GameWorld.Pulser.submit(new Pulse(1) {
            int tick = 0;

            @Override
            public boolean pulse() {
                tick++;
                currentWellTime = tick;
                saveWell();

                //SystemLogger.log("Current well task tick: "+tick+"/"+getHowLongActivatedFor());
                if (tick == howLongActivatedFor) { // Sends this when the timer reachs the end
                    wellActive = false;
                    resetWell();
                    this.stop();
                } else if (tick % (howLongActivatedFor /2) == 0) { // Sends this message when the timer reaches half the time
                    Repository.sendNews("The Double XP will be enabled for more half an hour, good luck!", messagesIcon, messagesColor);
                } else if (!WellOfGoodwill.isWellActive() && getTotalDonated() == 0 && lastDonation == null) {
                    this.stop();
                }
                return false;
            }
        });
    }

    /**
     * Resets the well
     */
    public static void resetWell() {
        wellAmount = 0;
        lastDonation = null;
        wellActive = false;
        Repository.sendNews("The XP well has been reset!",messagesIcon, messagesColor);
        saveWell();
    }

    /**
     * Contains a donation
     */
    public static class Donation {

        // The last donator
        private final String donator;

        // The donation amount
        private final Integer amount;

        // When the player donated
        private final LocalDate donatedAt;

        // Gets the donator
        public String getDonator() {
            return donator;
        }

        // Gets the amount donated
        public Integer getAmount() {
            return amount;
        }

        // Gets when the donation was made
        public LocalDate getDonatedAt() {
            return donatedAt;
        }

        /**
         * Creates a new donation instance
         * @param donator The donator (player)
         * @param amount The amount
         */
        public Donation(String donator, Integer amount) {
            this.donator = donator;
            this.amount = amount;
            this.donatedAt = LocalDate.now();
        }

        public Donation(String donator, Integer amount, LocalDate ldt) {
            this.donator = donator;
            this.amount = amount;
            this.donatedAt = ldt;
        }
    }
}
